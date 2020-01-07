package slive.jedis.client.session;

import java.util.List;

/**
 * 描述：<br>
 *
 * @author slive
 * @date 2020/1/4
 */
public interface RelateSessionCache<T> extends SessionCache<T> {

    /**
     * 通过外键获取对应的pojo值
     * @param fField 外键字段
     * @param fkey 外键
     * @return 返回pojo值
     */
    T getByFKey(String fField, String fkey);

    boolean put(T value);

    List<T> getByCategory(String categoryField, String categoryVal);
}
