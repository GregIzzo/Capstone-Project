package com.example.android.tagsalenow.utils;

public class OpenWeatherMapUtils {

    private static final String OWM_ICON_URL_PREFIX = "weather";
    /**
     * THIS FUNCTION COMES FROM THE SUNSHINE APP - UDACITY ANDROID NANO DEGREE COURSE
     * This method will convert a temperature from Celsius to Fahrenheit.
     *
     * @param temperatureInCelsius Temperature in degrees Celsius(°C)
     *
     * @return Temperature in degrees Fahrenheit (°F)
     */
    private static double celsiusToFahrenheit(double temperatureInCelsius) {
        double temperatureInFahrenheit = (temperatureInCelsius * 1.8) + 32;
        return temperatureInFahrenheit;
    }

}
