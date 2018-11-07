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

import com.example.android.tagsalenow.OnSiteObject;
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
/*
OnSite DB :
<TagSaleId> { <userid>:true, <userid>:true, ...}
OnSiteObject is: <userid>
 */
public class OnSiteViewModel extends ViewModel {
    private String TAG = "OnSiteViewModel";
    //private static final DatabaseReference ONSITE_REF =  FirebaseDatabase.getInstance().getReference("/onsite");
    private static  DatabaseReference ONSITE_REF;
    //private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(ONSITE_REF);
    private FirebaseQueryLiveData liveData;
    private final MediatorLiveData<OnSiteObject> mldOnSiteObjectLiveData = new MediatorLiveData<>();

    //private  final LiveData<List<OnSiteObject>> OnSiteObjectLiveData = Transformations.map(liveData,  new Deserializer());
    private  LiveData<List<OnSiteObject>> OnSiteObjectLiveData;

    public OnSiteViewModel() {
        // Set up the MediatorLiveData to convert DataSnapshot objects into OnSite objects
        Log.d(TAG, "OnSiteViewModel: Creator");
        if (ONSITE_REF == null){
            ONSITE_REF = FirebaseDatabase.getInstance().getReference("/onsite");
        }
        if (liveData == null){
            liveData = new FirebaseQueryLiveData(ONSITE_REF);
        }
        OnSiteObjectLiveData = Transformations.map(liveData,  new Deserializer());
        mldOnSiteObjectLiveData.addSource(liveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable final DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChanged: OnSitedata=" + dataSnapshot.toString());
                if (dataSnapshot != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mldOnSiteObjectLiveData.postValue(dataSnapshot.getValue(OnSiteObject.class));
                        }
                    }).start();
                } else {
                    mldOnSiteObjectLiveData.setValue(null);
                }
            }
        });
    }
    //Deserializer class - to serialize the DataSnapshot into OnSiteObject
    private class Deserializer implements Function<DataSnapshot, List<OnSiteObject>> {
        @Override
        public List<OnSiteObject> apply(DataSnapshot dataSnapshot) {
            Log.d("OnSiteViewModel", "dataSnapShot: " + dataSnapshot.toString());
            List<OnSiteObject> values = null;
            Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
            try {
                values = Utilities.MapToONSITE(td);
            } catch (Exception ex){
                Log.d("GG", "OnSiteViewModel Deserializer try-catcherr:" + ex.getMessage());
            }
            return values;
        }
    }

    @NonNull
    public LiveData<List<OnSiteObject>> getOnSiteObjectLiveData() {
        return OnSiteObjectLiveData;
    }

}


