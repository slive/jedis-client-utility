package slive.jedis.client.session;

import slive.jedis.client.util.JedisUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 描述：<br>
 *     基本的缓存会话实现
 *
 * @author slive
 * @date 2020/1/4
 */
public class BaseSessionCache<T> implements SessionCache<T> {

    private static final Map<String, BaseSessionCache> CACHE_MAP = new HashMap<String, BaseSessionCache>();

    private int timeout;

    private String prefix;

    protected Class<T> clazz;

    public BaseSessionCache(String prefix, int secondTimeout, Class<T> clazz) {
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

    public int getTimeout() {
        return timeout;
    }

    public String get(String key) {
        String finalKey = convertFinalKey(key);
        return JedisUtils.Strings.get(finalKey);
    }

    private String convertFinalKey(String key) {
        if (key == null) {
            throw new NullPointerException("key is null.");
        }
        return prefix + "#" + key;
    }

    public T getObj(String key) {
        String finalKey = convertFinalKey(key);
        return JedisUtils.Strings.get(finalKey, clazz);
    }

    public boolean put(String key, T value) {
        String finalKey = convertFinalKey(key);
        return JedisUtils.Strings.setex(finalKey, timeout, value);
    }

    public boolean put(String key, T value, int timeout) {
        String finalKey = convertFinalKey(key);
        if (timeout <= 0) {
            timeout = getTimeout();
        }
        return JedisUtils.Strings.setex(finalKey, timeout, value);
    }

    public void remove(String key) {
        String finalKey = convertFinalKey(key);
        JedisUtils.Strings.del(finalKey);
    }

    public boolean expire(String key) {
        String finalKey = convertFinalKey(key);
        return JedisUtils.Strings.expire(finalKey, timeout);
    }

    public boolean expire(String key, int timeout) {
        String finalKey = convertFinalKey(key);
        if (timeout <= 0) {
            timeout = getTimeout();
        }
        return JedisUtils.Strings.expire(finalKey, timeout);
    }
}
