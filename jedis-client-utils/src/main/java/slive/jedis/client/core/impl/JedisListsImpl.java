//==============================================================================
//
//	@author Slive
//	@date  2018-9-12
//
//==============================================================================
package slive.jedis.client.core.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import redis.clients.jedis.Jedis;
import slive.jedis.client.core.JedisLists;
import slive.jedis.client.util.PojoJsonUtils;

/**
 * 描述：redis List相关操作类，参考<b>http://www.redis.net.cn/order/"></b>
 * 
 */
public final class JedisListsImpl extends JedisKeysImpl implements JedisLists {

    public JedisListsImpl(Jedis jedis) {
        super(jedis);
    }

    public JedisListsImpl() {
        super();
    }

    /**
     * 移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
     * @param timeoutSecond
     * @param key
     * @return 第一个元素是被弹出元素所属的 key ，第二个元素是被弹出元素的值。
     */
    @SuppressWarnings("unchecked")
    public List<String> blpop(int timeoutSecond, String key) {
        if (key == null) {
            return Collections.EMPTY_LIST;
        }
        return jedis.blpop(timeoutSecond, key);
    }

    /**
     * 移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
     * 
     * @param timeoutSecond
     * @param key
     * @return 第假如在指定时间内没有任何元素被弹出，则返回一个 nil 和等待时长。 反之，返回一个含有两个元素的列表，第一个元素是被弹出元素所属的 key ，第二个元素是被弹出元素的值。
     */
    @SuppressWarnings("unchecked")
    public List<String> brpop(int timeoutSecond, String key) {
        if (key == null) {
            return Collections.EMPTY_LIST;
        }
        return jedis.brpop(timeoutSecond, key);
    }

    /**
     * 从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
     * @param srcKey
     * @param dstKey
     * @param timeoutSecond
     * @return 假如在指定时间内没有任何元素被弹出，则返回一个 nil 和等待时长。 反之，返回一个含有两个元素的列表，第一个元素是被弹出元素的值，第二个元素是等待时长。
     */
    public String brpoppush(String srcKey, String dstKey, int timeoutSecond) {
        if (srcKey == null || dstKey == null) {
            return null;
        }
        return jedis.brpoplpush(srcKey, dstKey, timeoutSecond);
    }

    /**
     * 用于通过索引获取列表中的元素。你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     * @param key
     * @param index
     * @return 列表中下标为指定索引值的元素。 如果指定索引值不在列表的区间范围内，返回 nil 。
     */
    public String lindex(String key, long index) {
        if (key == null) {
            return null;
        }
        return jedis.lindex(key, index);
    }

    /**
     * 用于通过索引获取列表中的元素。你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     * @param key
     * @param index
     * @param pojoClazz
     * @return 列表中下标为指定索引值的元素。 如果指定索引值不在列表的区间范围内，返回 nil 。
     */
    public <T> T lindex(String key, long index, Class<T> pojoClazz) {
        if (key == null) {
            return null;
        }
        String ret = lindex(key, index);
        if (ret != null) {
            return PojoJsonUtils.convert2Object(ret, pojoClazz);
        }
        return null;
    }

    /**
     * 用于返回列表的长度。 如果列表 key 不存在，则 key 被解释为一个空列表，返回 0 。 如果 key 不是列表类型，返回一个错误。
     * @param key
     * @return
     */
    public long llen(String key) {
        if (key == null) {
            return DEFAULT_VAL_INT;
        }
        return jedis.llen(key);
    }

    /**
     * 用于移除并返回列表的第一个元素
     * @param key
     * @return
     */
    public String lpop(String key) {
        if (key == null) {
            return null;
        }
        return jedis.lpop(key);
    }

    /**
     * 用于移除并返回列表的第一个元素
     * @param key
     * @param pojoClazz
     * @return
     */
    public <T> T lpop(String key, Class<T> pojoClazz) {
        String ret = lpop(key);
        if (ret != null) {
            return PojoJsonUtils.convert2Object(ret, pojoClazz);
        }
        return null;
    }

    /**
     * 命令将一个或多个值插入到列表头部。 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。 当 key 存在但不是列表类型时，返回一个错误。
     * @param key
     * @param values
     * @return 执行 LPUSH 命令后，列表的长度
     */
    @SuppressWarnings("unchecked")
    public <T> long lpush(String key, T... values) {
        if (key == null || values == null) {
            return DEFAULT_VAL_INT;
        }
        String[] str = new String[values.length];
        int index = 0;
        for (T t : values) {
            str[index++] = PojoJsonUtils.convert2String(t);
        }
        return jedis.lpush(key, str);
    }

    /**
     * 将一个或多个值插入到已存在的列表头部，列表不存在时操作无效。
     * @param key
     * @param values
     * @return LPUSHX 命令执行之后，列表的长度。
     */
    @SuppressWarnings("unchecked")
    public <T> long lpushx(String key, T... values) {
        if (key == null || values == null) {
            return DEFAULT_VAL_INT;
        }
        String[] str = new String[values.length];
        int index = 0;
        for (T t : values) {
            str[index++] = PojoJsonUtils.convert2String(t);
        }
        return jedis.lpushx(key, str);
    }

    /**
     * 返回列表中指定区间内的元素，区间以偏移量 START 和 END 指定。 其中 0 表示列表的第一个元素， 1 表示列表的第二个元素，以此类推。 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2
     * 表示列表的倒数第二个元素，以此类推。
     * @param key
     * @param start
     * @param end
     * @return 一个列表，包含指定区间内的元素。
     */
    @SuppressWarnings("unchecked")
    public List<String> lrange(String key, long start, long end) {
        if (key == null) {
            return Collections.EMPTY_LIST;
        }
        return jedis.lrange(key, start, end);
    }

    /**
     * 返回列表中指定区间内的元素，区间以偏移量 START 和 END 指定。 其中 0 表示列表的第一个元素， 1 表示列表的第二个元素，以此类推。 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2
     * 表示列表的倒数第二个元素，以此类推。
     * @param key
     * @param start
     * @param end
     * @param pojoClazz
     * @return 一个列表，包含指定区间内的元素。
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> lrange(String key, long start, long end, Class<T> pojoClazz) {
        List<String> ret = lrange(key, start, end);
        if (ret == null || ret.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<T> retList = new LinkedList<T>();
        for (String s : ret) {
            retList.add(PojoJsonUtils.convert2Object(s, pojoClazz));
        }
        return retList;
    }

    /**
     * 据参数 COUNT 的值，移除列表中与参数 VALUE 相等的元素。 COUNT 的值可以是以下几种：
     * <ul>
     * <li>count > 0 : 从表头开始向表尾搜索，移除与 VALUE 相等的元素，数量为 COUNT 。
     * <li>count < 0 : 从表尾开始向表头搜索，移除与 VALUE 相等的元素，数量为 COUNT 的绝对值。
     * <li>count = 0 : 移除表中所有与 VALUE 相等的值。
     * </ul>
     * @param key
     * @param count
     * @param value
     * @return 被移除元素的数量。 列表不存在时返回 0 。
     */
    public <T> long lrem(String key, long count, T value) {
        if (key == null || value == null) {
            return DEFAULT_VAL_INT;
        }
        return jedis.lrem(key, count, PojoJsonUtils.convert2String(value));
    }

    /**
     * 通过索引来设置元素的值。 当索引参数超出范围，或对一个空列表进行 LSET 时，返回一个错误。 关于列表下标的更多信息，请参考 LINDEX 命令。
     * @param key
     * @param index
     * @param value
     * @return
     */
    public <T> boolean lset(String key, long index, T value) {
        if (key == null || value == null) {
            return false;
        }
        return RET_OK_STR.equals(jedis.lset(key, index, PojoJsonUtils.convert2String(value)));
    }

    /**
     * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。 下标 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。 你也可以使用负数下标，以 -1
     * 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     * @param key
     * @param start
     * @param end
     * @return
     */
    public String ltrim(String key, long start, long end) {
        if (key == null) {
            return null;
        }
        return jedis.ltrim(key, start, end);
    }

    /**
     * 用于移除并返回列表的最后一个元素
     * @param key
     * @return
     */
    public String rpop(String key) {
        if (key == null) {
            return null;
        }
        return jedis.rpop(key);
    }

    /**
     * 用于移除并返回列表的最后一个元素
     * @param key
     * @param pojoClazz
     * @return
     */
    public <T> T rpop(String key, Class<T> pojoClazz) {
        String ret = rpop(key);
        if (ret != null) {
            return PojoJsonUtils.convert2Object(ret, pojoClazz);
        }
        return null;
    }

    /**
     * 命令用于移除列表的最后一个元素，并将该元素添加到另一个列表并返回。
     * @param srcKey
     * @param dstKey
     * @return 移除的元素
     */
    public String rpoplpush(String srcKey, String dstKey) {
        if (srcKey == null || dstKey == null) {
            return null;
        }
        return jedis.rpoplpush(srcKey, dstKey);
    }

    /**
     * 命令用于移除列表的最后一个元素，并将该元素添加到另一个列表并返回。
     * @param srcKey
     * @param dstKey
     * @param pojoClazz
     * @return 移除的元素
     */
    public <T> T rpoplpush(String srcKey, String dstKey, Class<T> pojoClazz) {
        String ret = rpoplpush(srcKey, dstKey);
        if (ret != null) {
            return PojoJsonUtils.convert2Object(ret, pojoClazz);
        }
        return null;
    }

    /**
     * 命令将一个或多个值插入到列表尾部。 如果 key 不存在，一个空列表会被创建并执行 RPUSH 操作。 当 key 存在但不是列表类型时，返回一个错误。
     * @param key
     * @param values
     * @return 执行 RPUSH 命令后，列表的长度
     */
    @SuppressWarnings("unchecked")
    public <T> long rpush(String key, T... values) {
        if (key == null || values == null) {
            return DEFAULT_VAL_INT;
        }
        String[] str = new String[values.length];
        int index = 0;
        for (T t : values) {
            str[index++] = PojoJsonUtils.convert2String(t);
        }
        return jedis.rpush(key, str);
    }

    /**
     * 将一个或多个值插入到已存在的列表尾部，列表不存在时操作无效。
     * @param key
     * @param values
     * @return RPUSHX 命令执行之后，列表的长度。
     */
    @SuppressWarnings("unchecked")
    public <T> long rpushx(String key, T... values) {
        if (key == null || values == null) {
            return DEFAULT_VAL_INT;
        }
        String[] str = new String[values.length];
        int index = 0;
        for (T t : values) {
            str[index++] = PojoJsonUtils.convert2String(t);
        }
        return jedis.rpushx(key, str);
    }
}
