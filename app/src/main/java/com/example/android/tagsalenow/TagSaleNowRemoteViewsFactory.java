package com.example.android.tagsalenow;

import android.app.LauncherActivity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.tagsalenow.data.PrefHandler;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class TagSaleNowRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static String TAG = "TAGSALENOWREMOTEVIEWSFACTORY";
    private ArrayList<WidgetListItem> itemList = new ArrayList<WidgetListItem>();
    private Context context = null;
    private int appWidgetId;
    //private Gson gsonWorker;

    public TagSaleNowRemoteViewsFactory(Context applicationContext, Intent intent){
        context = applicationContext;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        //gsonWorker = new Gson();
        //populate items
    }
    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        Log.d(TAG, "TagSaleNowRemoteViewsFactory.onDataSetChanged: !!!!");

        String top3String = PrefHandler.getTop3(context);
        JSONArray jarray = null;
        try{
             jarray = new JSONArray(top3String);
        } catch (Exception ex){
            Log.d(TAG, "onDataSetChanged: ERROR CONVERTING STRING TO JSON:"+ex.getMessage());
            return;
        }
        itemList = new ArrayList<WidgetListItem>();
        int n = jarray.length();
        for (int i =0; i< n; i++){
            WidgetListItem wi = new WidgetListItem();
            try {
                //Data contains 'formattedDate' which looks like 'Nov 17', so use that with start time
                JSONObject job = (JSONObject) jarray.get(i);
                //TagSaleEventObject(String id, String locationId, String address, String city, String state,
                // String zip, String ownerId, String date, String startTime,
                //                              String endTime, String description, String tags, double lat, double lon)
                TagSaleEventObject to = new TagSaleEventObject(
                        job.getString("id"),
                        job.getString("locationId"),
                        job.getString("address"),
                        job.getString("city"),
                        job.getString("state"),
                        job.getString("zip"),
                        job.getString("ownerId"),
                        job.getString("date"),
                        job.getString("startTime"),
                        job.getString("endTime"),
                        job.getString("description"),
                        job.getString("tags"),
                        job.getDouble("lat"),
                        job.getDouble("lon")
                        );
                wi.date = to.getFormattedDate();//job.getString("formattedDate");
                wi.time = to.getStartTime();// job.getString("startTime");
                wi.description = to.getLocationId();// job.getString("locationId");
            } catch(Exception ex){
                Log.d(TAG, "onDataSetChanged: INNERFORLOOP err i=:"+i+" : "+ex.getMessage());
            }
            Log.d(TAG, "onDataSetChanged: SETTING ITEM:"+wi.toString());
            itemList.add(wi);
        }

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_tagsale_row);
        WidgetListItem listItem = itemList.get(i);
        remoteView.setTextViewText(R.id.tv_date, listItem.date);
        remoteView.setTextViewText(R.id.tv_time, listItem.time);
        remoteView.setTextViewText(R.id.tv_desc, listItem.description);
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
