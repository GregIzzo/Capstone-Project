package com.example.android.tagsalenow.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class WeatherModel {

    @PrimaryKey
    public int id;
    private String description;
    private String icon;
    private double temperature;//Fahrenheit
    private double temp_min;//Fahrenheit
    private double temp_max;//Fahrenheit
    private int humidity;
    private int visibility;
    private double wind_speed;
    private int wind_degrees;
    private String location_name;
    private double longitude;
    private double latitude;
    private String zipcode;

    public WeatherModel(String description,
                        String icon,
                        double temperature,
                        double temp_min,
                        double temp_max,
                        int humidity,
                        int visibility,
                        double wind_speed,
                        int wind_degrees,
                        String location_name,
                        double longitude,
                        double latitude,
                        String zipcode){
        this.description = description;
        this.icon = icon;
        this.temperature = temperature;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.humidity = humidity;
        this.visibility = visibility;
        this.wind_speed = wind_speed;
        this.wind_degrees = wind_degrees;
        this.location_name = location_name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.zipcode = zipcode;

        id = 1;
    }
    public String getDescription(){
        return description;
    }
    public String getIcon() { return icon;}
    public double getTemperature(){
        return temperature;
    }
    public double getTemp_min(){
        return temp_min;
    }
    public double getTemp_max(){
        return temp_max;
    }
    public int getHumidity(){
        return humidity;
    }
    public int getVisibility(){
        return visibility;
    }
    public double getWind_speed(){
        return wind_speed;
    }
    public int getWind_degrees(){
        return wind_degrees;
    }
    public String getLocation_name(){
        return location_name;
    }
    public double getLongitude(){
        return longitude;
    }
    public double getLatitude(){
        return latitude;
    }
    public String getZipcode(){
        return zipcode;
    }


}
