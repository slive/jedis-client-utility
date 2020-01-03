package slive.jedis.client.lock;

/**
 * 描述：<br>
 * 锁执行器的上下文操作
 *
 * @author slive
 * @date 2020/1/1
 */
public interface LockExecutorContext {

    String getKey();

    String getOwner();

    String getAction();

    boolean isLocked();

    long getStartTime();

    long getLockSpendTime();

    long getHandleSpendTime();

    long getTotalSpendTime();

    Object getResult();

    String toSimpleString();
}
