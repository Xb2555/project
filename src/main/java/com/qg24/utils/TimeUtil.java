package com.qg24.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 用于对时间格式化与解析
 */
public class TimeUtil {

    /**
     * 格式化时间为字符串
     * @param localDateTime 当前时间
     * @return 当前时间的字符串
     */
    public static String formatDateTime(LocalDateTime localDateTime){
        //获取格式化器对象
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //格式化时间
        String time = localDateTime.format(dateTimeFormatter);
        //返回该字符串
        return time;
    }

    /**
     * 解析字符串
     * @param time
     * @return
     */
    public static LocalDateTime parseDateTime(String time){
        //获取格式化器对象
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //获取时间格式
        LocalDateTime localDateTime = LocalDateTime.parse(time, dateTimeFormatter);
        //返回时间
        return localDateTime;
    }
}
