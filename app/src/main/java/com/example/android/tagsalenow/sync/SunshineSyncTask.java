package com.example.android.tagsalenow.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.android.tagsalenow.ui.OpenWeatherJsonUtils;
import com.example.android.tagsalenow.utils.NetworkUtils;

import java.net.URL;

public class SunshineSyncTask {
    private static String TAG = "GGG";

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

            /* Parse the JSON into a list of weather values */
            ContentValues weatherValues = OpenWeatherJsonUtils
                    .getWeatherContentValuesFromJson(context, jsonWeatherResponse);

            /*
             * In cases where our JSON contained an error code, getWeatherContentValuesFromJson
             * would have returned null. We need to check for those cases here to prevent any
             * NullPointerExceptions being thrown. We also have no reason to insert fresh data if
             * there isn't any to insert.
             */
            if (weatherValues != null ) {
                /* Get a handle on the ContentResolver to delete and insert data */
                ///G ContentResolver sunshineContentResolver = context.getContentResolver();

                /* Delete old weather data because we don't need to keep multiple days' data */
                ///G sunshineContentResolver.delete(
                ///G         WeatherContract.WeatherEntry.CONTENT_URI,
                ///G         null,
                ///G         null);

                /* Insert our new weather data into Sunshine's ContentProvider */
                ///G  sunshineContentResolver.bulkInsert(
                ///G         WeatherContract.WeatherEntry.CONTENT_URI,
                ///G         weatherValues);
                Log.d(TAG, "syncWeather GOT DATA: Description: "+weatherValues.getAsString("description")+" Temp:"+ weatherValues.getAsString("temp"));
            }

            /* If the code reaches this point, we have successfully performed our sync */

        } catch (Exception e) {
            /* Server probably invalid */
            e.printStackTrace();
        }
    }

}
