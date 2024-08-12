package io.fireflyest.emberlib.database.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务
 * 
 * @author Fireflyest
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {

    /**
     * 是否需要建表
     * 
     * @return 是否需要建表
     */
    boolean createTable() default true;

}
