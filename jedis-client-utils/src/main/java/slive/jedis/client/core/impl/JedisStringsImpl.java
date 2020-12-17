//==============================================================================
//
//	@author Slive
//	@date  2018-9-11
//
//==============================================================================
package slive.jedis.client.core.impl;

import java.util.Collections;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;
import slive.jedis.client.core.JedisStrings;
import slive.jedis.client.util.JsonUtils;

/**
 * 描述：redis String相关操作类，参考<b>http://www.redis.net.cn/order/"></b>
 * 
 */
public final class JedisStringsImpl extends JedisKeysImpl implements JedisStrings {

    public JedisStringsImpl(Jedis jedis) {
        super(jedis);
    }

    public JedisStringsImpl() {
        super();
    }

    /**
     * 获取值
     * @param key 键值，不为空
     */
    public String get(String key) {
        if (key == null) {
            return null;
        }
        return jedis.get(key);
    }

    /**
     * 获取值，并转换为pojoObject对象
     * @param key 键值，不为空
     * @param pojoClazz 值对象，pojo类或者String，不为空
     */
    public <T> T get(String key, Class<T> pojoClazz) {
        String ret = get(key);
        if (ret != null) {
            return JsonUtils.convert2Object(ret, pojoClazz);
        }
        return null;
    }

    /**
     * 获取存储在指定 key 中字符串的子字符串。字符串的截取范围由 start 和 end 两个偏移量决定(包括 start 和 end 在内)。
     * @param key 键值，不为空
     * @param startOffset
     * @param endOffset
     */
    public String getrange(String key, long startOffset, long endOffset) {
        if (key == null) {
            return null;
        }
        return jedis.getrange(key, startOffset, endOffset);
    }

    /**
     * 设置新值，并返回旧值，当 key 没有旧值时，即 key 不存在时，返回null
     * @param key 键值，不为空
     * @param newPojoObject 值对象，pojo类或者String，不为空
     */
    @SuppressWarnings("unchecked")
    public <T> T getset(String key, T newPojoObject) {
        if (key == null || newPojoObject == null) {
            return null;
        }
        boolean isString = false;
        String newVal = JsonUtils.convert2String(newPojoObject);
        String ret = jedis.getSet(key, newVal);
        if (ret != null) {
            if (isString) {
                return (T) ret;
            }
            return (T) JsonUtils.convert2Object(ret, newPojoObject.getClass());
        }
        return null;
    }

    /**
     * 字符串值指定偏移量上的位(bit), 当偏移量 OFFSET 比字符串值的长度大，或者 key 不存在时，返回 flase
     * @param key
     * @param offset
     * @return
     */
    public boolean getbit(String key, long offset) {
        if (key == null) {
            return false;
        }
        return jedis.getbit(key, offset);
    }

    /**
     * 获取所有(一个或多个)给定 key 的值。 如果给定的 key 里面，有某个 key 不存在，那么这个 key 返回特殊值 null
     * @param keys
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> mget(String... keys) {
        if (keys == null) {
            return Collections.EMPTY_LIST;
        }
        return jedis.mget(keys);
    }

    /**
     * 获取所有(一个或多个)给定 key 的值。 如果给定的 key 里面，有某个 key 不存在，那么这个 key 返回特殊值 null
     * @param keys
     * @param pojoClazz 值对象，pojo类或者String，不为空
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> mget(Class<T> pojoClazz, String... keys) {
        List<String> mget = mget(keys);
        if (mget != null) {
            return JsonUtils.convert2ObjectList(mget, pojoClazz);
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * 用于对 key 所储存的字符串值，设置或清除指定偏移量上的位(bit)
     * @param key
     * @param offset
     * @param value
     * @return 指定偏移量原来储存的位值
     */
    public boolean setbit(String key, long offset, boolean value) {
        if (key == null) {
            return false;
        }
        return jedis.setbit(key, offset, value);
    }

    /**
     * 设置值
     * @param key 键值，不为空
     * @param pojoObject 值对象，pojo类或者String，不为空
     */
    public boolean set(String key, Object pojoObject) {
        return _set(key, pojoObject, false);
    }

    /**
     * 为指定的 key 设置值及其过期时间。如果 key 已经存在， SETEX 命令将会替换旧的值
     * @param key
     * @param seconds 超时时间，秒
     * @param pojoObject 值对象，pojo类或者String，不为空
     * @return 返回设置成功与否
     */
    public boolean setex(String key, int seconds, Object pojoObject) {
        if (key == null || pojoObject == null) {
            return false;
        }
        String setVal = JsonUtils.convert2String(pojoObject);
        return RET_OK_STR.equals(jedis.setex(key, seconds, setVal));
    }

    /**
     * 以毫秒为单位设置 key 的生存时间
     * @param key
     * @param millisseconds 毫秒
     * @param pojoObject
     * @return
     */
    public boolean psetex(String key, long millisseconds, Object pojoObject) {
        if (key == null || pojoObject == null) {
            return false;
        }
        String setVal = JsonUtils.convert2String(pojoObject);
        return RET_OK_STR.equals(jedis.psetex(key, millisseconds, setVal));
    }

    /**
     * 在指定的 key 不存在时，为 key 设置指定的值
     * @param key 键值，不为空
     * @param pojoObject 值对象，pojo类或者String，不为空
     * @param millisseconds 超时时间ms
     */
    public boolean psetnx(String key, long millisseconds, Object pojoObject) {
        if (key == null || pojoObject == null) {
            return false;
        }
        String setVal = JsonUtils.convert2String(pojoObject);
        SetParams params = new SetParams();
        params.nx().px(millisseconds);
        return RET_OK_STR.equals(jedis.set(key, setVal, params));
    }

    /**
     * 在指定的 key 不存在时，为 key 设置指定的值
     * @param key 键值，不为空
     * @param pojoObject 值对象，pojo类或者String，不为空
     */
    public boolean setnx(String key, Object pojoObject) {
        return _set(key, pojoObject, true);
    }

    private boolean _set(String key, Object pojoObject, boolean whenExist) {
        if (key == null || pojoObject == null) {
            return false;
        }
        String setVal = JsonUtils.convert2String(pojoObject);
        if (whenExist) {
            return RET_OK_INT == jedis.setnx(key, setVal);
        }
        else {
            return RET_OK_STR.equals(jedis.set(key, setVal));
        }
    }

    /**
     * 在指定的 key 不存在时，为 key 设置指定的值
     * @param key 键值，不为空
     * @param seconds 超时时间s
     * @param pojoObject 值对象，pojo类或者String，不为空
     */
    public boolean setnx(String key, int seconds, Object pojoObject) {
        if (key == null || pojoObject == null) {
            return false;
        }
        String setVal = JsonUtils.convert2String(pojoObject);
        SetParams params = new SetParams();
        params.nx().ex(seconds);
        return RET_OK_STR.equals(jedis.set(key, setVal, params));
    }

    /**
     * 用指定的字符串覆盖给定 key 所储存的字符串值，覆盖的位置从偏移量 offset 开始
     * @param key
     * @param offset
     * @param value
     * @return 被修改后的字符串长度
     */
    public long setrange(String key, long offset, String value) {
        if (key == null || value == null) {
            return DEFAULT_VAL_INT;
        }
        return jedis.setrange(key, offset, value);
    }

    /**
     * 获取指定 key 所储存的字符串值的长度
     * @param key
     * @return 字符串值的长度。 当 key 不存在时，返回 0
     */
    public long strlen(String key) {
        if (key == null) {
            return DEFAULT_VAL_INT;
        }
        return jedis.strlen(key);
    }

    /**
     * 同时设置一个或多个 key-value 对
     * @param keysvalues
     * @return
     */
    public boolean mset(String... keysvalues) {
        if (keysvalues == null) {
            return false;
        }
        return RET_OK_STR.equals(jedis.mset(keysvalues));
    }

    /**
     * 用于所有给定 key 都不存在时，同时设置一个或多个 key-value 对
     * @param keysvalues
     * @return
     */
    public boolean msetnx(String... keysvalues) {
        if (keysvalues == null) {
            return false;
        }
        return RET_OK_INT == jedis.msetnx(keysvalues);
    }

    /**
     * 将 key 中储存的数字值增一,如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。 本操作的值限制在 64
     * 位(bit)有符号数字表示之内
     * @param key
     * @return 执行命令之后 key 的值
     */
    public long incr(String key) {
        if (key == null) {
            return DEFAULT_VAL_INT;
        }
        return jedis.incr(key);
    }

    /**
     * 将 key 中储存的数字加上指定的增量值,如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCRBY 操作。 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * 本操作的值限制在 64 位(bit)有符号数字表示之内
     * @param key
     * @param value
     * @return 执行命令之后 key 的值
     */
    public long incrBy(String key, long value) {
        if (key == null) {
            return DEFAULT_VAL_INT;
        }
        return jedis.incrBy(key, value);
    }

    /**
     * 为 key 中所储存的值加上指定的浮点数增量值。 如果 key 不存在，那么 INCRBYFLOAT 会先将 key 的值设为 0 ，再执行加法操作
     * @param key
     * @param value
     * @return 执行命令之后 key 的值
     */
    public double incrByFloat(String key, double value) {
        if (key == null) {
            return DEFAULT_VAL_INT;
        }
        return jedis.incrByFloat(key, value);
    }

    /**
     * 将 key 中储存的数字值减一,如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作。 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。 本操作的值限制在 64
     * 位(bit)有符号数字表示之内
     * @param key
     * @return 执行命令之后 key 的值
     */
    public long decr(String key) {
        if (key == null) {
            return DEFAULT_VAL_INT;
        }
        return jedis.decr(key);
    }

    /**
     * 将 key 中储存的数字加上指定的减量值,如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECRBY 操作。 如果值包含错误的类型， 或字符串类型的值不能表示为数字，那么返回一个错误。
     * 本操作的值限制在 64 位(bit)有符号数字表示之内
     * @param key
     * @param value
     * @return 执行命令之后 key 的值
     */
    public long decrBy(String key, long value) {
        if (key == null) {
            return DEFAULT_VAL_INT;
        }
        return jedis.decrBy(key, value);
    }

    /**
     * 命令用于为指定的 key 追加值。 如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾。 如果 key 不存在， APPEND 就简单地将给定 key 设为 value
     * ，就像执行 SET key value 一样
     * @param key
     * @param value
     * @return 追加指定值之后， key 中字符串的长度
     */
    public long append(String key, String value) {
        return jedis.append(key, value);
    }
}
