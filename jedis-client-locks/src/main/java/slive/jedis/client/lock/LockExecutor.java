package slive.jedis.client.lock;

/**
 * 描述：<br>
 * 锁执行器接口，循环获取锁，直到成功或者失败后执行{@link LockHandler}操作
 *
 * @author slive
 * @date 2020/1/1
 */
public interface LockExecutor<T extends BaseLockExecutorContext> {

    /**
     * 获取定义锁的前缀，用于区分相同的key在不同模块中使用不同的锁
     *
     * @return
     */
    String getPrefix();

    /**
     * 获取默认锁超时时间，单位ms
     *
     * @return
     */
    long getDefaultMillisTimeout();

    /**
     * 执行锁操作
     *
     * @param context 锁上下文，见{@link LockExecutorContext}
     * @param handler 在处理器中执行操作，见{@link LockHandler}
     * @return
     */
    LockExecutor execute(T context, LockHandler handler);

}
