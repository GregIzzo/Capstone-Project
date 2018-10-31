package com.example.android.tagsalenow.utils;

import android.util.Log;

import com.example.android.tagsalenow.AttendingObject;
import com.example.android.tagsalenow.FriendRequestObject;
import com.example.android.tagsalenow.Friends;
import com.example.android.tagsalenow.OneFriend;
import com.example.android.tagsalenow.TagSaleEventObject;
import com.example.android.tagsalenow.TagSaleReviewObject;

import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Utilities {

    public static List<TagSaleEventObject> MapToTSEO(Map<String, Object> map){
        //expect map to be: {"key": {"key":value, "key":value...}}
        Log.d("UTILITIES", "MapToTSEO: INPUT:"+map.toString());

        List<TagSaleEventObject> outlist =  new ArrayList<>();
        Log.d("UTILITIES", "MapToTSEO: listofkeys:"+map.keySet().toString());
        List<Object> listOfObjects =  new ArrayList(map.values());//Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
        Log.d("UTILITIES", "MapToTSEO: listOfObjects:"+listOfObjects.toString());

        //Map<String, Object> interm = (Map<String,Object>)SingleValue;
        //Log.d("UTILITIES", "MapToTSEO: interm:"+interm.toString());
        //List<Object> mapObjects = (List<Object>) interm.values(); //[ {K:V, K:V,..}, {K:V, ...}]
        int count = listOfObjects.size();
        Log.d("UTILITIES", "MapToTSEO: count("+count+") ");

        for (int i =0;i<count ;i++){
            Map<String, Object> innermap = (Map<String, Object>) listOfObjects.get(i);
            //String key = (String) listOfObjects.
            //(String locationId, String ownerId, String date, String startTime, String endTime, String description, String tags)
            try {
                TagSaleEventObject to = new TagSaleEventObject(
                        innermap.get("id").toString(),
                        innermap.get("locationId").toString(),
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

            } catch (Exception ex){
                Log.d("UTIL", "MapToTSEO: error converting to TagSaleEventObject:" + ex.getMessage());
            }
        }
        return outlist;

    }
    public static List<Friends> MapToFRO(Map<String, Object> map){
        //expect map to be:  {"userId":{"FriendID":TRUE, "FriendID":TRUE...}}
        Log.d("UTILITIES", "MapToFRO: INPUT:"+map.toString());

        List<Friends> outlist =  new ArrayList<>();
        List<String> listOfKeys = new ArrayList<String>(map.keySet());
        List<Object> listOfObjects =  new ArrayList(map.values());//Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
        int count = listOfObjects.size();

        for (int i =0;i<count ;i++){
            String userId = listOfKeys.get(i);
            Map<String, Object> innermap = (Map<String, Object>) listOfObjects.get(i);
            String[] keyArray = null;
            try {
                keyArray = innermap.keySet().toArray(new String[0]);
            } catch (Exception ex){
                Log.d("UTILITIES", "MapToFRO: ERROR converting to array:"+ex.getMessage());
            }
           if(innermap != null) {
               ArrayList<String> friendIds =null;
                try {
                     friendIds = new ArrayList<String>(Arrays.asList(keyArray));//all the friend ids
                } catch (Exception ex){
                    Log.d("UTILITIES", "MapToFRO: ERROR2 converting to ArrayList:"+ex.getMessage());
                }

               //(String locationId, String ownerId, String date, String startTime, String endTime, String description, String tags)
               try {
                   Friends fro = new Friends(userId,
                           friendIds);
                   outlist.add(fro);
                   Log.d("UTILITIES", "MapToFRO: ADDED: userId=" + userId + " friends[" + friendIds.toString() + "]");
               } catch (Exception ex) {
                   Log.d("UTIL", "MapToFRO: error converting to Friends:" + ex.getMessage());
               }
           }
        }
        return outlist;

    }
    public static List<OneFriend> MapToONEF(Map<String, Object> map){
        //expect map to be:  {"userId":{"FriendID":TRUE, "FriendID":TRUE...}}
        Log.d("UTILITIES", "MapToONEF: INPUT:"+map.toString());

        List<OneFriend> outlist =  new ArrayList<>();
        List<String> listOfKeys = new ArrayList<String>(map.keySet());
        int count = listOfKeys.size();

        for (int i =0;i<count ;i++){
            String userId = listOfKeys.get(i);

                //(String locationId, String ownerId, String date, String startTime, String endTime, String description, String tags)
                try {
                    OneFriend fro = new OneFriend(userId);
                    outlist.add(fro);
                    Log.d("UTILITIES", "MapToONEF: ADDED: userId=" + userId );
                } catch (Exception ex) {
                    Log.d("UTIL", "MapToONEF: error converting to OneFriend:" + ex.getMessage());
                }

        }
        return outlist;

    }
    public static List<FriendRequestObject> MapToFR(Map<String, Object> map){
        //expect map to be:  {"userId":{"FromUserId":STRING, "ToUserId":String...}}
        List<FriendRequestObject> outlist =  new ArrayList<>();
        Log.d("UTILITIES", "MapToFR: listofkeys:"+map.keySet().toString());
        List<Object> listOfObjects =  new ArrayList(map.values());//Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
        Log.d("UTILITIES", "MapToFR: listOfObjects:"+listOfObjects.toString());
        int count = listOfObjects.size();
        for (int i =0;i<count ;i++){
            Map<String, Object> innermap = (Map<String, Object>) listOfObjects.get(i);
            Log.d("UTILITIES", "MapToFR: innermap="+innermap.toString());
            try {
                FriendRequestObject fro = new FriendRequestObject(
                        innermap.get("fromUserId").toString(),
                        innermap.get("toUserId").toString(),
                        innermap.get("friendemail").toString(),
                        innermap.get("dbkey").toString()
                );
                outlist.add(fro);
                Log.d("UTILITIES", "MapToFR: ADDED: fromuserId=" + fro.getFromUserId()+" touserid="+fro.getToUserId() );
            } catch (Exception ex) {
                Log.d("UTIL", "MapToFR: error converting to OneFriend:" + ex.getMessage());
            }
        }
        return outlist;
    }
    public static List<TagSaleReviewObject> MapToREVIEWO(Map<String, Object> map){
        //expect map to be: {"key": {"key":value, "key":value...}}
       // private String tagSaleID;
       // private String reviewerID;
       // private String description;
       // private int fiveStarRating;

        List<TagSaleReviewObject> outlist =  new ArrayList<>();
        List<Object> listOfObjects =  new ArrayList(map.values());//Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
        int count = listOfObjects.size();
        Log.d("UTILITIES", "MapToREVIEWO: count("+count+") ");

        for (int i =0;i<count ;i++){
            Map<String, Object> innermap = (Map<String, Object>) listOfObjects.get(i);
            Log.d("UTILITIES", "MapToREVIEWO: DATA IS: "+listOfObjects.get(i).toString());
            //(String locationId, String ownerId, String date, String startTime, String endTime, String description, String tags)
            try {
                TagSaleReviewObject to = new TagSaleReviewObject(
                        innermap.get("id").toString(),
                        innermap.get("tagSaleID").toString(),
                        innermap.get("reviewerID").toString(),
                        innermap.get("description").toString(),
                        ((Long) innermap.get("fiveStarRating")).intValue() );
                outlist.add(to);
                Log.d("UTILITIES", "MapToREVIEWO: ADDED:"+innermap.get("tagSaleID").toString());
            } catch (Exception ex){
                Log.d("UTILITIES", "MapToREVIEWO: error converting to Friends:" + ex.getMessage());
            }
        }
        return outlist;

    }
    public static List<AttendingObject> MapToATTENDING(Map<String, Object> map){
        //expect map to be: {"key": {"key":value, "key":value...}}

        List<AttendingObject> outlist =  new ArrayList<>();
        List<Object> listOfObjects =  new ArrayList(map.values());//Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
        int count = listOfObjects.size();
        Log.d("UTILITIES", "MapToATTENDING: count("+count+") ");

        for (int i =0;i<count ;i++){
            Map<String, Object> innermap = (Map<String, Object>) listOfObjects.get(i);
            Log.d("UTILITIES", "MapToATTENDING: DATA IS: "+listOfObjects.get(i).toString());
            //(String locationId, String ownerId, String date, String startTime, String endTime, String description, String tags)
            try {
                AttendingObject atto = new AttendingObject(
                        innermap.get("id").toString(),
                        innermap.get("userId").toString(),
                        innermap.get("eventId").toString() );
                outlist.add(atto);
                Log.d("UTILITIES", "MapToATTENDING: ADDED:"+innermap.get("id").toString());
            } catch (Exception ex){
                Log.d("UTILITIES", "MapToATTENDING: error converting to AttendingObject:" + ex.getMessage());
            }
        }
        return outlist;

    }

}
