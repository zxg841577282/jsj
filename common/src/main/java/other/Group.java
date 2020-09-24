package other;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;

/**
 * @Author: lch
 * @CreateTime: 2019-10-31 12:01
 * @Description: 校验分组注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ PARAMETER})
public @interface Group {

    Class value() default void.class;
}
