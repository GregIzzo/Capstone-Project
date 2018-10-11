package com.example.android.tagsalenow.ui;
/*
THIS CLASS COMES FROM THE SUNSHINE APP PROJECT FROM THE UDACITY ANDROID NANO DEGREE COURSE
 */
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.android.tagsalenow.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public final class OpenWeatherJsonUtils {
    private static final String TAG = "GGG";
    private static final String OWM_WEATHER = "weather";
    private static final String OWM_ICON = "icon";
    private static final String OWM_TEMP = "temp";
    private static final String OWM_DESCRIPTION = "description";
    private static final String OWM_MESSAGE_CODE = "cod";
    private static final String OWM_MAIN = "main";

    public static ContentValues getWeatherContentValuesFromJson(Context context, String forecastJsonStr)
            throws JSONException {
        Log.d(TAG, "getWeatherContentValuesFromJson: **RAWJSON:"+forecastJsonStr);
        JSONObject forecastJson = new JSONObject(forecastJsonStr);

        /* Is there an error? */
        if (forecastJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = forecastJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }
        /* Get Weather Description
        *  expect the format: {id:xxx, main:"TEXT", description:"TEXT", icon:"TEXT"}
        * */
        ContentValues weatherValues = new ContentValues();
        JSONArray jsonWeatherOlist = forecastJson.getJSONArray(OWM_WEATHER);

        //pull info from the 'main' object
        JSONObject jsonMainObject = forecastJson.getJSONObject(OWM_MAIN);//
        String weatherTemp = jsonMainObject.getString("temp");

        weatherValues.put(OWM_TEMP, weatherTemp);

        if (jsonWeatherOlist.length() > 0) {

            JSONObject jsonWeatherObject = (JSONObject)jsonWeatherOlist.get(0);
            String weatherDescription = jsonWeatherObject.getString(OWM_DESCRIPTION);
            String weatherIconPath = NetworkUtils.getIconFilePath(jsonWeatherObject.getString(OWM_ICON));
            weatherValues.put(OWM_DESCRIPTION, weatherDescription);
            weatherValues.put(OWM_ICON, weatherIconPath);
            Log.d(TAG, "getWeatherContentValuesFromJson:**WEATHER DATA*** Desc["+weatherDescription+"] icon["+weatherIconPath+"] ");
        }
        return weatherValues;

    }



}
