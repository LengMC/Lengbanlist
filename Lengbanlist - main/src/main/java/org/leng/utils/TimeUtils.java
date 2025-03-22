package org.leng.utils;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtils {

    /**
     * 生成从当前时间开始的指定天数后的时间戳
     *
     * @param days 天数
     * @return 时间戳（毫秒）
     */
    public static long generateTimestampFromDays(int days) {
        long currentTimeMillis = System.currentTimeMillis();
        long daysInMillis = TimeUnit.DAYS.toMillis(days);
        return currentTimeMillis + daysInMillis;
    }

    /**
     * 将时间戳转换为可读的日期时间字符串
     *
     * @param timestamp 时间戳（毫秒）
     * @return 格式化的日期时间字符串（yyyy-MM-dd HH:mm:ss）
     */
    public static String timestampToReadable(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 解析时间字符串（如 "1d"、"2h"、"30m"）为时间戳
     *
     * @param timeStr 时间字符串
     * @return 时间戳（毫秒），解析失败返回 -1
     */
    public static long parseTime(String timeStr) {
        if (timeStr.equalsIgnoreCase("forever")) {
            return Long.MAX_VALUE; // 返回一个非常大的值，表示永久封禁
        }

        Matcher matcher = Pattern.compile("(\\d+)([smhdwMy])").matcher(timeStr);
        if (!matcher.matches()) {
            return -1;
        }
        int amount = Integer.parseInt(matcher.group(1));
        String unit = matcher.group(2);
        long millis;
        switch (unit) {
            case "s":
                millis = Duration.ofSeconds(amount).toMillis();
                break;
            case "m":
                millis = Duration.ofMinutes(amount).toMillis();
                break;
            case "h":
                millis = Duration.ofHours(amount).toMillis();
                break;
            case "d":
                millis = Duration.ofDays(amount).toMillis();
                break;
            case "w":
                millis = Duration.ofDays(amount * 7).toMillis();
                break;
            case "M":
                millis = Duration.ofDays(amount * 30).toMillis();
                break;
            case "y":
                millis = Duration.ofDays(amount * 365).toMillis();
                break;
            default:
                return -1;
        }
        return System.currentTimeMillis() + millis;
    }

    /**
     * 校验时间字符串是否合法
     *
     * @param timeStr 时间字符串
     * @return 如果合法返回 true，否则返回 false
     */
    public static boolean isValidTime(String timeStr) {
        return timeStr.matches("\\d+[smhdwMy]");
    }

    /**
     * 将时间戳转换为剩余时间的可读字符串（如 "2天3小时"）
     *
     * @param timestamp 时间戳（毫秒）
     * @return 剩余时间的可读字符串
     */
    public static String getRemainingTime(long timestamp) {
        if (timestamp == Long.MAX_VALUE) {
            return "永久封禁";
        }

        long remainingMillis = timestamp - System.currentTimeMillis();
        if (remainingMillis <= 0) {
            return "已过期";
        }

        long days = TimeUnit.MILLISECONDS.toDays(remainingMillis);
        long hours = TimeUnit.MILLISECONDS.toHours(remainingMillis) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(remainingMillis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(remainingMillis) % 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append("天");
        }
        if (hours > 0) {
            sb.append(hours).append("小时");
        }
        if (minutes > 0) {
            sb.append(minutes).append("分钟");
        }
        if (seconds > 0) {
            sb.append(seconds).append("秒");
        }
        return sb.toString();
    }
}