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
 * 基本的关联会话缓存类，主要实现功能包括： 基于主键的加入，更新，删除和会话延长等。 支持基于外键{@link SessionFKey}或者分类{@link SessionCategory}查询
 *
 * <i>注意：所有操作不具有原子性，可能存在数据同步问题。</i>
 *
 * @author slive
 * @date 2020/1/5
 */
public class BaseRelateSessionCache<T> extends BaseSessionCache<T> implements RelateSessionCache<T> {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRelateSessionCache.class);

    private static final int PUT_ACTION = 1;

    private static final int REMOVE_ACTION = 2;

    private static final int UPDATE_ACTION = 3;

    private Map<String, FSessionCache> fSessionCaches = new HashMap<>();

    private Map<String, CategorySessionCache> categorys = new HashMap<>();

    private boolean isRelate;

    private Method sessionKeyMethod;

    private Field sessionKeyField;

    public BaseRelateSessionCache(String prefix, int secondTimeout, Class<T> clazz) {
        super(prefix, secondTimeout, clazz);
        // 要求必须有一个主键
        // 可以有N个外键，每个外键和主键关系为：1:1
        // 可以有M个分类键，每个外键和主键关系为：1:m
        Field[] fields = clazz.getDeclaredFields();
        for (int index = 0; index < fields.length; index++) {
            Field field = fields[index];
            Class<?> fClazz = field.getType();
            String fName = field.getName();
            SessionKey sk = field.getAnnotation(SessionKey.class);
            if (sk != null) {
                vaildType(fClazz, fName);
                // 主键只能一个
                if (sessionKeyField != null) {
                    throw new RuntimeException("SessionKey has existed, the field:" + fName);
                }
                else {
                    sessionKeyField = field;
                    sessionKeyMethod = fetchGetMethod(clazz, fName);
                    // 主键不能做外键
                    continue;
                }
            }

            // 初始化外键
            SessionFKey fk = field.getAnnotation(SessionFKey.class);
            if (fk != null) {
                vaildFkType(fClazz, fName);
                String fPrefix = convertFinalFPrefix(prefix, fk, fName);
                if (!fSessionCaches.containsKey(fPrefix)) {
                    Method getMethod = fetchGetMethod(clazz, fName);
                    FSessionCache fbs = new FSessionCache(fPrefix, secondTimeout, field, getMethod);
                    fSessionCaches.put(fPrefix, fbs);
                }
                else {
                    throw new RuntimeException("SessionFKey has existed, the field:" + fName);
                }
            }

            // 初始化分类
            SessionCategory sc = field.getAnnotation(SessionCategory.class);
            if (sc != null) {
                vaildType(fClazz, fName);
                Method getMethod = fetchGetMethod(clazz, fName);
                String categoryName = convertFinalCategoryName(prefix, sc, fName);
                CategorySessionCache csc = new CategorySessionCache(prefix, getTimeout(), categoryName, getMethod);
                categorys.put(csc.getCategoryName(), csc);
            }
            if (sessionKeyMethod == null) {
                throw new RuntimeException("SessionKey is null.");
            }
            isRelate = !(categorys.isEmpty() && fSessionCaches.isEmpty());
        }
    }

    private static Method fetchGetMethod(Class<?> clazz, String fName) {
        String first = fName.substring(0, 1);
        String firstUpperFName = first.toUpperCase();
        String nFName = fName.replaceFirst(first, firstUpperFName);
        String readMethodName = "get" + nFName;
        Method gMethod = null;
        try {
            gMethod = clazz.getMethod(readMethodName);
        }
        catch (NoSuchMethodException e) {
            // ignore
            LOGGER.warn("method can not found1:{}", readMethodName);
        }
        try {
            if (gMethod == null) {
                readMethodName = "get" + fName;
                gMethod = clazz.getMethod(readMethodName);
            }
        }
        catch (NoSuchMethodException e) {
            // ignore
            LOGGER.warn("method can not found2:{}", readMethodName);
        }
        if (gMethod == null) {
            throw new RuntimeException(readMethodName + " has not existed.");
        }
        LOGGER.info("method:{}", gMethod);
        return gMethod;
    }

    private static String convertFinalFPrefix(String prefix, SessionFKey fk, String fName) {
        String fkName = fk.value();
        if (fkName == null || fkName.equals("")) {
            fkName = fName;
        }
        return prefix + "#fk#" + fkName;
    }

    private static String convertFinalFPrefix(String prefix, String fkName) {
        return prefix + "#fk#" + fkName;
    }

    private static void vaildType(Class<?> clazz, String fName) {
        if (!ClassUtils.isStringType(clazz)) {
            throw new RuntimeException("SessionKey field:" + fName + " must be [String] type.");
        }
    }

    private static void vaildFkType(Class<?> clazz, String fName) {
        // 集合等类型，必须是key或者值是String
        if (ClassUtils.isAnyCollectionType(clazz) || ClassUtils.isMapType(clazz) || ClassUtils.isArrayType(clazz)) {
            clazz = ClassUtils.getComponentType(clazz);
        }

        if (!(ClassUtils.isStringType(clazz))) {
            throw new RuntimeException("SessionKey field:" + fName + " must be [String] type.");
        }
    }

    @Override
    public boolean put(String key, T value) {
        if (isRelate) {
            T oldObj = getObj(key);
            LOGGER.info("oldvalue:{}", oldObj);
            boolean ret = super.put(key, value);
            if (ret) {
                handle(key, value, oldObj, PUT_ACTION);
            }
            return ret;
        }
        else {
            return super.put(key, value);
        }
    }

    private void handle(String key, T value, T oldObj, int handleAction) {
        // 处理外键
        try {
            for (Map.Entry<String, FSessionCache> fsce : fSessionCaches.entrySet()) {
                FSessionCache fsc = fsce.getValue();
                // TODO 数组的情况需要处理
                Method method = fsc.getMethod();
                Object fV = null;
                if (oldObj != null) {
                    // 若有旧的值，则先删除
                    fV = method.invoke(oldObj);
                    if (fV != null) {
                        String[] fvs = fetchFKeys(fV);
                        if (fvs != null) {
                            fsc.remove(fvs);
                        }
                        else {
                            fsc.remove(fV.toString());
                        }
                    }
                }
                if (value != null) {
                    // 更新新的值
                    fV = method.invoke(value);
                    if (fV != null) {
                        String[] fvs = fetchFKeys(fV);
                        if (handleAction == PUT_ACTION) {
                            for (String newFkey : fvs) {
                                fsc.put(newFkey, key);
                            }
                        }
                        else if (handleAction == REMOVE_ACTION) {
                            fsc.remove(fvs);
                        }
                        else {
                            for (String newFkey : fvs) {
                                fsc.expire(newFkey);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            LOGGER.error("fkey handle error.", e);
        }

        try {
            for (CategorySessionCache csc : categorys.values()) {
                Method method = csc.getMethod();
                Object fV = null;
                // 若有旧的值，则先删除
                String fkey = null;
                if (oldObj != null) {
                    fV = method.invoke(oldObj, null);
                    if (fV != null) {
                        fkey = fV.toString();
                        csc.remove(fkey, key);
                    }
                }

                if (value != null) {
                    // 更新新的值
                    fV = method.invoke(value, null);
                    if (fV != null) {
                        fkey = fV.toString();
                        if (handleAction == PUT_ACTION) {
                            csc.put(fkey, key);
                        }
                        else if (handleAction == REMOVE_ACTION) {
                            csc.remove(fkey, key);
                        }
                        else {
                            csc.expire(fkey);
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            LOGGER.error("category handle error.", e);
        }
    }

    private String[] fetchFKeys(Object fV) {
        String[] fvs = null;
        Class<?> fvClass = fV.getClass();
        if (ClassUtils.isArrayType(fvClass)) {
            fvs = (String[]) fV;
        }
        else if (ClassUtils.isMapType(fvClass)) {
            Set<String> fkSet = ((Map<String, Object>) fV).keySet();
            fvs = new String[fkSet.size()];
            fkSet.toArray(fvs);
        }
        else if (ClassUtils.isCollectionType(fvClass)) {
            Collection<String> fkSet = ((Collection<String>) fV);
            fvs = new String[fkSet.size()];
            fkSet.toArray(fvs);
        }
        else {
            fvs = new String[] { fV.toString() };
        }
        return fvs;
    }

    @Override
    public void remove(String... keys) {
        if (keys != null && isRelate) {
            for (String key : keys) {
                T obj = getObj(key);
                handle(key, null, obj, REMOVE_ACTION);
            }
        }
        super.remove(keys);
    }

    @Override
    public boolean expire(String key) {
        if (key != null && isRelate) {
            T obj = getObj(key);
            handle(key, null, obj, UPDATE_ACTION);
        }
        return super.expire(key);
    }

    @Override
    public T getByFKey(String fkName, String fkVal) {
        FSessionCache fSessionCache = fSessionCaches.get(convertFinalFPrefix(prefix, fkName));
        if (fSessionCache == null) {
            LOGGER.warn("FSessionCache is null, fkName:{}", fkName);
        }

        String key = fSessionCache.getObj(fkVal);
        return getObj(key);
    }

    @Override
    public boolean put(T value) {
        try {
            Object key = sessionKeyMethod.invoke(value);
            LOGGER.info("key:{}", key);
            if (key == null) {
                return false;
            }
            return put(key.toString(), value);
        }
        catch (Exception e) {
            LOGGER.error("put value error:", e);
        }
        return false;
    }

    @Override
    public List<T> getByCategory(String categoryName, String cVal) {
        CategorySessionCache css = categorys.get(convertFinalCategoryName(prefix, categoryName));
        if (css == null) {
            LOGGER.warn("CategorySessionCache is null, categoryName:{}", categoryName);
            return null;
        }

        Set<String> keys = css.get(cVal);
        if (keys == null || keys.isEmpty()) {
            return null;
        }
        return getObjs(keys.toArray(new String[keys.size()]));
    }

    @Override
    public boolean expireByFKey(String fkName, String fkey) {
        FSessionCache fSessionCache = fSessionCaches.get(convertFinalFPrefix(prefix, fkName));
        if (fSessionCache == null) {
            LOGGER.warn("FSessionCache is null, fkName:{}", fkName);
        }
        String key = fSessionCache.getObj(fkey);
        return expire(key);
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

        private Method method;

        private String categoryName;

        private int timeout;

        public CategorySessionCache(String prefix, int secondTimeout, String categoryName, Method method) {
            this.method = method;
            this.prefix = prefix;
            this.categoryName = categoryName;
            this.timeout = secondTimeout;
        }

        public Method getMethod() {
            return method;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void put(String key, String value) {
            key = convertFinalCategoryKey(prefix, key);
            JedisUtils.SortSets.zadd(key, -System.currentTimeMillis() * 0.1, value);
            JedisUtils.SortSets.expire(key, timeout);
        }

        public void remove(String key, String value) {
            key = convertFinalCategoryKey(prefix, key);
            JedisUtils.SortSets.zrem(key, value);
            JedisUtils.SortSets.expire(key, timeout);
        }

        public void expire(String key) {
            key = convertFinalCategoryKey(prefix, key);
            JedisUtils.SortSets.expire(key, timeout);
        }

        public void clear(String key) {
            key = convertFinalCategoryKey(prefix, key);
            JedisUtils.SortSets.del(key);
        }

        public Set<String> get(String key) {
            key = convertFinalCategoryKey(prefix, key);
            long zcar = JedisUtils.SortSets.zcar(key);
            if (zcar == 0) {
                return Collections.emptySet();
            }
            return JedisUtils.SortSets.zrange(key, 0, zcar);
        }

    }

    private static String convertFinalCategoryName(String prefix, SessionCategory sc, String fName) {
        String scName = sc.value();
        if (scName == null || scName.equals("")) {
            scName = fName;
        }
        return prefix + "#cs#" + scName;
    }

    private static String convertFinalCategoryName(String prefix, String scName) {
        return prefix + "#cs#" + scName;
    }

    private static String convertFinalCategoryKey(String prefix, String key) {
        return prefix + "#cs#" + key;
    }

}
