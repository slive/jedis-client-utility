package slive.jedis.client.session;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slive.jedis.client.session.annotation.SessionCategory;
import slive.jedis.client.session.annotation.SessionFKey;
import slive.jedis.client.session.annotation.SessionKey;
import slive.jedis.client.util.JedisUtils;

/**
 * 描述：<br>
 *
 * @author slive
 * @date 2020/1/5
 */
public class BaseRelateSessionCache<T> extends BaseSessionCache<T> implements RelateSessionCache<T> {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRelateSessionCache.class);

    private Map<String, FSessionCache> fSessionCaches = new HashMap<>();

    private Map<String, CategorySessionCache> categorys = new HashMap<>();

    private Method sessionKeyMethod;

    private Field sessionKeyField;

    public BaseRelateSessionCache(String prefix, int secondTimeout, Class<T> clazz) {
        super(prefix, secondTimeout, clazz);
        // 要求必须有一个主键
        // 可以有N个外键1:1
        // 可以有M个分类键，1:m
        // 做排序？
        Field[] fields = clazz.getDeclaredFields();
        for (int index = 0; index < fields.length; index++) {
            Field field = fields[index];
            Class<?> fClazz = field.getType();
            String fName = field.getName();
            SessionKey sk = field.getAnnotation(SessionKey.class);
            if (sk != null) {
                vaildType(fClazz, fName);
                if (sessionKeyField != null) {
                    throw new RuntimeException("SessionKey has existed, the field:" + fName);
                }
                else {
                    sessionKeyField = field;
                    sessionKeyMethod = fetchGetMethod(clazz, fName);
                    continue;
                }
            }

            SessionFKey fk = field.getAnnotation(SessionFKey.class);
            if (fk != null) {
                vaildType(fClazz, fName);
                String fPrefix = convertFinalFPrefix(prefix, fName);
                if (!fSessionCaches.containsKey(fPrefix)) {
                    Method getMethod = fetchGetMethod(clazz, fName);
                    FSessionCache fbs = new FSessionCache(fPrefix, secondTimeout, field, getMethod);
                    fSessionCaches.put(fPrefix, fbs);
                }
                else {
                    throw new RuntimeException("SessionFKey has existed, the field:" + fName);
                }
            }

            SessionCategory sc = field.getAnnotation(SessionCategory.class);
            if (sc != null) {
                vaildType(fClazz, fName);
                Method getMethod = fetchGetMethod(clazz, fName);
                CategorySessionCache csc = new CategorySessionCache(prefix, getTimeout(), field, getMethod);
                categorys.put(csc.getCategoryName(), csc);
            }
        }
    }

    private Method fetchGetMethod(Class<T> clazz, String fName) {
        String first = fName.substring(0, 1);
        String firstUpperFName = first.toUpperCase();
        String nFName = fName.replaceFirst(first, firstUpperFName);
        String readMethodName = "get" + nFName;
        Method getMethod = null;
        try {
            getMethod = clazz.getMethod(readMethodName, null);
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
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
        LOGGER.info("method:{}", getMethod);
        return getMethod;
    }

    private static String convertFinalFPrefix(String prefix, String fName) {
        return prefix + "#fk#" + fName;
    }

    private static void vaildType(Class<?> clazz, String fName) {
        if (!ClassUtils.isStringType(clazz)) {
            throw new RuntimeException("SessionKey field:" + fName + " must be [String] type.");
        }
    }

    @Override
    public boolean put(String key, T value) {
        T oldObj = getObj(key);
        LOGGER.info("oldvalue:{}", oldObj);
        super.put(key, value);
        // 处理外键
        for (Map.Entry<String, FSessionCache> fsce : fSessionCaches.entrySet()) {
            FSessionCache fsc = fsce.getValue();
            try {
                Method method = fsc.getMethod();
                Object fV = null;
                // 若有旧的值，则先删除
                if (oldObj != null) {
                    fV = method.invoke(value, null);
                    if (fV != null) {
                        fsc.remove(fV.toString());
                    }
                }
                // 更新新的值
                fV = method.invoke(value, null);
                if (fV != null) {
                    fsc.put(fV.toString(), key);
                }
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        for (CategorySessionCache csc : categorys.values()) {
            try {
                Method method = csc.getMethod();
                Object fV = null;
                // 若有旧的值，则先删除
                if (oldObj != null) {
                    fV = method.invoke(value, null);
                    if (fV != null) {
                        csc.remove(fV.toString(), key);
                    }
                }
                // 更新新的值
                fV = method.invoke(value, null);
                if (fV != null) {
                    csc.put(fV.toString(), key);
                }
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    @Override
    public void remove(String key) {
        super.remove(key);
        ///
    }

    @Override
    public T getByFKey(String fkName, String fkVal) {
        String key = fSessionCaches.get(convertFinalFPrefix(prefix, fkName)).getObj(fkVal);
        return getObj(key);
    }

    @Override
    public boolean put(T value) {
        try {
            Object key = sessionKeyMethod.invoke(value, null);
            LOGGER.info("key:{}", key);
            return put(key.toString(), value);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<T> getByCategory(String cName, String cVal) {
        Set<String> keys = categorys.get(convertFinalCategoryName(prefix, cName)).get(cVal);
        if (keys == null || keys.isEmpty()) {
            return null;
        }
        return getObjs(keys.toArray(new String[keys.size()]));
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

    class CategorySessionCache {

        private String prefix;

        private Field field;

        private Method method;

        private String categoryName;

        private int timeout;

        public CategorySessionCache(String prefix, int secondTimeout, Field field, Method method) {
            this.field = field;
            this.method = method;
            this.prefix = prefix;
            String cName = field.getName();
            this.categoryName = convertFinalCategoryName(prefix, cName);
            this.timeout = secondTimeout;
        }

        public Field getField() {
            return field;
        }

        public Method getMethod() {
            return method;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void put(String key, String value) {
            key = convertFinalCategoryKey(prefix, key);
            JedisUtils.SortSets.zadd(key, -System.currentTimeMillis(), value);
            JedisUtils.SortSets.expire(key, timeout);
        }

        public void remove(String key, String value) {
            key = convertFinalCategoryKey(prefix, key);
            JedisUtils.SortSets.zrem(key, value);
            JedisUtils.SortSets.expire(key, timeout);
        }

        public void clear(String key) {
            key = convertFinalCategoryKey(prefix, key);
            JedisUtils.SortSets.del(key);
        }

        public Set<String> get(String key) {
            key = convertFinalCategoryKey(prefix, key);
            long zcar = JedisUtils.SortSets.zcar(key);
            if(zcar == 0){
                return Collections.emptySet();
            }
            return JedisUtils.SortSets.zrange(key, 0, zcar);
        }

    }

    private static String convertFinalCategoryName(String prefix, String cName) {
        return prefix + "#cs#" + cName;
    }

    private static String convertFinalCategoryKey(String prefix, String key) {
        return prefix + "#cs#" + key;
    }

}
