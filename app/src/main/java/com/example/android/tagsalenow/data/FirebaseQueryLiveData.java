package com.example.android.tagsalenow.data;

import android.arch.lifecycle.LiveData;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/*
    This class is based on the 'FirebaseQueryLiveData' class written by Doug Sevenson in his "The Firebase Blog" :
        "Using Android Architecture Components with Firebase Realtime Database (Part 1)
   From: https://firebase.googleblog.com/2017/12/using-android-architecture-components.html

 */
public class FirebaseQueryLiveData extends LiveData<DataSnapshot> {
    private static final String LOG_TAG = "FirebaseQueryLiveData";

    private final Query query;
    private final MyValueEventListener listener = new MyValueEventListener();
    //Added to delay listener removal/re-add on configuration change (delay 2 seconds)
    // from the 3rd installment of the blog post by Doug Sevenson

    private boolean listenerRemovePending = false;
    private final Handler handler = new Handler();
    //

    public FirebaseQueryLiveData(Query query) {
        this.query = query;
    }
    public FirebaseQueryLiveData(DatabaseReference ref) {
        this.query = ref;
    }




    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            setValue(dataSnapshot);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(LOG_TAG, "Can't listen to query " + query, databaseError.toException());
        }
    }
    //
    private final Runnable removeListener = new Runnable() {
        @Override
        public void run() {
            query.removeEventListener(listener);
            listenerRemovePending = false;
        }
    };

    @Override
    protected void onActive() {
        if (listenerRemovePending) {
            handler.removeCallbacks(removeListener);
        }
        else {
            query.addValueEventListener(listener);
        }
        listenerRemovePending = false;
    }

    @Override
    protected void onInactive() {
        // Listener removal is schedule on a two second delay
        handler.postDelayed(removeListener, 2000);
        listenerRemovePending = true;
    }
}
