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

    <T extends BaseLockContext> LockExecute execute(T context, LockHandler handler);

    <T extends BaseLockContext> LockExecute execute(T context, LockConf conf, LockHandler handler);

}
