//==============================================================================
//
//	@author Slive
//	@date  2019-2-13
//
//==============================================================================
package slive.jedis.core;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 描述：
 * 
 */
public interface JedisHashes extends JedisKeys {

    /**
     * 用于删除哈希表 key 中的一个或多个指定字段，不存在的字段将被忽略
     * @param key
     * @param fieldKeys
     * @return 返回删除数量
     */
    long hdel(String key, String... fieldKeys);

    /**
     * 用于查看哈希表的指定字段是否存在
     * @param key
     * @param fieldKey
     * @return
     */
    boolean hexists(String key, String fieldKey);

    /**
     * 用于返回哈希表中指定字段的值。
     * @param key
     * @param fieldKey
     * @return 返回字段值
     */
    String hget(String key, String fieldKey);

    /**
     * 用于返回哈希表中指定字段的值。
     * @param key
     * @param fieldKey
     * @param pojoClazz pojo对象类型
     * @return 返回字段对象
     */
    <T> T hget(String key, String fieldKey, Class<T> pojoClazz);

    /**
     * 用于返回哈希表中，所有的字段和值。
     * @param key
     * @param pojoClazz pojo值对象类型
     * @return 返回所有的字段和值
     */
    Map<String, String> hgetAll(String key);

    /**
     * 用于返回哈希表中，所有的字段和值。
     * @param key
     * @return 返回所有的字段和值
     */
    <T> Map<String, T> hgetAll(String key, Class<T> pojoClazz);

    /**
     * 命令用于返回哈希表中，一个或多个给定字段的值。 如果指定的字段不存在于哈希表，那么返回一个 nil 值
     * @param key
     * @param fieldKeys
     * @return 一个包含多个给定字段关联值的表，表值的排列顺序和指定字段的请求顺序一样
     */
    List<String> hmget(String key, String... fieldKeys);

    /**
     * 命令用于返回哈希表中，一个或多个给定字段的值。 如果指定的字段不存在于哈希表，那么返回一个 nil 值
     * @param key
     * @param pojoClazz pojo值对象类型
     * @param fieldKeys
     * @return 一个包含多个给定字段关联值的表，表值的排列顺序和指定字段的请求顺序一样
     */
    <T> List<T> hmget(String key, Class<T> clazz, String... fieldKeys);

    /**
     * 命令用于同时将多个 field-value (字段-值)对设置到哈希表中。 此命令会覆盖哈希表中已存在的字段。 如果哈希表不存在，会创建一个空哈希表，并执行 HMSET 操作
     * @param key
     * @param hash
     * @return
     */
    boolean hmset(String key, Map<String, Object> hash);

    /**
     * 用于为哈希表中的字段赋值 。 如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作。 如果字段已经存在于哈希表中，旧值将被覆盖
     * @param key
     * @param fieldKey
     * @param fieldVal
     * @return
     */
    boolean hset(String key, String fieldKey, Object fieldVal);

    /**
     * 用于为哈希表中不存在的的字段赋值 。 如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作。 如果字段已经存在于哈希表中，操作无效。 如果 key 不存在，一个新哈希表被创建并执行 HSETNX 命令。
     * @param key
     * @param fieldKey
     * @param fieldVal
     * @return
     */
    boolean hsetnx(String key, String fieldKey, Object fieldVal);

    /**
     * 命令用于为哈希表中的字段值加上指定增量值。 增量也可以为负数，相当于对指定字段进行减法操作。 如果哈希表的 key 不存在，一个新的哈希表被创建并执行 HINCRBY 命令。
     * 如果指定的字段不存在，那么在执行命令前，字段的值被初始化为 0 。 对一个储存字符串值的字段执行 HINCRBY 命令将造成一个错误。 本操作的值被限制在 64 位(bit)有符号数字表示之内。
     * @param key
     * @param fieldKey
     * @param value
     * @return 执行 HINCRBY 命令之后，哈希表中字段的值
     */
    long hincrBy(String key, String fieldKey, long value);

    /**
     * 用于为哈希表中的字段值加上指定浮点数增量值。 如果指定的字段不存在，那么在执行命令前，字段的值被初始化为 0
     * @param key
     * @param fieldKey
     * @param value
     * @return 执行 Hincrbyfloat 命令之后，哈希表中字段的值
     */
    double hincrByFloat(String key, String fieldKey, double value);

    /**
     * 用于获取哈希表中的所有字段名。
     * @param key
     * @return 包含哈希表中所有字段的列表。 当 key 不存在时，返回一个空列表。
     */
    Set<String> hkeys(String key);

    /**
     * 返回哈希表所有字段的值
     * @param key
     * @return 包含哈希表中所有字段的列表。 当 key 不存在时，返回一个空列表。
     */
    List<String> hvals(String key);

    /**
     * 返回哈希表所有字段的值
     * @param key
     * @param pojoClazz pojo值对象类型
     * @return 包含哈希表中所有字段的列表。 当 key 不存在时，返回一个空列表。
     */
    <T> List<T> hvals(String key, Class<T> pojoClazz);

    /**
     * 用于获取哈希表中字段的数量
     * @param key
     * @return 哈希表中字段的数量。 当 key 不存在时，返回 0
     */
    long hlen(String key);

}