package slive.jedis.client.lock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import slive.jedis.client.lock.impl.JedisDistlock;
import slive.jedis.client.util.JedisUtils;

/**
 * 描述：<br>
 * 锁执行器测试类
 *
 * @author slive
 * @date 2020/1/1
 */
public class BaseLockExecutorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseLockExecutorTest.class);

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss.SSS");

    private BaseLockExecutor execute;

    private AtomicLong atomicLong;

    private Jedis jedis;

    private long reentryIndex = 4;

    @org.junit.Before
    public void setUp() throws Exception {
        jedis = new Jedis("192.168.235.204", 6379);
        jedis.connect();
        JedisUtils.init(jedis);
        atomicLong = new AtomicLong();
        execute = new BaseLockExecutor("test", 5000, new JedisDistlock());
        LOGGER.info("[{}] init...", getDataStr());
        LOGGER.info("--------------------------------------------------------------------------\r\n");
    }

    @Test
    public void testExecute() {
        int count = 3;
        final CountDownLatch countDownLatch = new CountDownLatch(count);
        for (int index = 0; index < count; index++) {
            doExecutor(count, countDownLatch);
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testExecuteOnError() {
        String key = "1255222222";
        String owner = "slive";
        String action = "testExecuteOnError";
        BaseLockExecutorContext context = createExecutorContext(2, key, owner, action);
        execute.execute(context, new BaseLockHandler() {
            public Object onHandle(LockExecutorContext context) {
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                throw new RuntimeException("throw exception.");
            }
        });
    }

    @Test
    public void testExecuteOnFailed() {

    }

    @Test
    public void testExecuteReentry() {
        final String key = "1255222222";
        final String owner = "slive";
        String action = "testExecuteReentry";
        BaseLockExecutorContext context = createExecutorContext(1, key, owner, action);
        context.setTimeout(2000);
        execute.execute(context, new BaseLockHandler() {
            public Object onHandle(LockExecutorContext context) {
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                BaseLockExecutorContext recontext = createExecutorContext(1, key, owner, "rentry");
                execute.execute(recontext, new BaseLockHandler() {
                    public Object onHandle(LockExecutorContext context) {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return context.getOwner() + ":" + context.getAction();
                    }
                });
                return context.getOwner() + ":" + context.getAction();
            }
        });
    }

    private void doExecutor(int count, final CountDownLatch countDownLatch) {
        String key = "12344";
        String owner = "slive";
        String action = "doExecutor";
        BaseLockExecutorContext context = createExecutorContext(count, key, owner, action);
        execute.execute(context, new BaseLockHandler() {
            public Object onHandle(LockExecutorContext context) {
                String owner = context.getOwner();
                LOGGER.info("[{}] key:{}, owner:{}", getDataStr(), context.getKey(), owner);
                long l = atomicLong.incrementAndGet();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return owner;
            }

            @Override
            public void afterHandle(LockExecutorContext context) {
                super.afterHandle(context);
                countDownLatch.countDown();
            }
        });

        LOGGER.info("[{}] result:{}", getDataStr(), context.getResult());
        LOGGER.info("--------------------------------------------------------------------------\r\n");
    }

    private BaseLockExecutorContext createExecutorContext(int count, String key, String owner, String action) {
        BaseLockExecutorContext context = new BaseLockExecutorContext(key);
        owner = owner + (int) (Math.random() * count * 10);
        LOGGER.info("action:{}, owner:{}", action, owner);
        context.setOwner(owner).setAction(action);
        return context;
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