package io.fireflyest.emberlib.util;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang.StringUtils;
import org.bukkit.util.NumberConversions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 文本工具类
 * @author Fireflyest
 * @since 1.0
 */
public final class TextUtils {
    
    private static final Gson gson = new Gson();

    /**
     * 变量格式 %key%
     */
    public static final Pattern PERCENT_PATTERN = Pattern.compile("%([^%]*)%");

    /**
     * 变量格式 ${key}
     */
    public static final Pattern BRACE_PATTERN = Pattern.compile("\\$\\{([^{}]*)}");

    /**
     * 变量格式 {index}
     */
    public static final Pattern FORMAT_PATTERN = Pattern.compile("\\{([^{}]*)}");

    /**
     * 驼峰后面的每个首大写字母
     */
    public static final Pattern CASE_BORDER_LETTER = Pattern.compile("(?<=[a-z])[A-Z]");
    
    /**
     * 纯文本
     */
    public static final Pattern PURE_TEXT = Pattern.compile("[a-zA-Z0-9]+");

    /**
     * 完整的纯文本
     */
    public static final Pattern COMPLETE_PURE_TEXT = Pattern.compile("^[a-zA-Z0-9]+$");

    /**
     * 用于获取对应变量
     */
    public interface VariableContainer {

        /**
         * 获取对应键的值
         * 
         * @param key 键
         * @return 值
         */
        String getVar(String key);

    }

    private TextUtils() {
        // 
    }

    /**
     * 匹配文本
     * 
     * @param pattern 正则表达式的编译表示
     * @param str 需要变量替换的文本
     * @return 替换后的文本
     */
    public static boolean match(@Nonnull Pattern pattern, @Nullable String str) {
        if (str == null) {
            return false;
        }
        return pattern.matcher(str).matches();
    }

    /**
     * 正则表达式查询
     * 
     * @param pattern 正则表达式的编译表示
     * @param str 查询的文本来源
     * @return 查询结果
     */
    public static String[] find(@Nonnull Pattern pattern, @Nullable String str) {
        if (str == null) {
            return new String[0];
        }
        final Matcher matcher = pattern.matcher(str);
        final List<String> found = new ArrayList<>();
        while (matcher.find()) {
            found.add(matcher.group());
        }
        return found.toArray(new String[0]);
    }

    /**
     * 变量替换
     * @param pattern 正则表达式的编译表示
     * @param str 需要变量替换的文本
     * @param begin 变量前方包裹符号的数量
     * @param end 变量后方包裹符号的数量
     * @param container 存储变量的容器
     * @return 替换后的文本
     */
    public static String varReplace(@Nonnull Pattern pattern, 
                                    @Nullable String str, 
                                    int begin, 
                                    int end, 
                                    @Nonnull VariableContainer container) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        final Matcher varMatcher = pattern.matcher(str);
        final StringBuilder stringBuilder = new StringBuilder();
        while (varMatcher.find()) {
            final String parameter = varMatcher.group();
            final String parameterName = parameter.substring(begin, parameter.length() - end);
            varMatcher.appendReplacement(stringBuilder, container.getVar(parameterName));
        }
        varMatcher.appendTail(stringBuilder);
        return stringBuilder.toString();
    }

    /**
     * 格式化文本
     * @param text 文本
     * @param vars 变量
     * @return 文本
     */
    public static String format(@Nullable String text, Object... vars) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return varReplace(FORMAT_PATTERN, text, 1, 1, new VariableContainer() {

            private int index = 0;

            @Override
            public String getVar(String key) {
                // 有序号用序号，没有序号按顺序给
                if (!"".equals(key)) {
                    index = NumberConversions.toInt(key);
                }
                // 判断是否越界
                if (index >= vars.length) {
                    index = vars.length - 1;
                }
                return String.valueOf(vars[index++]);
            }

        });
    }

    /**
     * 列表转换为文本
     * @param obj 列表
     * @return 文本
     */
    public static String toJson(@Nonnull Object obj) {
        return gson.toJson(obj);
    }

    /**
     * 文本转列表
     * @param <T> 对象类型
     * @param str 文本
     * @return 列表
     */
    public static <T> List<T> jsonToList(@Nullable String str) {
        if (str == null || str.isEmpty()) {
            return Collections.emptyList();
        }
        return gson.fromJson(str, new TypeToken<List<T>>() {}.getType());
    }

    /**
     * 文本转对象
     * @param <T> 对象类型
     * @param str 文本
     * @param type 对象的类
     * @return 对象
     */
    public static <T> T jsonToObj(@Nullable String str, Class<T> type) {
        return str == null ? null : gson.fromJson(str, type);
    }

    /**
     * 拼接字符串
     * @param objs 要拼接的对象
     * @return 拼接结果
     */
    public static String append(@Nonnull Object... objs) {
        final StringBuilder sb = new StringBuilder();
        for (Object obj : objs) {
            sb.append(obj);
        }
        return sb.toString();
    }

    /**
     * 转为Base64格式
     * 
     * @param str 原文本
     * @return Base64格式
     */
    public static String base64Encode(String str) {
        if (str == null) {
            return str;
        }
        return Base64.getEncoder().encodeToString(str.getBytes());
    }

    /**
     * 转Base64为普通文本
     * 
     * @param str Base64格式文本
     * @return 普通文本
     */
    public static String base64Decode(String str) {
        if (str == null) {
            return str;
        }
        return new String(Base64.getDecoder().decode(str));
    }

    /**
     * test转换为Test
     * @param str 文本
     * @return 首字母大写
     */
    public static String upperFirst(@Nullable String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1).toLowerCase();
    }

    /**
     * 转为小驼峰
     * 
     * @param str 原文本
     * @return 小驼峰文本
     */
    public static String toCamel(@Nullable String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        final Matcher wordMatcher = PURE_TEXT.matcher(str);
        final StringBuilder stringBuilder = new StringBuilder();
        while (wordMatcher.find()) {
            stringBuilder.append(upperFirst(wordMatcher.group()));
        }
        return stringBuilder.toString();
    }

    /**
     * 分割驼峰
     * 
     * @param str 驼峰文本
     * @param delimiter 分隔符
     * @return 分割后的文本
     */
    public static String camelSplit(@Nullable String str, @Nonnull String delimiter) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        return varReplace(CASE_BORDER_LETTER, str, 0, 0, key -> delimiter + key.toLowerCase());
    }

    /**
     * 分割驼峰
     * 
     * @param str 驼峰文本
     * @return 分割后的文本
     */
    public static String camelSplit(@Nullable String str) {
        return camelSplit(str, " ");
    }

    /**
     * 按符号分割
     * 
     * @param str 带符号文本
     * @param delimiter 分隔符
     * @return 分割后的文本
     */
    public static String symbolSplit(@Nullable String str, @Nonnull String delimiter) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        final Matcher wordMatcher = PURE_TEXT.matcher(str);
        final StringBuilder stringBuilder = new StringBuilder();
        while (wordMatcher.find()) {
            stringBuilder.append(delimiter)
                         .append(wordMatcher.group());
        }
        return StringUtils.removeStart(stringBuilder.toString(), delimiter);
    }

    /**
     * 按符号分割
     * 
     * @param str 带符号文本
     * @return 分割后的文本
     */
    public static String symbolSplit(@Nullable String str) {
        return symbolSplit(str, " ");
    }



}
