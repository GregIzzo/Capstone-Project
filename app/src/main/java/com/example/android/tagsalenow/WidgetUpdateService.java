package com.example.android.tagsalenow;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.tagsalenow.data.PrefHandler;

public class WidgetUpdateService extends IntentService {

    private static final String TAG = "WIDGETUPDATESERVICE";

    public static final String ACTION_UPDATE_TAGSALES = "com.example.android.tagsalenow.action.update_tagsales";
    static Context mContext;

    public static void startActionUpdateTagSales(Context context){
        mContext = context;
        Intent intent = new Intent(context, WidgetUpdateService.class);
        intent.setAction((ACTION_UPDATE_TAGSALES));
        context.startService(intent);
    }

    public WidgetUpdateService(){
        super("WidgetUpdateService");
    }
    @Override
    protected void onHandleIntent( @Nullable Intent intent) {
        if (intent != null){
            final String action = intent.getAction();
            if (ACTION_UPDATE_TAGSALES.equals(action)){
                handleUpdateTagSales();
            }
        }

    }

    private void handleUpdateTagSales(){
        //update based on tag sale info in prefs
        Log.d(TAG, "handleUpdateTagSales: TIME TO UPDATE TAG SALES IN THE WIDGET");
        String top3 = PrefHandler.getTop3(mContext);
        Log.d(TAG, "handleUpdateTagSales: top3 string="+top3);




        //final long plantId = intent.getLongExtra(EXTRA_PLANT_ID, PlantContract.INVALID_PLANT_ID);
        //handleActionWaterPlant(plantId);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,TagSaleNowAppWidget.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_tagsales);
        TagSaleNowAppWidget.updateTagSaleNowWidgets(this,appWidgetManager,appWidgetIds);
    }
}
