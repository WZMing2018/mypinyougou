package com.mypinyougou.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    /**
     * 将日期格式 格式化为对应的字符串
     * @param date
     * @return
     */
    public static String formatDateToString(Date date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateStr = simpleDateFormat.format(date);
            return dateStr;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
