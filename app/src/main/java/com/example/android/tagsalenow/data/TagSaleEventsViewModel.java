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
        "Using Android Architecture Components with Firebase Realtime Database (Part 1 and Part 2)
    From: https://firebase.googleblog.com/2017/12/using-android-architecture-components.html
    https://firebase.googleblog.com/2017/12/using-android-architecture-components_20.html
 */
public class TagSaleEventsViewModel extends ViewModel {
    private static final DatabaseReference TAG_SALE_EVENTS_REF = FirebaseDatabase.getInstance().getReference("/tagsaleevents");

    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(TAG_SALE_EVENTS_REF);
    private final MediatorLiveData<TagSaleEventObject> tagSaleEventsObjectLiveData = new MediatorLiveData<>();

    public TagSaleEventsViewModel() {
        // Set up the MediatorLiveData to convert DataSnapshot objects into TagSaleEvent objects
        tagSaleEventsObjectLiveData.addSource(liveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable final DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    Log.d("TAGSALEEVENTSVIEWMODEL", " onChanged:TSEVIEWMODEL ");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            TagSaleEventObject to = dataSnapshot.getValue(TagSaleEventObject.class);

                            tagSaleEventsObjectLiveData.postValue(dataSnapshot.getValue(TagSaleEventObject.class));

                        }
                    }).start();
                } else {
                    tagSaleEventsObjectLiveData.setValue(null);
                }
            }
        });
    }
    private  final LiveData<List<TagSaleEventObject>> tagSaleEventObjectLiveData = Transformations.map(liveData, new Deserializer());
    //Deserializer class - to serialize the DataSnapshot into TagSaleEventObject
    private class Deserializer implements Function<DataSnapshot, List<TagSaleEventObject>> {
        @Override
        public List<TagSaleEventObject> apply(DataSnapshot dataSnapshot) {
            Log.d("TAGSALEEVENTSVIEWMODEL", "dataSnapShot: " + dataSnapshot.toString());
            List<TagSaleEventObject> values = null;
            Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
            try {
                values = Utilities.MapToTSEO(td);
            } catch (Exception ex){
                Log.d("GG", "Deserializer try-catcherr:" + ex.getMessage());
            }
            return values;
        }
    }

    @NonNull
    public LiveData<List<TagSaleEventObject>> getTagSaleEventObjectLiveData() {
        return tagSaleEventObjectLiveData;
    }


}
