//==============================================================================
//
//	@author Slive
//	@date  2018-9-11
//
//==============================================================================
package slive.jedis.client.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import com.alibaba.fastjson.JSON;

/**
 * 描述：pojo对象->json字符串，json字符串->pojo对象转换
 */
public final class PojoJsonUtils {

    @SuppressWarnings("unchecked")
    public static <T> T convert2Object(String value, Class<T> clazz) {
        if (value == null || clazz == null) {
            return null;
        }
        if (String.class.equals(clazz)) {
            return (T) value;
        }
        return JSON.parseObject(value, clazz);
    }

    public static String convert2String(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        }
        return JSON.toJSONString(value);
    }

    public static <T> List<T> convert2ObjectList(Collection<String> values, Class<T> clazz) {
        List<T> l = new LinkedList<T>();
        for (String s : values) {
            l.add(convert2Object(s, clazz));
        }
        return l;
    }

    public static List<String> convert2ObjectList(Collection<Object> values) {
        List<String> l = new LinkedList<String>();
        for (Object t : values) {
            l.add(convert2String(t));
        }
        return l;
    }
}
