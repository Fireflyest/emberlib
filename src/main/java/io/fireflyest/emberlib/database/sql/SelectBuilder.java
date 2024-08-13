package io.fireflyest.emberlib.database.sql;

/**
 * 查询语句
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class SelectBuilder {
    
    private final String selections;
    private final String tableName;

    private String where;
    private String limit;

    public SelectBuilder(String selections, String tableName) {
        this.selections = selections;
        this.tableName = tableName;
    }

    /**
     * 查询条件
     * 
     * @param where 条件
     * @return 本身
     */
    public SelectBuilder where(String where) {
        this.where = where;
        return this;
    }

    /**
     * 查询条件
     * 
     * @param whereBuilder 条件
     * @return 本身
     */
    public SelectBuilder where(WhereBuilder whereBuilder) {
        return this.where(whereBuilder.build());
    }

    /**
     * 限制数量
     * LIMIT count
     * 
     * @param count 数量
     * @return 本身
     */
    public SelectBuilder limit(int count) {
        this.limit = String.valueOf(count);
        return this;
    }

    /**
     * 限制数量
     * LIMIT start,count
     * 
     * @param start 起始
     * @param count 数量
     * @return 本身
     */
    public SelectBuilder limit(int start, int count) {
        this.limit = String.valueOf(start) + "," + count;
        return this;
    }

    /**
     * 构建查询语句
     * 
     * @return 查询语句
     */
    public String build() {
        final StringBuilder stringBuilder = new StringBuilder("SELECT ")
            .append(selections)
            .append(" FROM `")
            .append(tableName)
            .append("`");

        if (where != null) {
            stringBuilder.append(" ").append(where);
        }
        if (limit != null) {
            stringBuilder.append(" LIMIT ").append(limit);
        }

        return stringBuilder.append(";").toString();
    }

}
