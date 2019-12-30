package slive.jedis.client.core.inter;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import slive.jedis.client.core.JedisStrings;

/**
 * 描述：<br>
 *
 * @author slive
 * @date 2019/12/27 8:43
 */
public class JedisStringsImplTest {
    private JedisStrings Strings;

    @Before
    public void setUp() throws Exception {
        Jedis jedis = new Jedis("192.168.235.184", 6379);
        Strings = new JedisStringsImpl();
        Strings.initJedis(jedis);
    }

    @Test
    public void get() {
        String key = "1234";
        Strings.set(key, "23423423423");
        System.out.println(Strings.get(key));
    }

    @Test
    public void get1() {
    }

    @Test
    public void getrange() {
    }

    @Test
    public void getset() {
    }

    @Test
    public void getbit() {
    }

    @Test
    public void mget() {
    }

    @Test
    public void mget1() {
    }

    @Test
    public void setbit() {
    }

    @Test
    public void set() {
    }

    @Test
    public void setex() {
    }

    @Test
    public void psetex() {
    }

    @Test
    public void setnx() {

    }

    @Test
    public void setrange() {
    }

    @Test
    public void strlen() {
    }

    @Test
    public void mset() {
    }

    @Test
    public void msetnx() {
    }

    @Test
    public void incr() {
    }

    @Test
    public void incrBy() {
    }

    @Test
    public void incrByFloat() {
    }

    @Test
    public void decr() {
    }

    @Test
    public void decrBy() {
    }

    @Test
    public void append() {
    }
}