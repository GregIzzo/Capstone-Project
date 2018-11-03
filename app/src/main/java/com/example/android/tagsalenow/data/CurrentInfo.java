package com.example.android.tagsalenow.data;

import android.location.Location;
import android.util.Log;

import com.example.android.tagsalenow.TagSaleEventObject;
import com.example.android.tagsalenow.TagSaleReviewObject;
import com.example.android.tagsalenow.TagSaleUserObject;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.HashMap;
import java.util.Map;

/*
* Store static (current) information, such as 'currently selected Tag Sale' or 'Currently selected Friend'
 */
public  class CurrentInfo {
    private static Location currentLocationObject;

    private static String currentTagSaleID = "";
    private static String currentFriendID = "";
    private static TagSaleEventObject currentTagSaleEventObject;
    private static TagSaleUserObject currentUser;

    private static Map<String,TagSaleUserObject> tsUsersMap;
    private static Map<String,TagSaleEventObject> tsEventsMap;
    private static Map<String,TagSaleReviewObject> tsReviewsMap;
    //private static Map<String,Friend>

    //Device current location is stored in currentLocationObject
    public static Location getCurrentLocationObject(){ return currentLocationObject;}
    public static void setCurrentLocationObject(Location inCurrentLocationObject) { currentLocationObject = inCurrentLocationObject;}

    public static String getCurrentTagSaleID(){ return currentTagSaleID;}
    public static void setCurrentTagSaleID(String incomingTagSaleID){ currentTagSaleID = incomingTagSaleID;}

    public static String getCurrentFriendID(){ return currentFriendID;}
    public static void setCurrentFriendID(String incomingFriendID) {currentFriendID = incomingFriendID;}


    public static TagSaleEventObject getCurrentTagSaleEventObject(){ return currentTagSaleEventObject;}
    public static void setCurrentTagSaleEventObject(TagSaleEventObject tagSaleEventObject) {
        currentTagSaleEventObject = tagSaleEventObject;
       setCurrentTagSaleID(tagSaleEventObject.getId());
    }

    public static TagSaleUserObject getCurrentUser(){ return currentUser;}
    public static void setCurrentUser(TagSaleUserObject tagSaleUserObject) { currentUser = tagSaleUserObject;}

    public static TagSaleUserObject getUserByKey(String key) {
        if (tsUsersMap == null) return null;
        return tsUsersMap.get(key);
    }
    public static void addUser(TagSaleUserObject user){
        if (tsUsersMap == null){
            tsUsersMap = new HashMap<String, TagSaleUserObject>();
        }
        Log.d("CURRENTINFO", "addUser: object is"+user.getDisplayName());
        tsUsersMap.put(user.getUserId(), user);
    }

    public static TagSaleEventObject getEventByKey(String key){
        if (tsEventsMap == null) return null;
        return tsEventsMap.get(key);
    }

    public static void addEvent(TagSaleEventObject eventObject){
        if (tsEventsMap == null){
            tsEventsMap = new HashMap<String, TagSaleEventObject>();
        }
        tsEventsMap.put(eventObject.getId(), eventObject);
    }

    public static TagSaleReviewObject getReviewByKey(String key){
        if (tsReviewsMap == null) return null;
        return tsReviewsMap.get(key);
    }
    public static void addReview(TagSaleReviewObject reviewObject){
        if (tsReviewsMap == null){
            tsReviewsMap = new HashMap<String, TagSaleReviewObject>();
        }
        tsReviewsMap.put(reviewObject.getId(), reviewObject);
    }
}
