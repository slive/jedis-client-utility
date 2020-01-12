package slive.jedis.client.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 描述：<br>
 * 默认实现的锁处理器
 * @author slive
 * @date 2020/1/3
 */
public abstract class BaseLockHandler implements LockHandler {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseLockHandler.class);

    public void beforeHandle(LockExecutorContext context) {
        // nothing
    }

    public void onFailed(LockExecutorContext context) {
        LOGGER.warn("failed to execute, context:{}", context.toSimpleString());
    }

    public void onError(LockExecutorContext context, Throwable ex) {
        LOGGER.error("handle error, context:{}, error:{}", context, ex);
    }

    public void afterHandle(LockExecutorContext context) {
        // nothing
    }
}
