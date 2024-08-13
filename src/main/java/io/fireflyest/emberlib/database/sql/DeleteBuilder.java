package io.fireflyest.emberlib.database.sql;

/**
 * 删除
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class DeleteBuilder {
    
    private final String tableName;

    private String where;

    /**
     * 删除
     * 
     * @param tableName 表名
     */
    public DeleteBuilder(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 查询条件
     * 
     * @param where 条件
     * @return 本身
     */
    public DeleteBuilder where(String where) {
        this.where = where;
        return this;
    }

    /**
     * 查询条件
     * 
     * @param whereBuilder 条件
     * @return 本身
     */
    public DeleteBuilder where(WhereBuilder whereBuilder) {
        return this.where(whereBuilder.build());
    }

    /**
     * 构建删除指令
     * 
     * @return 删除指令
     */
    public String build() {
        final StringBuilder stringBuilder = new StringBuilder("DELETE FROM `")
            .append(tableName).append("`");
        if (where != null) {
            stringBuilder.append(" ").append(where);
        }
        return stringBuilder.append(";").toString();
    }

}
