package io.fireflyest.emberlib.database.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 列
 * 
 * @author Fireflyest
 * @since 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface Column {

    /**
     * 列名
     * 
     * @return 列名
     */
    String name() default "";

    /**
     * 数据类型
     * 
     * @return 数据类型
     */
    String dataType() default "";

    /**
     * 是否非空
     * 
     * @return 是否非空
     */
    boolean noNull() default false;



    /**
     * 是否主键
     * 
     * @return 是否主键
     */
    boolean primary() default false;

    /**
     * 默认值
     * 
     * @return 默认值
     */
    String defaultValue() default "NULL";

}
