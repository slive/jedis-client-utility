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
public interface JedisLists extends JedisKeys {

    /**
     * 移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
     * @param timeoutSecond
     * @param key
     * @return 第一个元素是被弹出元素所属的 key ，第二个元素是被弹出元素的值。
     */
    List<String> blpop(int timeoutSecond, String key);

    /**
     * 移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
     * 
     * @param timeoutSecond
     * @param key
     * @return 第假如在指定时间内没有任何元素被弹出，则返回一个 nil 和等待时长。 反之，返回一个含有两个元素的列表，第一个元素是被弹出元素所属的 key ，第二个元素是被弹出元素的值。
     */
    List<String> brpop(int timeoutSecond, String key);

    /**
     * 从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
     * @param srcKey
     * @param dstKey
     * @param timeoutSecond
     * @return 假如在指定时间内没有任何元素被弹出，则返回一个 nil 和等待时长。 反之，返回一个含有两个元素的列表，第一个元素是被弹出元素的值，第二个元素是等待时长。
     */
    String brpoppush(String srcKey, String dstKey, int timeoutSecond);

    /**
     * 用于通过索引获取列表中的元素。你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     * @param key
     * @param index
     * @return 列表中下标为指定索引值的元素。 如果指定索引值不在列表的区间范围内，返回 nil 。
     */
    String lindex(String key, long index);

    /**
     * 用于通过索引获取列表中的元素。你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     * @param key
     * @param index
     * @param pojoClazz
     * @return 列表中下标为指定索引值的元素。 如果指定索引值不在列表的区间范围内，返回 nil 。
     */
    <T> T lindex(String key, long index, Class<T> pojoClazz);

    /**
     * 用于返回列表的长度。 如果列表 key 不存在，则 key 被解释为一个空列表，返回 0 。 如果 key 不是列表类型，返回一个错误。
     * @param key
     * @return
     */
    long llen(String key);

    /**
     * 用于移除并返回列表的第一个元素
     * @param key
     * @return
     */
    String lpop(String key);

    /**
     * 用于移除并返回列表的第一个元素
     * @param key
     * @param pojoClazz
     * @return
     */
    <T> T lpop(String key, Class<T> pojoClazz);

    /**
     * 命令将一个或多个值插入到列表头部。 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。 当 key 存在但不是列表类型时，返回一个错误。
     * @param key
     * @param values
     * @return 执行 LPUSH 命令后，列表的长度
     */
    <T> long lpush(String key, T... values);

    /**
     * 将一个或多个值插入到已存在的列表头部，列表不存在时操作无效。
     * @param key
     * @param values
     * @return LPUSHX 命令执行之后，列表的长度。
     */
    <T> long lpushx(String key, T... values);

    /**
     * 返回列表中指定区间内的元素，区间以偏移量 START 和 END 指定。 其中 0 表示列表的第一个元素， 1 表示列表的第二个元素，以此类推。 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2
     * 表示列表的倒数第二个元素，以此类推。
     * @param key
     * @param start
     * @param end
     * @return 一个列表，包含指定区间内的元素。
     */
    List<String> lrange(String key, long start, long end);

    /**
     * 返回列表中指定区间内的元素，区间以偏移量 START 和 END 指定。 其中 0 表示列表的第一个元素， 1 表示列表的第二个元素，以此类推。 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2
     * 表示列表的倒数第二个元素，以此类推。
     * @param key
     * @param start
     * @param end
     * @param pojoClazz
     * @return 一个列表，包含指定区间内的元素。
     */
    <T> List<T> lrange(String key, long start, long end, Class<T> pojoClazz);

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
    <T> long lrem(String key, long count, T value);

    /**
     * 通过索引来设置元素的值。 当索引参数超出范围，或对一个空列表进行 LSET 时，返回一个错误。 关于列表下标的更多信息，请参考 LINDEX 命令。
     * @param key
     * @param index
     * @param value
     * @return
     */
    <T> boolean lset(String key, long index, T value);

    /**
     * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。 下标 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。 你也可以使用负数下标，以 -1
     * 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     * @param key
     * @param start
     * @param end
     * @return
     */
    String ltrim(String key, long start, long end);

    /**
     * 用于移除并返回列表的最后一个元素
     * @param key
     * @return
     */
    String rpop(String key);

    /**
     * 用于移除并返回列表的最后一个元素
     * @param key
     * @param pojoClazz
     * @return
     */
    <T> T rpop(String key, Class<T> pojoClazz);

    /**
     * 命令用于移除列表的最后一个元素，并将该元素添加到另一个列表并返回。
     * @param srcKey
     * @param dstKey
     * @return 移除的元素
     */
    String rpoplpush(String srcKey, String dstKey);

    /**
     * 命令用于移除列表的最后一个元素，并将该元素添加到另一个列表并返回。
     * @param srcKey
     * @param dstKey
     * @param pojoClazz
     * @return 移除的元素
     */
    <T> T rpoplpush(String srcKey, String dstKey, Class<T> pojoClazz);

    /**
     * 命令将一个或多个值插入到列表尾部。 如果 key 不存在，一个空列表会被创建并执行 RPUSH 操作。 当 key 存在但不是列表类型时，返回一个错误。
     * @param key
     * @param values
     * @return 执行 RPUSH 命令后，列表的长度
     */
    <T> long rpush(String key, T... values);

    /**
     * 将一个或多个值插入到已存在的列表尾部，列表不存在时操作无效。
     * @param key
     * @param values
     * @return RPUSHX 命令执行之后，列表的长度。
     */
    <T> long rpushx(String key, T... values);

}