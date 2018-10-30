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

import com.example.android.tagsalenow.FriendRequestObject;
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
public class FriendRequestViewModel extends ViewModel {
    //private static  DatabaseReference FRIENDREQUEST_REF=  FirebaseDatabase.getInstance().getReference("/friendrequest");
    private  DatabaseReference FRIENDREQUEST_REF;
    private String TAG = "FRIENDREQUESTVIEWMODEL";
    //private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(FRIENDREQUEST_REF);
    private  FirebaseQueryLiveData liveData;
    private  MediatorLiveData<FriendRequestObject> mldFriendRequestObjectLiveData = new MediatorLiveData<>();
    private  LiveData<List<FriendRequestObject>> friendRequestObjectLiveData;

    public FriendRequestViewModel() {
        // Set up the MediatorLiveData to convert DataSnapshot objects into FriendRelation objects
        if (FRIENDREQUEST_REF == null){
            FRIENDREQUEST_REF=  FirebaseDatabase.getInstance().getReference().child("friendrequest");
        }
        String currUserId = CurrentInfo.getCurrentUser().getUserId();
        Query queryRef = FRIENDREQUEST_REF.orderByChild("toUserId").startAt(currUserId).endAt(currUserId);
        if (liveData == null){
            liveData = new FirebaseQueryLiveData(queryRef);
        }
        Log.d(TAG, "FriendRequestViewModel: Query="+queryRef.toString());
        friendRequestObjectLiveData =  Transformations.map(liveData, new Deserializer());
        //Query queryRef = mDatabaseReference.orderByChild("email").startAt(friendEmail).endAt(friendEmail);
        Log.d(TAG, "FriendRequestViewModel: Creator. mldFriendRequestObjectLiveData=" + mldFriendRequestObjectLiveData.toString());
        mldFriendRequestObjectLiveData.addSource(liveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable final DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChanged: FriendRequestdata=" + dataSnapshot.toString());
                if (dataSnapshot != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mldFriendRequestObjectLiveData.postValue(dataSnapshot.getValue(FriendRequestObject.class));
                        }
                    }).start();
                } else {
                    mldFriendRequestObjectLiveData.setValue(null);
                }
            }
        });
    }
    //private  final LiveData<List<FriendRequestObject>> friendRequestObjectLiveData = Transformations.map(liveData, new Deserializer());
    //Deserializer class - to serialize the DataSnapshot into TagSaleEventObject
    private class Deserializer implements Function<DataSnapshot, List<FriendRequestObject>> {
        @Override
        public List<FriendRequestObject> apply(DataSnapshot dataSnapshot) {
            Log.d("FRIENDREQUESTVIEWMODEL", "dataSnapShot: " + dataSnapshot.toString());
            List<FriendRequestObject> values = null;
            String userId = (String) dataSnapshot.getKey();
            Map<String, Object> onf = (Map<String,Object>) dataSnapshot.getValue();
            try {
                values = Utilities.MapToFR(onf);
            } catch (Exception ex){
                Log.d("GG", "FriendRequestViewModel Deserializer try-catcherr:" + ex.getMessage());
            }
            return values;
        }
    }

    @NonNull
    public LiveData<List<FriendRequestObject>> getFriendRequestObjectLiveData() {
        return friendRequestObjectLiveData;
    }

}
