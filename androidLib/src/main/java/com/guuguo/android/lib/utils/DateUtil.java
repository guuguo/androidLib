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

    public static String formDate(String format, long dateTime) {
        return new SimpleDateFormat(format).format(dateTime);
    }
}
