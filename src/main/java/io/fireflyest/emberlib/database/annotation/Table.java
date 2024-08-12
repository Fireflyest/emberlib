package io.fireflyest.emberlib.database.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表
 * 
 * @author Fireflyest
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Table {

    /**
     * 表名
     * 
     * @return 表名
     */
    String value() default "";

}
