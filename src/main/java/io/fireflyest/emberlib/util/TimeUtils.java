package io.fireflyest.emberlib.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import org.apache.commons.lang.StringUtils;

/**
 * 时间工具类
 * 
 * @author Fireflyest
 * @since 1.0
 */
public final class TimeUtils {

    /**
     * 秒
     */
    public static final int SECOND = 1000;

    /**
     * 分钟
     */
    public static final int MINUTE = SECOND * 60;

    /**
     * 小时
     */
    public static final int HOUR = MINUTE * 60;

    /**
     * 天
     */
    public static final int DAY = HOUR * 24;

    private TimeUtils() {
    }

    /**
     * 获取当前纪元时间
     * 
     * @return 毫秒
     */
    public static long getTime() {
        return Instant.now().toEpochMilli();
    }

    /**
     * 获取当前时间
     * HH:mm:ss
     * 
     * @return 时间字符串
     */
    public static String getLocalTime() {
        final String string = LocalTime.now().toString();
        return string.substring(0, string.lastIndexOf("."));
    }

    /**
     * 转化成时间字符串
     * HH:mm:ss
     * 
     * @param time 纪元时间
     * @return 时间字符串
     */
    public static String getLocalTime(long time) {
        final String string =  LocalTime.ofInstant(
            Instant.ofEpochMilli(time), ZoneId.systemDefault()).toString();
        return string.substring(0, string.lastIndexOf("."));
    }

    /**
     * 获取当前日期
     * uuuu-MM-dd
     * 
     * @return 日期字符串
     */
    public static String getLocalDate() {
        return LocalDate.now().toString();
    }

    /**
     * 转化成日期字符串
     * uuuu-MM-dd
     * 
     * @param time 纪元时间
     * @return 日期字符串
     */
    public static String getLocalDate(long time) {
        return LocalDate.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()).toString();
    }

    /**
     * 转换时间为Instant对象
     * 
     * @param time 时间
     * @return 转化为对象
     */
    public static Instant getInstant(long time) {
        return Instant.ofEpochMilli(time);
    }

    /**
     * 持续时间时间
     * 
     * @param time 时间数据
     * @return String
     */
    public static String howLong(long time) {
        final long day = time / DAY;
        final long hour = (time - day * DAY) / HOUR;
        final long minute = (time - day * DAY - hour * HOUR) / MINUTE;
        final long second = (time - day * DAY - hour * HOUR - minute * MINUTE) / SECOND;
        final StringBuilder stringBuilder = new StringBuilder();
        if (day > 0) {
            stringBuilder.append(day).append("d ");
        }
        if (hour > 0) {
            stringBuilder.append(hour).append("h ");
        }
        if (minute > 0) {
            stringBuilder.append(minute).append("m ");
        }
        if (second > 0) {
            stringBuilder.append(second).append("s ");
        }
        return StringUtils.removeEnd(stringBuilder.toString(), " ");
    }

}
