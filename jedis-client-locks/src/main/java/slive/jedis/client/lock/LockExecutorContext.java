package slive.jedis.client.lock;

/**
 * 描述：<br>
 * 锁执行器的上下文接口，用于记录锁主键，所属，动作，锁状态，耗时和结果等
 *
 * @author slive
 * @date 2020/1/1
 */
public interface LockExecutorContext {

    /**
     * 获取锁主键
     *
     * @return
     */
    String getKey();

    /**
     * 获取锁所属者
     *
     * @return
     */
    String getOwner();

    /**
     * 获取执行动作
     *
     * @return
     */
    String getAction();

    /**
     * 获取是否还是已锁定状态
     *
     * @return
     */
    boolean isStillLocked();

    /**
     * 获取锁超时时间，如果小于0，则取执行器默认时间，默认单位ms
     *
     * @return
     */
    long getTimeout();

    /**
     * 设置执行锁超时时间
     *
     * @param timeout 锁超时时间ms
     * @return
     */
    LockExecutorContext setTimeout(long timeout);

    /**
     * 是否已超时
     *
     * @return
     */
    boolean isTimeout();

    /**
     * 获取开始时间戳
     *
     * @return
     */
    long getStartTime();

    /**
     * 获取锁耗时，单位ms
     *
     * @return
     */
    long getLockSpendTime();

    /**
     * 执行正常操作耗时，单位ms
     *
     * @return
     */
    long getHandleSpendTime();

    /**
     * 整个执行下来耗时，单位ms
     *
     * @return
     */
    long getTotalSpendTime();

    /**
     * 返回结果，需根据时间情况进行转换
     *
     * @return
     */
    Object getResult();

    /**
     * 返回简单的类信息
     *
     * @return
     */
    String toSimpleString();
}
