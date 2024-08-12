package io.fireflyest.emberlib.database.code;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import org.apache.commons.lang.StringUtils;

/**
 * 方法块
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class MethodBlock extends SourceBlock {

    private final List<String> annotationList = new ArrayList<>();
    private final List<String> parameterList = new ArrayList<>();
    private final Map<String, String> parameterMap = new LinkedHashMap<>();

    private final String access;
    private final String returnType;
    private final String methodName;

    /**
     * 方法块
     * 
     * @param access 访问权限
     * @param returnType 返回类型
     * @param methodName 方法名
     */
    public MethodBlock(@Nonnull String access, 
                       @Nonnull String returnType, 
                       @Nonnull String methodName) {
        this.access = access;
        this.returnType = returnType;
        this.methodName = methodName;
    }

    /**
     * 获取返回类型
     * 
     * @return 返回类型
     */
    public String getReturnType() {
        return returnType;
    }

    /**
     * 获取参数表
     * 
     * @return 参数表
     */
    public Map<String, String> getParameterMap() {
        return parameterMap;
    }

    /**
     * 添加方法参数
     * 
     * @param parameterType 参数变量类型
     * @param parameterName 参数变量
     * @return 本身
     */
    public MethodBlock addParameter(String parameterType, String parameterName) {
        parameterMap.put(parameterName, parameterType);
        parameterList.add(parameterType + " " + parameterName);
        return this;
    }

    /**
     * 添加注解
     * 
     * @param annotation 注解
     * @return 本身
     */
    public MethodBlock addAnnotation(String annotation) {
        annotationList.add(annotation);
        return this;
    }

    @Override
    public MethodBlock addBlock(@Nonnull SourceBlock sourceBlock) {
        super.addBlock(sourceBlock);
        return this;
    }

    @Override
    public MethodBlock addBlock(@Nonnull SourceBlock sourceBlock, boolean condition) {
        super.addBlock(sourceBlock, condition);
        return this;
    }

    @Override
    public MethodBlock addLine(@Nonnull String line) {
        super.addLine(line);
        return this;
    }

    @Override
    public List<String> getLineList() {
        final List<String> allLineList = new ArrayList<>();

        // 注解
        for (String annotation : annotationList) {
            allLineList.add("@" + annotation);
        }

        // 方法头
        final StringBuilder stringBuilder = new StringBuilder(access);
        if (!"".equals(returnType)) {
            stringBuilder.append(" ").append(returnType);
        }
        stringBuilder.append(" ").append(methodName)
            .append("(")
            .append(StringUtils.join(parameterList, ", "))
            .append(") {");
        allLineList.add(stringBuilder.toString());

        // 方法内容
        allLineList.addAll(lineList);

        // 结尾括号
        allLineList.add("}\n");

        return allLineList;
    }

}
