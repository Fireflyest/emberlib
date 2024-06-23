package io.fireflyest.spigot.emberlib.util;

import java.util.regex.Pattern;

/**
 * 文本工具类
 * @author Fireflyest
 * @since 1.0
 */
public final class StringUtils {
    
    public static final Pattern PERCENT_PATTERN = Pattern.compile("%([^%]*)%"); // %value%

    private StringUtils() {
        // 
    }

}
