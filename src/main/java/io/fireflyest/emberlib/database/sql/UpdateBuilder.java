package io.fireflyest.emberlib.database.sql;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.apache.commons.lang.StringUtils;

/**
 * 更新
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class UpdateBuilder {

    private final List<String> valueList = new ArrayList<>();

    private final String tableName;

    private String where;

    public UpdateBuilder(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 设置值
     * 
     * @param columnName 列名
     * @param value 值
     * @return 本身
     */
    public UpdateBuilder set(@Nonnull String columnName, @Nonnull String value) {
        valueList.add("`" + columnName + "`=" + value);
        return this;
    }

    /**
     * 设置数字
     * 
     * @param columnName 列名
     * @param symbol 操作符号
     * @param number 操作数字
     * @return 本身
     */
    public UpdateBuilder set(@Nonnull String columnName, 
                             @Nonnull String symbol, 
                             @Nonnull Number number) {
        valueList.add("`" + columnName + "`=`" + columnName + "`" + symbol + number);
        return this;
    }

    /**
     * 查询条件
     * 
     * @param where 条件
     * @return 本身
     */
    public UpdateBuilder where(String where) {
        this.where = where;
        return this;
    }

    /**
     * 查询条件
     * 
     * @param whereBuilder 条件
     * @return 本身
     */
    public UpdateBuilder where(WhereBuilder whereBuilder) {
        return this.where(whereBuilder.build());
    }

    /**
     * 构建更新指令
     * 
     * @return 更新指令
     */
    public String build() {
        final StringBuilder stringBuilder = new StringBuilder("UPDATE `")
            .append(tableName).append("` SET ").append(StringUtils.join(valueList, ','));
        if (where != null) {
            stringBuilder.append(" ").append(where);
        }
        return stringBuilder.append(";").toString();
    }

}
