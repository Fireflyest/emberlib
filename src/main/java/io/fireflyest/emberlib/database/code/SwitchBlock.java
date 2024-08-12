package io.fireflyest.emberlib.database.code;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 多判断
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class SwitchBlock extends SourceBlock {
    
    private final String variable;

    public SwitchBlock(@Nullable String variable) {
        this.variable = variable;
    }

    /**
     * 添加情况
     * 
     * @param caseVariable 情况变量
     * @return 本身
     */
    public SwitchBlock addCase(String caseVariable) {
        lineList.add(SourceBuilder.INDENT +  "case " + caseVariable + ":");
        return this;
    }

    /**
     * 添加默认情况
     * 
     * @return 本身
     */
    public SwitchBlock addDefault() {
        lineList.add(SourceBuilder.INDENT +  "default:");
        return this;
    }

    @Override
    public SwitchBlock addBlock(@Nonnull SourceBlock sourceBlock) {
        return this.addBlock(sourceBlock, true);
    }

    @Override
    public SwitchBlock addBlock(@Nonnull SourceBlock sourceBlock, boolean condition) {
        if (condition) {
            for (String line : sourceBlock.getLineList()) {
                this.addLine(line);
            } 
        }
        return this;
    }

    @Override
    public SwitchBlock addLine(@Nonnull String line) {
        return this.addLine(line, true);
    }

    @Override
    public SwitchBlock addLine(@Nonnull String line, boolean condition) {
        if (condition) {
            lineList.add(SourceBuilder.INDENT.repeat(2) + line);
        }
        return this;
    }

    @Override
    public List<String> getLineList() {
        final List<String> allLineList = new ArrayList<>();

        // 判断头
        final StringBuilder stringBuilder = new StringBuilder("switch (")
            .append(variable)
            .append(") {");
        allLineList.add(stringBuilder.toString());

        // 内容
        allLineList.addAll(lineList);

        // 结尾括号
        allLineList.add("}");

        return allLineList;
    }

}
