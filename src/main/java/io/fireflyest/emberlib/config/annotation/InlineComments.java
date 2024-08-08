package io.fireflyest.emberlib.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 生成配置资源文件中的一个键值对的行注释
 * 
 * @author Fireflyest
 * @since 1.0
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD})
public @interface InlineComments {

    /**
     * 注释
     * 
     * @return 注释
     */
    String[] value();

}
