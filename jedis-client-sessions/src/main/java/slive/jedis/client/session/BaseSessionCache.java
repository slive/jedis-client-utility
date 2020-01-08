package slive.jedis.client.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import slive.jedis.client.util.JedisUtils;

/**
 * 描述：<br>
 * 基本的缓存会话实现
 *
 * @author slive
 * @date 2020/1/4
 */
public class BaseSessionCache<T> implements SessionCache<T> {

    private static final Map<String, BaseSessionCache> CACHE_MAP = new HashMap<String, BaseSessionCache>();

    private int timeout;

    protected String prefix;

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

    @Override
    public int getTimeout() {
        return timeout;
    }

    @Override
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

    @Override
    public T getObj(String key) {
        String finalKey = convertFinalKey(key);
        return JedisUtils.Strings.get(finalKey, clazz);
    }

    @Override
    public List<T> getObjs(String... keys) {
        if (keys == null) {
            return null;
        }
        List<String> finalKeys = new ArrayList<>(keys.length);
        for (String key : keys) {
            finalKeys.add(convertFinalKey(key));
        }
        return JedisUtils.Strings.mget(clazz, finalKeys.toArray(new String[finalKeys.size()]));
    }

    @Override
    public boolean put(String key, T value) {
        String finalKey = convertFinalKey(key);
        return JedisUtils.Strings.setex(finalKey, timeout, value);
    }

    @Override
    public boolean put(String key, T value, int timeout) {
        String finalKey = convertFinalKey(key);
        if (timeout <= 0) {
            timeout = getTimeout();
        }
        return JedisUtils.Strings.setex(finalKey, timeout, value);
    }

    @Override
    public void remove(String key) {
        String finalKey = convertFinalKey(key);
        JedisUtils.Strings.del(finalKey);
    }

    @Override
    public boolean expire(String key) {
        String finalKey = convertFinalKey(key);
        return JedisUtils.Strings.expire(finalKey, timeout);
    }

    @Override
    public boolean expire(String key, int timeout) {
        String finalKey = convertFinalKey(key);
        if (timeout <= 0) {
            timeout = getTimeout();
        }
        return JedisUtils.Strings.expire(finalKey, timeout);
    }
}
