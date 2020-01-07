package slive.jedis.client.session;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import slive.jedis.client.session.annotation.SessionCategory;
import slive.jedis.client.session.annotation.SessionFKey;
import slive.jedis.client.session.annotation.SessionKey;

/**
 * 描述：<br>
 *
 * @author slive
 * @date 2020/1/5
 */
public class BaseRelateSessionCache<T> extends BaseSessionCache<T> implements RelateSessionCache<T> {

    private Map<String, FSessionCache> fSessionCaches = new HashMap<>();

    private Method sessionKeyMethod;

    private Field sessionKeyField;

    public BaseRelateSessionCache(String prefix, int secondTimeout, Class<T> clazz) {
        super(prefix, secondTimeout, clazz);
        // 要求必须有一个主键
        // 可以有N个外键1:1
        // 可以有M个分类键，1:m
        // 做排序？
        Field[] fields = clazz.getFields();
        for (int index = 0; index < fields.length; index++) {
            Field field = fields[index];
            Class<?> fClazz = field.getType();
            if (!ClassUtils.isStringType(fClazz)) {
                throw new RuntimeException("");
            }
            String fName = field.getName();
            SessionKey sk = field.getAnnotation(SessionKey.class);
            if (sk != null) {
                vaildType(clazz, fName);
                if (sessionKeyField != null) {
                    throw new RuntimeException("SessionKey has existed, the field:" + fName);
                }
                else {
                    sessionKeyField = field;
                    continue;
                }
            }

            SessionFKey fk = field.getAnnotation(SessionFKey.class);
            if (fk != null) {
                vaildType(clazz, fName);
                String fPrefix = convertFinalFPrefix(prefix, fName);
                if (fSessionCaches.containsKey(fPrefix)) {
                    String first = fName.substring(0, 1);
                    String firstUpperFName = first.toUpperCase();
                    String nFName = fName.replaceFirst(first, firstUpperFName);
                    String readMethodName = "get" + nFName;
                    Method getMethod = null;
                    try {
                        getMethod = clazz.getMethod(readMethodName, null);
                        if (getMethod == null) {
                            readMethodName = "get" + fName;
                            getMethod = clazz.getMethod(readMethodName, null);
                        }
                    }
                    catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    if (getMethod == null) {
                        throw new RuntimeException(readMethodName + " has not existed.");
                    }
                    FSessionCache fbs = new FSessionCache(fPrefix, secondTimeout, field, getMethod);
                    fSessionCaches.put(fPrefix, fbs);
                }
                else {
                    throw new RuntimeException("SessionFKey has existed, the field:" + fName);
                }
            }

            SessionCategory sc = field.getAnnotation(SessionCategory.class);
            if (sc != null) {
                vaildType(clazz, fName);
            }
        }
    }

    private static String convertFinalFPrefix(String prefix, String fName) {
        return prefix + "#" + fName;
    }

    private static void vaildType(Class<?> clazz, String fName) {
        if (!ClassUtils.isStringType(clazz)) {
            throw new RuntimeException("SessionKey field:" + fName + " must be [String] type.");
        }
    }

    @Override
    public boolean put(String key, T value) {

        return super.put(key, value);
    }

    @Override
    public T getByFKey(String fField, String fkey) {
        return null;
    }

    @Override
    public boolean put(T value) {
        return false;
    }

    @Override
    public Set<T> getByCategory(String categoryField, String categoryVal) {
        return null;
    }

    class FSessionCache extends BaseSessionCache<String> {

        private Field field;

        private Method method;

        public FSessionCache(String prefix, int secondTimeout, Field field, Method method) {
            super(prefix, secondTimeout, String.class);
            this.field = field;
            this.method = method;
        }

        public Field getField() {
            return field;
        }

        public Method getMethod() {
            return method;
        }
    }

}
