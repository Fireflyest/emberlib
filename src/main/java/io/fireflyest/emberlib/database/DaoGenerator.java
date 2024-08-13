package io.fireflyest.emberlib.database;

import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementScanner8;
import javax.tools.FileObject;
import org.apache.commons.lang.StringUtils;
import io.fireflyest.emberlib.database.annotation.Delete;
import io.fireflyest.emberlib.database.annotation.Insert;
import io.fireflyest.emberlib.database.annotation.Select;
import io.fireflyest.emberlib.database.annotation.Update;
import io.fireflyest.emberlib.database.code.WhileBlock;
import io.fireflyest.emberlib.database.code.ForBlock;
import io.fireflyest.emberlib.database.code.IfElseBlock;
import io.fireflyest.emberlib.database.code.MethodBlock;
import io.fireflyest.emberlib.database.code.SourceBlock;
import io.fireflyest.emberlib.database.code.SourceBuilder;
import io.fireflyest.emberlib.database.code.TryBlock;
import io.fireflyest.emberlib.database.sql.CreateTableBuilder;
import io.fireflyest.emberlib.database.sql.DeleteBuilder;
import io.fireflyest.emberlib.database.sql.InsertBuilder;
import io.fireflyest.emberlib.database.sql.SelectBuilder;
import io.fireflyest.emberlib.database.sql.UpdateBuilder;
import io.fireflyest.emberlib.database.sql.WhereBuilder;
import io.fireflyest.emberlib.util.TextUtils;

/**
 * 配置文件生成器
 * 
 * @since 1.0
 */
public class DaoGenerator extends ElementScanner8<Void, Void> {
    
    public static final Pattern SELECT_PATTERN = Pattern.compile("(?<=SELECT ).+(?= FROM)");
    public static final Pattern FUNCTION_PATTERN = Pattern.compile("\\w+\\(.+\\)");

    private static final String CONNECTION_LINE = "final Connection databaseConnection = " 
        + DatabaseConnector.class.getSimpleName()
        + ".getConnect(url);";
    
    private static final String CONDITION_RESULT_NEXT = "resultSet.next()";
    private static final String SQL_EXCEPTION = "SQLException e";
    private static final String PRINT_STACK_TRACE = "e.printStackTrace();";

    private final SourceBuilder source;
    private final ProcessingEnvironment processingEnv;
    private final String daoSimpleName;
    private final String daoClassName;
    private final String tableClassName;
    private final TableInfo tableInfo;

    /**
     * 生成器构造
     * 
     * @param processingEnv 处理环境
     * @param typeElement 被注释的类元素
     * @param tableClassName 数据类的类名
     * @param tableInfo 数据表信息
     */
    public DaoGenerator(@Nonnull ProcessingEnvironment processingEnv, 
                         @Nonnull TypeElement typeElement, 
                         @Nonnull String tableClassName,
                         @Nonnull TableInfo tableInfo) {
        this.processingEnv = processingEnv;
        this.daoSimpleName = typeElement.getSimpleName().toString();
        this.daoClassName = typeElement.getQualifiedName().toString();
        this.tableClassName = tableClassName;
        this.tableInfo = tableInfo;

        this.source = new SourceBuilder(
            daoSimpleName + "Impl",
            StringUtils.removeEnd(daoClassName, "." + daoSimpleName)
        );

        // 建表指令全局变量
        final String sqlCreateTable = new CreateTableBuilder().table(tableInfo).build();
        // 构造函数
        final MethodBlock ctor = new MethodBlock(SourceBuilder.PUBLIC, "", source.getSimpleName())
            .addParameter("String", "url")
            .addLine("this.url = url;");

        source.addImport(tableClassName)
            .addImport(daoClassName)
            .addImport(ArrayList.class.getName())
            .addImport(Arrays.class.getName())
            .addImport(List.class.getName())
            .addImport(Connection.class.getName())
            .addImport(PreparedStatement.class.getName())
            .addImport(Statement.class.getName())
            .addImport(ResultSet.class.getName())
            .addImport(SQLException.class.getName())
            .addImport(DatabaseConnector.class.getName())
            .addImplement(daoSimpleName)
            .addField("public static final String CREATE_TABLE_SQL = \"" + sqlCreateTable + "\"")
            .addField("private final String url")
            .addMethod(ctor);
    }

    /**
     * 写入文件
     * 
     * @throws IOException 文件传输错误
     */
    public void generate() throws IOException {
        // 写入文件
        final FileObject ymlFileObject = processingEnv.getFiler().createSourceFile(
            source.getClassName()
        );
        final Writer writer = ymlFileObject.openWriter();
        writer.write("// File " + source.getSimpleName() + " generated by EmberLib\n\n");
        writer.write(source.build());
        writer.flush();
        writer.close();
    }

    @Override
    public Void visitExecutable(ExecutableElement executableElement, Void p) {
        final Select select = executableElement.getAnnotation(Select.class);
        final Insert insert = executableElement.getAnnotation(Insert.class);
        final Update update = executableElement.getAnnotation(Update.class);
        final Delete delete = executableElement.getAnnotation(Delete.class);
        final String methodName = executableElement.getSimpleName().toString();
        final String returnType = source.simplifyType(
            executableElement.getReturnType().toString()
        );

        final MethodBlock methodBlock = new MethodBlock(
            SourceBuilder.PUBLIC,
            returnType, 
            methodName
        );

        methodBlock.addAnnotation(Override.class.getSimpleName());
        // 方法的参数
        for (VariableElement varElement : executableElement.getParameters()) {
            final String varType = source.simplifyType(varElement.asType().toString());
            final String varName = varElement.toString();
            methodBlock.addParameter(varType, varName);
        }

        if (select != null) {
            this.codeSelect(select.value(), methodBlock);
        }

        if (insert != null) {
            this.codeInsert(insert.value(), methodBlock);
        }

        if (update != null) {
            this.codeUpdate(update.value(), methodBlock);
        }

        if (delete != null) {
            this.codeUpdate(delete.value(), methodBlock);
        }

        source.addMethod(methodBlock);

        return null;
    }

    /**
     * 根据变量替换文本
     * 
     * @param sql 需要替换的指令
     * @param parameterTypeMap 参数对应类型
     * @return 替换容器
     */
    private String varReplace(@Nonnull String sql, @Nonnull Map<String, String> parameterTypeMap) {
        // 参数替换
        return TextUtils.varReplace(
            TextUtils.BRACE_PATTERN, 
            sql, 
            2, 1, 
            key -> {
                String type = parameterTypeMap.get(key);
                String replace = key;
                if (type == null && key.contains(".")) {
                    final String[] keySplit = StringUtils.split(key, '.');
                    final ColumnInfo columnInfo = tableInfo.getColumnInfo(keySplit[1]);
                    if (columnInfo != null) {
                        type = source.simplifyType(columnInfo.getVarDataType());
                        replace = keySplit[0] + "." 
                            + (Boolean.class.getSimpleName().equalsIgnoreCase(type) ? "is" : "get")
                            + TextUtils.upperFirst(keySplit[1])
                            + "()";
                    }
                }
                if (String.class.getSimpleName().equals(type)) {
                    replace += ".replace(\"'\", \"''\")";
                } else if (boolean.class.getSimpleName().equalsIgnoreCase(type)) {
                    replace = "(" + replace + " ? 1 : 0)";
                }
                return "\" + " + replace + " + \"";
            }
        );
    }

    /**
     * 查询代码
     * 
     * @param sql 指令
     * @param methodBlock 方法块
     */
    private void codeSelect(@Nonnull String sql, @Nonnull MethodBlock methodBlock) {
        final String returnType = methodBlock.getReturnType();
        final boolean arrayReturn = StringUtils.endsWith(returnType, "[]");
        final String returnTypeSingle = arrayReturn 
            ? StringUtils.removeEnd(returnType, "[]") : returnType;
        final String warpTypeSingle = this.warpType(returnTypeSingle);
        final boolean beanReturn = returnTypeSingle.equals(source.simplifyType(tableClassName));
        if ("SELECT".equals(sql)) {
            sql = this.defaultSelect(methodBlock.getParameterMap(), arrayReturn, beanReturn);
        }
        final String sqlReplaced = this.varReplace(sql, methodBlock.getParameterMap());
        final String selections = StringUtils.remove(TextUtils.find(SELECT_PATTERN, sql)[0], '`');
        final String resource = "Statement statement = databaseConnection.createStatement(); "
            + "ResultSet resultSet = statement.executeQuery(sqlSelect)";

        // 函数名不同替换一下
        final IfElseBlock ifSqliteBlock = new IfElseBlock(
            "sqlSelect.contains(\"RAND()\") && url.contains(\"sqlite\")"
        ).addLine("sqlSelect = sqlSelect.replace(\"RAND()\", \"RANDOM()\");");

        // 查询结果读取
        final SourceBlock resultBlock = this.getResultBlock(
            arrayReturn, 
            beanReturn, 
            selections, 
            returnTypeSingle
        );

        // 用尝试块包裹
        final TryBlock tryBlock = new TryBlock(resource)
            .addBlock(resultBlock)
            .addCatch(SQL_EXCEPTION)
            .addLine(PRINT_STACK_TRACE);

        methodBlock.addLine("// " + sql)
            .addLine("String sqlSelect = ")
            .addLine(SourceBuilder.INDENT + "\"" + sqlReplaced + "\";")
            .addBlock(ifSqliteBlock)
            .addLine(CONNECTION_LINE)
            .addLine("final List<" + warpTypeSingle + "> valueList = new ArrayList<>();")
            .addBlock(tryBlock)
            .addLine("return valueList.toArray(new " + returnTypeSingle + "[0]);", 
                arrayReturn && returnTypeSingle.equals(warpTypeSingle));

        // 不是返回集合
        if (!arrayReturn) {
            final IfElseBlock ifNotEmptyBlock = new IfElseBlock("!valueList.isEmpty()")
                .addLine("returnValue = valueList.get(0);");
            if (boolean.class.getSimpleName().equalsIgnoreCase(returnType)) {
                methodBlock.addLine(returnType + " returnValue = false;");
            } else if (String.class.getSimpleName().equalsIgnoreCase(returnType) || beanReturn) {
                methodBlock.addLine(returnType + " returnValue = null;");
            } else {
                methodBlock.addLine(returnType + " returnValue = 0;");
            }
            methodBlock.addBlock(ifNotEmptyBlock).addLine("return returnValue;");
        }

        // 列表要拆箱
        if (arrayReturn && !returnTypeSingle.equals(warpTypeSingle)) {
            final ForBlock forBlock = new ForBlock(warpTypeSingle + " warpedValue : valueList")
                .addLine("returnValue[index++] = warpedValue;");
            methodBlock.addLine(SourceBuilder.FINAL + " " + returnType 
                + " returnValue = new " 
                + returnTypeSingle 
                + "[valueList.size()];"
            );
            methodBlock.addLine("int index = 0;").addBlock(forBlock).addLine("return returnValue;");
        }

    }


    /**
     * 获取查询结果的方法块
     * 
     * @param arrayReturn 是否返回集合
     * @param beanReturn 是否返回整个数据对象
     * @param selections 指令选择的列
     * @param returnTypeSingle 数据对象的简单类名
     * @return 查询结果的方法块
     */
    private SourceBlock getResultBlock(boolean arrayReturn, boolean beanReturn, 
            String selections, String returnTypeSingle) {
        final SourceBlock resultBlock = arrayReturn
            ? new WhileBlock(CONDITION_RESULT_NEXT) : new IfElseBlock(CONDITION_RESULT_NEXT);
        if (beanReturn) {
            final List<ColumnInfo> columnInfoList = this.getColumnInfoList(selections);
            resultBlock.addLine(SourceBuilder.FINAL + " " + returnTypeSingle 
                + " obj = new " + returnTypeSingle + "();"
            );
            columnInfoList.forEach(columnInfo -> 
                resultBlock.addLine("obj.set" 
                    + TextUtils.upperFirst(columnInfo.getVarName()) + "(resultSet.get"
                    + TextUtils.upperFirst(source.simplifyType(columnInfo.getVarDataType()))
                    + "(\"" + columnInfo.getColumnName() + "\"));"
                )
            );
            resultBlock.addLine("valueList.add(obj);");
        } else {
            String columnToGet = "1";
            if (!TextUtils.match(FUNCTION_PATTERN, selections)) {
                final ColumnInfo columnInfo = tableInfo.getColumnInfo(selections);
                if (columnInfo != null) {
                    columnToGet = "\"" + columnInfo.getColumnName() + "\"";
                }
            }
            resultBlock.addLine("valueList.add(resultSet.get" 
                + TextUtils.upperFirst(returnTypeSingle) + "(" + columnToGet + "));"
            );
        }
        return resultBlock;
    }

    /**
     * 根据SELECT指令获取列信息列表
     * 
     * @param selections 选择
     * @return 列信息列表
     */
    private List<ColumnInfo> getColumnInfoList(@Nonnull String selections) {
        final List<ColumnInfo> columnInfoList;
        if ("*".equals(selections)) {
            columnInfoList = new ArrayList<>(tableInfo.columnInfoSet());
        } else {
            columnInfoList = new ArrayList<>();
            for (String columnName : StringUtils.split(selections, ',')) {
                final ColumnInfo columnInfo = tableInfo.getColumnInfo(columnName);
                if (columnInfo != null) {
                    columnInfoList.add(tableInfo.getColumnInfo(columnName));
                }
            }
        }
        return columnInfoList;
    }

    /**
     * 装箱类型
     * 
     * @param type 类型
     * @return 装箱类型
     */
    private String warpType(String type) {
        if (int.class.getSimpleName().equals(type)) {
            return Integer.class.getSimpleName();
        }
        return TextUtils.upperFirst(type);
    }

    /**
     * 插入代码
     * 
     * @param sql 指令
     * @param methodBlock 方法块
     */
    private void codeInsert(@Nonnull String sql, @Nonnull MethodBlock methodBlock) {
        final String returnType = methodBlock.getReturnType();
        final boolean voidReturn = void.class.getSimpleName().equals(methodBlock.getReturnType());
        if ("INSERT".equals(sql)) {
            sql = this.defaultInsert(methodBlock.getParameterMap());
        }
        final String sqlReplaced = this.varReplace(sql, methodBlock.getParameterMap());
        final String resource = "PreparedStatement preparedStatement"
            + " = databaseConnection.prepareStatement(sqlInsert" 
            + (voidReturn ? ")" : ", Statement.RETURN_GENERATED_KEYS)");
        final IfElseBlock ifBlock = new IfElseBlock(CONDITION_RESULT_NEXT)
            .addLine("insertId = resultSet.get" + TextUtils.upperFirst(returnType) + "(1);");
        final TryBlock tryBlock = new TryBlock(resource)
            .addLine("preparedStatement.executeUpdate();")
            .addLine("ResultSet resultSet = preparedStatement.getGeneratedKeys();", !voidReturn)
            .addBlock(ifBlock, !voidReturn)
            .addLine("resultSet.close();", !voidReturn)
            .addLine("return insertId;", !voidReturn)
            .addCatch(SQL_EXCEPTION)
            .addLine(PRINT_STACK_TRACE);

        methodBlock.addLine("// " + sql)
            .addLine("final String sqlInsert = ")
            .addLine(SourceBuilder.INDENT + "\"" + sqlReplaced + "\";")
            .addLine(CONNECTION_LINE)
            .addLine(methodBlock.getReturnType() + " insertId = 0;", !voidReturn)
            .addBlock(tryBlock)
            .addLine("return insertId;", !voidReturn);
    }

    /**
     * 更新代码
     * 
     * @param sql 指令
     * @param methodBlock 方法块
     */
    private void codeUpdate(@Nonnull String sql, @Nonnull MethodBlock methodBlock) {
        final boolean voidReturn = void.class.getSimpleName().equals(methodBlock.getReturnType());
        if ("UPDATE".equals(sql)) {
            sql = this.defaultUpdate(methodBlock.getParameterMap());
        } else if ("DELETE".equals(sql)) {
            sql = this.defaultDelete(methodBlock.getParameterMap());
        }
        final String sqlReplaced = this.varReplace(sql, methodBlock.getParameterMap());
        final String resource = 
            "PreparedStatement preparedStatement = databaseConnection.prepareStatement(sqlUpdate)";
        final TryBlock tryBlock = new TryBlock(resource)
            .addLine("preparedStatement.executeUpdate();", voidReturn)
            .addLine("return preparedStatement.executeUpdate();", !voidReturn)
            .addCatch(SQL_EXCEPTION)
            .addLine(PRINT_STACK_TRACE);

        methodBlock.addLine("// " + sql)
            .addLine("final String sqlUpdate = ")
            .addLine(SourceBuilder.INDENT + "\"" + sqlReplaced + "\";")
            .addLine(CONNECTION_LINE)
            .addBlock(tryBlock)
            .addLine("return 0;", !voidReturn);
    }

    /**
     * 默认查询指令
     * 
     * @param parameterTypeMap 参数
     * @param arrayReturn 返回集合
     * @param beanReturn 返回完整数据对象
     * @return 默认查询指令
     */
    private String defaultSelect(@Nonnull Map<String, String> parameterTypeMap, 
                                 boolean arrayReturn, 
                                 boolean beanReturn) {
        final String selections = beanReturn ? "*" : "COUNT(*)";
        final WhereBuilder whereBuilder = new WhereBuilder();
        final SelectBuilder selectBuilder = new SelectBuilder(selections, tableInfo.getTableName());
        for (Entry<String, String> entry : parameterTypeMap.entrySet()) {
            final ColumnInfo columnInfo = tableInfo.getColumnInfo(entry.getKey());
            if (columnInfo != null) {
                String value = "${" + entry.getKey() + "}";
                if (String.class.getSimpleName().equals(entry.getValue())) {
                    value = "'" + value + "'";
                }
                whereBuilder.equalTo(columnInfo.getColumnName(), value);
            }
        }
        if (!arrayReturn) {
            selectBuilder.limit(1);
        }
        return selectBuilder.where(whereBuilder).build();
    }

    /**
     * 默认插入指令
     * 
     * @param parameterTypeMap 参数
     * @return 默认插入指令
     */
    private String defaultInsert(@Nonnull Map<String, String> parameterTypeMap) {
        final InsertBuilder insertBuilder = new InsertBuilder(tableInfo.getTableName());
        for (Entry<String, String> entry : parameterTypeMap.entrySet()) {
            final ColumnInfo columnInfo = tableInfo.getColumnInfo(entry.getKey());
            if (columnInfo != null) {
                String value = "${" + entry.getKey() + "}";
                if (String.class.getSimpleName().equals(entry.getValue())) {
                    value = "'" + value + "'";
                }
                insertBuilder.columnValue(columnInfo.getColumnName(), value);
            }
        }
        return insertBuilder.build();
    }

    /**
     * 默认更新指令
     * 
     * @param parameterTypeMap 参数
     * @return 默认更新指令
     */
    private String defaultUpdate(@Nonnull Map<String, String> parameterTypeMap) {
        final WhereBuilder whereBuilder = new WhereBuilder();
        final UpdateBuilder updateBuilder = new UpdateBuilder(tableInfo.getTableName());
        for (Entry<String, String> entry : parameterTypeMap.entrySet()) {
            final ColumnInfo columnInfo = tableInfo.getColumnInfo(entry.getKey());
            if (columnInfo != null) {
                String value = "${" + entry.getKey() + "}";
                if (String.class.getSimpleName().equals(entry.getValue())) {
                    value = "'" + value + "'";
                }
                if (columnInfo.isPrimary()) {
                    whereBuilder.equalTo(columnInfo.getColumnName(), value);
                } else {
                    updateBuilder.set(columnInfo.getColumnName(), value);
                }
            }
        }
        return updateBuilder.where(whereBuilder).build();
    }

    /**
     * 默认删除指令
     * 
     * @param parameterTypeMap 参数
     * @return 默认删除指令
     */
    public String defaultDelete(@Nonnull Map<String, String> parameterTypeMap) {
        final WhereBuilder whereBuilder = new WhereBuilder();
        final DeleteBuilder deleteBuilder = new DeleteBuilder(tableInfo.getTableName());
        for (Entry<String, String> entry : parameterTypeMap.entrySet()) {
            final ColumnInfo columnInfo = tableInfo.getColumnInfo(entry.getKey());
            if (columnInfo != null) {
                String value = "${" + entry.getKey() + "}";
                if (String.class.getSimpleName().equals(entry.getValue())) {
                    value = "'" + value + "'";
                }
                whereBuilder.equalTo(columnInfo.getColumnName(), value);
            }
        }
        return deleteBuilder.where(whereBuilder).build();
    }

}
