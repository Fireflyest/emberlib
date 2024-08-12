package io.fireflyest.emberlib.database.code;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * 循环判断块
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class WhileBlock extends SourceBlock {

    private final String condition;

    private boolean doWhite = false;

    /**
     * 循环判断块
     * 
     * @param condition 条件
     */
    public WhileBlock(@Nonnull String condition) {
        this.condition = condition;
    }

    /**
     * 设置格式
     * 
     * @param doWhite 是否doWhite格式
     */
    public void setDoWhite(boolean doWhite) {
        this.doWhite = doWhite;
    }

    @Override
    public WhileBlock addBlock(@Nonnull SourceBlock sourceBlock) {
        super.addBlock(sourceBlock);
        return this;
    }

    @Override
    public WhileBlock addBlock(@Nonnull SourceBlock sourceBlock, boolean condition) {
        super.addBlock(sourceBlock, condition);
        return this;
    }

    @Override
    public WhileBlock addLine(@Nonnull String line) {
        super.addLine(line);
        return this;
    }

    @Override
    public WhileBlock addLine(@Nonnull String line, boolean condition) {
        super.addLine(line, condition);
        return this;
    }

    @Override
    public List<String> getLineList() {
        final List<String> allLineList = new ArrayList<>();

        // 判断头
        if (doWhite) {
            allLineList.add("do {");
        } else {
            final StringBuilder stringBuilder = new StringBuilder()
                .append("while (")
                .append(condition)
                .append(") {");
            allLineList.add(stringBuilder.toString());
        } 

        // 内容
        allLineList.addAll(lineList);

        // 结尾括号
        if (doWhite) {
            final StringBuilder stringBuilder = new StringBuilder()
                .append("} while (")
                .append(condition)
                .append(");");
            allLineList.add(stringBuilder.toString());
        } else {
            allLineList.add("}");
        }

        return allLineList;
    }

}
