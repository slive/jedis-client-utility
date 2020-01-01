package slive.jedis.client.lock;

/**
 * 描述：<br>
 *
 * @author slive
 * @date 2020/1/1
 */
public interface LockExecute {

    String getPrefix();

    LockExecute setLockConf(LockConf conf);

    LockExecute execute(LockContext context, LockHandler handler);

    LockExecute execute(LockContext context, LockConf conf, LockHandler handler);

}
