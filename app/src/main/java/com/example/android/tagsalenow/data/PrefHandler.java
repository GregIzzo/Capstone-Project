package com.example.android.tagsalenow.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONObject;

public class PrefHandler {
    static SharedPreferences tagSaleNowPref;
    private static String TAG = "PREFHANDLER";

    private static void readPrefs(Context context){
        if (tagSaleNowPref == null) tagSaleNowPref= context.getSharedPreferences("TagSaleNow", Context.MODE_PRIVATE);
    }
    public static void setTop3(Context context, String tslistString){
        readPrefs(context);
        // use Editor to update prefs
        SharedPreferences.Editor editor = tagSaleNowPref.edit();
        editor.putString("top3", tslistString);
        Log.d(TAG, "setTop3:SAVEPREFSTRING: "+tslistString);
        editor.commit();
    }
    public static String getTop3(Context context){
        readPrefs(context);//make sure we've read prefs
        String top3String = tagSaleNowPref.getString("top3","");
        Log.d(TAG, "setTop3:GETPREFSTRING: "+top3String);
        return top3String;
    }
}
