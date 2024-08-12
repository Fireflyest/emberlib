package io.fireflyest.emberlib.database.code;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * 逻辑判断块
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class IfElseBlock extends SourceBlock {

    private final String condition;

    /**
     * 逻辑判断块
     * 
     * @param condition 条件
     */
    public IfElseBlock(@Nonnull String condition) {
        this.condition = condition;
    }

    /**
     * 添加逻辑判断块
     * 
     * @param condition 条件
     * @return 本身
     */
    public IfElseBlock addElseIf(@Nonnull String condition) {
        lineList.add("} else if ( " + condition + ") {");
        return this;
    }

    /**
     * 添加逻辑判断块
     * 
     * @return 本身
     */
    public IfElseBlock addElse() {
        lineList.add("} else {");
        return this;
    }

    @Override
    public IfElseBlock addBlock(@Nonnull SourceBlock sourceBlock) {
        super.addBlock(sourceBlock);
        return this;
    }

    @Override
    public IfElseBlock addBlock(@Nonnull SourceBlock sourceBlock, boolean condition) {
        super.addBlock(sourceBlock, condition);
        return this;
    }

    @Override
    public IfElseBlock addLine(@Nonnull String line) {
        super.addLine(line);
        return this;
    }

    @Override
    public IfElseBlock addLine(@Nonnull String line, boolean condition) {
        super.addLine(line, condition);
        return this;
    }

    @Override
    public List<String> getLineList() {
        final List<String> allLineList = new ArrayList<>();

        // 判断头
        final StringBuilder stringBuilder = new StringBuilder("if (")
            .append(condition)
            .append(") {");
        allLineList.add(stringBuilder.toString());

        // 内容
        allLineList.addAll(lineList);

        // 结尾括号
        allLineList.add("}");

        return allLineList;
    }

}
