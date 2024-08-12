package io.fireflyest.emberlib.database.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 插入
 * INSERT INTO table_name (column1,column2,column3,...)
 * VALUES (value1,value2,value3,...);
 * 
 * @author Fireflyest
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Insert {

    /**
     * 插入指令
     * 
     * @return 插入指令
     */
    String value() default "INSERT";

}
