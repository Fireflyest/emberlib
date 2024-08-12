package io.fireflyest.emberlib.database;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import com.google.auto.service.AutoService;
import io.fireflyest.emberlib.database.annotation.Dao;
import io.fireflyest.emberlib.database.annotation.Table;

/**
 * DAO文件生成注解处理
 * 
 * @author Fireflyest
 * @since 1.0
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({
    "io.fireflyest.emberlib.database.annotation.Table",
    "io.fireflyest.emberlib.database.annotation.Dao"
})
public class DatabaseProcessor extends AbstractProcessor {


    private final Map<String, TableInfo> tableMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Table.class)) {
            this.processTableElement(element);
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(Dao.class)) {
            this.processDaoElement(element);
        }
        return true;
    }
    
    /**
     * 处理被注解的类
     * 
     * @param element 元素
     */
    private void processTableElement(@Nonnull Element element) {
        final Table table = element.getAnnotation(Table.class);
        final TableGenerator generator = new TableGenerator(table.value());

        element.accept(generator, null);

        tableMap.put(element.asType().toString(), generator.generator());
    }

    /**
     * 处理被注解的类
     * 
     * @param element 元素
     */
    private void processDaoElement(@Nonnull Element element) {
        final Dao dao = element.getAnnotation(Dao.class);
        final DaoGenerator generator = new DaoGenerator(
            processingEnv, 
            (TypeElement) element, 
            dao.value(),
            tableMap.get(dao.value())
        );

        element.accept(generator, null);

        try {
            generator.generate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
