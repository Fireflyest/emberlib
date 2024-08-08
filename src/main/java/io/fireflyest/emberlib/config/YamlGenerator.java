package io.fireflyest.emberlib.config;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import javax.annotation.Nonnull;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementScanner8;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import org.bukkit.configuration.file.YamlConfiguration;
import io.fireflyest.emberlib.config.annotation.Entry;
import io.fireflyest.emberlib.config.annotation.InlineComments;
import io.fireflyest.emberlib.config.annotation.Comments;
import io.fireflyest.emberlib.util.YamlUtils;

/**
 * 配置文件生成器
 * 
 * @since 1.0
 */
public class YamlGenerator extends ElementScanner8<Void, Void> {
    
    private final YamlConfiguration yaml = new YamlConfiguration();
    private final ProcessingEnvironment processingEnv;
    private final TypeElement typeElement;
    private final String targetFile;

    /**
     * 生成器构造
     * 
     * @param processingEnv 处理环境
     * @param typeElement 被注释的类元素
     * @param targetFile 保存文件路径
     */
    public YamlGenerator(@Nonnull ProcessingEnvironment processingEnv, 
                         @Nonnull TypeElement typeElement, 
                         @Nonnull String targetFile) {
        this.processingEnv = processingEnv;
        this.typeElement = typeElement;
        this.targetFile = targetFile;
    }

    /**
     * 写入文件
     * 
     * @throws IOException 文件传输错误
     */
    public void generate() throws IOException {
        final FileObject ymlFileObject = processingEnv.getFiler().createResource(
            StandardLocation.CLASS_OUTPUT, 
            "", 
            targetFile, 
            typeElement
        );
        final Writer writer = ymlFileObject.openWriter();
        writer.write("# File " + targetFile + " generated by EmberLib\n\n");
        writer.write(yaml.saveToString());
        writer.flush();
        writer.close();
    }

    @Override
    public Void visitVariable(VariableElement varElement, Void p) {
        final Entry entry = varElement.getAnnotation(Entry.class);
        final Comments comments = varElement.getAnnotation(Comments.class);
        final InlineComments inlineComments = varElement.getAnnotation(InlineComments.class);
        if (entry != null) {
            // 键
            String key = entry.value();
            if ("".equals(key)) {
                key = YamlUtils.defaultKey(varElement.getSimpleName().toString());
            }
            // 配置文件内设置值及其注释
            yaml.createSection(key);
            if (comments != null) {
                yaml.setComments(key, Arrays.asList(comments.value()));
            }
            if (inlineComments != null) {
                yaml.setInlineComments(key, Arrays.asList(inlineComments.value()));
            }
        }
        return null;
    }

}
