package slive.jedis.client.session.annotation;

import java.lang.annotation.*;

/**
 * 描述：标识会话类中的主键，只能用在字段中，用于标识该字段是否是主键
 *
 * @author slive
 * @date 2019/12/30
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface SessionPKey {

}
