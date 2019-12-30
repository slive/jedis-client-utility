//==============================================================================
//
//	@author Slive
//	@date  2019-2-13
//
//==============================================================================
package slive.jedis.client.core;

import java.util.List;
import java.util.Set;

import redis.clients.jedis.ScanResult;

/**
 * 描述：
 * 
 */
public interface JedisSets extends JedisKeys {

    /**
     * 命令将一个或多个成员元素加入到集合中，已经存在于集合的成员元素将被忽略。 假如集合 key 不存在，则创建一个只包含添加的元素作成员的集合。 当集合 key 不是集合类型时，返回一个错误。
     * @param key
     * @param members 添加的成员值
     * @return 被添加到集合中的新元素的数量，不包括被忽略的元素。
     */
    <T> long sadd(String key, T... members);

    /**
     * 命令返回集合中元素的数量
     * @param key
     * @return
     */
    long scard(String key);

    /**
     * 返回给定集合之间的差集。不存在的集合 key 将视为空集。
     * @param keys
     * @return 包含差集成员的列表。
     */
    Set<String> sdiff(String... keys);

    /**
     * 返回给定集合之间的差集。不存在的集合 key 将视为空集。
     * @param keys
     * @param pojoClazz
     * @return 包含差集成员的列表。
     */
    <T> Set<T> sdiff(Class<T> pojoClazz, String... keys);

    /**
     * 将给定集合之间的差集存储在指定的集合中。如果指定的集合 key 已存在，则会被覆盖。
     * @param dstkey 指定集合key
     * @param keys 给定集合keys
     * @return 结果集中的元素数量
     */
    long sdiffstore(String dstkey, String... keys);

    /**
     * 命令返回给定所有给定集合的交集。 不存在的集合 key 被视为空集。 当给定集合当中有一个空集时，结果也为空集(根据集合运算定律)。
     * @param keys
     * @return 包含交集成员的列表。
     */
    Set<String> sinter(String... keys);

    /**
     * 命令返回给定所有给定集合的交集。 不存在的集合 key 被视为空集。 当给定集合当中有一个空集时，结果也为空集(根据集合运算定律)。
     * @param keys
     * @param pojoClazz
     * @return 包含交集成员的列表。
     */
    <T> Set<T> sinter(Class<T> pojoClazz, String... keys);

    /**
     * 将给定集合之间的交集存储在指定的集合中。如果指定的集合已经存在，则将其覆盖。
     * @param dstkey 指定集合key
     * @param keys 给定集合keys
     * @return 结果集中的元素数量
     */
    long sinterstore(String dstkey, String... keys);

    /**
     * 判断成员元素是否是集合的成员。
     * @param key
     * @param pojoMember 成员函数pojo类
     * @return
     */
    boolean sismember(String key, Object pojoMember);

    /**
     * 返回集合中的所有的成员。 不存在的集合 key 被视为空集合。
     * @param keys
     * @return 集合中的所有成员。
     */
    Set<String> smembers(String key);

    /**
     * 返回集合中的所有的成员。 不存在的集合 key 被视为空集合。
     * @param keys
     * @param pojoClazz
     * @return 集合中的所有成员。
     */
    <T> Set<T> smembers(String key, Class<T> pojoClazz);

    /**
     * 将指定成员 member 元素从 source 集合移动到 destination 集合。 SMOVE 是原子性操作。 如果 source 集合不存在或不包含指定的 member 元素，则 SMOVE
     * 命令不执行任何操作，仅返回 0 。否则， member 元素从 source 集合中被移除，并添加到 destination 集合中去。 当 destination 集合已经包含 member 元素时， SMOVE
     * 命令只是简单地将 source 集合中的 member 元素删除。 当 source 或 destination 不是集合类型时，返回一个错误。
     * @param srckey
     * @param dstkey
     * @param pojoMember
     * @return
     */
    boolean smove(String srckey, String dstkey, Object pojoMember);

    /**
     * 用于移除并返回集合中的一个随机元素。
     * @param key
     * @return
     */
    String spop(String key);

    /**
     * 命令用于返回集合中的一个随机元素。
     * @param key
     * @param pojoClazz
     * @return
     */
    <T> T spop(String key, Class<T> pojoClazz);

    /**
     * 命令用于返回集合中的一个随机元素。
     * @param key
     * @return
     */
    String srandmember(String key);

    /**
     * 用于移除并返回集合中的一个随机元素。
     * @param key
     * @param pojoClazz
     * @return
     */
    <T> T srandmember(String key, Class<T> pojoClazz);

    /**
     * 如果 count 为正数，且小于集合基数，那么命令返回一个包含 count 个元素的数组，数组中的元素各不相同。如果 count 大于等于集合基数，那么返回整个集合。 如果 count
     * 为负数，那么命令返回一个数组，数组中的元素可能会重复出现多次，而数组的长度为 count 的绝对值。 该操作和 SPOP 相似，但 SPOP 将随机元素从集合中移除并返回，而 Srandmember
     * 则仅仅返回随机元素，而不对集合进行任何改动。
     * @param key
     * @param count
     * @return 只提供集合 key 参数时，返回一个元素；如果集合为空，返回 nil 。 如果提供了 count 参数，那么返回一个数组；如果集合为空，返回空数组。
     */
    List<String> srandmember(String key, int count);

    /**
     * 如果 count 为正数，且小于集合基数，那么命令返回一个包含 count 个元素的数组，数组中的元素各不相同。如果 count 大于等于集合基数，那么返回整个集合。 如果 count
     * 为负数，那么命令返回一个数组，数组中的元素可能会重复出现多次，而数组的长度为 count 的绝对值。 该操作和 SPOP 相似，但 SPOP 将随机元素从集合中移除并返回，而 Srandmember
     * 则仅仅返回随机元素，而不对集合进行任何改动。
     * @param keys
     * @param pojoClazz
     * @return 只提供集合 key 参数时，返回一个元素；如果集合为空，返回 nil 。 如果提供了 count 参数，那么返回一个数组；如果集合为空，返回空数组。
     */
    <T> List<T> srandmember(String key, int count, Class<T> pojoClazz);

    /**
     * 命令用于移除集合中的一个或多个成员元素，不存在的成员元素会被忽略。 当 key 不是集合类型，返回一个错误。
     * @param key
     * @param members
     * @return 被成功移除的元素的数量，不包括被忽略的元素。
     */
    <T> long srem(String key, T... members);

    /**
     * 返回给定集合的并集。不存在的集合 key 被视为空集。
     * @param keys
     * @return 成员的列表。
     */
    Set<String> sunion(String... keys);

    /**
     * 返回给定集合的并集。不存在的集合 key 被视为空集。
     * @param keys
     * @param pojoClazz
     * @return 成员的列表。
     */
    <T> Set<T> sunion(Class<T> pojoClazz, String... keys);

    /**
     * 将给定集合的并集存储在指定的集合 destination 中。
     * @param dstkey 目标集合key
     * @param keys
     * @return
     */
    long sunionstore(String dstkey, String... keys);

    /**
     * 迭代集合键中的元素。
     * @param key
     * @param pattern
     * @return
     */
    ScanResult<String> sscan(String key, String pattern);

}