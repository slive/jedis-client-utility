package slive.jedis.client.lock;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 描述：默认基本的锁执行器
 *
 * @author slive
 * @date 2020/1/1
 */
public class BaseLockExecutor implements LockExecutor<BaseLockExecutorContext> {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseLockExecutor.class);

    private ThreadLocal<BaseLockExecutorContext> threadLocal = new ThreadLocal<BaseLockExecutorContext>();

    private long millisTimeout;

    private String prefix;

    private Lock lock;

    public BaseLockExecutor(String prefix, long defMillisTimeout, Lock lock) {
        init(prefix, defMillisTimeout, lock);
    }

    private void init(String prefix, long millisTimeout, Lock lock) {
        setPrefix(prefix);
        setMillisTimeout(millisTimeout);
        setLock(lock);
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

    private void setMillisTimeout(long millisTimeout) {
        if (millisTimeout <= 0) {
            throw new RuntimeException("default millisTimeout below 0.");
        }
        this.millisTimeout = millisTimeout;
    }

    private void setLock(Lock lock) {
        if (lock == null) {
            throw new NullPointerException("lock is null.");
        }
        this.lock = lock;
    }

    public LockExecutor execute(BaseLockExecutorContext context, LockHandler handler) {
        return execute(context, millisTimeout, handler);
    }

    public LockExecutor execute(BaseLockExecutorContext context, long millisTimeout, LockHandler handler) {
        LOGGER.info("start to execute, context:{}, millisTimeout:{}", context, millisTimeout);
        if (context == null) {
            throw new NullPointerException("context is null.");
        }

        if (millisTimeout <= 0) {
            throw new NullPointerException("millisTimeout below 0.");
        }

        if (handler == null) {
            throw new NullPointerException("handler is null.");
        }
        String finalKey = converFinalKey(context);
        String value = converFinalValue(context);
        BaseLockExecutorContext tContext = null;
        boolean reLocked = false;
        boolean locked = false;
        boolean reentry = false;
        try {
            tContext = getLocalContext();
            if (tContext != null) {
                // 非重入，设置进线程变量中
                reentry = tContext.equals(context);
                LOGGER.info("reentry execute , tContext:{}, reentry:{}", tContext, reentry);
            }
            addLocalContext(context);

            if (reentry && tContext.isLocked()) {
                reLocked = true;
                locked = reLocked;
            }

            // 已获取锁后，不需要继续获取
            LOGGER.info("reLocked:{}", reLocked);
            if (!reLocked) {
                locked = lock.tryLock(finalKey, value, millisTimeout);
            }
            context.setLocked(locked);
            if (locked) {
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
            if (locked && !reLocked) {
                lock.unLock(finalKey, value);
            }
            // 删除当前context
            clearLocalContext();
            if (reentry) {
                // 恢复重入保留原有的context
                addLocalContext(tContext);
            }

            try {
                handler.onFinally(context);
            }
            catch (Exception e) {
                LOGGER.error("execute onFinally exception:{}", e);
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
