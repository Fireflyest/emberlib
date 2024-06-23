package io.fireflyest.spigot.emberlib.util;

import java.util.regex.Pattern;

/**
 * 文本工具类
 * @author Fireflyest
 * @since 1.0
 */
public final class StringUtils {
    
    public static final Pattern PERCENT_PATTERN = Pattern.compile("%([^%]*)%"); // 百分号
    public static final Pattern BRACE_PATTERN = Pattern.compile("\\$\\{([^{}]*)}"); // 美元大括号
    public static final Pattern FORMAT_PATTERN = Pattern.compile("\\{([^{}]*)}"); // 大括号

    private StringUtils() {
        // 
    }

}
