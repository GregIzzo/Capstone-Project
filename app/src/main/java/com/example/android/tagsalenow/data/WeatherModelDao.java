package com.example.android.tagsalenow.data;
/*
This class is based on a similar class in this article on Android Architecture components:
https://blog.iamsuleiman.com/android-architecture-components-tutorial-room-livedata-viewmodel/

 */
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao public interface WeatherModelDao {

    @Query("select * from WeatherModel")
    LiveData<List<WeatherModel>> getWeatherRecords();

    @Query("select * from WeatherModel where id = :id")
    WeatherModel getItemById(String id);

    @Insert(onConflict = REPLACE)
    void addWeather(WeatherModel weatherModel);

    @Delete
    void deleteWeather(WeatherModel weatherModel);



}
