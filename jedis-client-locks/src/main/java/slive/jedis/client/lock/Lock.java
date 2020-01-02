package slive.jedis.client.lock;

import java.security.PrivateKey;
import java.util.concurrent.TimeUnit;

/**
 * 描述：<br>
 * 锁接口包括lock,tryLock,unlock等功能
 *
 * @author slive
 * @date 2020/1/1
 */
public interface Lock {

    /**
     * 获取锁
     * @param key 锁主键，需保证唯一性
     * @param ower 锁拥有者
     * @param millisTimeout 锁定时长，单位是ms
     * @return 返回是否锁定成功
     */
    boolean lock(String key, String ower, long millisTimeout);

    /**
     * 尝试获取锁，直到锁获取超时
     * @param key 锁主键，需保证唯一性
     * @param ower 锁拥有者
     * @param millisTimeout 锁定时长，单位是ms
     * @return 返回是否锁定成功
     */
    boolean tryLock(String key, String ower, long millisTimeout);

    /**
     * 解锁
     * @param key 锁主键，需保证唯一性
     * @param ower 锁拥有者
     */
    void unLock(String key, String ower);

}
