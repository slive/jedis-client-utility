//==============================================================================
//
//	@author Slive
//	@date  2018-9-5
//
//==============================================================================
package slive.jedis.client.core.impl;

import slive.jedis.client.core.JedisKeys;

import java.util.Collections;
import java.util.Set;

import redis.clients.jedis.Jedis;
import slive.jedis.client.core.ValueType;

/**
 * 描述：redis key相关功能，参考<b>http://www.redis.net.cn/order/"></b>
 * 
 */
public class JedisKeysImpl implements JedisKeys {

    protected static final int RET_OK_INT = 1;

    protected static final int RET_FAIL_INT = 0;

    protected static final String RET_OK_STR = "OK";

    protected static final int DEFAULT_VAL_INT = 0;

    private static final int MAX_VAL_INT = -1;

    protected Jedis jedis;

    public JedisKeysImpl(Jedis jedis) {
        if (jedis == null) {
            throw new NullPointerException("jedis does not allow null.");
        }
        this.jedis = jedis;
    }

    public JedisKeysImpl(){

    }

    public void initJedis(Jedis jedis) {
        this.jedis = jedis;
    }

    /**
     * 删除缓存值。
     * @param key
     * @return 被删除 key 的数量。
     */
    public long del(String key) {
        if (key == null) {
            return RET_FAIL_INT;
        }
        return jedis.del(key);
    }

    /**
     * 删除多个缓存值。
     * @param keys
     * @return 被删除 keys 的数量。
     */
    public long del(String... keys) {
        if (keys == null) {
            return RET_FAIL_INT;
        }
        return jedis.del(keys);
    }

    /**
     * 判断键是否存在。
     * @param key
     * @return 存在与否。
     */
    public boolean exists(String key) {
        if (key == null) {
            return false;
        }
        return jedis.exists(key);
    }

    /**
     * 设置过期时间，精确到秒
     * @param key 键值
     * @param seconds 多少秒后过期
     * @return 返回设置成功与否
     */
    public boolean expire(String key, int seconds) {
        if (key == null) {
            return false;
        }
        return RET_OK_INT == jedis.expire(key, seconds);
    }

    /**
     * 设置过期时间，精确到毫秒
     * @param key 键值
     * @param milliseconds 多少毫秒后过期
     * @return 返回设置成功与否
     */
    public boolean pexpire(String key, long milliseconds) {
        if (key == null) {
            return false;
        }
        return RET_OK_INT == jedis.pexpire(key, milliseconds);
    }

    /**
     * 设置某一个时刻过期时间
     * @param key 键值
     * @param unixTime 过期时间点（UNIX 时间戳格式设置 ）
     * @return 返回设置成功与否
     */
    public boolean expireAt(String key, int unixTime) {
        if (key == null) {
            return false;
        }
        return RET_OK_INT == jedis.expireAt(key, unixTime);
    }

    /**
     * 设置某一个时刻过期时间
     * @param key 键值
     * @param millisecondsTimestamp 过期时间点（时间戳）
     * @return 返回设置成功与否
     */
    public boolean pexpireAt(String key, long millisecondsTimestamp) {
        if (key == null) {
            return false;
        }
        return RET_OK_INT == jedis.pexpireAt(key, millisecondsTimestamp);
    }

    /**
     * 查找所有符合给定模式 pattern 的 key
     * @param pattern 给定模式
     * @return
     */
    @SuppressWarnings("unchecked")
    public Set<String> keys(String pattern) {
        if (pattern == null) {
            return Collections.EMPTY_SET;
        }
        return jedis.keys(pattern);
    }

    /**
     * 命令以秒为单位返回 key 的剩余过期时间
     * @param key
     * @return
     */
    public long ttl(String key) {
        if (key == null) {
            return MAX_VAL_INT;
        }
        return jedis.ttl(key);
    }

    /**
     * 命令以毫秒为单位返回 key 的剩余过期时间
     * @param key
     * @return
     */
    public long pttl(String key) {
        if (key == null) {
            return MAX_VAL_INT;
        }
        return jedis.pttl(key);
    }

    /**
     * 命令用于修改 key 的名称
     * @param oldkey 旧key
     * @param newkey 新key
     * @return
     */
    public boolean rename(String oldkey, String newkey) {
        if (oldkey == null || newkey == null) {
            return false;
        }
        return RET_OK_STR.equalsIgnoreCase(jedis.rename(oldkey, newkey));
    }

    /**
     * 命令用于修改 key 的名称，新key不存在修改成功
     * @param oldkey 旧key
     * @param newkey 新key
     * @return
     */
    public boolean renamenx(String oldkey, String newkey) {
        if (oldkey == null || newkey == null) {
            return false;
        }
        return RET_OK_INT == jedis.renamenx(oldkey, newkey);
    }

    /**
     * 返回 key 所储存的值的类型
     * @param key
     * @return
     */
    public ValueType type(String key) {
        if (key == null) {
            return ValueType.none;
        }
        return ValueType.getType(jedis.type(key));
    }

}
