package com.example.android.tagsalenow;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.example.android.tagsalenow.data.CurrentInfo;
import com.example.android.tagsalenow.data.WeatherModel;
import com.example.android.tagsalenow.data.WeatherViewModel;
import com.example.android.tagsalenow.utils.NetworkUtils;
import com.google.android.gms.common.api.internal.LifecycleCallback;
import com.google.android.gms.common.api.internal.LifecycleFragment;

import java.util.List;


public class WeatherFragment extends Fragment {
    private static String TAG = "GGG";
    private Context mContext;
    private ConstraintLayout mConstraintLayout;
    private ImageView iv_WeatherIcon;
    private TextView  tv_weather_description;
    private TextView  tv_weather_temp;
    private WeatherViewModel viewModel;
    private LiveData<List<WeatherModel>> liveData;
    private int howManyTries =0;//sometimes the weatherModels.size() == 0 on first run after install. Retry

    public WeatherFragment(){

    }
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


        viewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        liveData = viewModel.getweatherDataList();
        liveData.observe(this, new Observer<List<WeatherModel>>() {
            @Override
            public void onChanged(@Nullable List<WeatherModel> weatherModels) {
                //GOT DATA
                Log.d(TAG, "WeatherFragment.onChanged: DATA CHANGED: list count= " + weatherModels.size());
                if (weatherModels.size() > 0) {

                    WeatherModel weather = weatherModels.get(0);//get first
                    fillInfo(weather);
                   // setWeatherDescription(weather.getDescription() + " (user="+CurrentInfo.getCurrentUser().getDisplayName()+")");
                   // setWeatherTemp(
                   //         String.format("%.2f", weather.getTemperature()) +
                   //         getString(R.string.wf_temperature_units));
                   // setWeatherIcon(weather.getIcon());
                } else {
                    //empty weatherModel
                    setWeatherDescription("Problem Reading Weather (user="+CurrentInfo.getCurrentUser().getDisplayName()+")");
                    tryWeatherAgain();
                }
            }
        });
        return rootView;
    }
    public void setWeatherIcon(String iconId){
        String iconUrl = NetworkUtils.getIconFilePath(iconId);
        Glide.with(this).load(iconUrl).into(iv_WeatherIcon);
    }
    public void setWeatherDescription(String desc){
        tv_weather_description.setText(desc);
    }
    public void setWeatherTemp(String temp){
        tv_weather_temp.setText(temp);
    }

    public void fillInfo(WeatherModel wm){
        setWeatherDescription(wm.getDescription() + " (user="+CurrentInfo.getCurrentUser().getDisplayName()+")");
        setWeatherTemp(
                String.format("%.0f", wm.getTemperature()) +
                        getString(R.string.wf_temperature_units));
        setWeatherIcon(wm.getIcon());
    }
    public void tryWeatherAgain(){
        WeatherModel wm = CurrentInfo.getWeatherModel();
        if (wm != null){
            fillInfo(wm);
        }
    }


}
