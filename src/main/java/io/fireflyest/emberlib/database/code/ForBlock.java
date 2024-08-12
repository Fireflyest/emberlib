package io.fireflyest.emberlib.database.code;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * 循环块
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class ForBlock extends SourceBlock  {
    
    private final String condition;

    /**
     * 循环块
     * 
     * @param condition 循环条件
     */
    public ForBlock(String condition) {
        this.condition = condition;
    }

    @Override
    public ForBlock addBlock(@Nonnull SourceBlock sourceBlock, boolean condition) {
        super.addBlock(sourceBlock, condition);
        return this;
    }

    @Override
    public ForBlock addLine(@Nonnull String line) {
        super.addLine(line);
        return this;
    }

    @Override
    public ForBlock addLine(@Nonnull String line, boolean condition) {
        super.addLine(line, condition);
        return this;
    }

    @Override
    public List<String> getLineList() {
        final List<String> allLineList = new ArrayList<>();

        // 判断头
        final StringBuilder stringBuilder = new StringBuilder()
            .append("for (")
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
