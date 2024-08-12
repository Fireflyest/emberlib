package io.fireflyest.emberlib.database.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 主键
 * @author Fireflyest
 * @since 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface Primary {

    /**
     * 是否自增
     * 
     * @return 是否自增
     */
    boolean autoIncrement() default false;

}
