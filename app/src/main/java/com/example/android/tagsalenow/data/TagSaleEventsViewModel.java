package com.example.android.tagsalenow.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
/*
    This class was inspired by a similar class from Doug Sevenson in his "The Firebase Blog" :
        "Using Android Architecture Components with Firebase Realtime Database (Part 1)
    From: https://firebase.googleblog.com/2017/12/using-android-architecture-components.html
 */
public class TagSaleEventsViewModel extends ViewModel {
    private static final DatabaseReference TAG_SALE_EVENTS_REF =
            FirebaseDatabase.getInstance().getReference("/tagsaleevents");

    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(TAG_SALE_EVENTS_REF);

    @NonNull
    public LiveData<DataSnapshot> getDataSnapshotLiveData() {
        return liveData;
    }


}
