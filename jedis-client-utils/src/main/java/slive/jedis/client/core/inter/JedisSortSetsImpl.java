//==============================================================================
//
//	@author Slive
//	@date  2018-9-13
//
//==============================================================================
package slive.jedis.client.core.inter;

import java.util.*;
import java.util.Map.Entry;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.Tuple;
import slive.jedis.client.util.PojoJsonUtils;
import slive.jedis.client.core.JedisSortSets;

/**
 * 描述：redis SortSet相关操作类，参考<b>http://www.redis.net.cn/order/"></b>
 *
 */
public final class JedisSortSetsImpl extends JedisKeysImpl implements JedisSortSets {

    public JedisSortSetsImpl(Jedis jedis) {
        super(jedis);
    }

    public JedisSortSetsImpl(){
        super();
    }

    /**
     * 用于将一个或多个成员元素及其分数值加入到有序集当中。 如果某个成员已经是有序集的成员，那么更新这个成员的分数值，并通过重新插入这个成员元素，来保证该成员在正确的位置上。 分数值可以是整数值或双精度浮点数。 如果有序集合 key
     * 不存在，则创建一个空的有序集并执行 ZADD 操作。 当 key 存在但不是有序集类型时，返回一个错误。
     * @param key
     * @param score
     * @param member
     * @return 插入数量
     */
    public long zadd(String key, double score, Object member) {
        if (key == null || member == null) {
            return DEFAULT_VAL_INT;
        }
        String val = PojoJsonUtils.convert2String(member);
        return jedis.zadd(key, score, val);
    }

    /**
     * 用于将一个或多个成员元素及其分数值加入到有序集当中。 如果某个成员已经是有序集的成员，那么更新这个成员的分数值，并通过重新插入这个成员元素，来保证该成员在正确的位置上。 分数值可以是整数值或双精度浮点数。 如果有序集合 key
     * 不存在，则创建一个空的有序集并执行 ZADD 操作。 当 key 存在但不是有序集类型时，返回一个错误。
     * @param key
     * @param score
     * @param member
     * @return 插入数量
     */
    public <T> long zadd(String key, Map<T, Double> members) {
        if (key == null || members == null) {
            return DEFAULT_VAL_INT;
        }
        Map<String, Double> vals = new LinkedHashMap<String, Double>(members.size());
        Set<Entry<T, Double>> entrySet = members.entrySet();
        for (Entry<T, Double> e : entrySet) {
            vals.put(PojoJsonUtils.convert2String(e.getKey()), e.getValue());
        }
        return jedis.zadd(key, vals);
    }

    /**
     * 用于计算集合中元素的数量。
     * @param key
     * @return 当 key 存在且是有序集类型时，返回有序集的基数。 当 key 不存在时，返回 0 。
     */
    public long zcar(String key) {
        if (key == null) {
            return DEFAULT_VAL_INT;
        }
        return jedis.zcard(key);
    }

    /**
     * 用于计算有序集合中指定分数区间的成员数量。
     * @param key
     * @param min
     * @param max
     * @return
     */
    public long zcount(String key, double min, double max) {
        if (key == null) {
            return DEFAULT_VAL_INT;
        }
        return jedis.zcount(key, min, max);
    }

    /**
     * 对有序集合中指定成员的分数加上增量 increment 可以通过传递一个负数值 increment ，让分数减去相应的值，比如 ZINCRBY key -5 member ，就是让 member 的 score 值减去 5 。
     * 当 key 不存在，或分数不是 key 的成员时， ZINCRBY key increment member 等同于 ZADD key increment member 。 当 key 不是有序集类型时，返回一个错误。
     * 分数值可以是整数值或双精度浮点数。
     * @param key
     * @param score
     * @param member
     * @return 新分数值
     */
    public double zincrby(String key, double score, Object member) {
        if (key == null || member == null) {
            return DEFAULT_VAL_INT;
        }
        String val = PojoJsonUtils.convert2String(member);
        return jedis.zincrby(key, score, val);
    }

    /**
     * 计算给定的一个或多个有序集的交集，其中给定 key 的数量必须以 numkeys 参数指定，并将该交集(结果集)储存到 destination 。 默认情况下，结果集中某个成员的分数值是所有给定集下该成员分数值之和。
     * @param dstkey
     * @param sets
     * @return 保存到目标结果集的的成员数量。
     */
    public long zinterstore(String dstkey, String... sets) {
        if (dstkey == null || sets == null) {
            return DEFAULT_VAL_INT;
        }
        return jedis.zinterstore(dstkey, sets);
    }

    /**
     * 在计算有序集合中指定字典区间内成员数量。
     * @param key
     * @param min
     * @param max
     * @return
     */
    public long zlexcount(String key, String min, String max) {
        if (key == null || min == null || max == null) {
            return DEFAULT_VAL_INT;
        }
        return jedis.zlexcount(key, min, max);
    }

    /**
     * 返回有序集中，指定区间内的成员。 其中成员的位置按分数值递增(从小到大)来排序。 具有相同分数值的成员按字典序(lexicographical order )来排列。 如果你需要成员按 值递减(从大到小)来排列，请使用
     * ZREVRANGE 命令。 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以表示有序集第二个成员，以此类推。 你也可以使用负数下标，以 -1 表示最后一个成员， -2
     * 表示倒数第二个成员，以此类推。
     * @param key
     * @param start
     * @param end
     * @return 一个列表，包含指定区间内的元素。
     */
    @SuppressWarnings("unchecked")
    public Set<String> zrange(String
            key, long start, long end) {
        if (key == null) {
            return Collections.EMPTY_SET;
        }
        return jedis.zrange(key, start, end);
    }

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
    @SuppressWarnings("unchecked")
    public <T> Set<T> zrange(String key, long start, long end, Class<T> pojoClazz) {
        Set<String> ret = zrange(key, start, end);
        if (ret == null || ret.isEmpty()) {
            return Collections.EMPTY_SET;
        }
        Set<T> retList = new LinkedHashSet<T>(ret.size());
        for (String s : ret) {
            retList.add(PojoJsonUtils.convert2Object(s, pojoClazz));
        }
        return retList;
    }

    /**
     * 通过字典区间返回有序集合的成员。
     * @param key
     * @param min
     * @param max
     * @return 指定区间内的元素列表。
     */
    @SuppressWarnings("unchecked")
    public Set<String> zrangeByLex(String key, String min, String max) {
        if (key == null || min == null || max == null) {
            return Collections.EMPTY_SET;
        }
        return jedis.zrangeByLex(key, min, max);
    }

    /**
     * 通过字典区间返回有序集合的成员。
     * @param key
     * @param min
     * @param max
     * @param pojoClazz
     * @return 指定区间内的元素列表。
     */
    @SuppressWarnings("unchecked")
    public <T> Set<T> zrangeByLex(String key, String min, String max, Class<T> pojoClazz) {
        Set<String> ret = zrangeByLex(key, min, max);
        if (ret == null || ret.isEmpty()) {
            return Collections.EMPTY_SET;
        }
        Set<T> retList = new LinkedHashSet<T>(ret.size());
        for (String s : ret) {
            retList.add(PojoJsonUtils.convert2Object(s, pojoClazz));
        }
        return retList;
    }

    /**
     * 通过区间返回有序集合的成员。
     * @param key
     * @param min
     * @param max
     * @return 指定区间内的元素列表。
     */
    @SuppressWarnings("unchecked")
    public Set<String> zrangeByScore(String key, long min, long max) {
        if (key == null) {
            return Collections.EMPTY_SET;
        }
        return jedis.zrangeByScore(key, min, max);
    }

    /**
     * 通过区间返回有序集合的成员。
     * @param key
     * @param min
     * @param max
     * @param pojoClazz
     * @return 指定区间内的元素列表。
     */
    @SuppressWarnings("unchecked")
    public <T> Set<T> zrangeByScore(String key, long min, long max, Class<T> pojoClazz) {
        Set<String> ret = zrangeByScore(key, min, max);
        if (ret == null || ret.isEmpty()) {
            return Collections.EMPTY_SET;
        }
        Set<T> retList = new LinkedHashSet<T>(ret.size());
        for (String s : ret) {
            retList.add(PojoJsonUtils.convert2Object(s, pojoClazz));
        }
        return retList;
    }

    /**
     * 有序集中指定成员的排名。其中有序集成员按分数值递增(从小到大)顺序排列。
     * @param key
     * @param member
     * @return
     */
    public long zrank(String key, Object member) {
        if (key == null || member == null) {
            return DEFAULT_VAL_INT;
        }
        Long zrank = jedis.zrank(key, PojoJsonUtils.convert2String(member));
        return zrank != null ? zrank : DEFAULT_VAL_INT;
    }

    /**
     * 命令用于移除集合中的一个或多个成员元素，不存在的成员元素会被忽略。 当 key 不是集合类型，返回一个错误。
     * @param key
     * @param members
     * @return 被成功移除的元素的数量，不包括被忽略的元素。
     */
    @SuppressWarnings("unchecked")
    public <T> long zrem(String key, T... members) {
        if (key == null || members == null) {
            return DEFAULT_VAL_INT;
        }
        String[] ms = new String[members.length];
        int index = 0;
        for (T t : members) {
            ms[index++] = PojoJsonUtils.convert2String(t);
        }
        return jedis.zrem(key, ms);
    }

    /**
     * 于移除有序集合中给定的字典区间的所有成员。
     * @param key
     * @param min
     * @param max
     * @return 被成功移除的成员的数量，不包括被忽略的成员。
     */
    public long zremrangeBylex(String key, String min, String max) {
        if (key == null || min == null || max == null) {
            return DEFAULT_VAL_INT;
        }
        return jedis.zremrangeByLex(key, min, max);
    }

    /**
     * 于移除有序集合中给定的区间的所有成员。
     * @param key
     * @param start
     * @param end
     * @return 被成功移除的成员的数量，不包括被忽略的成员。
     */
    public long zremrangeByScore(String key, double start, double end) {
        if (key == null) {
            return DEFAULT_VAL_INT;
        }
        return jedis.zremrangeByScore(key, start, end);
    }

    /**
     * 于移除有序集合中给定排名 的所有成员。
     * @param key
     * @param start
     * @param end
     * @return 被成功移除的成员的数量，不包括被忽略的成员。
     */
    public long zremrangeByRank(String key, long start, long end) {
        if (key == null) {
            return DEFAULT_VAL_INT;
        }
        return jedis.zremrangeByRank(key, start, end);
    }

    /**
     * 返回有序集中成员的排名。其中有序集成员按分数值递减(从大到小)排序。 排名以 0 为底，也就是说， 分数值最大的成员排名为 0 。 使用 ZRANK 命令可以获得成员按分数值递增(从小到大)排列的排名。
     * @param key
     * @param start
     * @param end
     * @return 一个列表，包含指定区间内的元素。
     */
    @SuppressWarnings("unchecked")
    public Set<String> zrevrange(String key, long start, long end) {
        if (key == null) {
            return Collections.EMPTY_SET;
        }
        return jedis.zrevrange(key, start, end);
    }

    /**
     * 返回有序集中成员的排名。其中有序集成员按分数值递减(从大到小)排序。 排名以 0 为底，也就是说， 分数值最大的成员排名为 0 。 使用 ZRANK 命令可以获得成员按分数值递增(从小到大)排列的排名。
     * 表示倒数第二个成员，以此类推。
     * @param key
     * @param start
     * @param end
     * @param pojoClazz
     * @return 一个列表，包含指定区间内的元素。
     */
    @SuppressWarnings("unchecked")
    public <T> Set<T> zrevrange(String key, long start, long end, Class<T> pojoClazz) {
        Set<String> ret = zrevrange(key, start, end);
        if (ret == null || ret.isEmpty()) {
            return Collections.EMPTY_SET;
        }
        Set<T> retList = new LinkedHashSet<T>(ret.size());
        for (String s : ret) {
            retList.add(PojoJsonUtils.convert2Object(s, pojoClazz));
        }
        return retList;
    }

    /**
     * 返回有序集中，成员的分数值。 如果成员元素不是有序集 key 的成员，或 key 不存在，返回 nil 。
     * @param key
     * @param member
     * @return
     */
    public Double zscore(String key, Object member) {
        if (key == null || member == null) {
            return null;
        }
        Double zrank = jedis.zscore(key, PojoJsonUtils.convert2String(member));
        return zrank != null ? zrank : DEFAULT_VAL_INT;
    }

    /**
     * 计算给定的一个或多个有序集的并集，其中给定 key 的数量必须以 numkeys 参数指定，并将该并集(结果集)储存到 destination 。 默认情况下，结果集中某个成员的分数值是所有给定集下该成员分数值之和 。
     * @param dstkey
     * @param sets
     * @return 保存到目标结果集的的成员数量。
     */
    public long zunionstore(String dstkey, String... sets) {
        if (dstkey == null || sets == null) {
            return DEFAULT_VAL_INT;
        }
        return jedis.zunionstore(dstkey, sets);
    }

    /**
     * 用于迭代有序集合中的元素（包括元素成员和元素分值）
     * @param key
     * @param cursor
     * @return
     */
    public ScanResult<Tuple> zscan(String key, String cursor) {
        if (key == null || cursor == null) {
            return null;
        }
        return jedis.zscan(key, cursor);
    }
}
