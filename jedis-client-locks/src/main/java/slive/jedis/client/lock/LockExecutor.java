package slive.jedis.client.lock;

/**
 * 描述：<br>
 * 锁执行器接口
 *
 * @author slive
 * @date 2020/1/1
 */
public interface LockExecutor<T extends BaseLockExecutorContext> {

    /**
     * 获取定义锁的前缀，用于区分相同的key在不同模块中使用不同的锁
     * @return
     */
    String getPrefix();

    /**
     * 执行锁操作
     *
     * @param context 锁上下文，见{@link LockExecutorContext}
     * @param handler 在处理器中执行操作，见{@link LockHandler}
     * @return
     */
    LockExecutor execute(T context, LockHandler handler);

    /**
     * 执行锁操作
     *
     * @param context 锁上下文，见{@link LockExecutorContext}
     * @param handler 在处理器中执行操作，见{@link LockHandler}
     * @return
     */
    LockExecutor execute(T context, long millisTimeout, LockHandler handler);

}
