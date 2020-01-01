package slive.jedis.client.lock.impl;

import slive.jedis.client.lock.LockConf;
import slive.jedis.client.lock.LockContext;
import slive.jedis.client.lock.LockExecute;
import slive.jedis.client.lock.LockHandler;

/**
 * 描述：<br>
 *
 * @author slive
 * @date 2020/1/1
 */
public class JedisLockExecute implements LockExecute {

    private LockConf conf;

    private String prefix;

    public JedisLockExecute(String prefix, long timeout) {
        this.prefix = prefix;
    }

    public JedisLockExecute(String prefix, LockConf conf) {
        this.prefix = prefix;
        setLockConf(conf);
    }

    public String getPrefix() {
        return prefix;
    }

    public LockExecute setLockConf(LockConf conf) {
        if (conf == null) {
            throw new NullPointerException("lockconf is null.");
        }

        this.conf = conf;
        return this;
    }

    public LockExecute execute(LockContext context, LockHandler handler) {
        return null;
    }

    public LockExecute execute(LockContext context, LockConf conf, LockHandler handler) {
        return null;
    }
}
