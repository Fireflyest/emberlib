package io.fireflyest.emberlib.database.sql;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang.StringUtils;

/**
 * 查询条件
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class WhereBuilder {
    
    private final List<String> whereList = new ArrayList<>();

    /**
     * 比较
     * 
     * @param columnName 键
     * @param symbol 比较符号
     * @param number 数值
     * @return 条件语句
     */
    public WhereBuilder compare(@Nonnull String columnName, 
                                @Nonnull String symbol, 
                                @Nonnull Number number) {
        whereList.add("`" + columnName + "`" + symbol + number);
        return this;
    }

    /**
     * BETWEEN {start} AND {end}
     * 
     * @param columnName 键
     * @param start 开始数值
     * @param end 结束数值
     * @return 条件语句
     */
    public WhereBuilder between(@Nonnull String columnName, 
                                @Nonnull Number start, 
                                @Nonnull Number end) {
        whereList.add("`" + columnName + "` BETWEEN " + start + " AND " + end);
        return this;
    }

    /**
     * 等于
     * 
     * @param columnName 键
     * @param value 值
     * @return 条件语句
     */
    public WhereBuilder equalTo(@Nonnull String columnName, @Nonnull Object value) {
        whereList.add("`" + columnName + "`=" + value);
        return this;
    }

    /**
     * 不等于
     * 
     * @param columnName 键
     * @param value 值
     * @return 条件语句
     */
    public WhereBuilder notEqualTo(@Nonnull String columnName, @Nonnull Object value) {
        whereList.add("`" + columnName + "`<>" + value);
        return this;
    }

    /**
     * 是空
     * 
     * @param columnName 键
     * @return 条件语句
     */
    public WhereBuilder isNull(@Nonnull String columnName) {
        whereList.add("`" + columnName + "` IS NULL");
        return this;
    }

    /**
     * `columnName` IN (var1,var2)
     * 通配符 % _ []
     * 
     * @param columnName 键
     * @param values 值
     * @return 条件语句
     */
    public WhereBuilder in(@Nonnull String columnName, @Nonnull String... values) {
        final StringBuilder stringBuilder = new StringBuilder()
            .append("`")
            .append(columnName)
            .append("` IN (")
            .append(StringUtils.join(values, ','))
            .append(")");
        whereList.add(stringBuilder.toString());
        return this;
    }

    /**
     * `columnName` LIKE 'string'
     * 
     * @param columnName 键
     * @param string 条件
     * @return 条件语句
     */
    public WhereBuilder like(@Nonnull String columnName, @Nonnull String string) {
        whereList.add("`" + columnName + "` LIKE '" + string + "'");
        return this;
    }

    /**
     * `columnName` NOT LIKE 'string'
     * 
     * @param columnName 键
     * @param string 条件
     * @return 条件语句
     */
    public WhereBuilder notLike(@Nonnull String columnName, @Nonnull String string) {
        whereList.add("`" + columnName + "` NOT LIKE '" + string + "'");
        return this;
    }

    /**
     * 构建查询条件语句
     * 
     * @return 条件语句
     */
    @Nullable
    public String build() {
        if (whereList.isEmpty()) {
            return null;
        }
        final StringBuilder stringBuilder = new StringBuilder("WHERE ")
            .append(StringUtils.join(whereList, " AND "));
        return stringBuilder.toString();
    }

}
