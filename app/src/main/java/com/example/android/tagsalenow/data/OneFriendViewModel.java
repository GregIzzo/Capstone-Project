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

import com.example.android.tagsalenow.OneFriend;
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
public class OneFriendViewModel extends ViewModel {
    //private static  DatabaseReference ONEFRIEND_REF=  FirebaseDatabase.getInstance().getReference("/friends");
    private  DatabaseReference ONEFRIEND_REF;
    private String TAG = "ONEFRIENDVIEWMODEL";
    // private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(ONEFRIEND_REF);
    private  FirebaseQueryLiveData liveData;
    private final MediatorLiveData<OneFriend> mldOneFriendObjectLiveData = new MediatorLiveData<>();
    private   LiveData<List<OneFriend>> onefriendObjectLiveData;//= Transformations.map(liveData, new Deserializer());

    public OneFriendViewModel() {

        if (ONEFRIEND_REF == null){
            ONEFRIEND_REF =  FirebaseDatabase.getInstance().getReference("/friends/"+ CurrentInfo.getCurrentUser().getUserId());
            Log.d(TAG, "OneFriendViewModel: ONEFRIEND_REF=["+ONEFRIEND_REF+"]");
        }
        if (liveData == null){
            liveData = new FirebaseQueryLiveData(ONEFRIEND_REF);
            Log.d(TAG, "OneFriendViewModel: liveData ="+liveData.toString());
        }

        onefriendObjectLiveData = Transformations.map(liveData, new Deserializer());

        // Set up the MediatorLiveData to convert DataSnapshot objects into FriendRelation objects
        Log.d(TAG, "OneFriendViewModel: Creator. mldOneFriendObjectLiveData=" + mldOneFriendObjectLiveData.toString());
        mldOneFriendObjectLiveData.addSource(liveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable final DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChanged: OneFrienddata=" + dataSnapshot.toString());
                if (dataSnapshot != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mldOneFriendObjectLiveData.postValue(dataSnapshot.getValue(OneFriend.class));
                        }
                    }).start();
                } else {
                    mldOneFriendObjectLiveData.setValue(null);
                }
            }
        });
    }
    //   private  final LiveData<List<Friends>> onefriendObjectLiveData = Transformations.map(liveData, new Deserializer());
    //Deserializer class - to serialize the DataSnapshot into TagSaleEventObject
    private class Deserializer implements Function<DataSnapshot, List<OneFriend>> {
        @Override
        public List<OneFriend> apply(DataSnapshot dataSnapshot) {
            Log.d("ONEFRIENDVIEWMODEL", "dataSnapShot: " + dataSnapshot.toString());
            List<OneFriend> values = null;
            String userId = dataSnapshot.getKey();
            Map<String, Object> onf = (Map<String,Object>) dataSnapshot.getValue();//{<userid>:{<friendId>:TRUE, <friendId>:TRUE,...}
            try {
                values = Utilities.MapToONEF(onf);
            } catch (Exception ex){
                Log.d("GG", "OneFriendViewModel Deserializer try-catcherr:" + ex.getMessage());
            }
            return values;
        }
    }

    @NonNull
    public LiveData<List<OneFriend>> getOneFriendObjectLiveData() {
        return onefriendObjectLiveData;
    }

}
