package com.example.android.tagsalenow.data;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.tagsalenow.Friends;
import com.example.android.tagsalenow.utils.Utilities;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    This class was inspired by a similar class from Doug Sevenson in his "The Firebase Blog" :
        "Using Android Architecture Components with Firebase Realtime Database (Part 1)
    From: https://firebase.googleblog.com/2017/12/using-android-architecture-components.html
 */
public class FriendsViewModel extends ViewModel {
    //private static  DatabaseReference FRIENDS_REF =  FirebaseDatabase.getInstance().getReference("/friends");
    private  DatabaseReference FRIENDS_REF;
    private String TAG = "FRIENDSVIEWMODEL";
    //private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(FRIENDS_REF);
    private  FirebaseQueryLiveData liveData;
    private final MediatorLiveData<Friends> mldFriendsObjectLiveData = new MediatorLiveData<>();

    private   LiveData<List<Friends>> friendsObjectLiveData;
    //private   LiveData<List<Friends>> friendsObjectLiveData= Transformations.map(liveData, new Deserializer());

    public FriendsViewModel() {

        if (FRIENDS_REF == null){
            FRIENDS_REF =  FirebaseDatabase.getInstance().getReference("/friends/"+ CurrentInfo.getCurrentUser().getUserId());
            Log.d(TAG, "FriendsViewModel: FRIENDS_REF=["+FRIENDS_REF+"]");
        }
        if (liveData == null){
            liveData = new FirebaseQueryLiveData(FRIENDS_REF);
            Log.d(TAG, "FriendsViewModel: liveData ="+liveData.toString());
        }
       
        friendsObjectLiveData = Transformations.map(liveData, new Deserializer());

        // Set up the MediatorLiveData to convert DataSnapshot objects into FriendRelation objects
        Log.d(TAG, "FriendsViewModel: Creator. mldFriendsObjectLiveData=" + mldFriendsObjectLiveData.toString());
        mldFriendsObjectLiveData.addSource(liveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable final DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChanged: Friendsdata=" + dataSnapshot.toString());
                if (dataSnapshot != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mldFriendsObjectLiveData.postValue(dataSnapshot.getValue(Friends.class));
                        }
                    }).start();
                } else {
                    mldFriendsObjectLiveData.setValue(null);
                }
            }
        });
    }
 //   private  final LiveData<List<Friends>> friendsObjectLiveData = Transformations.map(liveData, new Deserializer());
    //Deserializer class - to serialize the DataSnapshot into TagSaleEventObject
    private class Deserializer implements Function<DataSnapshot, List<Friends>> {
        @Override
        public List<Friends> apply(DataSnapshot dataSnapshot) {
            Log.d("FRIENDSVIEWMODEL", "dataSnapShot: " + dataSnapshot.toString());
            List<Friends> values = null;
            String userId = (String) dataSnapshot.getKey();
            Map<String, Object> td = (Map<String,Object>) dataSnapshot.getValue();//{<userid>:{<friendId>:TRUE, <friendId>:TRUE,...}
            try {
                values = Utilities.MapToFRO(td);
            } catch (Exception ex){
                Log.d("GG", "FriendsViewModel Deserializer try-catcherr:" + ex.getMessage());
            }
            return values;
        }
    }

    @NonNull
    public LiveData<List<Friends>> getFriendsObjectLiveData() {
        return friendsObjectLiveData;
    }

}
