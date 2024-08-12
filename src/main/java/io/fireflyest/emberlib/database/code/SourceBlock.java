package io.fireflyest.emberlib.database.code;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * 代码块
 * 
 * @author Fireflyest
 * @since 1.0
 */
public abstract class SourceBlock {
    
    protected final List<String> lineList = new ArrayList<>();

    /**
     * 添加代码块
     * 
     * @param sourceBlock 代码块
     * @return 本身
     */
    public SourceBlock addBlock(@Nonnull SourceBlock sourceBlock) {
        return this.addBlock(sourceBlock, true);
    }

    /**
     * 根据条件添加代码块
     * 
     * @param sourceBlock 代码块
     * @param condition 条件
     * @return 本身
     */
    public SourceBlock addBlock(@Nonnull SourceBlock sourceBlock, boolean condition) {
        if (condition) {
            for (String line : sourceBlock.getLineList()) {
                this.addLine(line);
            }
        }
        return this;
    }

    /**
     * 添加一行代码
     * 
     * @param line 一行
     * @return 本身
     */
    public SourceBlock addLine(@Nonnull String line) {
        return this.addLine(line, true);
    }

    /**
     * 根据条件添加一行代码
     * 
     * @param line 一行
     * @param condition 条件
     * @return 本身
     */
    public SourceBlock addLine(@Nonnull String line, boolean condition) {
        if (condition) {
            lineList.add(SourceBuilder.INDENT + line);
        }
        return this;
    }

    /**
     * 获取所有代码行
     * 
     * @return 代码行列表
     */
    public List<String> getLineList() {
        return lineList;
    }

}
