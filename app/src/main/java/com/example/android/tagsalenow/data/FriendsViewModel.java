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

import com.example.android.tagsalenow.FriendRelationObject;
import com.example.android.tagsalenow.TagSaleEventObject;
import com.example.android.tagsalenow.utils.Utilities;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    This class was inspired by a similar class from Doug Sevenson in his "The Firebase Blog" :
        "Using Android Architecture Components with Firebase Realtime Database (Part 1)
    From: https://firebase.googleblog.com/2017/12/using-android-architecture-components.html
 */
public class FriendsViewModel extends ViewModel {
    private static final DatabaseReference FRIENDS_REF =  FirebaseDatabase.getInstance().getReference("/friends");
    private String TAG = "FRIENDSVIEWMODEL";
    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(FRIENDS_REF);
    private final MediatorLiveData<FriendRelationObject> mldFriendRelationObjectLiveData = new MediatorLiveData<>();

    public FriendsViewModel() {
        // Set up the MediatorLiveData to convert DataSnapshot objects into FriendRelation objects
        Log.d(TAG, "FriendsViewModel: Creator");
        mldFriendRelationObjectLiveData.addSource(liveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable final DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChanged: Friendsdata=" + dataSnapshot.toString());
                if (dataSnapshot != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mldFriendRelationObjectLiveData.postValue(dataSnapshot.getValue(FriendRelationObject.class));
                        }
                    }).start();
                } else {
                    mldFriendRelationObjectLiveData.setValue(null);
                }
            }
        });
    }
    private  final LiveData<List<FriendRelationObject>> friendRelationObjectLiveData = Transformations.map(liveData, new Deserializer());
    //Deserializer class - to serialize the DataSnapshot into TagSaleEventObject
    private class Deserializer implements Function<DataSnapshot, List<FriendRelationObject>> {
        @Override
        public List<FriendRelationObject> apply(DataSnapshot dataSnapshot) {
            Log.d("FRIENDSVIEWMODEL", "dataSnapShot: " + dataSnapshot.toString());
            List<FriendRelationObject> values = null;
            Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
            try {
                values = Utilities.MapToFRO(td);
            } catch (Exception ex){
                Log.d("GG", "FriendsViewModel Deserializer try-catcherr:" + ex.getMessage());
            }
            return values;
        }
    }

    @NonNull
    public LiveData<List<FriendRelationObject>> getFriendRelationObjectLiveData() {
        return friendRelationObjectLiveData;
    }

}
