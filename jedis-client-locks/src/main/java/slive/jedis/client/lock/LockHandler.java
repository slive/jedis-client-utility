package slive.jedis.client.lock;

/**
 * 描述：<br>
 *
 * @author slive
 * @date 2020/1/1
 */
public interface LockHandler {

    Object onHandle(LockContext context);

    Object onFailed(LockContext context);

    Object onError(LockContext context, Throwable ex);
}
