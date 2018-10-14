package com.example.android.tagsalenow.data;
/*
This class is based on a similar class in this article on Android Architecture components:
https://blog.iamsuleiman.com/android-architecture-components-tutorial-room-livedata-viewmodel/

 */
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {WeatherModel.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public static String WEATHERDB = "weather_db";
    public static AppDatabase getDatabase(Context context){
        if (INSTANCE == null){
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, WEATHERDB)
                            .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
    public abstract WeatherModelDao weatherDaoModel();
}
