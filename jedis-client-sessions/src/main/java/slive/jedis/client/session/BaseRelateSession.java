package slive.jedis.client.session;

import java.util.Set;

/**
 * 描述：<br>
 *
 * @author slive
 * @date 2020/1/4
 */
public interface BaseRelateSession<T extends BaseSession> extends SessionCache<BaseSession> {

    T getByFKey(String fField, String fkey);

    Set<T> getByCategory(String categoryField, String categoryVal);
}
