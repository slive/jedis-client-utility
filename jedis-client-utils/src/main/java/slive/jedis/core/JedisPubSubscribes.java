//==============================================================================
//
//	@author Slive
//	@date  2019-2-13
//
//==============================================================================
package slive.jedis.core;

import java.util.List;
import java.util.Map;

import redis.clients.jedis.JedisPubSub;

/**
 * 描述：
 * 
 */
public interface JedisPubSubscribes extends JedisKeys {

    /**
     * 订阅一个或多个符合给定模式的频道。 每个模式以 * 作为匹配符，比如 it* 匹配所有以 it 开头的频道( it.news 、 it.blog 、 it.tweets 等等)。 news.* 匹配所有以 news.
     * 开头的频道( news.it 、 news.global.today 等等)，诸如此类。
     * @param jedisPubSub
     * @param patterns
     */
    void psubscribe(JedisPubSub jedisPubSub, String... patterns);

    /**
     * 用于将信息发送到指定的频道。
     * @param channel
     * @param pojoMsg
     * @return 接收到信息的订阅者数量。
     */
    long publish(String channel, Object pojoMsg);

    /**
     * 用于查看订阅与发布系统状态，它由数个不同格式的子命令组成。
     * @param pattern
     * @return 由活跃频道组成的列表。
     */
    List<String> pubsubChannels(String pattern);

    /**
     * 用于查看订阅与发布系统状态，它由数个不同格式的子命令组成。
     * @param channels
     * @return 由活跃频道组成的列表。
     */
    Map<String, String> pubsubNumSub(String... channels);

    /**
     * 用于订阅给定的一个或多个频道的信息。
     * @param jedisPubSub
     * @param channels
     */
    void subscribe(JedisPubSub jedisPubSub, String... channels);

}