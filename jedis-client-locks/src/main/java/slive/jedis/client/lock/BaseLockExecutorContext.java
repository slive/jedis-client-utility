package slive.jedis.client.lock;

/**
 * 描述：<br>
 * 通用实现的基本的锁执行器上下文
 *
 * @author slive
 * @date 2020/1/1
 */
public class BaseLockExecutorContext implements LockExecutorContext {

    private static final String CLASS_NAME = BaseLockExecutorContext.class.getSimpleName();

    private String key;

    private String owner = "";

    private String action = "";

    private long timeout;

    private boolean locked = false;

    private long startTime = System.currentTimeMillis();

    private long lockedTime = System.currentTimeMillis();

    private long handledTime = System.currentTimeMillis();

    private long endTime = System.currentTimeMillis();

    private Object result;

    public BaseLockExecutorContext(String key) {
        setKey(key);
    }

    public BaseLockExecutorContext(String key, long timeout) {
        setKey(key);
        setTimeout(timeout);
    }

    public String getKey() {
        return key;
    }

    private void setKey(String key) {
        if (key == null) {
            throw new NullPointerException("key is null.");
        }
        this.key = key;
    }

    /**
     * 锁的拥有者
     * @return
     */
    public String getOwner() {
        return owner;
    }

    BaseLockExecutorContext setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public String getAction() {
        return action;
    }

    BaseLockExecutorContext setAction(String action) {
        this.action = action;
        return this;
    }

    public boolean isStillLocked() {
        return locked && !isTimeout();
    }

    public boolean isTimeout() {
        return (timeout - (System.currentTimeMillis() - startTime)) <= 0;
    }

    BaseLockExecutorContext setLocked(boolean locked) {
        this.locked = locked;
        this.lockedTime = System.currentTimeMillis();
        return this;
    }

    /**
     * 获取设置的超时时间ms
     * @return
     */
    public long getTimeout() {
        return timeout;
    }

    public BaseLockExecutorContext setTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    public long getStartTime() {
        return startTime;
    }

    BaseLockExecutorContext setStartTime(long startTime) {
        this.startTime = startTime;
        return this;
    }

    public long getLockSpendTime() {
        return Math.max(0, lockedTime - startTime);
    }

    BaseLockExecutorContext setLockedTime(long lockedTime) {
        this.lockedTime = lockedTime;
        return this;
    }

    public long getHandleSpendTime() {
        return Math.max(0, handledTime - lockedTime);
    }

    BaseLockExecutorContext setHandledTime() {
        this.handledTime = System.currentTimeMillis();
        return this;
    }

    public long getTotalSpendTime() {
        return Math.max(0, endTime - startTime);
    }

    BaseLockExecutorContext setEndTime() {
        this.endTime = System.currentTimeMillis();
        return this;
    }

    public Object getResult() {
        return result;
    }

    BaseLockExecutorContext setResult(Object result) {
        this.result = result;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BaseLockExecutorContext that = (BaseLockExecutorContext) o;
        return key != null ? key.equals(that.key) : that.key == null;
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    @Override
    public String toString() {
        StringBuilder sbd = new StringBuilder();
        sbd.append(CLASS_NAME).append(" [key=").append(getKey());
        sbd.append(", owner=").append(getOwner());
        sbd.append(", action=").append(getAction());
        sbd.append(", startTime=").append(getStartTime());
        sbd.append(", isLocked=").append(locked);
        sbd.append(", isTimeout=").append(isTimeout());
        sbd.append(", timeout(ms)=").append(getTimeout());
        sbd.append(", lockSpendTime(ms)=").append(getLockSpendTime());
        sbd.append(", handleSpendTime(ms)=").append(getHandleSpendTime());
        sbd.append(", totalSpendTime(ms)=").append(getTotalSpendTime());
        if (getResult() != null) {
            sbd.append(", result=").append(getResult());
        }
        sbd.append("]");
        return sbd.toString();
    }

    public String toSimpleString() {
        StringBuilder sbd = new StringBuilder();
        sbd.append("[key=").append(getKey());
        sbd.append(", owner=").append(getOwner());
        sbd.append(", action=").append(getAction());
        sbd.append(", isLocked=").append(locked);
        sbd.append(", isTimeout=").append(isTimeout());
        sbd.append(", timeout(ms)=").append(getTimeout());
        sbd.append("]");
        return sbd.toString();
    }
}
