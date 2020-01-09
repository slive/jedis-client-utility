package slive.jedis.client.core.impl;

import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import slive.jedis.client.core.JedisStrings;

/**
 * 描述：<br>
 *
 * @author slive
 * @date 2020/1/9
 */
public class JedisKeysImplTest {

    private JedisStrings jedisKeys;

    @Before
    public void setUp() throws Exception {
        Jedis jedis = new Jedis("192.168.235.204", 6379);
        jedisKeys = new JedisStringsImpl();
        jedisKeys.initJedis(jedis);
    }

    @Test
    public void testDelEqValue(){
        String key = "4522222";
        String value = "value111";
        jedisKeys.set(key, value);
        System.out.println(jedisKeys.delEqValue(key,value));
        System.out.println(jedisKeys.get(key));
        System.out.println(jedisKeys.del(key));
    }
}