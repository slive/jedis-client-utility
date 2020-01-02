package slive.jedis.client.lock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import slive.jedis.client.util.JedisUtils;

/**
 * 描述：<br>
 *
 * @author slive
 * @date 2020/1/1
 */
public class BaseLockExecuteTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseLockExecuteTest.class);

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss.SSS");

    private BaseLockExecute execute;

    private AtomicLong atomicLong;

    private Jedis jedis;

    @org.junit.Before
    public void setUp() throws Exception {
        jedis = new Jedis("192.168.235.189", 6379);
        jedis.connect();
        JedisUtils.init(jedis);
        atomicLong = new AtomicLong();
        execute = new BaseLockExecute("test", 5000);
        LOGGER.info("[{}] init...", getDataStr());
        LOGGER.info("--------------------------------------------------------------------------\r\n");
    }

    @Test
    public void testExecute() {
        int count = 5;
        final CountDownLatch countDownLatch = new CountDownLatch(count);
        for (int index = 0; index < count; index++) {
            BaseLockContext context = new BaseLockContext();
            String key = "12344";
            context.setKey(key);
            String operater = "slive" + (int) (Math.random() * count * 10);
            context.setOperater(operater);
            execute.execute(context, new LockHandler() {
                public Object onHandle(LockContext context) {
                    String owner = context.getOwner();
                    LOGGER.info("[{}] key:{}, owner:{}", getDataStr(), context.getKey(), owner);
                    doSomething(countDownLatch);
                    return owner;
                }

                public Object onFailed(LockContext context) {
                    String owner = context.getOwner();
                    LOGGER.error("[{}] lock failed, key:{}, owner:{}", getDataStr(), context.getKey(), owner);
                    countDownLatch.countDown();
                    return null;
                }

                public Object onError(LockContext context, Throwable ex) {
                    String owner = context.getOwner();
                    LOGGER.error("[{}] handler error, key:{}, errmsg:{}", getDataStr(), context.getKey(),
                            ex.getMessage());
                    countDownLatch.countDown();
                    return null;
                }
            });

            LOGGER.info("[{}] result:{}", getDataStr(), context.getResult());
            LOGGER.info("--------------------------------------------------------------------------\r\n");
        }
        try {
            countDownLatch.await();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doSomething(CountDownLatch countDownLatch) {
        long l = atomicLong.incrementAndGet();
        if (l == 3) {
            throw new RuntimeException("exception value is " + l);
        }
        try {
            Thread.sleep(3000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        countDownLatch.countDown();
    }

    private static String getDataStr() {
        synchronized (SIMPLE_DATE_FORMAT) {
            return SIMPLE_DATE_FORMAT.format(new Date());
        }
    }

    @org.junit.After
    public void tearDown() throws Exception {
        if (jedis != null && jedis.isConnected()) {
            jedis.close();
        }
        LOGGER.info("[{}] finish...", getDataStr());
    }
}