package slive.jedis.client.util;

import redis.clients.jedis.Jedis;
import slive.jedis.client.core.*;
import slive.jedis.client.core.inter.*;
import slive.jedis.core.inter.*;

/**
 * 描述：<br>
 * jedis工具类
 * @author slive
 * @date 2019/12/26 9:11
 */
public class JedisUtils {

    public static final JedisStrings Strings;

    public static final JedisHashes Hashs;

    public static final JedisLists Lists;

    public static final JedisSets Sets;

    public static final JedisSortSets SortSets;

    static {
        Strings = new JedisStringsImpl();
        Hashs = new JedisHashesImpl();
        Lists = new JedisListsImpl();
        Sets = new JedisSetsImpl();
        SortSets = new JedisSortSetsImpl();
    }

    public static void init(Jedis jedis) {
        Strings.initJedis(jedis);
        Hashs.initJedis(jedis);
        Lists.initJedis(jedis);
        Sets.initJedis(jedis);
        SortSets.initJedis(jedis);
    }
}
