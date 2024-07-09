package io.fireflyest.emberlib.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 生成语言资源文件
 * 
 * @author Fireflyest
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Lang {
    
    /**
     * 生成的文件名称
     */
    String name() default "default";

}
