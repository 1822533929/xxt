package com.qjn.xiangxi_system.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * 获取当前日期时间字符串
     * @return 格式化的日期时间字符串 (yyyy-MM-dd HH:mm:ss)
     */
    public static String getDateTime() {
        return LocalDateTime.now().format(DATE_TIME_FORMATTER);
    }
    /**
     * 获取当前日期字符串
     * @return 格式化的日期字符串 (yyyy-MM-dd)
     */
    public static String getDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}