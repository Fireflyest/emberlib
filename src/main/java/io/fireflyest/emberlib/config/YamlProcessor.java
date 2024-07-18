package io.fireflyest.emberlib.config;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
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
                final YamlGenerator generator = new YamlGenerator(element, "lang/default.yml");
                try {
                    generator.write();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
         * 被注释的类元素
         */
        private final Element element;

        /**
         * 保存文件路径
         */
        private final String targetFile;

        /**
         * 生成器构造
         * 
         * @param element 被注释的类元素
         * @param targetFile 保存文件路径
         */
        public YamlGenerator(@Nonnull Element element, @Nonnull String targetFile) {
            this.element = element;
            this.targetFile = targetFile;
        }

        /**
         * 写入文件
         * 
         * @throws IOException 文件传输错误
         */
        public void write() throws IOException {
            final FileObject fileObject = processingEnv.getFiler().createResource(
                StandardLocation.CLASS_OUTPUT, 
                "", 
                targetFile, 
                element
            );
            final Writer writer = fileObject.openWriter();
            writer.write(yaml.saveToString());
            writer.flush();
            writer.close();
        }
        
    }
    
}
