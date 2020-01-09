package slive.jedis.client.session.annotation;

import java.lang.annotation.*;

/**
 * 描述：标识会话分类，只能用在字段中，用于标识某类会话，目前只能用在String类型的字段中
 *
 * @author slive
 * @date 2019/12/30
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface SessionCategory {
    /**
     * 自定义字段名称，默认为空
     * @return
     */
    String value() default "";
}
