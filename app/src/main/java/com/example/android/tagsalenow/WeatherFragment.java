package com.example.android.tagsalenow;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;


public class WeatherFragment extends Fragment {
    private Context mContext;
    private ConstraintLayout mConstraintLayout;
    private ImageView iv_WeatherIcon;
    private TextView  tv_weather_description;
    private TextView  tv_weather_temp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weather, container, false);
        mConstraintLayout = rootView.findViewById(R.id.cl_weather);
        assert mConstraintLayout != null;
        iv_WeatherIcon = rootView.findViewById(R.id.weather_icon);
        tv_weather_description = rootView.findViewById(R.id.weather_description);
        tv_weather_temp = rootView.findViewById(R.id.weather_temp);


        return rootView;
    }
    public void setWeatherIcon(String iconUrl){
        Glide.with(this).load(iconUrl).into(iv_WeatherIcon);
    }
    public void setWeatherDescription(String desc){
        tv_weather_description.setText(desc);
    }
    public void setWeatherTemp(String temp){
        tv_weather_temp.setText(temp);
    }
}
