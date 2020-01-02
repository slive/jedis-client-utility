package slive.jedis.client.lock.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slive.jedis.client.lock.Lock;
import slive.jedis.client.util.JedisUtils;

/**
 * 描述：基于redis客户端实现的分布式锁
 *
 * @author slive
 * @date 2020/1/1
 */
public class JedisLock implements Lock {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(JedisLock.class);

    public boolean lock(String key, String ower, long millisTimeout) {
        boolean setnx = JedisUtils.Strings.setnx(key, ower);
        if (setnx) {
            return JedisUtils.Strings.pexpire(key, millisTimeout);
        }
        return false;
    }

    public boolean tryLock(String key, String ower, long millisTimeout) {
        boolean ret = false;
        long lefTimeout = millisTimeout;
        long startTime = System.currentTimeMillis();
        do {
            ret = lock(key, ower, millisTimeout);
            // 等待下一次循环
            if (!ret) {
                try {
                    Thread.sleep(50);
                }
                catch (InterruptedException e) {
                    // ignore
                }
            }

            // 计算剩余时间，如果剩余时间太短，可能没必提供锁
            lefTimeout = (lefTimeout - (System.currentTimeMillis() - startTime));
            if (lefTimeout < 100) {
                unLock(key, ower);
                break;
            }
        }
        while (!ret);
        return ret;
    }

    public void unLock(String key, String ower) {
        String v = JedisUtils.Strings.get(key);
        if (v != null && v.equals(ower)) {
            JedisUtils.Strings.del(key);
        }
    }
}
