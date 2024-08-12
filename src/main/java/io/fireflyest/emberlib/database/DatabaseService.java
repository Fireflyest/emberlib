package io.fireflyest.emberlib.database;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.annotation.Nonnull;
import io.fireflyest.emberlib.database.annotation.Auto;
import io.fireflyest.emberlib.database.annotation.Service;
import io.fireflyest.emberlib.util.ReflectionUtils;
import io.fireflyest.emberlib.util.TextUtils;

/**
 * 数据库服务类
 * 
 * @author Fireflyest
 * @since 1.0
 */
public abstract class DatabaseService {

    private final String url;

    /**
     * 初始化数据库服务对象
     * @param url 连接地址
     */
    protected DatabaseService(@Nonnull String url) {
        this.url = url;

        try {
            this.init();
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException 
                | InstantiationException | IllegalAccessException 
                | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接地址
     * @return 连接地址
     */
    public String getUrl() {
        return url;
    }

    private void init() throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, InstantiationException,
            IllegalAccessException, NoSuchFieldException, SecurityException {
        final Class<?> serviceClass = this.getClass();
        final Service serviceAnnotation = serviceClass.getAnnotation(Service.class);
        if (serviceAnnotation == null) {
            return;
        }
        // 遍历dao成员变量
        for (Field daoField : serviceClass.getDeclaredFields()) {
            // 判断是否自动实现
            if (daoField.getAnnotation(Auto.class) == null) {
                continue;
            }
            // 获取实例类后实例化对象并自动赋值
            final Class<?> daoClass = daoField.getType();
            final Class<?> daoImplClass = 
                Class.forName(daoClass.getPackageName() + "." + daoClass.getSimpleName() + "Impl");
            final Constructor<?> constructor = daoImplClass.getDeclaredConstructor(String.class);
            ReflectionUtils.makeAccessible(daoField);
            ReflectionUtils.setField(daoField, this, constructor.newInstance(url));
            // 判断是否建表
            if (serviceAnnotation.createTable()) {
                // 获取sql
                final Field sqlField = daoImplClass.getDeclaredField("CREATE_TABLE_SQL");
                final String sql = TextUtils.varReplace(
                    TextUtils.BRACE_PATTERN, 
                    ((String) sqlField.get(null)), 
                    2, 1, 
                    key -> url.contains("sqlite") ? sqliteType(key) : mysqlType(key)
                );
                this.execute(sql);
            }
        } 
        
    }

    /**
     * 执行数据库指令
     * @param sql 指令
     */
    public void execute(@Nonnull String sql) {
        // 获取连接
        final Connection connection = DatabaseConnector.getConnect(url);
        // 执行指令
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将java数据类型转化为sql数据类型
     * 
     * @param type java数据类型
     * @return sql数据类型
     */
    private static String mysqlType(String type) {
        switch (type) {
            case "int":
                return "int";
            case "long":
            case "java.lang.Long":
                return "bigint";
            case "boolean":
            case "java.lang.Boolean":
            case "short":
            case "java.lang.Short":
                return "tinyint";
            case "java.lang.String":
                return "varchar(127)";
            case "java.lang.Double":
            case "java.lang.Float":
            case "double":
            case "float":
                return "decimal(10,3)";
            case "java.lang.Integer":
                return "integer";
            case "AUTO_INCREMENT":
                return type;
            default:
        }
        return "varchar(63)";
    }

    /**
     * 将java数据类型转化为sql数据类型
     * 
     * @param type java数据类型
     * @return sql数据类型
     */
    private static String sqliteType(String type) {
        switch (type) {
            case "int":
            case "java.lang.Integer":
            case "long":
            case "java.lang.Long":
            case "boolean":
            case "java.lang.Boolean":
            case "short":
            case "java.lang.Short":
                return "integer";
            case "java.lang.String":
                return "varchar(127)";
            case "java.lang.Double":
            case "java.lang.Float":
            case "float":
            case "double":
                return "real";
            case "AUTO_INCREMENT":
                return "AUTOINCREMENT";
            default:
        }
        return "varchar(63)";
    }

}
