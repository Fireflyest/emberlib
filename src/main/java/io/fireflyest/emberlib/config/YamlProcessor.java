package io.fireflyest.emberlib.config;

import java.io.IOException;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import com.google.auto.service.AutoService;
import io.fireflyest.emberlib.config.annotation.Yaml;

/**
 * 配置文件生成注解处理
 * 
 * @author Fireflyest
 * @since 1.0
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("io.fireflyest.emberlib.config.annotation.Yaml")
public class YamlProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Yaml.class)) {
            this.processElement(element);
        }
        return true;
    }

    /**
     * 处理被注解的类
     * 
     * @param element 元素
     */
    private void processElement(@Nonnull Element element) {
        final Yaml yaml = element.getAnnotation(Yaml.class);
        final YamlGenerator generator = new YamlGenerator(processingEnv, element, yaml.value());

        element.accept(generator, null);

        try {
            generator.generate();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // JavacTrees.instance(processingEnv);
    }
    
}
