//==============================================================================
//
//	@author Slive
//	@date  2019-2-13
//
//==============================================================================
package slive.jedis.client.core;

/**
 * 描述：
 * 
 */
public interface JedisTranscations {

    /**
     * 用于监视一个(或多个) key ，如果在事务执行之前这个(或这些) key 被其他命令所改动，那么事务将被打断
     * @param keys
     * @return
     */
    boolean watch(String... keys);

    /**
     * 取消 WATCH 命令对所有 key 的监视。
     * @return
     */
    boolean unwatch();

}