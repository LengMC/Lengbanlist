package org.leng.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeUtils {

    // 保留原有方法
    public static long daysToMillis(int days) {
        return days * 24L * 60 * 60 * 1000;
    }

    // 添加 parseTime 方法（兼容旧代码）
    public static long parseTime(String timeStr) {
        return parseDurationToMillis(timeStr);
    }

    // 添加 isValidTime 方法（兼容旧代码）
    public static boolean isValidTime(String timeStr) {
        return isValidTimeFormat(timeStr);
    }

    // 以下是之前的新方法
    public static long parseDurationToMillis(String timeStr) {
        if (timeStr.equalsIgnoreCase("forever")) {
            return Long.MAX_VALUE;
        }

        try {
            char unit = timeStr.charAt(timeStr.length() - 1);
            long amount = Long.parseLong(timeStr.substring(0, timeStr.length() - 1));
            
            switch (unit) {
                case 's': return amount * 1000L;
                case 'm': return amount * 60L * 1000;
                case 'h': return amount * 60L * 60 * 1000;
                case 'd': return amount * 24L * 60 * 60 * 1000;
                case 'w': return amount * 7L * 24 * 60 * 60 * 1000;
                case 'M': return amount * 30L * 24 * 60 * 60 * 1000;
                case 'y': return amount * 365L * 24 * 60 * 60 * 1000;
                default: return -1L;
            }
        } catch (Exception e) {
            return -1L;
        }
    }

    public static String formatDuration(long millis) {
        if (millis == Long.MAX_VALUE) return "永久";
        
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        if (days > 0) return days + "天";
        if (hours > 0) return hours + "小时";
        if (minutes > 0) return minutes + "分钟";
        return seconds + "秒";
    }

    public static String timestampToReadable(long timestamp) {
        if (timestamp == Long.MAX_VALUE) {
            return "永久";
        }
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String getRemainingTime(long endTime) {
        if (endTime == Long.MAX_VALUE) {
            return "永久";
        }

        long remaining = endTime - System.currentTimeMillis();
        if (remaining <= 0) {
            return "已过期";
        }

        long days = TimeUnit.MILLISECONDS.toDays(remaining);
        long hours = TimeUnit.MILLISECONDS.toHours(remaining) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(remaining) % 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append("天");
        if (hours > 0) sb.append(hours).append("小时");
        if (minutes > 0) sb.append(minutes).append("分钟");
        if (sb.length() == 0) sb.append("<1分钟");

        return sb.toString();
    }

    public static boolean isValidTimeFormat(String timeStr) {
        return timeStr.matches("\\d+[smhdwMy]") || timeStr.equalsIgnoreCase("forever");
    }
}