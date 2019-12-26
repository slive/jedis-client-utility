//==============================================================================
//
//	@author Slive
//	@date  2018-9-12
//
//==============================================================================
package slive.jedis.core.inter;

import slive.jedis.core.JedisHashes;
import java.util.*;
import java.util.Map.Entry;

import redis.clients.jedis.Jedis;

/**
 * 描述：redis Hash相关操作类，参考<b>http://www.redis.net.cn/order/"></b>
 * 
 */
public final class JedisHashesImpl extends JedisKeysImpl implements JedisHashes {

    public JedisHashesImpl(Jedis jedis) {
        super(jedis);
    }

    public JedisHashesImpl(){
        super();
    }

    /**
     * 用于删除哈希表 key 中的一个或多个指定字段，不存在的字段将被忽略
     * @param key
     * @param fieldKeys
     * @return 返回删除数量
     */
    public long hdel(String key, String... fieldKeys) {
        if (key == null || fieldKeys == null) {
            return DEFAULT_VAL_INT;
        }
        Long ret = jedis.hdel(key, fieldKeys);
        return ret != null ? ret : DEFAULT_VAL_INT;
    }

    /**
     * 用于查看哈希表的指定字段是否存在
     * @param key
     * @param fieldKey
     * @return
     */
    public boolean hexists(String key, String fieldKey) {
        if (key == null || fieldKey == null) {
            return false;
        }
        Boolean ret = jedis.hexists(key, fieldKey);
        return ret != null ? ret : false;
    }

    /**
     * 用于返回哈希表中指定字段的值。
     * @param key
     * @param fieldKey
     * @return 返回字段值
     */
    public String hget(String key, String fieldKey) {
        if (key == null || fieldKey == null) {
            return null;
        }
        return jedis.hget(key, fieldKey);
    }

    /**
     * 用于返回哈希表中指定字段的值。
     * @param key
     * @param fieldKey
     * @param pojoClazz pojo对象类型
     * @return 返回字段对象
     */
    public <T> T hget(String key, String fieldKey, Class<T> pojoClazz) {
        String ret = hget(key, fieldKey);
        if (ret != null) {
            return slive.jedis.util.PojoJsonUtils.convert2Object(ret, pojoClazz);
        }
        return null;
    }

    /**
     * 用于返回哈希表中，所有的字段和值。
     * @param key
     * @param pojoClazz pojo值对象类型
     * @return 返回所有的字段和值
     */
    public Map<String, String> hgetAll(String key) {
        if (key == null) {
            return null;
        }
        return jedis.hgetAll(key);
    }

    /**
     * 用于返回哈希表中，所有的字段和值。
     * @param key
     * @return 返回所有的字段和值
     */
    public <T> Map<String, T> hgetAll(String key, Class<T> pojoClazz) {
        Map<String, String> ret = hgetAll(key);
        if (ret != null) {
            Map<String, T> retMap = new HashMap<String, T>(ret.size());
            Set<Entry<String, String>> entrySet = ret.entrySet();
            T t = null;
            for (Entry<String, String> e : entrySet) {
                t = slive.jedis.util.PojoJsonUtils.convert2Object(e.getValue(), pojoClazz);
                if (t != null) {
                    retMap.put(e.getKey(), t);
                }
            }
            return retMap;
        }
        return null;
    }

    /**
     * 命令用于返回哈希表中，一个或多个给定字段的值。 如果指定的字段不存在于哈希表，那么返回一个 nil 值
     * @param key
     * @param fieldKeys
     * @return 一个包含多个给定字段关联值的表，表值的排列顺序和指定字段的请求顺序一样
     */
    public List<String> hmget(String key, String... fieldKeys) {
        if (key == null || fieldKeys == null) {
            return null;
        }
        return jedis.hmget(key, fieldKeys);
    }

    /**
     * 命令用于返回哈希表中，一个或多个给定字段的值。 如果指定的字段不存在于哈希表，那么返回一个 nil 值
     * @param key
     * @param pojoClazz pojo值对象类型
     * @param fieldKeys
     * @return 一个包含多个给定字段关联值的表，表值的排列顺序和指定字段的请求顺序一样
     */
    public <T> List<T> hmget(String key, Class<T> clazz, String... fieldKeys) {
        List<String> ret = hmget(key, fieldKeys);
        if (ret != null) {
            List<T> retList = new LinkedList<T>();
            for (String r : ret) {
                retList.add(slive.jedis.util.PojoJsonUtils.convert2Object(r, clazz));
            }
            return retList;
        }
        return null;
    }

    /**
     * 命令用于同时将多个 field-value (字段-值)对设置到哈希表中。 此命令会覆盖哈希表中已存在的字段。 如果哈希表不存在，会创建一个空哈希表，并执行 HMSET 操作
     * @param key
     * @param hash
     * @return
     */
    public boolean hmset(String key, Map<String, Object> hash) {
        if (key == null || hash == null || hash.isEmpty()) {
            return false;
        }

        Map<String, String> newHash = new LinkedHashMap<String, String>();
        Set<Entry<String, Object>> entrySet = hash.entrySet();
        for (Entry<String, Object> e : entrySet) {
            newHash.put(e.getKey(), slive.jedis.util.PojoJsonUtils.convert2String(e.getValue()));
        }
        return RET_OK_STR.equals(jedis.hmset(key, newHash));
    }

    /**
     * 用于为哈希表中的字段赋值 。 如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作。 如果字段已经存在于哈希表中，旧值将被覆盖
     * @param key
     * @param fieldKey
     * @param fieldVal
     * @return
     */
    public boolean hset(String key, String fieldKey, Object fieldVal) {
        if (key == null || fieldKey == null || fieldVal == null) {
            return false;
        }
        return RET_OK_INT == jedis.hset(key, fieldKey, slive.jedis.util.PojoJsonUtils.convert2String(fieldVal));
    }

    /**
     * 用于为哈希表中不存在的的字段赋值 。 如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作。 如果字段已经存在于哈希表中，操作无效。 如果 key 不存在，一个新哈希表被创建并执行 HSETNX 命令。
     * @param key
     * @param fieldKey
     * @param fieldVal
     * @return
     */
    public boolean hsetnx(String key, String fieldKey, Object fieldVal) {
        if (key == null || fieldKey == null || fieldVal == null) {
            return false;
        }
        return RET_OK_INT == jedis.hsetnx(key, fieldKey, slive.jedis.util.PojoJsonUtils.convert2String(fieldVal));
    }

    /**
     * 命令用于为哈希表中的字段值加上指定增量值。 增量也可以为负数，相当于对指定字段进行减法操作。 如果哈希表的 key 不存在，一个新的哈希表被创建并执行 HINCRBY 命令。
     * 如果指定的字段不存在，那么在执行命令前，字段的值被初始化为 0 。 对一个储存字符串值的字段执行 HINCRBY 命令将造成一个错误。 本操作的值被限制在 64 位(bit)有符号数字表示之内。
     * @param key
     * @param fieldKey
     * @param value
     * @return 执行 HINCRBY 命令之后，哈希表中字段的值
     */
    public long hincrBy(String key, String fieldKey, long value) {
        if (key == null || fieldKey == null) {
            return DEFAULT_VAL_INT;
        }
        return jedis.hincrBy(key, fieldKey, value);
    }

    /**
     * 用于为哈希表中的字段值加上指定浮点数增量值。 如果指定的字段不存在，那么在执行命令前，字段的值被初始化为 0
     * @param key
     * @param fieldKey
     * @param value
     * @return 执行 Hincrbyfloat 命令之后，哈希表中字段的值
     */
    public double hincrByFloat(String key, String fieldKey, double value) {
        if (key == null || fieldKey == null) {
            return DEFAULT_VAL_INT;
        }
        return jedis.hincrByFloat(key, fieldKey, value);
    }

    /**
     * 用于获取哈希表中的所有字段名。
     * @param key
     * @return 包含哈希表中所有字段的列表。 当 key 不存在时，返回一个空列表。
     */
    @SuppressWarnings("unchecked")
    public Set<String> hkeys(String key) {
        if (key == null) {
            return Collections.EMPTY_SET;
        }
        return jedis.hkeys(key);
    }

    /**
     * 返回哈希表所有字段的值
     * @param key
     * @return 包含哈希表中所有字段的列表。 当 key 不存在时，返回一个空列表。
     */
    @SuppressWarnings("unchecked")
    public List<String> hvals(String key) {
        if (key == null) {
            return Collections.EMPTY_LIST;
        }
        return jedis.hvals(key);
    }

    /**
     * 返回哈希表所有字段的值
     * @param key
     * @param pojoClazz pojo值对象类型
     * @return 包含哈希表中所有字段的列表。 当 key 不存在时，返回一个空列表。
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> hvals(String key, Class<T> pojoClazz) {
        List<String> ret = hvals(key);
        if (ret != null) {
            List<T> retList = new LinkedList<T>();
            for (String r : ret) {
                retList.add(slive.jedis.util.PojoJsonUtils.convert2Object(r, pojoClazz));
            }
            return retList;
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * 用于获取哈希表中字段的数量
     * @param key
     * @return 哈希表中字段的数量。 当 key 不存在时，返回 0
     */
    public long hlen(String key) {
        if (key == null) {
            return DEFAULT_VAL_INT;
        }
        return jedis.hlen(key);
    }
}
