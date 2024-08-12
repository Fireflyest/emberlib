package io.fireflyest.emberlib.database.code;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 尝试块
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class TryBlock extends SourceBlock {
    
    private final String resource;

    /**
     * 尝试块
     */
    public TryBlock() {
        this(null);
    }

    /**
     * 尝试块
     * 
     * @param resource 资源
     */
    public TryBlock(@Nullable String resource) {
        this.resource = resource;
    }

    /**
     * 添加异常捕获
     * 
     * @param exception 异常
     * @return 本身
     */
    public TryBlock addCatch(@Nonnull String exception) {
        lineList.add("} catch (" + exception + ") {");
        return this;
    }

    /**
     * 添加最终执行
     * 
     * @return 本身
     */
    public TryBlock addElse() {
        lineList.add("} finally {");
        return this;
    }

    @Override
    public TryBlock addBlock(@Nonnull SourceBlock sourceBlock) {
        super.addBlock(sourceBlock);
        return this;
    }

    @Override
    public TryBlock addBlock(@Nonnull SourceBlock sourceBlock, boolean condition) {
        super.addBlock(sourceBlock, condition);
        return this;
    }

    @Override
    public TryBlock addLine(@Nonnull String line) {
        super.addLine(line);
        return this;
    }

    @Override
    public TryBlock addLine(@Nonnull String line, boolean condition) {
        super.addLine(line, condition);
        return this;
    }

    @Override
    public List<String> getLineList() {
        final List<String> allLineList = new ArrayList<>();

        // 判断头
        final StringBuilder stringBuilder = new StringBuilder("try ");
        if (resource != null) {
            stringBuilder.append("(").append(resource).append(") ");
        }
        stringBuilder.append("{");
        allLineList.add(stringBuilder.toString());

        // 内容
        allLineList.addAll(lineList);

        // 结尾括号
        allLineList.add("}");

        return allLineList;
    }

}
