package io.fireflyest.emberlib.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 生成配置资源文件中的一个键值对
 * 
 * @author Fireflyest
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Entry {
    
    /**
     * 生成的键名称
     * 
     * @return 生成的键名称
     */
    String value() default "";

}
