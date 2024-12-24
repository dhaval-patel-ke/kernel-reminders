package com.kernelequity.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtil {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");

    public static long getDifferenceDays(Date d1, Date d2) {
        if (d1 == null || d2 == null) return 0;
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static String getFormattedDate(Date date) {
        if (date == null) return "";
        return dateFormat.format(date);
    }

}