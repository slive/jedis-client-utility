//==============================================================================
//
//	@author Slive
//	@date  2019-2-13
//
//==============================================================================
package slive.jedis.client.core;

import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * 描述：
 * 
 */
public interface JedisKeys {

    void initJedis(Jedis jedis);

    /**
     * 删除缓存值。
     * @param key
     * @return 被删除 key 的数量。
     */
    long del(String key);

    /**
     * 删除多个缓存值。
     * @param keys
     * @return 被删除 keys 的数量。
     */
    long del(String... keys);

    /**
     * 判断键是否存在。
     * @param key
     * @return 存在与否。
     */
    boolean exists(String key);

    /**
     * 设置过期时间，精确到秒
     * @param key 键值
     * @param seconds 多少秒后过期
     * @return 返回设置成功与否
     */
    boolean expire(String key, int seconds);

    /**
     * 设置过期时间，精确到毫秒
     * @param key 键值
     * @param milliseconds 多少毫秒后过期
     * @return 返回设置成功与否
     */
    boolean pexpire(String key, long milliseconds);

    /**
     * 设置某一个时刻过期时间
     * @param key 键值
     * @param unixTime 过期时间点（UNIX 时间戳格式设置 ）
     * @return 返回设置成功与否
     */
    boolean expireAt(String key, int unixTime);

    /**
     * 设置某一个时刻过期时间
     * @param key 键值
     * @param millisecondsTimestamp 过期时间点（时间戳）
     * @return 返回设置成功与否
     */
    boolean pexpireAt(String key, long millisecondsTimestamp);

    /**
     * 查找所有符合给定模式 pattern 的 key
     * @param pattern 给定模式
     * @return
     */
    Set<String> keys(String pattern);

    /**
     * 命令以秒为单位返回 key 的剩余过期时间
     * @param key
     * @return
     */
    long ttl(String key);

    /**
     * 命令以毫秒为单位返回 key 的剩余过期时间
     * @param key
     * @return
     */
    long pttl(String key);

    /**
     * 命令用于修改 key 的名称
     * @param oldkey 旧key
     * @param newkey 新key
     * @return
     */
    boolean rename(String oldkey, String newkey);

    /**
     * 命令用于修改 key 的名称，新key不存在修改成功
     * @param oldkey 旧key
     * @param newkey 新key
     * @return
     */
    boolean renamenx(String oldkey, String newkey);

    /**
     * 返回 key 所储存的值的类型
     * @param key
     * @return
     */
    ValueType type(String key);

    /**
     * 当值相等时删除
     * @param key
     * @param value
     */
    boolean delEqValue(String key, String value);

}