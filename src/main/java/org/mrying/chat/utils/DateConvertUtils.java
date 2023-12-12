package org.mrying.chat.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 时间转化工具
 * @author 邓和颖
 */
public class DateConvertUtils {

    /**
     * 根据时间转换为时间戳
     */
    public static long getTimeStamp(Date date) {
        return date.getTime();
    }

    /**
     * 时间戳转时间
     */
    public static Date getDateTime(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return Date.from(instant);
    }

    /**
     * 格式化传入的时间，将时间转化为指定格式字符串
     */
    public static String getDateTimeString(Date date) {
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 格式化传入的时间戳，将时间戳转化为指定格式字符串
     */
    public static String getTimeStampString(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        // 格式化日期
        return localDateTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}

