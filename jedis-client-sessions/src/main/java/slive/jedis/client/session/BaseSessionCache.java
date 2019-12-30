package slive.jedis.client.session;

import java.util.Set;

/**
 * 描述：<br>
 *
 * @author slive
 * @date 2019/12/30
 */
public interface BaseSessionCache<T extends BaseSession> {

    T get(String key);

    T getByFKey(String fkey);

    Set<T> getByCategory(String category);

    boolean put(String key, T value);

    boolean put(T value);

    boolean remove(String key);

    boolean expire(String key);
}
