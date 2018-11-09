package com.example.android.tagsalenow.ui;
/*
THIS CLASS COMES FROM THE SUNSHINE APP PROJECT FROM THE UDACITY ANDROID NANO DEGREE COURSE
 */
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.android.tagsalenow.data.WeatherModel;
import com.example.android.tagsalenow.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public final class OpenWeatherJsonUtils {
    private static final String TAG = "GGG";
    private static final String OWM_COORD = "coord";
    private static final String OWM_COORD_LON = "lon";
    private static final String OWM_COORD_LAT = "lat";
    private static final String OWM_WEATHER = "weather";
    private static final String OWM_ICON = "icon";
    private static final String OWM_DESCRIPTION = "description";
    private static final String OWM_MESSAGE_CODE = "cod";
    private static final String OWM_MAIN = "main";
    private static final String OWM_MAIN_TEMP = "temp";
    private static final String OWM_MAIN_TEMP_MIN = "temp";
    private static final String OWM_MAIN_TEMP_MAX = "temp";
    private static final String OWM_MAIN_HUMIDITY = "humidity";
    private static final String OWM_WIND = "wind";
    private static final String OWM_WIND_SPEED = "speed";
    private static final String OWM_WIND_DEGREES = "degrees";
    private static final String OWM_VISIBILITY = "visibility";
    private static final String OWM_LOCATION_NAME="name";
    private static final String OWM_ZIPCODE="zipcode";



    public static WeatherModel getWeatherContentValuesFromJson(Context context, String forecastJsonStr)
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

        String description = "";
        String icon = "";
        double temperature = 0.0f;
        double temp_min = 0.0f;
        double temp_max = 0.0f;
        int humidity =0;
        int visibility = 0;
        double wind_speed = 0.0f;
        int wind_degrees= 0;
        String location_name = "";
        double longitude = 0.0f;
        double latitude = 0.0f;
        String zipcode = "";
        if (forecastJson.has(OWM_WEATHER)){
            //weather contains: [ { id:xx, main:xx, description:xx, icon: xx }...]
            JSONArray jsonWeatherOlist = forecastJson.getJSONArray(OWM_WEATHER);
            //Just use first object
            if (jsonWeatherOlist.length() > 0){
                JSONObject wObj = (JSONObject)jsonWeatherOlist.get(0);
                if (wObj.has(OWM_DESCRIPTION)) {
                    description = wObj.getString(OWM_DESCRIPTION);
                }
                if (wObj.has(OWM_ICON)) {
                    icon = wObj.getString(OWM_ICON);
                }
            }
        }
        if (forecastJson.has(OWM_MAIN)){
            //expect "main" to be  {temp:nn.nn, pressure:nnn, humidity:nn, temp_min:nn.nn, temp_max:nn.nn}
            JSONObject wObj = forecastJson.getJSONObject(OWM_MAIN);
            if (wObj.has(OWM_MAIN_TEMP)){ temperature = wObj.getDouble(OWM_MAIN_TEMP);}
            if (wObj.has(OWM_MAIN_HUMIDITY)){ humidity = wObj.getInt(OWM_MAIN_HUMIDITY);}
            if (wObj.has(OWM_MAIN_TEMP_MAX)){ temp_max = wObj.getDouble(OWM_MAIN_TEMP_MAX);}
            if (wObj.has(OWM_MAIN_TEMP_MIN)){ temp_min = wObj.getDouble(OWM_MAIN_TEMP_MIN);}
        }
        if (forecastJson.has(OWM_VISIBILITY)){
            visibility = forecastJson.getInt(OWM_VISIBILITY);
        }
        if (forecastJson.has(OWM_WIND)){
            JSONObject wObj = forecastJson.getJSONObject(OWM_WIND);
            if (wObj.has(OWM_WIND_SPEED)) wind_speed = wObj.getDouble(OWM_WIND_SPEED);
            if (wObj.has(OWM_WIND_DEGREES)) wind_degrees = wObj.getInt(OWM_WIND_DEGREES);
        }
        if (forecastJson.has(OWM_LOCATION_NAME)){
            location_name = forecastJson.getString(OWM_LOCATION_NAME);
        }
        if (forecastJson.has(OWM_COORD)){
            JSONObject wObj = forecastJson.getJSONObject(OWM_COORD);
            if (wObj.has(OWM_COORD_LON)) longitude = wObj.getDouble(OWM_COORD_LON);
            if (wObj.has(OWM_COORD_LAT)) latitude = wObj.getDouble(OWM_COORD_LAT);
        }
        if (forecastJson.has(OWM_ZIPCODE)){
            zipcode = forecastJson.getString(OWM_ZIPCODE);
        }
        /* Get Weather Description
        *  expect the format: {id:xxx, main:"TEXT", description:"TEXT", icon:"TEXT"}
        * */
        WeatherModel weatherModel = new WeatherModel(description,
                icon, temperature, temp_min, temp_max, humidity, visibility,wind_speed, wind_degrees, location_name,longitude, latitude,zipcode);
        return  weatherModel;

/*


        ContentValues weatherValues = new ContentValues();
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
        */

    }



}
