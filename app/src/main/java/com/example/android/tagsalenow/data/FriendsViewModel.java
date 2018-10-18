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
public class FriendsViewModel extends ViewModel {
    private static final DatabaseReference FRIENDS_REF =
            FirebaseDatabase.getInstance().getReference("/friends");

    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(FRIENDS_REF);

    @NonNull
    public LiveData<DataSnapshot> getDataSnapshotLiveData() {
        return liveData;
    }

}
