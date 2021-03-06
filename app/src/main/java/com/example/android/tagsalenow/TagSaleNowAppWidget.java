package com.example.android.tagsalenow;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class TagSaleNowAppWidget extends AppWidgetProvider {
    private static String TAG = "TAGSALENOWAPPWIDGET";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.d(TAG, "updateAppWidget: CALLED");
        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.tag_sale_now_app_widget);
        //Set the TagSaleNowRemoteViewsService intent to act as the adapter for the ListView

        Intent intent = new Intent(context, TagSaleNowRemoteViewsService.class);
        views.setRemoteAdapter(R.id.lv_tagsales, intent);

        Intent appIntent = new Intent(context, MainActivity.class);

        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.lv_tagsales, appPendingIntent);
        //Handle Empty Ingredient List
        views.setEmptyView(R.id.lv_tagsales, R.id.empty_view);
        views.setTextViewText(R.id.tv_desc, "PLACEHOLDER");///RecipeJSON.getCurrRecipeName());
      //  appWidgetManager.updateAppWidget(appWidgetId, views);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, appIntent, 0);
        views.setOnClickPendingIntent(R.id.widget_header, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);

/*
        //views.setTextViewText(R.id.appwidget_text, widgetText);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        views.setOnClickPendingIntent(R.id.appwidget_desktopimage,pendingIntent);
        // Instruct the widget manager to update the widget

        */
    }

    public static void updateTagSaleNowWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

