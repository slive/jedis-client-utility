package slive.jedis.client.lock;

/**
 * 描述：<br>
 *
 * @author slive
 * @date 2020/1/1
 */
public interface LockContext {

    String getKey();

    String getOwner();

    String getOperater();

    boolean isLocked();

    long getStartTime();

    long getLockSpendTime();

    long getHandleSpendTime();

    long getTotalSpendTime();

    Object getResult();
}
