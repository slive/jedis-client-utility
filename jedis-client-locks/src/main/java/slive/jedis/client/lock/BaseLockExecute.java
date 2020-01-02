package slive.jedis.client.lock;

import org.apache.commons.lang.StringUtils;
import slive.jedis.client.lock.impl.JedisLock;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 描述：<br>
 *
 * @author slive
 * @date 2020/1/1
 */
public class BaseLockExecute implements LockExecute {

    private LockConf conf;

    private String prefix;

    private JedisLock lock = new JedisLock();

    public BaseLockExecute(String prefix, long timeout) {
        setPrefix(prefix);
        setLockConf(new BaseLockConf(timeout));
    }

    public BaseLockExecute(String prefix, LockConf conf) {
        setPrefix(prefix);
        setLockConf(conf);
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

    public LockExecute setLockConf(LockConf conf) {
        if (conf == null) {
            throw new NullPointerException("lockconf is null.");
        }
        this.conf = conf;
        return this;
    }

    public LockExecute execute(BaseLockContext context, LockHandler handler) {
        return execute(context, conf, handler);
    }

    public LockExecute execute(BaseLockContext context, LockConf conf, LockHandler handler) {
        if (context == null) {
            throw new NullPointerException("context is null.");
        }

        if (conf == null) {
            throw new NullPointerException("conf is null.");
        }

        if (handler == null) {
            throw new NullPointerException("handler is null.");
        }

        String finalKey = converFinalKey(context);
        String owner = converFinalOwner(context);
        boolean locked = lock.tryLock(finalKey, owner, conf.getTimeout());
        context.setLocked(locked);
        if (locked) {
            try {
                Object ret = handler.onHandle(context);
                context.setResult(ret).setHandledTime();
            }
            catch (Exception ex) {
                try {
                    Object ret = handler.onError(context, ex);
                    context.setResult(ret);
                }
                catch (Exception ex1) {
                    // ignore
                }
            }
            finally {
                lock.unLock(finalKey, owner);
            }
        }
        else {
            try {
                Object ret = handler.onFailed(context);
                context.setResult(ret);
            }
            catch (Exception ex2) {
                // ignore
            }
        }

        context.setEndTime();
        return this;
    }

    private static String converFinalOwner(BaseLockContext context) {
        String owner = context.getOwner();
        if (StringUtils.isEmpty(owner)) {
            owner = UUID.randomUUID().toString().replaceAll("-", "");
        }

        String operater = context.getOperater();
        if (StringUtils.isNotEmpty(operater)) {
            owner = owner + ":" + operater;
        }
        context.setOwner(owner);
        return owner;
    }

    private String converFinalKey(BaseLockContext context) {
        String key = context.getKey();
        if (key == null) {
            throw new NullPointerException("key is null.");
        }
        return prefix + "#" + key;
    }

    class BaseLockConf implements LockConf {

        private long timeout;

        private TimeUnit timeoutUnit = TimeUnit.MILLISECONDS;

        public BaseLockConf(long timeout) {
            setTimeout(timeout);
        }

        public long getTimeout() {
            return timeout;
        }

        public LockConf setTimeout(long timeout) {
            if (timeout <= 0) {
                throw new NullPointerException("timeout is null.");
            }
            this.timeout = timeout;
            return this;
        }

        public LockConf setTimeout(long timeout, TimeUnit timeoutUnit) {
            setTimeout(timeout);
            setTimeoutUnit(timeoutUnit);
            return this;
        }

        public TimeUnit getTimeoutUnit() {
            return timeoutUnit;
        }

        public LockConf setTimeoutUnit(TimeUnit timeoutUnit) {
            if (timeoutUnit == null) {
                throw new NullPointerException("timout unit is null.");
            }
            this.timeoutUnit = timeoutUnit;
            return this;
        }
    }
}
