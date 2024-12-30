package com.qjn.xiangxi_system.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtil {
//    public static String getDateTime() {
//        LocalDateTime currentDateTime = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        return currentDateTime.format(formatter);
//    }
    public static Date getDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}