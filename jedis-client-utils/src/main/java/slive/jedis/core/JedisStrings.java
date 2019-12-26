//==============================================================================
//
//	@author Slive
//	@date  2019-2-13
//
//==============================================================================
package slive.jedis.core;

import java.util.List;

/**
 * 描述：
 * 
 */
public interface JedisStrings extends JedisKeys {

    /**
     * 获取值
     * @param key 键值，不为空
     */
    String get(String key);

    /**
     * 获取值，并转换为pojoObject对象
     * @param key 键值，不为空
     * @param pojoClazz 值对象，pojo类或者String，不为空
     */
    <T> T get(String key, Class<T> pojoClazz);

    /**
     * 获取存储在指定 key 中字符串的子字符串。字符串的截取范围由 start 和 end 两个偏移量决定(包括 start 和 end 在内)。
     * @param key 键值，不为空
     * @param startOffset
     * @param endOffset
     */
    String getrange(String key, long startOffset, long endOffset);

    /**
     * 设置新值，并返回旧值，当 key 没有旧值时，即 key 不存在时，返回null
     * @param key 键值，不为空
     * @param newPojoObject 值对象，pojo类或者String，不为空
     */
    <T> T getset(String key, T newPojoObject);

    /**
     * 字符串值指定偏移量上的位(bit), 当偏移量 OFFSET 比字符串值的长度大，或者 key 不存在时，返回 flase
     * @param key
     * @param offset
     * @return
     */
    boolean getbit(String key, long offset);

    /**
     * 获取所有(一个或多个)给定 key 的值。 如果给定的 key 里面，有某个 key 不存在，那么这个 key 返回特殊值 null
     * @param keys
     * @return
     */
    List<String> mget(String... keys);

    /**
     * 获取所有(一个或多个)给定 key 的值。 如果给定的 key 里面，有某个 key 不存在，那么这个 key 返回特殊值 null
     * @param keys
     * @param pojoClazz 值对象，pojo类或者String，不为空
     * @return
     */
    <T> List<T> mget(Class<T> pojoClazz, String... keys);

    /**
     * 用于对 key 所储存的字符串值，设置或清除指定偏移量上的位(bit)
     * @param key
     * @param offset
     * @param value
     * @return 指定偏移量原来储存的位值
     */
    boolean setbit(String key, long offset, boolean value);

    /**
     * 设置值
     * @param key 键值，不为空
     * @param pojoObject 值对象，pojo类或者String，不为空
     */
    boolean set(String key, Object pojoObject);

    /**
     * 为指定的 key 设置值及其过期时间。如果 key 已经存在， SETEX 命令将会替换旧的值
     * @param key
     * @param seconds 超时时间，秒
     * @param pojoObject 值对象，pojo类或者String，不为空
     * @return 返回设置成功与否
     */
    boolean setex(String key, int seconds, Object pojoObject);

    /**
     * 以毫秒为单位设置 key 的生存时间
     * @param key
     * @param millisseconds 毫秒
     * @param pojoObject
     * @return
     */
    boolean psetex(String key, long millisseconds, Object pojoObject);

    /**
     * 在指定的 key 不存在时，为 key 设置指定的值
     * @param key 键值，不为空
     * @param pojoObject 值对象，pojo类或者String，不为空
     */
    boolean setnx(String key, Object pojoObject);

    /**
     * 用指定的字符串覆盖给定 key 所储存的字符串值，覆盖的位置从偏移量 offset 开始
     * @param key
     * @param offset
     * @param value
     * @return 被修改后的字符串长度
     */
    long setrange(String key, long offset, String value);

    /**
     * 获取指定 key 所储存的字符串值的长度
     * @param key
     * @return 字符串值的长度。 当 key 不存在时，返回 0
     */
    long strlen(String key);

    /**
     * 同时设置一个或多个 key-value 对
     * @param keysvalues
     * @return
     */
    boolean mset(String... keysvalues);

    /**
     * 用于所有给定 key 都不存在时，同时设置一个或多个 key-value 对
     * @param keysvalues
     * @return
     */
    boolean msetnx(String... keysvalues);

    /**
     * 将 key 中储存的数字值增一,如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。 本操作的值限制在 64
     * 位(bit)有符号数字表示之内
     * @param key
     * @return 执行命令之后 key 的值
     */
    long incr(String key);

    /**
     * 将 key 中储存的数字加上指定的增量值,如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCRBY 操作。 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * 本操作的值限制在 64 位(bit)有符号数字表示之内
     * @param key
     * @param value
     * @return 执行命令之后 key 的值
     */
    long incrBy(String key, long value);

    /**
     * 为 key 中所储存的值加上指定的浮点数增量值。 如果 key 不存在，那么 INCRBYFLOAT 会先将 key 的值设为 0 ，再执行加法操作
     * @param key
     * @param value
     * @return 执行命令之后 key 的值
     */
    double incrByFloat(String key, double value);

    /**
     * 将 key 中储存的数字值减一,如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作。 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。 本操作的值限制在 64
     * 位(bit)有符号数字表示之内
     * @param key
     * @return 执行命令之后 key 的值
     */
    long decr(String key);

    /**
     * 将 key 中储存的数字加上指定的减量值,如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECRBY 操作。 如果值包含错误的类型， 或字符串类型的值不能表示为数字，那么返回一个错误。
     * 本操作的值限制在 64 位(bit)有符号数字表示之内
     * @param key
     * @param value
     * @return 执行命令之后 key 的值
     */
    long decrBy(String key, long value);

    /**
     * 命令用于为指定的 key 追加值。 如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾。 如果 key 不存在， APPEND 就简单地将给定 key 设为 value
     * ，就像执行 SET key value 一样
     * @param key
     * @param value
     * @return 追加指定值之后， key 中字符串的长度
     */
    long append(String key, String value);

}