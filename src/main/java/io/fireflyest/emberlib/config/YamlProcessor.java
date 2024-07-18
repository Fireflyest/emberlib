package io.fireflyest.emberlib.config;

import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * 配置文件生成注解处理
 * 
 * @author Fireflyest
 * @since 1.0
 */
@SupportedAnnotationTypes("io.fireflyest.emberlib.config.annotation.Yaml")
public class YamlProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        final Messager messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "Processing database dao...");
        // 当前处理器支持的所有注解种类
        for (TypeElement typeElement : annotations) {
            // 获得被该注解声明的元素
            for (Element element : roundEnv.getElementsAnnotatedWith(typeElement)) {
                messager.printMessage(Diagnostic.Kind.NOTE, element.getSimpleName());
            }
        }
        return true;
    }


    private class YamlGenerator {
        
        /**
         * 配置文件
         */
        private final YamlConfiguration yaml = new YamlConfiguration();

        /**
         * 保存文件路径
         */
        private final String targetFile;

        public YamlGenerator(String targetFile) {
            this.targetFile = targetFile;
        }
        
    }
    
}
