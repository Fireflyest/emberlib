package io.fireflyest.emberlib.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 生成配置资源文件
 * 
 * @author Fireflyest
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Config {
    
    /**
     * 生成的文件名称
     */
    String name() default "config";

}
