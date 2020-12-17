package slive.jedis.client.lock;

/**
 * 描述：<br>
 * 锁处理器接口，获取锁前后执行的操作
 *
 * @author slive
 * @date 2020/1/1
 */
public interface LockHandler {

    /**
     * 在开始获取锁操作之前
     *
     * @param context 操作上下文
     */
    void beforeHandle(LockExecutorContext context);

    /**
     * 获得锁后的操作
     *
     * @param context 操作上下文
     * @return 返回结果
     */
    Object onHandle(LockExecutorContext context);

    /**
     * 获取锁失败后执行的操作，该操作不再锁中执行
     *
     * @param context 操作上下文
     */
    void onFailed(LockExecutorContext context);

    /**
     * 获取锁执行相关操作后，可能存在异常情况的操作，该操作仍然在锁中
     *
     * @param context 操作上下文
     * @param ex      异常信息
     */
    void onError(LockExecutorContext context, Throwable ex);

    /**
     * 释放锁后的操作
     *
     * @param context 操作上下文
     */
    void afterHandle(LockExecutorContext context);
}
