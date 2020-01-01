package slive.jedis.client.lock;

/**
 * 描述：<br>
 *
 * @author slive
 * @date 2020/1/1
 */
public interface LockHandler {

    void onHandle(LockContext context);

    void onFailed(LockContext context);

    void onError(LockContext context);
}
