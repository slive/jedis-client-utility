package slive.jedis.client.lock;

import java.util.concurrent.TimeUnit;

/**
 * 描述：<br>
 *
 * @author slive
 * @date 2020/1/1
 */
public interface LockConf {

    long getTimeout();

    LockConf setTimeout(long timeout);

    LockConf setTimeout(long timeout, TimeUnit timeoutUnit);

    TimeUnit getTimeoutUnit();

    LockConf setTimeoutUnit(TimeUnit timeoutUnit);

}
