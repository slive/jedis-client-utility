package slive.jedis.client.lock;

import java.security.PrivateKey;
import java.util.concurrent.TimeUnit;

/**
 * 描述：<br>
 * 基于redis锁，包括lock,unlock,tryLock,unlock等功能
 *
 * @author slive
 * @date 2020/1/1
 */
public interface Lock {

    boolean lock(String key, String ower, long timeout);

    boolean tryLock(String key, String ower, long timeout);

    void unLock(String key, String ower);

}
