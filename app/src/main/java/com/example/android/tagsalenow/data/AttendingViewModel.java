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

import com.example.android.tagsalenow.AttendingObject;
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
public class AttendingViewModel extends ViewModel {
    private static final DatabaseReference ATTENDING_REF =  FirebaseDatabase.getInstance().getReference("/attending");
    private String TAG = "AttendingViewModel";
    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(ATTENDING_REF);
    private final MediatorLiveData<AttendingObject> mldAttendingObjectLiveData = new MediatorLiveData<>();

    public AttendingViewModel() {
        // Set up the MediatorLiveData to convert DataSnapshot objects into Attending objects
        Log.d(TAG, "AttendingViewModel: Creator");
        mldAttendingObjectLiveData.addSource(liveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable final DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChanged: Attendingdata=" + dataSnapshot.toString());
                if (dataSnapshot != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mldAttendingObjectLiveData.postValue(dataSnapshot.getValue(AttendingObject.class));
                        }
                    }).start();
                } else {
                    mldAttendingObjectLiveData.setValue(null);
                }
            }
        });
    }
    private  final LiveData<List<AttendingObject>> AttendingObjectLiveData = Transformations.map(liveData,  new Deserializer());
    //Deserializer class - to serialize the DataSnapshot into AttendingObject
    private class Deserializer implements Function<DataSnapshot, List<AttendingObject>> {
        @Override
        public List<AttendingObject> apply(DataSnapshot dataSnapshot) {
            Log.d("AttendingViewModel", "dataSnapShot: " + dataSnapshot.toString());
            List<AttendingObject> values = null;
            Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
            try {
                values = Utilities.MapToATTENDING(td);
            } catch (Exception ex){
                Log.d("GG", "AttendingViewModel Deserializer try-catcherr:" + ex.getMessage());
            }
            return values;
        }
    }

    @NonNull
    public LiveData<List<AttendingObject>> getAttendingObjectLiveData() {
        return AttendingObjectLiveData;
    }

}

