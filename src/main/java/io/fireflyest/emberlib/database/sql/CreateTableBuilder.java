package io.fireflyest.emberlib.database.sql;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.apache.commons.lang.StringUtils;
import io.fireflyest.emberlib.database.ColumnInfo;
import io.fireflyest.emberlib.database.TableInfo;

/**
 * 建表指令
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class CreateTableBuilder {
    
    
    private final List<String> lineList = new ArrayList<>();
    
    private String tableName;

    /**
     * 建表指令
     */
    public CreateTableBuilder() {
    }

    /**
     * 建表指令
     * 
     * @param tableName 表名
     */
    public CreateTableBuilder(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 使用表信息构建
     * 
     * @param tableInfo 表信息
     * @return 本身
     */
    public CreateTableBuilder table(@Nonnull TableInfo tableInfo) {
        this.tableName = tableInfo.getTableName();
        for (ColumnInfo columnInfo : tableInfo.columnInfoSet()) {
            this.column(columnInfo);
        }
        return this;
    }

    /**
     * 添加一条列信息
     * 
     * @param columnInfo 列信息
     * @return 本身
     */
    public CreateTableBuilder column(@Nonnull ColumnInfo columnInfo) {
        final StringBuilder stringBuilder = new StringBuilder()
            .append("`").append(columnInfo.getColumnName()).append("` ")
            .append(columnInfo.getColumnDataType());
        if (columnInfo.isNoNull()) {
            stringBuilder.append(" NOT NULL");
        }
        if (columnInfo.isPrimary()) {
            stringBuilder.append(" PRIMARY KEY");
            if (columnInfo.isAutoIncrement()) {
                stringBuilder.append(" ${AUTO_INCREMENT}");
            }
        } else {
            stringBuilder.append(" DEFAULT ").append(columnInfo.getDefaultValue());
        }
        lineList.add(stringBuilder.toString());
        return this;
    }

    /**
     * 构建建表指令
     * 
     * @return 建表指令
     */
    public String build() {
        final StringBuilder stringBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS `")
            .append(tableName)
            .append("`(");

        stringBuilder.append(StringUtils.join(lineList, ", "));

        return stringBuilder.append(");").toString();
    }

}
