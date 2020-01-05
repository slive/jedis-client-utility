package slive.jedis.client.session;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * 描述：<br>
 *
 * @author slive
 * @date 2020/1/5
 */
public class BaseRelateSessionCache<T extends BaseSession> extends BaseSessionCache<T>
        implements RelateSessionCache<T> {

    public BaseRelateSessionCache(String prefix, int secondTimeout, Class<T> clazz) {
        super(prefix, secondTimeout, clazz);
        // 要求必须有一个主键
        // 可以有N个外键1:1
        // 可以有M个分类键，1:m
        // 做排序？
        Field[] fields = clazz.getFields();
        for (int index = 0; index < fields.length; index++) {

        }
    }

    public T getByFKey(String fField, String fkey) {
        return null;
    }

    public boolean put(T value) {
        return false;
    }

    public Set<T> getByCategory(String categoryField, String categoryVal) {
        return null;
    }
}
