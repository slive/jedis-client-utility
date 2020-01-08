package slive.jedis.client.session;

import com.sun.javafx.font.t2k.T2KFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;
import slive.jedis.client.util.JedisUtils;

/**
 * 描述：<br>
 *
 * @author slive
 * @date 2020/1/8
 */
public class BaseRelateSessionCacheTest {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRelateSessionCacheTest.class);

    private RelateSessionCache<TestRelateSession> baseSessionCache;

    private Jedis jedis;

    @org.junit.Before
    public void setUp() throws Exception {
        try {
            jedis = new Jedis("192.168.235.201", 6379);
            jedis.connect();
            JedisUtils.init(jedis);
            LOGGER.info("init sessioncache");
            baseSessionCache = new BaseRelateSessionCache<TestRelateSession>("testrelate", 20, TestRelateSession.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("error:", e);
        }
    }

    @Test
    public void testCache() {
        TestRelateSession ts = new TestRelateSession();
        ts.setKey("124");
        ts.setNumber(1000);
        ts.setValue("v23424324");
        ts.setType("111");
        ts.setfKey("444444");
        baseSessionCache.put(ts);
        try {
            Thread.sleep(500);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        String ret = baseSessionCache.get(ts.getKey());
        LOGGER.info("ret:{}", ret);

        Object retByFkey = baseSessionCache.getByFKey("fKey", ts.getfKey());
        LOGGER.info("retByFkey:{}", JSON.toJSONString(retByFkey));

        ts.setKey("356222262");
        ts.setfKey("433222");
        ts.setType("1111");
        ts.setValue("3432222");
        baseSessionCache.put(ts);
        Object retByCategory = baseSessionCache.getByCategory("type", ts.getType());
        LOGGER.info("retByCategory:{}", JSON.toJSONString(retByCategory));

    }

    @org.junit.After
    public void tearDown() throws Exception {
    }
}