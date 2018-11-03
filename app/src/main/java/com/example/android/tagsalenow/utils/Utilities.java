package com.example.android.tagsalenow.utils;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.tagsalenow.AttendingObject;
import com.example.android.tagsalenow.FriendRequestObject;
import com.example.android.tagsalenow.Friends;
import com.example.android.tagsalenow.OneFriend;
import com.example.android.tagsalenow.TagSaleEventObject;
import com.example.android.tagsalenow.TagSaleReviewObject;
import com.example.android.tagsalenow.TagSaleUserObject;
import com.example.android.tagsalenow.data.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utilities {

    private final static String TAG = "UTILITIES";
    public static List<TagSaleEventObject> MapToTSEO(Map<String, Object> map){
        //expect map to be: {"key": {"key":value, "key":value...}}
        Log.d(TAG, "MapToTSEO: INPUT:"+map.toString());

        List<TagSaleEventObject> outlist =  new ArrayList<>();
        Log.d(TAG, "MapToTSEO: listofkeys:"+map.keySet().toString());
        List<Object> listOfObjects =  new ArrayList(map.values());//Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
        Log.d(TAG, "MapToTSEO: listOfObjects:"+listOfObjects.toString());

        //Map<String, Object> interm = (Map<String,Object>)SingleValue;
        //Log.d(TAG, "MapToTSEO: interm:"+interm.toString());
        //List<Object> mapObjects = (List<Object>) interm.values(); //[ {K:V, K:V,..}, {K:V, ...}]
        int count = listOfObjects.size();
        Log.d(TAG, "MapToTSEO: count("+count+") ");

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
                        innermap.get("tags").toString(),
                        (double) innermap.get("lat"),
                        (double) innermap.get("lon")
                );
                outlist.add(to);

            } catch (Exception ex){
                Log.d(TAG, "MapToTSEO: error converting to TagSaleEventObject:" + ex.getMessage());
            }
        }
        return outlist;

    }
    public static List<TagSaleUserObject> MapToTU(Map<String, Object> map){
        //expect map to be: {"key": {"key":value, "key":value...}}
        List<TagSaleUserObject> outlist =  new ArrayList<>();
        List<Object> listOfObjects =  new ArrayList(map.values());//Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
        int count = listOfObjects.size();
        for (int i =0;i<count ;i++){
            Map<String, Object> innermap = (Map<String, Object>) listOfObjects.get(i);
            try {
                //TagSaleUserObject(String userId, String joinDate, String displayName, String email, String photoUrl)
                TagSaleUserObject tu = new TagSaleUserObject(
                        innermap.get("userId").toString(),
                        innermap.get("joinDate").toString(),
                        innermap.get("displayName").toString(),
                        innermap.get("email").toString(),
                        innermap.get("photoUrl").toString());
                outlist.add(tu);

            } catch (Exception ex){
                Log.d(TAG, "MapToTU: error converting to TagSaleUserObject:" + ex.getMessage());
            }
        }
        return outlist;

    }

    public static List<Friends> MapToFRO(Map<String, Object> map){
        //expect map to be:  {"userId":{"FriendID":TRUE, "FriendID":TRUE...}}
        Log.d(TAG, "MapToFRO: INPUT:"+map.toString());

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
                Log.d(TAG, "MapToFRO: ERROR converting to array:"+ex.getMessage());
            }
           if(innermap != null) {
               ArrayList<String> friendIds =null;
                try {
                     friendIds = new ArrayList<String>(Arrays.asList(keyArray));//all the friend ids
                } catch (Exception ex){
                    Log.d(TAG, "MapToFRO: ERROR2 converting to ArrayList:"+ex.getMessage());
                }

               //(String locationId, String ownerId, String date, String startTime, String endTime, String description, String tags)
               try {
                   Friends fro = new Friends(userId,
                           friendIds);
                   outlist.add(fro);
                   Log.d(TAG, "MapToFRO: ADDED: userId=" + userId + " friends[" + friendIds.toString() + "]");
               } catch (Exception ex) {
                   Log.d(TAG, "MapToFRO: error converting to Friends:" + ex.getMessage());
               }
           }
        }
        return outlist;

    }
    public static List<OneFriend> MapToONEF(Map<String, Object> map){
        //expect map to be:  {"userId":{"FriendID":TRUE, "FriendID":TRUE...}}
        Log.d(TAG, "MapToONEF: INPUT:"+map.toString());

        List<OneFriend> outlist =  new ArrayList<>();
        List<String> listOfKeys = new ArrayList<String>(map.keySet());
        int count = listOfKeys.size();

        for (int i =0;i<count ;i++){
            String userId = listOfKeys.get(i);
                try {
                    OneFriend fro = new OneFriend(userId);
                    outlist.add(fro);
                    Log.d(TAG, "MapToONEF: ADDED: userId=" + userId );
                } catch (Exception ex) {
                    Log.d(TAG, "MapToONEF: error converting to OneFriend:" + ex.getMessage());
                }
        }
        return outlist;

    }
    public static List<FriendRequestObject> MapToFR(Map<String, Object> map){
        //expect map to be:  {"userId":{"FromUserId":STRING, "ToUserId":String...}}
        List<FriendRequestObject> outlist =  new ArrayList<>();
        Log.d(TAG, "MapToFR: listofkeys:"+map.keySet().toString());
        List<Object> listOfObjects =  new ArrayList(map.values());//Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
        Log.d(TAG, "MapToFR: listOfObjects:"+listOfObjects.toString());
        int count = listOfObjects.size();
        for (int i =0;i<count ;i++){
            Map<String, Object> innermap = (Map<String, Object>) listOfObjects.get(i);
            Log.d(TAG, "MapToFR: innermap="+innermap.toString());
            try {
                FriendRequestObject fro = new FriendRequestObject(
                        innermap.get("fromUserId").toString(),
                        innermap.get("toUserId").toString(),
                        innermap.get("friendemail").toString(),
                        innermap.get("dbkey").toString()
                );
                outlist.add(fro);
                Log.d(TAG, "MapToFR: ADDED: fromuserId=" + fro.getFromUserId()+" touserid="+fro.getToUserId() );
            } catch (Exception ex) {
                Log.d(TAG, "MapToFR: error converting to OneFriend:" + ex.getMessage());
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
        Log.d(TAG, "MapToREVIEWO: count("+count+") ");

        for (int i =0;i<count ;i++){
            Map<String, Object> innermap = (Map<String, Object>) listOfObjects.get(i);
            Log.d(TAG, "MapToREVIEWO: DATA IS: "+listOfObjects.get(i).toString());
            //(String locationId, String ownerId, String date, String startTime, String endTime, String description, String tags)
            try {
                TagSaleReviewObject to = new TagSaleReviewObject(
                        innermap.get("id").toString(),
                        innermap.get("tagSaleID").toString(),
                        innermap.get("reviewerID").toString(),
                        innermap.get("description").toString(),
                        ((Long) innermap.get("fiveStarRating")).intValue() );
                outlist.add(to);
                Log.d(TAG, "MapToREVIEWO: ADDED:"+innermap.get("tagSaleID").toString());
            } catch (Exception ex){
                Log.d(TAG, "MapToREVIEWO: error converting to Friends:" + ex.getMessage());
            }
        }
        return outlist;

    }
    public static List<AttendingObject> MapToATTENDING(Map<String, Object> map){
        //expect map to be: {"key": {"key":value, "key":value...}}

        List<AttendingObject> outlist =  new ArrayList<>();
        List<String> listOfKeys =  new ArrayList<String>(map.keySet());//Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
        int count = listOfKeys.size();

        for (int i =0;i<count ;i++){
            String userId = listOfKeys.get(i);
            try {
                AttendingObject atto = new AttendingObject(userId);
                outlist.add(atto);
                Log.d(TAG, "MapToATTENDING: ADDED:"+userId);
            } catch (Exception ex){
                Log.d(TAG, "MapToATTENDING: error converting to AttendingObject:" + ex.getMessage());
            }
        }
        return outlist;

    }
    /*
    The method below came from a post on StackOverflow.com:
    https://stackoverflow.com/questions/3574644/how-can-i-find-the-latitude-and-longitude-from-address
     */

    public static LatLng getLocationFromAddress(Context context, String strAddress) {
        Log.d(TAG, "getLocationFromAddress: INPUT["+strAddress+"]");
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            Log.d(TAG, "getLocationFromAddress: GOT LOCATION:"+location.toString());
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            Log.d(TAG, "getLocationFromAddress:ERRROR input("+strAddress+") message="+ex.getMessage());
        }

        return p1;
    }
    public static void setTagSaleLocationData(final Context context, final String TagSaleEventId, final String strAddress){
        new AsyncTask<Void, Void, List<Address>>() {
            @Override
            protected List<Address> doInBackground(Void... voids) {
                Geocoder geo = new Geocoder(context);
                List<Address> listAddresses = null;
                try {
                    listAddresses = geo.getFromLocationName(strAddress, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return listAddresses;
            }
            public void onPostExecute(List<Address> listAddresses) {
                Address foundAddress = null;
                if ((listAddresses != null) && (listAddresses.size() > 0)) {
                    foundAddress = listAddresses.get(0);
                   if( foundAddress.hasLatitude()){
                       double myLat = foundAddress.getLatitude();
                       if (foundAddress.hasLongitude()){
                           double myLon = foundAddress.getLongitude();
                           //GOT BOTH, so Update the TagSaleEvent record with this new info
                           DatabaseReference mTagSaleEventsDatabaseReference;
                           mTagSaleEventsDatabaseReference =  FirebaseDatabase.getInstance().getReference();

                           Map<String, Object> childUpdates = new HashMap<>();
                           childUpdates.put("/tagsaleevents/" +
                                   TagSaleEventId +"/"+
                                   "lat", myLat);
                           childUpdates.put("/tagsaleevents/" +
                                   TagSaleEventId +"/"+
                                   "lon", myLon);
                           Log.d(TAG, "SETGEOPOSITION onPostExecute: Updates="+childUpdates.toString());
                           mTagSaleEventsDatabaseReference.updateChildren(childUpdates);
                        }
                   }
                }
            }
        }.execute();
    }

}
