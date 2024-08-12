package io.fireflyest.emberlib.database;

import javax.annotation.Nonnull;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementScanner8;
import io.fireflyest.emberlib.database.annotation.Column;
import io.fireflyest.emberlib.database.annotation.Primary;

/**
 * 配置文件生成器
 * 
 * @since 1.0
 */
public class TableGenerator extends ElementScanner8<Void, Void> {
    
    private final TableInfo tableInfo;

    /**
     * 表信息构造
     * 
     * @param tableName 表名
     */
    public TableGenerator(@Nonnull String tableName) {
        this.tableInfo = new TableInfo(tableName);
    }

    /**
     * 获取表信息
     * 
     * @return 表信息
     */
    public TableInfo generator() {
        return tableInfo;
    }

    @Override
    public Void visitVariable(VariableElement varElement, Void p) {
        final Column column = varElement.getAnnotation(Column.class);
        final Primary primary = varElement.getAnnotation(Primary.class);
        if (column != null) {
            final String varName = varElement.getSimpleName().toString();
            final String columnName = "".equals(column.name()) ? varName : column.name();
            final String varDataType = varElement.asType().toString();
            final String columnDataType = "".equals(column.dataType()) 
                ? "${" + varDataType + "}" : column.dataType();

            final ColumnInfo columnInfo = new ColumnInfo(
                varName, 
                columnName, 
                varDataType, 
                columnDataType
            );

            if (primary != null) {
                columnInfo.setPrimary(true);
                columnInfo.setAutoIncrement(primary.autoIncrement());
                columnInfo.setNoNull(true);
            } else {
                columnInfo.setDefaultValue(column.defaultValue());
                columnInfo.setNoNull(column.noNull());
            }

            tableInfo.putColumnInfo(varName, columnInfo);
        }
        return null;
    }

}
