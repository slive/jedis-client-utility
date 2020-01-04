package slive.jedis.client.session;

import slive.jedis.client.util.JedisUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 描述：<br>
 *
 * @author slive
 * @date 2020/1/4
 */
public class BaseSessionCache<T extends BaseSession> implements SessionCache<T> {

    private static final Map<String, BaseSessionCache> CACHE_MAP = new HashMap<String, BaseSessionCache>();

    private long timeout;

    private String prefix;

    private Class<T> clazz;

    public BaseSessionCache(String prefix, long secondTimeout, Class<T> clazz) {
        if (prefix == null) {
            throw new NullPointerException("prefix is null.");
        }
        if (secondTimeout <= 0) {
            throw new RuntimeException("timeout is below 0.");
        }
        if (clazz == null) {
            throw new NullPointerException("clazz is null.");
        }
        if (CACHE_MAP.containsKey(prefix)) {
            throw new RuntimeException("prefix:[" + prefix + "] has existed.");
        }
        CACHE_MAP.put(prefix, this);
        this.prefix = prefix;
        this.timeout = secondTimeout;
        this.clazz = clazz;

    }

    public T getObj(String key) {
        return JedisUtils.Strings.get(key, clazz);
    }

    public boolean put(String key, BaseSession value) {
        return false;
    }

    public boolean put(BaseSession value) {
        return false;
    }

    public boolean remove(String key) {
        return false;
    }

    public boolean expire(String key) {
        return false;
    }
}
