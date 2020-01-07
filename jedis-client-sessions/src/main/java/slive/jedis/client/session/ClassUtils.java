package slive.jedis.client.session;

import java.util.*;

/**
 * 描述：<br>
 * 类相关工具类
 * @author slive
 * @date 2020/1/6
 */
public class ClassUtils {

    private static final Class<Void> VOID_CLASS = Void.class;

    private static final Class<Byte> BYTE_CLASS = Byte.class;

    private static final Class<Short> SHORT_CLASS = Short.class;

    private static final Class<Character> CHARACTER_CLASS = Character.class;

    private static final Class<Long> LONG_CLASS = Long.class;

    private static final Class<Integer> INT_CLASS = Integer.class;

    private static final Class<Float> FLOAT_CLASS = Float.class;

    private static final Class<Double> DOUBLE_CLASS = Double.class;

    private static final Class<String> STRING_CLASS = String.class;

    private static final Class<Number> NUMBER_CLASS = Number.class;

    private static final Class<Boolean> BOOLEAN_CLASS = Boolean.class;

    private static final Class<List> LIST_CLASS = List.class;

    private static final Class<Set> SET_CLASS = Set.class;

    private static final Class<Map> MAP_CLASS = Map.class;

    private static final Class<Queue> QUEUE_CLASS = Queue.class;

    private static final Class<Vector> VECTOR_CLASS = Vector.class;

    private static final Class<Collection> COLLECTION_CLASS = Collection.class;

    /**
     * 是否是空类型
     * @param clazz
     * @return
     */
    public static boolean isVoid(Class<?> clazz) {
        return VOID_CLASS.equals(clazz);
    }

    /**
     * 是否是数字类型
     * @param clazz
     * @return
     */
    public static boolean isNumber(Class<?> clazz) {
        return NUMBER_CLASS.isAssignableFrom(clazz);
    }

    /**
     * 是否是布尔类型
     * @param clazz
     * @return
     */
    public static boolean isBoolean(Class<?> clazz) {
        return BOOLEAN_CLASS.equals(clazz);
    }

    /**
     * 是否是原始的类型
     * @param clazz
     * @return
     * @see java.lang.Boolean#TYPE
     * @see java.lang.Character#TYPE
     * @see java.lang.Byte#TYPE
     * @see java.lang.Short#TYPE
     * @see java.lang.Integer#TYPE
     * @see java.lang.Long#TYPE
     * @see java.lang.Float#TYPE
     * @see java.lang.Double#TYPE
     * @see java.lang.Void#TYPE
     */
    public static boolean isPrimitiveType(Class<?> clazz) {
        if (INT_CLASS.equals(clazz)) {
            return true;
        }
        else if (BYTE_CLASS.equals(clazz)) {
            return true;
        }
        else if (CHARACTER_CLASS.equals(clazz)) {
            return true;
        }
        else if (SHORT_CLASS.equals(clazz)) {
            return true;
        }
        else if (LONG_CLASS.equals(clazz)) {
            return true;
        }
        else if (DOUBLE_CLASS.equals(clazz)) {
            return true;
        }
        else if (FLOAT_CLASS.equals(clazz)) {
            return true;
        }
        else {
            return isBoolean(clazz) || isVoid(clazz);
        }
    }

    /**
     * 是否是字符串类型
     * @param clazz
     * @return
     */
    public static boolean isStringType(Class<?> clazz) {
        return STRING_CLASS.equals(clazz);
    }

    /**
     * 是否是简单类型，即{@link #isStringType(Class)} 和{@link #isPrimitiveType(Class)}
     * @param clazz
     * @return
     */
    public static boolean isSimpleType(Class<?> clazz) {
        return (isStringType(clazz) || isPrimitiveType(clazz));
    }

    /**
     * 复合类型，非简单类型{@link #isSimpleType(Class)}或者非大集合类型{@link #isAnyCollectionType(Class)}
     * @param clazz
     * @return
     */
    public static boolean isComplexType(Class<?> clazz) {
        return !(isSimpleType(clazz) || isCollectionType(clazz));
    }

    /**
     * 是否是Map集合类型
     * @param clazz
     * @return
     */
    public static boolean isMapType(Class<?> clazz) {
        return MAP_CLASS.isAssignableFrom(clazz);
    }

    /**
     * 是否是任意集合类型，包括任意继承或者实现{@link Collection},{@link Map}类型
     * @param clazz
     * @return
     */
    public static boolean isAnyCollectionType(Class<?> clazz) {
        return isCollectionType(clazz) || isMapType(clazz) || isArrayType(clazz);
    }

    /**
     * 是否是基本的java集合{@link Collection}类型
     * @param clazz
     * @return
     */
    public static boolean isCollectionType(Class<?> clazz) {
        return COLLECTION_CLASS.isAssignableFrom(clazz);
    }

    /**
     * 是否是继承或者实现{@link List}类型
     * @param clazz
     * @return
     */
    public static boolean isListType(Class<?> clazz) {
        return LIST_CLASS.isAssignableFrom(clazz);
    }

    /**
     * 是否是继承或者实现{@link Set}类型
     * @param clazz
     * @return
     */
    public static boolean isSetType(Class<?> clazz) {
        return SET_CLASS.isAssignableFrom(clazz);
    }

    /**
     * 是否是继承或者实现{@link Queue}类型
     * @param clazz
     * @return
     */
    public static boolean isQueueType(Class<?> clazz) {
        return QUEUE_CLASS.isAssignableFrom(clazz);
    }

    /**
     * 是否是继承或者实现{@link Vector}类型
     * @param clazz
     * @return
     */
    public static boolean isVectorType(Class<?> clazz) {
        return VECTOR_CLASS.isAssignableFrom(clazz);
    }

    /**
     * 是否是数组类型
     * @param clazz
     * @return
     */
    public static boolean isArrayType(Class<?> clazz) {
        return clazz != null ? clazz.isArray() : false;
    }

    public static void main(String[] args) {
        System.out.println(isComplexType(ArrayList.class));
    }
}
