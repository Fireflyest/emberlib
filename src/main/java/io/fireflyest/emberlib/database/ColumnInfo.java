package io.fireflyest.emberlib.database;

import javax.annotation.Nonnull;

/**
 * 列信息
 * 
 * @author Fireflyest
 * @since 1.0
 */
public final class ColumnInfo {

    private final String varName;
    private final String columnName;
    private final String varDataType;
    private final String columnDataType;  // 数据库数据类型
    private boolean autoIncrement;  // 是否自增主键
    private boolean primary;  // 是否主键
    private String defaultValue;  // 默认值
    private boolean noNull;  // 是否非空

    /**
     * 列信息
     * 
     * @param varName 变量名称
     * @param columnName 列名称
     * @param varDataType 变量数据类型
     * @param columnDataType 列数据类型
     */
    public ColumnInfo(@Nonnull String varName, 
                      @Nonnull String columnName, 
                      @Nonnull String varDataType, 
                      @Nonnull String columnDataType) {
        this.varName = varName;
        this.columnName = columnName;
        this.varDataType = varDataType;
        this.columnDataType = columnDataType;
    }

    public String getVarName() {
        return varName;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getVarDataType() {
        return varDataType;
    }

    public String getColumnDataType() {
        return columnDataType;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isNoNull() {
        return noNull;
    }

    public void setNoNull(boolean noNull) {
        this.noNull = noNull;
    }

}
