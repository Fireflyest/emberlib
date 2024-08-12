package io.fireflyest.emberlib.database.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 选择
 * 
 * @author Fireflyest
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Select {

    /**
     * 查询指令
     * 
     * @return 查询指令
     */
    String value() default "SELECT";

}
