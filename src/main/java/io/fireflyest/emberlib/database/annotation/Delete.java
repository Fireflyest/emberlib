package io.fireflyest.emberlib.database.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 删除
 * DELETE FROM table_name
 * WHERE condition;
 * 
 * @author Fireflyest
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Delete {

    /**
     * 删除指令
     * 
     * @return 删除指令
     */
    String value() default "DELETE";

}
