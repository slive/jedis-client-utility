package slive.jedis.client.lock.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slive.jedis.client.core.JedisStrings;
import slive.jedis.client.lock.Lock;
import slive.jedis.client.util.JedisUtils;

/**
 * 描述：基于jedis客户端实现的分布式锁
 *
 * @author slive
 * @date 2020/1/1
 */
public class JedisDistlock implements Lock {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JedisDistlock.class);

    private static final long DEFAULT_WAITTIME = 50;

    private static final int MIX_LEFTTIME = 2;

    private JedisStrings jStrings = JedisUtils.Strings;

    public boolean lock(String key, String owner, long millisTimeout) {
        return jStrings.psetnx(key, millisTimeout, owner);
    }

    public boolean tryLock(String key, String owner, long millisTimeout) {
        boolean ret = false;
        long lefTimeout = millisTimeout;
        long startTime = System.currentTimeMillis();
        long sleepTime = startTime;
        long tryTimes = 0;
        LOGGER.info("start to tryLock, key:{}, value:{}", key, owner);
        do {
            tryTimes++;
            sleepTime = System.currentTimeMillis();
            ret = lock(key, owner, millisTimeout);
            // 等待下一次循环
            if (!ret) {
                try {
                    Thread.sleep(Math.round((System.currentTimeMillis() - sleepTime) * 0.5 + DEFAULT_WAITTIME));
                } catch (InterruptedException e) {
                    // ignore
                }
            }

            // 计算剩余时间，如果剩余时间太短，可能没必提供锁
            lefTimeout = (lefTimeout - (System.currentTimeMillis() - startTime));
            if (lefTimeout <= MIX_LEFTTIME) {
                unLock(key, owner);
                break;
            }
        }
        while (!ret);
        LOGGER.info("finish tryLock, isLocked:{}, spendTime:{}, try times:{}", ret,
                (System.currentTimeMillis() - startTime), tryTimes);
        return ret;
    }

    public void unLock(String key, String owner) {
        if (key != null) {
            boolean ret = jStrings.delEqValue(key, owner);
            LOGGER.info("finish unLock, key:{}, owner:{}, ret:{}", key, owner, ret);
        }
    }

    public boolean expireLock(String key, String owner, long millisTimeout) {
        String v = jStrings.get(key);
        if (v != null && v.equals(owner)) {
            return jStrings.pexpire(key, millisTimeout);
        }
        return false;
    }
}
