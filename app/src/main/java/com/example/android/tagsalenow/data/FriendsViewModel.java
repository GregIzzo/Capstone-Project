package com.example.android.tagsalenow.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.tagsalenow.FriendRelationObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/*
    This class was inspired by a similar class from Doug Sevenson in his "The Firebase Blog" :
        "Using Android Architecture Components with Firebase Realtime Database (Part 1)
    From: https://firebase.googleblog.com/2017/12/using-android-architecture-components.html
 */
public class FriendsViewModel extends ViewModel {
    private static final DatabaseReference FRIENDS_REF =  FirebaseDatabase.getInstance().getReference("/friends");

    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(FRIENDS_REF);
    private final MediatorLiveData<FriendRelationObject> friendRelationObjectLiveData = new MediatorLiveData<>();

    public FriendsViewModel() {
        // Set up the MediatorLiveData to convert DataSnapshot objects into FriendRelation objects
        friendRelationObjectLiveData.addSource(liveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable final DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            friendRelationObjectLiveData.postValue(dataSnapshot.getValue(FriendRelationObject.class));
                        }
                    }).start();
                } else {
                    friendRelationObjectLiveData.setValue(null);
                }
            }
        });
    }
    @NonNull
    public LiveData<DataSnapshot> getDataSnapshotLiveData() {
        return liveData;
    }

}
