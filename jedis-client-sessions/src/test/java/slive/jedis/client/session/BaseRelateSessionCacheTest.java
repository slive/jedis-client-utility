package slive.jedis.client.session;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            jedis = new Jedis("192.168.235.200", 6379);
            jedis.connect();
            JedisUtils.init(jedis);

            baseSessionCache = new BaseRelateSessionCache<TestRelateSession>("testrelate", 2, TestRelateSession.class);
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
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        String ret = baseSessionCache.get(ts.getKey());
        LOGGER.info("ret:{}", ret);

    }

    @org.junit.After
    public void tearDown() throws Exception {
    }
}