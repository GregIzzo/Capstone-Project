package com.example.android.tagsalenow.utils;

/*
This class comes from the following article on Android Architecture components:
https://blog.iamsuleiman.com/android-architecture-components-tutorial-room-livedata-viewmodel/
 */
import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class DateConverter {
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

}
