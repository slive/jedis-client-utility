package slive.jedis.client.lock;

/**
 * 描述：<br>
 *
 * @author slive
 * @date 2020/1/1
 */
public class BaseLockContext implements LockContext {

    private String key = null;

    private String owner = "";

    private String operater = "";

    private boolean locked = false;

    private long startTime = System.currentTimeMillis();

    private long lockedTime = System.currentTimeMillis();

    private long handledTime = System.currentTimeMillis();

    private long endTime = System.currentTimeMillis();

    private Object result = null;

    public String getKey() {
        return key;
    }

    BaseLockContext setKey(String key) {
        this.key = key;
        return this;
    }

    public String getOwner() {
        return owner;
    }

    BaseLockContext setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public String getOperater() {
        return operater;
    }

    BaseLockContext setOperater(String operater) {
        this.operater = operater;
        return this;
    }

    public boolean isLocked() {
        return locked;
    }

    BaseLockContext setLocked(boolean locked) {
        this.locked = locked;
        this.lockedTime = System.currentTimeMillis();
        return this;
    }

    public long getStartTime() {
        return startTime;
    }

    BaseLockContext setStartTime(long startTime) {
        this.startTime = startTime;
        return this;
    }

    public long getLockSpendTime() {
        return (lockedTime - startTime);
    }

    BaseLockContext setLockedTime(long lockedTime) {
        this.lockedTime = lockedTime;
        return this;
    }

    public long getHandleSpendTime() {
        return (handledTime - lockedTime);
    }

    BaseLockContext setHandledTime() {
        this.handledTime = System.currentTimeMillis();
        return this;
    }

    public long getTotalSpendTime() {
        return (endTime - startTime);
    }

    BaseLockContext setEndTime() {
        this.endTime = System.currentTimeMillis();
        return this;
    }

    public Object getResult() {
        return result;
    }

    BaseLockContext setResult(Object result) {
        this.result = result;
        return this;
    }
}
