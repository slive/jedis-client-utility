package slive.jedis.client.session;

import java.util.List;

/**
 * 描述：<br>
 *  会话缓存接口类，包括获取会话，加入会话，删除会话和延长会话等
 *
 * @author slive
 * @date 2019/12/30
 */
public interface SessionCache<T> {

    /**
     * 默认超时时间，单位s
     * @return
     */
    int getTimeout();

    /**
     * 通过主键获取最原始的字符串值
     * @param key 主键
     * @return 返回值，非空则成功
     */
    String get(String key);

    /**
     * 通过主键获取值对象
     * @param key 主键
     * @return 返回值，非空则成功
     */
    T getObj(String key);

    /**
     * 通过多个主键获取多个对象
     * @param keys 主键数组
     * @return 返回值列表，为空则获取失败
     */
    List<T> getObjs(String... keys);

    /**
     * 通过主键添加或者更新对应的值，更新成功后并在{@link #getTimeout()}时间后失效
     * @param key 主键
     * @param value 值，不可为空
     * @return 成功与否
     */
    boolean put(String key, T value);

    /**
     * 通过主键添加或者更新对应的值
     * @param key 主键
     * @param value 值，不可为空
     * @param timeout 超时时间，单位ms，如果小于或等于0，则取默认值{@link #getTimeout()}
     * @return 成功与否
     */
    boolean put(String key, T value, int timeout);

    /**
     * 删除值
     * @param keys 主键
     */
    void remove(String... keys);

    /**
     * 延长某个值的超时时间{@link #getTimeout()}s
     * @param key 主键
     * @return 成功与否
     */
    boolean expire(String key);

    /**
     * 延长某个值的超时时间
     * @param key 主键
     * @param timeout 超时时间，单位ms，如果小于或等于0，则取默认值{@link #getTimeout()}
     * @return 成功与否
     */
    boolean expire(String key, int timeout);
}
