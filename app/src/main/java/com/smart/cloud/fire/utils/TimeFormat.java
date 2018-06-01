package com.smart.cloud.fire.utils;

import java.text.SimpleDateFormat;

/**
 * Created by Rain on 2018/5/28.
 */
public class TimeFormat {
    public static final String DATE_FORMAT="yyyy-MM-dd hh:mm:ss";
    /**
     * 日期格式字符串转换成时间戳
     * @return
    */
    public static Long date2TimeStamp(String date_str) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            return sdf.parse(date_str).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
     }
    }
