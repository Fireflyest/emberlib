package io.fireflyest.emberlib.database.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 更新
 * UPDATE table_name
 * SET column1 = value1, column2 = value2, ...
 * WHERE condition;
 * 
 * @author Fireflyest
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Update {

    /**
     * 更新指令
     * 
     * @return 更新指令
     */
    String value() default "UPDATE";

}
