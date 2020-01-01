//==============================================================================
//
//	@author Slive
//	@date  2018-9-14
//
//==============================================================================
package slive.jedis.client.core.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import slive.jedis.client.core.JedisPubSubscribes;
import slive.jedis.client.util.PojoJsonUtils;

/**
 * 描述：jedis发布订阅操作类，参考<b>http://www.redis.net.cn/order/"></b>
 * 
 */
public final class JedisPubSubscribesImpl extends JedisKeysImpl implements JedisPubSubscribes {

    public JedisPubSubscribesImpl(Jedis jedis) {
        super(jedis);
    }

    public JedisPubSubscribesImpl() {
        super();
    }

    /**
     * 订阅一个或多个符合给定模式的频道。 每个模式以 * 作为匹配符，比如 it* 匹配所有以 it 开头的频道( it.news 、 it.blog 、 it.tweets 等等)。 news.* 匹配所有以 news.
     * 开头的频道( news.it 、 news.global.today 等等)，诸如此类。
     * @param jedisPubSub
     * @param patterns
     */
    public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {
        if (jedisPubSub == null || patterns == null) {
            return;
        }
        jedis.psubscribe(jedisPubSub, patterns);
    }

    /**
     * 用于将信息发送到指定的频道。
     * @param channel
     * @param pojoMsg
     * @return 接收到信息的订阅者数量。
     */
    public long publish(String channel, Object pojoMsg) {
        if (channel == null | pojoMsg == null) {
            return DEFAULT_VAL_INT;
        }
        return jedis.publish(channel, PojoJsonUtils.convert2String(pojoMsg));
    }

    /**
     * 用于查看订阅与发布系统状态，它由数个不同格式的子命令组成。
     * @param pattern
     * @return 由活跃频道组成的列表。
     */
    @SuppressWarnings("unchecked")
    public List<String> pubsubChannels(String pattern) {
        if (pattern == null) {
            return Collections.EMPTY_LIST;
        }
        return jedis.pubsubChannels(pattern);
    }

    /**
     * 用于查看订阅与发布系统状态，它由数个不同格式的子命令组成。
     * @param channels
     * @return 由活跃频道组成的列表。
     */
    public Map<String, String> pubsubNumSub(String... channels) {
        if (channels == null) {
            return new HashMap<String, String>();
        }
        return jedis.pubsubNumSub(channels);
    }

    /**
     * 用于订阅给定的一个或多个频道的信息。
     * @param jedisPubSub
     * @param channels
     */
    public void subscribe(JedisPubSub jedisPubSub, String... channels) {
        if (jedisPubSub == null || channels == null) {
            return;
        }
        jedis.subscribe(jedisPubSub, channels);
    }
}
