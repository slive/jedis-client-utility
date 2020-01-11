package slive.jedis.client.session.annotation;

import java.lang.annotation.*;

/**
 * 描述：<br><pre>
 *     标识会话类中的外键，只能用在字段中使用，用于标识该字段是否是外键，目前只能用在String类型或者Collection，Map，Array中String等类型的字段中。
 *     自定义字段名称，如果为空，则取字段名称
 * 注意：
 *  1、如果同一个缓存类实例中，与主键是一对一关系，如果a会话中外键F1与b会话中外键F1值相同，可能会导致覆盖；
 *  2、外键不可重复
 *
 * @author slive
 * @date 2019/12/30
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface SessionFKey {
    /**
     * 自定义字段名称，默认为空，则取字段名称
     * @return
     */
    String value() default "";
}
