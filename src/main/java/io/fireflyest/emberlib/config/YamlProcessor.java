package io.fireflyest.emberlib.config;

import java.io.IOException;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import com.google.auto.service.AutoService;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
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

    private Messager messager;   // 用来在编译期打印日志
    private JavacTrees trees;    // 提供了待处理的抽象语法树
    private TreeMaker treeMaker; // 封装了创建AST节点的一些方法
    private Names names;         // 提供了创建标识符的方法

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        final Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.messager = processingEnv.getMessager();
        this.trees = JavacTrees.instance(processingEnv);
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
    }

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
    }
    
}
