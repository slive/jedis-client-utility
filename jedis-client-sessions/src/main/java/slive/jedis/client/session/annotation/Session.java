package slive.jedis.client.session.annotation;

import java.lang.annotation.*;

/**
 * 描述：标识某个类为会话类，只能用在类中，用于标识该字段是否是一个会话类
 *
 * @author slive
 * @date 2019/12/30
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE })
public @interface Session {

}
