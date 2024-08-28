package io.fireflyest.emberlib.database.code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import org.apache.commons.lang.StringUtils;

/**
 * 源码生成
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class SourceBuilder {
    
    /**
     * public
     */
    public static final String PUBLIC = "public";

    /**
     * private
     */
    public static final String PRIVATE = "private";

    /**
     * protected
     */
    public static final String PROTECTED = "protected";

    /**
     * final
     */
    public static final String FINAL = "final";

    /**
     * static
     */
    public static final String STATIC = "static";

    /**
     * 缩进
     */
    public static final String INDENT = "    ";

    private final StringBuilder stringBuilder = new StringBuilder();

    private final String name;
    private final String classPackage;
    private final List<String> classImportList = new ArrayList<>();
    private final List<String> classImplementList = new ArrayList<>();
    private final List<String> fieldList = new ArrayList<>();
    private final List<MethodBlock> methodList = new ArrayList<>();

    private String classExtend;

    /**
     * 源码构建
     * 
     * @param name 类名
     * @param classPackage 包名
     */
    public SourceBuilder(@Nonnull String name, @Nonnull String classPackage) {
        this.name = name;
        this.classPackage = classPackage;
    }

    /**
     * 简化类名
     * 
     * @param className 类名
     * @return 简化后的类名
     */
    public String simplifyType(@Nonnull String className) {
        if (className.contains(".")) {
            // 有导入的类简化
            for (String classImport : classImportList) {
                if (!StringUtils.startsWith(className, classImport)) {
                    continue;
                }
                className = className.substring(className.lastIndexOf(".") + 1, className.length());
            }
            // 语言自带类简化
            className = StringUtils.removeStart(className, "java.lang.");
        }
        return className;
    }

    /**
     * 获取完整类名
     * 
     * @return 完整类名
     */
    public String getClassName() {
        return classPackage + "." + name;
    }

    /**
     * 获取不带包路径的类名
     * 
     * @return 类名
     */
    public String getSimpleName() {
        return name;
    }

    /**
     * 添加导入
     * 
     * @param classImports 类导入
     * @return 本身
     */
    public SourceBuilder addImport(@Nonnull String... classImports) {
        Collections.addAll(classImportList, classImports);
        return this;
    }

    /**
     * 添加导入
     * 
     * @param classImports 类导入
     * @return 本身
     */
    public SourceBuilder addImport(@Nonnull Class<?>... classImports) {
        for (Class<?> classImport : classImports) {
            classImportList.add(classImport.getName());
        }
        return this;
    }

    /**
     * 设置父类
     * 
     * @param classExtend 父类
     * @return 本身
     */
    public SourceBuilder addExtends(String classExtend) {
        this.classExtend = classExtend;
        return this;
    }

    /**
     * 添加接口
     * 
     * @param classImplement 接口
     * @return 本身
     */
    public SourceBuilder addImplement(String classImplement) {
        classImplementList.add(classImplement);
        return this;
    }

    /**
     * 添加全局变量
     * 
     * @param field 全局变量
     * @return 本身
     */
    public SourceBuilder addField(String field) {
        fieldList.add(field);
        return this;
    }

    /**
     * 添加方法
     * 
     * @param methodBlock 方法块
     * @return 本身
     */
    public SourceBuilder addMethod(MethodBlock methodBlock) {
        methodList.add(methodBlock);
        return this;
    }

    /**
     * 构建源码
     * 
     * @return 源码
     */
    public String build() {
        // 类起始
        this.appendStart();
        // 方法
        this.appendMethods();
        // 类结尾
        this.appendEnd();
        return stringBuilder.toString();
    }

    private void appendStart() {
        // 包
        stringBuilder.append("package " + classPackage + ";\n\n");

        // 导入
        for (String classImport : classImportList) {
            stringBuilder.append("import " + classImport + ";\n");
        }
        stringBuilder.append("\n");

        // 类开始
        stringBuilder.append("public class " + name);
        if (classExtend != null) {
            stringBuilder.append(" extends " + classExtend);
        }
        if (!classImplementList.isEmpty()) {
            stringBuilder.append(" implements");
            for (String classImplement : classImplementList) {
                stringBuilder.append(" " + classImplement);
            }
        }
        stringBuilder.append(" {\n\n");

        // 全局变量
        for (String field : fieldList) {
            stringBuilder.append(INDENT).append(field).append(";\n");
        }
        stringBuilder.append("\n");
    }

    private void appendMethods() {
        for (MethodBlock method : methodList) {
            for (String line : method.getLineList()) {
                stringBuilder.append(INDENT).append(line).append("\n");
            }
        }
    }

    private void appendEnd() {
        stringBuilder.append("}\n");
    }

}
