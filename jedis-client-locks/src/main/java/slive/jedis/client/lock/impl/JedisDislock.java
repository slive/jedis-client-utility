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
public class JedisDislock implements Lock {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(JedisDislock.class);

    private JedisStrings jStrings = JedisUtils.Strings;

    public boolean lock(String key, String value, long millisTimeout) {
        boolean setnx = (boolean) jStrings.setnx(key, value);
        if (setnx) {
            return jStrings.pexpire(key, millisTimeout);
        }
        return false;
    }

    public boolean tryLock(String key, String value, long millisTimeout) {
        boolean ret = false;
        long lefTimeout = millisTimeout;
        long startTime = System.currentTimeMillis();
        long sleepTime = startTime;
        long tryTimes = 0;
        LOGGER.info("start to tryLock, key:{}, value:{}", key, value);
        do {
            tryTimes++;
            sleepTime = System.currentTimeMillis();
            ret = lock(key, value, millisTimeout);
            // 等待下一次循环
            if (!ret) {
                try {
                    Thread.sleep(Math.round((System.currentTimeMillis() - sleepTime) * 0.5 + 10));
                }
                catch (InterruptedException e) {
                    // ignore
                }
            }

            // 计算剩余时间，如果剩余时间太短，可能没必提供锁
            lefTimeout = (lefTimeout - (System.currentTimeMillis() - startTime));
            if (lefTimeout < 100) {
                unLock(key, value);
                break;
            }
        }
        while (!ret);
        LOGGER.info("start to tryLock, isLocked:{}, spendTime:{}, try times:{}", ret,
                (System.currentTimeMillis() - startTime), tryTimes);
        return ret;
    }

    public void unLock(String key, String value) {
        String v = jStrings.get(key);
        if (v != null && v.equals(value)) {
            jStrings.del(key);
        }
    }
}
