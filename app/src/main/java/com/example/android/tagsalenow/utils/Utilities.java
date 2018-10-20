package com.example.android.tagsalenow.utils;

import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.tagsalenow.FriendRelationObject;
import com.example.android.tagsalenow.TagSaleEventObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class Utilities {

    public static List<TagSaleEventObject> MapToTSEO(Map<String, Object> map){
        //expect map to be: {"key": {"key":value, "key":value...}}
        Log.d("UTILITIES", "MapToTSEO: INPUT:"+map.toString());

        List<TagSaleEventObject> outlist =  new ArrayList<>();
        List<Object> listOfObjects =  new ArrayList(map.values());//Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
        Log.d("UTILITIES", "MapToTSEO: listOfObjects:"+listOfObjects.toString());

        //Map<String, Object> interm = (Map<String,Object>)SingleValue;
        //Log.d("UTILITIES", "MapToTSEO: interm:"+interm.toString());
        //List<Object> mapObjects = (List<Object>) interm.values(); //[ {K:V, K:V,..}, {K:V, ...}]
        int count = listOfObjects.size();
        Log.d("UTILITIES", "MapToTSEO: count("+count+") ");

        for (int i =0;i<count ;i++){
            Map<String, Object> innermap = (Map<String, Object>) listOfObjects.get(i);
            //(String locationId, String ownerId, String date, String startTime, String endTime, String description, String tags)
            try {
                TagSaleEventObject to = new TagSaleEventObject(innermap.get("locationId").toString(),
                        innermap.get("address").toString(),
                        innermap.get("city").toString(),
                        innermap.get("state").toString(),
                        innermap.get("zip").toString(),
                        innermap.get("ownerId").toString(),
                        innermap.get("date").toString(),
                        innermap.get("startTime").toString(),
                        innermap.get("endTime").toString(),
                        innermap.get("description").toString(),
                        innermap.get("tags").toString());
                outlist.add(to);
                Log.d("UTILITIES", "MapToTSEO: ADDED:"+innermap.get("locationId").toString());
            } catch (Exception ex){
                Log.d("UTIL", "MapToTSEO: error converting to TagSaleEventObject:" + ex.getMessage());
            }
        }
        return outlist;

    }
    public static List<FriendRelationObject> MapToFRO(Map<String, Object> map){
        //expect map to be: {"key": {"key":value, "key":value...}}
        Log.d("UTILITIES", "MapToFRO: INPUT:"+map.toString());

        List<FriendRelationObject> outlist =  new ArrayList<>();
        List<Object> listOfObjects =  new ArrayList(map.values());//Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
        Log.d("UTILITIES", "MapToFRO: listOfObjects:"+listOfObjects.toString());

        //Map<String, Object> interm = (Map<String,Object>)SingleValue;
        //Log.d("UTILITIES", "MapToFRO: interm:"+interm.toString());
        //List<Object> mapObjects = (List<Object>) interm.values(); //[ {K:V, K:V,..}, {K:V, ...}]
        int count = listOfObjects.size();
        Log.d("UTILITIES", "MapToFRO: count("+count+") ");

        for (int i =0;i<count ;i++){
            Map<String, Object> innermap = (Map<String, Object>) listOfObjects.get(i);
            //(String locationId, String ownerId, String date, String startTime, String endTime, String description, String tags)
            try {
                FriendRelationObject to = new FriendRelationObject(innermap.get("friendId1").toString(),
                        innermap.get("friendId2").toString());
                outlist.add(to);
                Log.d("UTILITIES", "MapToFRO: ADDED:"+innermap.get("locationId").toString());
            } catch (Exception ex){
                Log.d("UTIL", "MapToFRO: error converting to FriendRelationObject:" + ex.getMessage());
            }
        }
        return outlist;

    }
}
