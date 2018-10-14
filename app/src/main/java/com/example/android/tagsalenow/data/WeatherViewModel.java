package com.example.android.tagsalenow.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;

public class WeatherViewModel extends AndroidViewModel {

    private final LiveData<List<WeatherModel>> weatherDataList;
    private AppDatabase appDatabase;

    public WeatherViewModel(Application application) { super(application);
        appDatabase = AppDatabase.getDatabase(this.getApplication());
        weatherDataList = appDatabase.weatherDaoModel().getWeatherRecords();
    }

    public LiveData<List<WeatherModel>> getweatherDataList() { return
            weatherDataList;
    }

    public void deleteItem(WeatherModel WeatherModel) { new
            deleteAsyncTask(appDatabase).execute(WeatherModel);
    }

    private static class deleteAsyncTask extends AsyncTask<WeatherModel, Void, Void> {
        private AppDatabase db;

        deleteAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override protected Void doInBackground(final WeatherModel... params) {
            db.weatherDaoModel().deleteWeather(params[0]); return null;
        }
    }

}
