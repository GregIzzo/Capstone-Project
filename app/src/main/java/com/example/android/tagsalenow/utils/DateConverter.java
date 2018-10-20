package com.example.android.tagsalenow.utils;

/*
This class comes from the following article on Android Architecture components:
https://blog.iamsuleiman.com/android-architecture-components-tutorial-room-livedata-viewmodel/
 */
import android.arch.persistence.room.TypeConverter;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {
    private static SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat displayFormat = new SimpleDateFormat("MMM dd");

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    public static String toDisplayFormat(String dbDate){
        String returnDate = "";
        try {
            Date d = dbFormat.parse(dbDate);
            returnDate = displayFormat.format(d);
        } catch (Exception ex){
            Log.d("DateConverter", "toDisplayFormat: Error converting["+dbDate+"] : "+ex.getMessage());
        }
        return returnDate;
    }
}
