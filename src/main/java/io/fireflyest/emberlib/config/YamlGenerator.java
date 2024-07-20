package io.fireflyest.emberlib.config;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import javax.annotation.Nonnull;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementScanner8;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import org.bukkit.configuration.file.YamlConfiguration;
import io.fireflyest.emberlib.config.annotation.Entry;
import io.fireflyest.emberlib.config.annotation.EntryComments;
import io.fireflyest.emberlib.util.TextUtils;

/**
 * 配置文件生成器
 * 
 * @since 1.0
 */
public class YamlGenerator extends ElementScanner8<Void, Void> {
    
    /**
     * 配置文件
     */
    private final YamlConfiguration yaml = new YamlConfiguration();

    /**
     * 处理环境
     */
    private final ProcessingEnvironment processingEnv;

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
    public YamlGenerator(@Nonnull ProcessingEnvironment processingEnv, 
                         @Nonnull Element element, 
                         @Nonnull String targetFile) {
        this.processingEnv = processingEnv;
        this.element = element;
        this.targetFile = targetFile;
    }

    /**
     * 写入文件
     * 
     * @throws IOException 文件传输错误
     */
    public void generate() throws IOException {
        final FileObject fileObject = processingEnv.getFiler().createResource(
            StandardLocation.CLASS_OUTPUT, 
            "", 
            targetFile, 
            element
        );
        final Writer writer = fileObject.openWriter();
        writer.write("# File " + targetFile + " generated by EmberLib\n\n");
        writer.write(yaml.saveToString());
        writer.flush();
        writer.close();
    }

    @Override
    public Void visitVariable(VariableElement e, Void p) {
        final Entry entry = e.getAnnotation(Entry.class);
        final EntryComments comments = e.getAnnotation(EntryComments.class);
        if (entry != null) {
            final String key = "".equals(entry.value()) ? this.defaultKey(e) : entry.value();
            
            if (comments != null) {
                this.set(key, e.getConstantValue(), comments.value());
            } else {
                this.set(key, e.getConstantValue());
            }
        }
        return null;
    }

    /**
     * 获取变量的默认生成键
     * 
     * @param variableElement 变量元素
     * @return 默认生成键
     */
    private String defaultKey(VariableElement variableElement) {
        return TextUtils.symbolSplit(
            variableElement.getSimpleName().toString().toLowerCase(), "."
        );
    }

    /**
     * 设置键值
     * 
     * @param key 键
     * @param value 值
     */
    private void set(@Nonnull String key, Object value) {
        this.set(key, value, new String[0]);
    }

    /**
     * 设置键值
     * 
     * @param key 键
     * @param value 值
     */
    private void set(@Nonnull String key, Object value, String[] comments) {
        yaml.set(key, value);
        yaml.setComments(key, Arrays.asList(comments));
    }

}
