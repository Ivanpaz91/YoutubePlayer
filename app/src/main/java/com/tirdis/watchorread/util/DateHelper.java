package com.tirdis.watchorread.util;

/**
 * Created by admin on 5/28/2015.
 */

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {

    private static final String DATE_FORMAT = "dd MM yyyy";
    private static final String UTC_DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "hh:mm a";
    private static final String CUSTOM_FORMAT ="dd MMM yyyy";
    public static String formatDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(CUSTOM_FORMAT);
        String dateString = dateFormat.format(date);
        return dateString;
    }

    public static String formatTime(String time) {
        if (time == null) {
            return "";
        }
        String[] parts = time.split(":");
        if (parts.length >= 2) {
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);

            DateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
            String dateString = dateFormat.format(calendar.getTime());
            return dateString;
        }
        return "";
    }

    public static String formatUtcDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(UTC_DATE_FORMAT);
        String dateString = dateFormat.format(date);
        return dateString;
    }

    public static Date parseUtcDate(String date) {
        DateFormat dfm = new SimpleDateFormat(UTC_DATE_FORMAT);
        try {
            return dfm.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
    public static String getTheDayDate(){
        SimpleDateFormat sdf = new SimpleDateFormat(UTC_DATE_FORMAT);
        return  sdf.format(new Date());
    }


}
