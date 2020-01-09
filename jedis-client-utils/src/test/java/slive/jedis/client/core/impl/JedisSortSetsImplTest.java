package slive.jedis.client.core.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import slive.jedis.client.core.JedisSortSets;

import static org.junit.Assert.*;

/**
 * 描述：<br>
 *
 * @author slive
 * @date 2020/1/9
 */
public class JedisSortSetsImplTest {

    private JedisSortSets jedisSortSets;

    @Before
    public void setUp() throws Exception {
        Jedis jedis = new Jedis("192.168.235.203", 6379);
        jedisSortSets = new JedisSortSetsImpl();
        jedisSortSets.initJedis(jedis);
    }

    @Test
    public void testSortSet(){
        String key = "123123";
        jedisSortSets.zadd(key,0.1,"abc");
        jedisSortSets.zadd(key,0.2,"efg");
        System.out.println(jedisSortSets.zcar(key));
    }

    @After
    public void tearDown() throws Exception {

    }
}