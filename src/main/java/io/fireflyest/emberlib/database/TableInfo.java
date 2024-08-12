package io.fireflyest.emberlib.database;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 表信息
 * 
 * @author Fireflyest
 * @since 1.0
 */
public final class TableInfo {
    
    private final String tableName;

    private final Map<String, ColumnInfo> columnMap = new LinkedHashMap<>();

    /**
     * 主键的变量名称
     */
    private String primaryVarName;

    /**
     * 表信息
     * 
     * @param tableName 表名
     */
    public TableInfo(@Nonnull String tableName) {
        this.tableName = tableName;
    }

    /**
     * 获取表名
     * 
     * @return 表名
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * 获取主键的变量名称
     * 
     * @return 主键的变量名称
     */
    public String getPrimaryVarName() {
        return primaryVarName;
    }

    /**
     * 获取所有列信息
     * 
     * @return 列信息集合
     */
    public Collection<ColumnInfo> columnInfoSet() {
        return columnMap.values();
    }

    /**
     * 添加列信息
     * 
     * @param varName 列变量名
     * @param columnInfo 列信息
     */
    public void putColumnInfo(@Nonnull String varName, @Nonnull ColumnInfo columnInfo) {
        columnMap.put(varName, columnInfo);
        if (columnInfo.isPrimary()) {
            this.primaryVarName = varName;
        }
    }

    /**
     * 获取列信息
     * 
     * @param name 列变量或变量名
     * @return 列信息
     */
    @Nullable
    public ColumnInfo getColumnInfo(@Nonnull String name) {
        ColumnInfo columnInfo = columnMap.get(name);
        if (columnInfo != null) {
            return columnInfo;
        }
        for (Entry<String, ColumnInfo> entry : columnMap.entrySet()) {
            if (entry.getValue().getColumnName().equals(name)) {
                columnInfo = entry.getValue();
                break;
            }
        }
        return columnInfo;
    }

}
