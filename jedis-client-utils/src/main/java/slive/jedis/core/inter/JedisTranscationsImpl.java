//==============================================================================
//
//	@author Slive
//	@date  2018-9-14
//
//==============================================================================
package slive.jedis.core.inter;

import redis.clients.jedis.Jedis;
import slive.jedis.core.JedisTranscations;

/**
 * 描述：jedis事务操作类，参考<b>http://www.redis.net.cn/order/"></b>
 * 
 */
public final class JedisTranscationsImpl extends JedisKeysImpl implements JedisTranscations {

    public JedisTranscationsImpl(Jedis jedis) {
        super(jedis);
    }

    public JedisTranscationsImpl() {
        super();
    }

    /**
     * 用于监视一个(或多个) key ，如果在事务执行之前这个(或这些) key 被其他命令所改动，那么事务将被打断
     * @param keys
     * @return
     */
    public boolean watch(String... keys) {
        return RET_OK_STR.equals(jedis.watch(keys));
    }

    /**
     * 取消 WATCH 命令对所有 key 的监视。
     * @return
     */
    public boolean unwatch() {
        return RET_OK_STR.equals(jedis.unwatch());
    }

}
