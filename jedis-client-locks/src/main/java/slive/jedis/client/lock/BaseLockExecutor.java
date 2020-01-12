package slive.jedis.client.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 描述：<br>
 * 通用实现的基本的锁执行器
 *
 * @author slive
 * @date 2020/1/1
 */
public class BaseLockExecutor implements LockExecutor<BaseLockExecutorContext> {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseLockExecutor.class);

    private static final Map<String, BaseLockExecutor> EXECUTOR_MAP = new HashMap<String, BaseLockExecutor>();

    private static final long DEFAULT_MILLIS_TIMEOUT = 15 * 1000;

    private ThreadLocal<BaseLockExecutorContext> threadLocal = new ThreadLocal<BaseLockExecutorContext>();

    private long defaultMillsTimeout;

    private String prefix;

    private Lock lock;

    /**
     * 锁执行器构造函数
     * @param prefix 锁前缀，用于区分不同模块的锁，以免重复
     * @param defMillisTimeout 默认超时时间ms，必须为整数，注意设置该值，避免锁过快超时
     * @param lock 锁见{@link Lock}
     */
    public BaseLockExecutor(String prefix, long defMillisTimeout, Lock lock) {
        init(prefix, defMillisTimeout, lock);
    }

    /**
     * 锁执行器构造函数，锁时间为默认值，见{@link #DEFAULT_MILLIS_TIMEOUT}
     * @param prefix 锁前缀，用于区分不同模块的锁，以免重复
     * @param lock 锁见{@link Lock}
     */
    public BaseLockExecutor(String prefix, Lock lock) {
        init(prefix, DEFAULT_MILLIS_TIMEOUT, lock);
    }

    private void init(String prefix, long millisTimeout, Lock lock) {
        setPrefix(prefix);
        setDefaultMillsTimeout(millisTimeout);
        setLock(lock);
        if (EXECUTOR_MAP.containsKey(prefix)) {
            throw new RuntimeException("prefix:[" + prefix + "] has existed.");
        }
        EXECUTOR_MAP.put(prefix, this);
    }

    public String getPrefix() {
        return prefix;
    }

    private void setPrefix(String prefix) {
        if (prefix == null) {
            throw new NullPointerException("prefix is null.");
        }
        this.prefix = prefix;
    }

    public long getDefaultMillisTimeout() {
        return defaultMillsTimeout;
    }

    private void setDefaultMillsTimeout(long defaultMillsTimeout) {
        if (defaultMillsTimeout <= 0) {
            throw new RuntimeException("default defaultMillsTimeout below 0.");
        }
        this.defaultMillsTimeout = defaultMillsTimeout;
    }

    private void setLock(Lock lock) {
        if (lock == null) {
            throw new NullPointerException("lock is null.");
        }
        this.lock = lock;
    }

    public LockExecutor execute(BaseLockExecutorContext context, LockHandler handler) {
        if (context == null) {
            throw new NullPointerException("context is null.");
        }

        if (handler == null) {
            throw new NullPointerException("handler is null.");
        }
        LOGGER.info("start to execute, context:{}, defaultMillsTimeout:{}", context.toSimpleString(),
                defaultMillsTimeout);
        long timeout = context.getTimeout();
        if (timeout <= 0) {
            timeout = defaultMillsTimeout;
            context.setTimeout(timeout);
        }

        String finalKey = converFinalKey(context);
        String value = converFinalValue(context);
        BaseLockExecutorContext rtContext = null;
        boolean reentryLocked = false;
        boolean locked = false;
        try {
            rtContext = getLocalContext();
            if (rtContext != null) {
                // 非重入，设置进线程变量中
                boolean reentry = rtContext.equals(context);
                reentryLocked = rtContext.isStillLocked();
                LOGGER.info("reentry:{}, rtContext:{}", reentry, rtContext);
            }
            // 无论是否是已经存在，都更新为最新的context
            addLocalContext(context);

            if (reentryLocked) {
                // 已获取锁后延长锁的时间
                locked = lock.expireLock(finalKey, value, timeout);
            }
            else {
                // 未获取锁继续获取
                locked = lock.tryLock(finalKey, value, timeout);
            }
            context.setLocked(locked);
            if (locked) {
                LOGGER.info("execute handler, context:{}", context.toSimpleString());
                Object ret = handler.onHandle(context);
                context.setResult(ret).setHandledTime();
            }
            else {
                try {
                    handler.onFailed(context);
                }
                catch (Exception ex2) {
                    // ignore
                    LOGGER.error("execute onFailed exception:{}", ex2);
                }
            }
        }
        catch (Exception ex) {
            try {
                handler.onError(context, ex);
            }
            catch (Exception ex1) {
                // ignore
                LOGGER.error("execute onError exception:{}", ex1);
            }
        }
        finally {
            // 自己申请锁自己释放
            if (locked && !reentryLocked) {
                lock.unLock(finalKey, value);
            }

            // 删除当前context
            clearLocalContext();
            if (rtContext != null) {
                // 恢复重入保留原有的context
                addLocalContext(rtContext);
            }

            try {
                handler.afterHandle(context);
            }
            catch (Exception e) {
                LOGGER.error("execute afterHandle exception:{}", e);
            }
            context.setEndTime();
        }

        LOGGER.info("finish execute, context:{}", context);
        return this;
    }

    private static String converFinalValue(BaseLockExecutorContext context) {
        String owner = context.getOwner();
        if (StringUtils.isEmpty(owner)) {
            owner = UUID.randomUUID().toString().replaceAll("-", "");
            context.setOwner(owner);
        }

        String operator = context.getAction();
        String value = null;
        if (StringUtils.isNotEmpty(operator)) {
            value = owner + ":" + operator;
        }
        else {
            value = owner;
        }
        return value;
    }

    private String converFinalKey(BaseLockExecutorContext context) {
        String key = context.getKey();
        return prefix + "#" + key;
    }

    private void addLocalContext(BaseLockExecutorContext context) {
        if (context != null) {
            threadLocal.set(context);
        }
    }

    private BaseLockExecutorContext getLocalContext() {
        return threadLocal.get();
    }

    private void clearLocalContext() {
        threadLocal.remove();
    }

}
