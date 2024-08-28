package io.fireflyest.emberlib.util;

import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Color;
import org.bukkit.util.NumberConversions;

/**
 * 颜色工具类
 * 
 * @author Fireflyest
 * @since 1.0
 */
public final class ColorUtils {

    private static final Pattern colorPattern = Pattern.compile("^#([0-9a-fA-F]{6})$");

    private ColorUtils() {
    }

    /**
     * 将颜色转为字符串
     * 
     * @param color 颜色
     * @return 颜色字符串
     */
    public static String toString(@Nonnull Color color) {
        return "#" + Integer.toHexString(color.asRGB());
    }

    /**
     * 文本转换为颜色
     * 
     * @param color 颜色
     * @return 颜色
     */
    public static Color toColor(@Nonnull String color) {
        color = color.replace("#", "");
        return Color.fromRGB(Integer.parseInt(color, 16));
    }

    /**
     * 渐变颜色
     * 
     * @param start 起始颜色
     * @param end 结束颜色
     * @param num 过渡颜色数量，不少于2
     * @return 过渡颜色
     */
    public static String[] gradient(@Nonnull String start, @Nonnull String end, int num) {
        // 判断格式是否正确
        if (num < 2) {
            num = 2;
        }
        if (num > 255) {
            num = 255;
        }
        final String[] colors = new String[num];
        if (!colorPattern.matcher(start).matches() || !colorPattern.matcher(end).matches()) {
            return colors;
        }
        // 转化成整数计算
        final int startColor = Integer.parseInt(StringUtils.remove(start, '#'), 16);
        final int endColor = Integer.parseInt(StringUtils.remove(end, '#'), 16);
        // 计算过渡
        final int startR = rr(startColor);
        final int startG = gg(startColor);
        final int startB = bb(startColor);
        final double deltaR = (rr(endColor) - startR) / (num - 1.0);
        final double deltaG = (gg(endColor) - startG) / (num - 1.0);
        final double deltaB = (bb(endColor) - startB) / (num - 1.0);
        for (int i = 0; i < num; i++) {
            colors[i] = "#" 
                + Integer.toHexString(startR + NumberConversions.round(i * deltaR))
                + Integer.toHexString(startG + NumberConversions.round(i * deltaG))
                + Integer.toHexString(startB + NumberConversions.round(i * deltaB));
        }
        return colors;
    }

    /**
     * 两种颜色的距离
     * 
     * @param c1 颜色1
     * @param c2 颜色2
     * @return 距离
     */
    public static int distance(@Nonnull Color c1, @Nonnull Color c2) {
        final double rmean = (c1.getRed() + c2.getRed()) / 2.0;
        final int r = c1.getRed() - c2.getRed();
        final int g = c1.getGreen() - c2.getGreen();
        final int b = c1.getBlue() - c2.getBlue();
        final double weightR = 2 + rmean / 256.0;
        final double weightG = 4.0;
        final double weightB = 2 + (255 - rmean) / 256.0;
        return NumberConversions.round(weightR * r * r + weightG * g * g + weightB * b * b);
    }

    /**
     * 获取红色
     * 
     * @param color 颜色
     * @return 红色
     */
    public static int rr(int color) {
        return (color & 0xFF0000) >> 16;
    }

    /**
     * 获取绿色
     * 
     * @param color 颜色
     * @return 绿色
     */
    public static int gg(int color) {
        return (color & 0x00FF00) >> 8;
    }

    /**
     * 获取蓝色
     * 
     * @param color 颜色
     * @return 蓝色
     */
    public static int bb(int color) {
        return color & 0x0000FF;
    }

}
