package com.guuguo.android.lib.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by user on 2017-02-19.
 */

public class DateUtil {
    public static long toDate(String format, String dateStr) {
        try {
            return new SimpleDateFormat(format).parse(dateStr).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    public static String fromDate(String format, long dateTime) {
        dateTime = getMsDateTime(dateTime);
        return new SimpleDateFormat(format).format(dateTime);
    }


    public static String getTimeSpan(String format, long dateTime) {
        dateTime = getMsDateTime(dateTime);
        long timeSpan = System.currentTimeMillis() - dateTime;
        timeSpan = timeSpan / 1000;
        if (timeSpan < 60)
            return timeSpan + "秒前";
        timeSpan /= 60;
        if (timeSpan < 60) {
            return timeSpan + "分钟前";
        }
        timeSpan /= 60;
        if (timeSpan < 24) {
            return timeSpan + "小时前";
        }
        timeSpan /= 24;
        if (timeSpan < 7) {
            return timeSpan + "天前";
        }
        timeSpan /= 7;
        if (timeSpan < 4) {
            return timeSpan + "周前";
        }
        return fromDate(format, dateTime);
    }

    public static long getMsDateTime(long dateTime) {
        if ((dateTime + "").length() == 10)
            return dateTime * 1000;
        else
            return dateTime;
    }
    public static long getSecondDateTime(long dateTime) {
        if ((dateTime + "").length() == 13)
            return dateTime / 1000;
        else
            return dateTime;
    }
}
