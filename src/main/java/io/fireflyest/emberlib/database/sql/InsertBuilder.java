package io.fireflyest.emberlib.database.sql;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import org.apache.commons.lang.StringUtils;

/**
 * 插入
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class InsertBuilder {
    
    private final Map<String, String> valueMap = new LinkedHashMap<>();

    private final String tableName;

    /**
     * 插入
     * 
     * @param tableName 表名
     */
    public InsertBuilder(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 列的值
     * 
     * @param columnName 列名
     * @param value 值
     * @return 本身
     */
    public InsertBuilder columnValue(@Nonnull String columnName, @Nonnull String value) {
        valueMap.put("`" + columnName + "`", value);
        return this;
    }

    /**
     * 构建插入指令
     * 
     * @return 插入指令
     */
    public String build() {
        final StringBuilder stringBuilder = new StringBuilder("INSERT INTO ")
            .append("`").append(tableName).append("` (")
            .append(StringUtils.join(valueMap.keySet(), ','))
            .append(") VALUES (")
            .append(StringUtils.join(valueMap.values(), ','))
            .append(")");
        return stringBuilder.append(";").toString();
    }

}
