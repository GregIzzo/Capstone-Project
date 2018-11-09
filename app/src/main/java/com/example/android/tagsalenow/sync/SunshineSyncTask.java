package com.example.android.tagsalenow.sync;
/*
THIS IS FROM THE SUNSHINE APP FROM UDACITY ANDROID NANO DEGREE COURSE
 */

import android.arch.persistence.room.Room;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.android.tagsalenow.data.AppDatabase;
import com.example.android.tagsalenow.data.CurrentInfo;
import com.example.android.tagsalenow.data.WeatherContract;
import com.example.android.tagsalenow.data.WeatherModel;
import com.example.android.tagsalenow.data.WeatherModelDao;
import com.example.android.tagsalenow.ui.OpenWeatherJsonUtils;
import com.example.android.tagsalenow.utils.NetworkUtils;

import java.net.URL;

public class SunshineSyncTask {
    private static String TAG = "SUNSHINESYNCTASK";

    /**
     * Performs the network request for updated weather, parses the JSON from that request, and
     * inserts the new weather information into our ContentProvider. Will notify the user that new
     * weather has been loaded if the user hasn't been notified of the weather within the last day
     * AND they haven't disabled notifications in the preferences screen.
     *
     * @param context Used to access utility methods and the ContentResolver
     */
    synchronized public static void syncWeather(Context context) {


        try {
            /*
             * The getUrl method will return the URL that we need to get the forecast JSON for the
             * weather. It will decide whether to create a URL based off of the latitude and
             * longitude or off of a simple location as a String.
             */
            URL weatherRequestUrl = NetworkUtils.getUrl(context);

            /* Use the URL to retrieve the JSON */
            String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
            Log.d(TAG, "syncWeather GOT DATA: NETWORK RESPONSE:" + jsonWeatherResponse);
            /* Parse the JSON into a list of weather values */
            WeatherModelDao mWeatherDAO = Room.databaseBuilder(context,  AppDatabase.class, AppDatabase.WEATHERDB)
                    .build()
                    .weatherDaoModel();

            WeatherModel weatherModel = OpenWeatherJsonUtils
                    .getWeatherContentValuesFromJson(context, jsonWeatherResponse);

            CurrentInfo.setWeatherModel(weatherModel);


            mWeatherDAO.addWeather(weatherModel);

            Log.d(TAG, "syncWeather GOT DATA: ADDED WEATHER TO DAO:" + weatherModel.toString());

            /*
             * In cases where our JSON contained an error code, getWeatherContentValuesFromJson
             * would have returned null. We need to check for those cases here to prevent any
             * NullPointerExceptions being thrown. We also have no reason to insert fresh data if
             * there isn't any to insert.
             */
     /*
            if (weatherValues != null ) {
                Log.d(TAG, "syncWeather GOT DATA: START weatherValues=" + weatherValues.toString());

               // Get a handle on the ContentResolver to delete and insert data /
                 ContentResolver sunshineContentResolver = context.getContentResolver();

               // Delete old weather data because we don't need to keep multiple days' data /
                Log.d(TAG, "syncWeather GOT DATA: DELETING weatherValues=" + weatherValues.toString());
                 sunshineContentResolver.delete(
                         WeatherContract.WeatherEntry.CONTENT_URI,
                         null,
                         null);

               // Insert our new weather data into Sunshine's ContentProvider /
                ContentValues[] arrayOfValues = new ContentValues[]{weatherValues};

                  sunshineContentResolver.bulkInsert(
                         WeatherContract.WeatherEntry.CONTENT_URI,
                          arrayOfValues);
                Log.d(TAG, "syncWeather GOT DATA: INSERTED Description: "+weatherValues.getAsString("description")+" Temp:"+ weatherValues.getAsString("temp"));
            }

            // If the code reaches this point, we have successfully performed our sync
            */

        } catch (Exception e) {
            Log.d(TAG, "syncWeather: *** Error getting data:" + e.getMessage());
            /* Server probably invalid */
            e.printStackTrace();
        }
    }

}
