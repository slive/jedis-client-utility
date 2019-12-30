//==============================================================================
//
//	@author Slive
//	@date  2019-2-13
//
//==============================================================================
package slive.jedis.client.core;

import java.util.Map;
import java.util.Set;

import redis.clients.jedis.ScanResult;
import redis.clients.jedis.Tuple;

/**
 * 描述：
 * 
 */
public interface JedisSortSets extends JedisKeys {

    /**
     * 用于将一个或多个成员元素及其分数值加入到有序集当中。 如果某个成员已经是有序集的成员，那么更新这个成员的分数值，并通过重新插入这个成员元素，来保证该成员在正确的位置上。 分数值可以是整数值或双精度浮点数。 如果有序集合 key
     * 不存在，则创建一个空的有序集并执行 ZADD 操作。 当 key 存在但不是有序集类型时，返回一个错误。
     * @param key
     * @param score
     * @param member
     * @return 插入数量
     */
    long zadd(String key, double score, Object member);

    /**
     * 用于将一个或多个成员元素及其分数值加入到有序集当中。 如果某个成员已经是有序集的成员，那么更新这个成员的分数值，并通过重新插入这个成员元素，来保证该成员在正确的位置上。 分数值可以是整数值或双精度浮点数。 如果有序集合 key
     * 不存在，则创建一个空的有序集并执行 ZADD 操作。 当 key 存在但不是有序集类型时，返回一个错误。
     * @param key
     * @param score
     * @param member
     * @return 插入数量
     */
    <T> long zadd(String key, Map<T, Double> members);

    /**
     * 用于计算集合中元素的数量。
     * @param key
     * @return 当 key 存在且是有序集类型时，返回有序集的基数。 当 key 不存在时，返回 0 。
     */
    long zcar(String key);

    /**
     * 用于计算有序集合中指定分数区间的成员数量。
     * @param key
     * @param min
     * @param max
     * @return
     */
    long zcount(String key, double min, double max);

    /**
     * 对有序集合中指定成员的分数加上增量 increment 可以通过传递一个负数值 increment ，让分数减去相应的值，比如 ZINCRBY key -5 member ，就是让 member 的 score 值减去 5 。
     * 当 key 不存在，或分数不是 key 的成员时， ZINCRBY key increment member 等同于 ZADD key increment member 。 当 key 不是有序集类型时，返回一个错误。
     * 分数值可以是整数值或双精度浮点数。
     * @param key
     * @param score
     * @param member
     * @return 新分数值
     */
    double zincrby(String key, double score, Object member);

    /**
     * 计算给定的一个或多个有序集的交集，其中给定 key 的数量必须以 numkeys 参数指定，并将该交集(结果集)储存到 destination 。 默认情况下，结果集中某个成员的分数值是所有给定集下该成员分数值之和。
     * @param dstkey
     * @param sets
     * @return 保存到目标结果集的的成员数量。
     */
    long zinterstore(String dstkey, String... sets);

    /**
     * 在计算有序集合中指定字典区间内成员数量。
     * @param key
     * @param min
     * @param max
     * @return
     */
    long zlexcount(String key, String min, String max);

    /**
     * 返回有序集中，指定区间内的成员。 其中成员的位置按分数值递增(从小到大)来排序。 具有相同分数值的成员按字典序(lexicographical order )来排列。 如果你需要成员按 值递减(从大到小)来排列，请使用
     * ZREVRANGE 命令。 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。 你也可以使用负数下标，以 -1 表示最后一个成员， -2
     * 表示倒数第二个成员，以此类推。
     * @param key
     * @param start
     * @param end
     * @return 一个列表，包含指定区间内的元素。
     */
    Set<String> zrange(String key, long start, long end);

    /**
     * 返回有序集中，指定区间内的成员。 其中成员的位置按分数值递增(从小到大)来排序。 具有相同分数值的成员按字典序(lexicographical order )来排列。 如果你需要成员按 值递减(从大到小)来排列，请使用
     * ZREVRANGE 命令。 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。 你也可以使用负数下标，以 -1 表示最后一个成员， -2
     * 表示倒数第二个成员，以此类推。
     * @param key
     * @param start
     * @param end
     * @param pojoClazz
     * @return 一个列表，包含指定区间内的元素。
     */
    <T> Set<T> zrange(String key, long start, long end, Class<T> pojoClazz);

    /**
     * 通过字典区间返回有序集合的成员。
     * @param key
     * @param min
     * @param max
     * @return 指定区间内的元素列表。
     */
    Set<String> zrangeByLex(String key, String min, String max);

    /**
     * 通过字典区间返回有序集合的成员。
     * @param key
     * @param min
     * @param max
     * @param pojoClazz
     * @return 指定区间内的元素列表。
     */
    <T> Set<T> zrangeByLex(String key, String min, String max, Class<T> pojoClazz);

    /**
     * 通过区间返回有序集合的成员。
     * @param key
     * @param min
     * @param max
     * @return 指定区间内的元素列表。
     */
    Set<String> zrangeByScore(String key, long min, long max);

    /**
     * 通过区间返回有序集合的成员。
     * @param key
     * @param min
     * @param max
     * @param pojoClazz
     * @return 指定区间内的元素列表。
     */
    <T> Set<T> zrangeByScore(String key, long min, long max, Class<T> pojoClazz);

    /**
     * 有序集中指定成员的排名。其中有序集成员按分数值递增(从小到大)顺序排列。
     * @param key
     * @param member
     * @return
     */
    long zrank(String key, Object member);

    /**
     * 命令用于移除集合中的一个或多个成员元素，不存在的成员元素会被忽略。 当 key 不是集合类型，返回一个错误。
     * @param key
     * @param members
     * @return 被成功移除的元素的数量，不包括被忽略的元素。
     */
    <T> long zrem(String key, T... members);

    /**
     * 于移除有序集合中给定的字典区间的所有成员。
     * @param key
     * @param min
     * @param max
     * @return 被成功移除的成员的数量，不包括被忽略的成员。
     */
    long zremrangeBylex(String key, String min, String max);

    /**
     * 于移除有序集合中给定的区间的所有成员。
     * @param key
     * @param start
     * @param end
     * @return 被成功移除的成员的数量，不包括被忽略的成员。
     */
    long zremrangeByScore(String key, double start, double end);

    /**
     * 于移除有序集合中给定排名 的所有成员。
     * @param key
     * @param start
     * @param end
     * @return 被成功移除的成员的数量，不包括被忽略的成员。
     */
    long zremrangeByRank(String key, long start, long end);

    /**
     * 返回有序集中成员的排名。其中有序集成员按分数值递减(从大到小)排序。 排名以 0 为底，也就是说， 分数值最大的成员排名为 0 。 使用 ZRANK 命令可以获得成员按分数值递增(从小到大)排列的排名。
     * @param key
     * @param start
     * @param end
     * @return 一个列表，包含指定区间内的元素。
     */
    Set<String> zrevrange(String key, long start, long end);

    /**
     * 返回有序集中成员的排名。其中有序集成员按分数值递减(从大到小)排序。 排名以 0 为底，也就是说， 分数值最大的成员排名为 0 。 使用 ZRANK 命令可以获得成员按分数值递增(从小到大)排列的排名。
     * 表示倒数第二个成员，以此类推。
     * @param key
     * @param start
     * @param end
     * @param pojoClazz
     * @return 一个列表，包含指定区间内的元素。
     */
    <T> Set<T> zrevrange(String key, long start, long end, Class<T> pojoClazz);

    /**
     * 返回有序集中，成员的分数值。 如果成员元素不是有序集 key 的成员，或 key 不存在，返回 nil 。
     * @param key
     * @param member
     * @return
     */
    Double zscore(String key, Object member);

    /**
     * 计算给定的一个或多个有序集的并集，其中给定 key 的数量必须以 numkeys 参数指定，并将该并集(结果集)储存到 destination 。 默认情况下，结果集中某个成员的分数值是所有给定集下该成员分数值之和 。
     * @param dstkey
     * @param sets
     * @return 保存到目标结果集的的成员数量。
     */
    long zunionstore(String dstkey, String... sets);

    /**
     * 用于迭代有序集合中的元素（包括元素成员和元素分值）
     * @param key
     * @param cursor
     * @return
     */
    ScanResult<Tuple> zscan(String key, String cursor);

}