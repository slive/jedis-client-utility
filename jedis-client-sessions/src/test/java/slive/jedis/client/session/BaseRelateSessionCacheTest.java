package slive.jedis.client.session;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
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

    @BeforeClass
    public void setUp() throws Exception {
        try {
            jedis = new Jedis("192.168.235.210", 6379);
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
        ts.setType("111");
        ts.setValue("3432222");
        ts.setfKeys(new String[]{ "666", "111"});
        baseSessionCache.put(ts);
        Object retByCategory = baseSessionCache.getByCategory("t", ts.getType());
        LOGGER.info("retByCategory:{}", JSON.toJSONString(retByCategory));

        retByFkey = baseSessionCache.getByFKey("fKeys", ts.getfKeys()[1]);
        Assert.assertNotNull(retByFkey);
        LOGGER.info("retByFkeys:{}", JSON.toJSONString(retByFkey));

    }

    @AfterClass
    public void tearDown() throws Exception {
    }
}