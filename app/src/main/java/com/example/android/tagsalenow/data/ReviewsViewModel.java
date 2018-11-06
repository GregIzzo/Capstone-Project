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
import com.example.android.tagsalenow.TagSaleReviewObject;
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
public class ReviewsViewModel extends ViewModel {
    private static  DatabaseReference REVIEWS_REF = null;// FirebaseDatabase.getInstance().getReference("/reviews");
    private String TAG = "ReviewsViewModel";
    private  FirebaseQueryLiveData liveData = null;//new FirebaseQueryLiveData(REVIEWS_REF);
    private  LiveData<List<TagSaleReviewObject>> reviewObjectLiveData;
    private final MediatorLiveData<TagSaleReviewObject> mldTagSaleReviewObjectLiveData = new MediatorLiveData<>();

    public ReviewsViewModel() {
        // Set up the MediatorLiveData to convert DataSnapshot objects into Reviews objects

        if (REVIEWS_REF == null){
            REVIEWS_REF=  FirebaseDatabase.getInstance().getReference().child("reviews");
        }
        String currTagSaleId = CurrentInfo.getCurrentTagSaleID();
        Query queryRef = REVIEWS_REF.orderByChild("tagSaleID").startAt(currTagSaleId).endAt(currTagSaleId);
        if (liveData == null){
            liveData = new FirebaseQueryLiveData(queryRef);
        }
        reviewObjectLiveData =  Transformations.map(liveData, new Deserializer());

        ///REVIEWS_REF = FirebaseDatabase.getInstance().getReference("/reviews/"+ CurrentInfo.getCurrentUser().getUserId());
        Log.d(TAG, "ReviewsViewModel: Creator");
        mldTagSaleReviewObjectLiveData.addSource(liveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable final DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChanged: Reviewsdata=" + dataSnapshot.toString());
                if (dataSnapshot != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mldTagSaleReviewObjectLiveData.postValue(dataSnapshot.getValue(TagSaleReviewObject.class));
                        }
                    }).start();
                } else {
                    mldTagSaleReviewObjectLiveData.setValue(null);
                }
            }
        });
    }
    //Deserializer class - to serialize the DataSnapshot into TagSaleEventObject
    private class Deserializer implements Function<DataSnapshot, List<TagSaleReviewObject>> {
        @Override
        public List<TagSaleReviewObject> apply(DataSnapshot dataSnapshot) {
            Log.d("ReviewsViewModel", "dataSnapShot: " + dataSnapshot.toString());
            List<TagSaleReviewObject> values = null;
            Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
            try {
                values = Utilities.MapToREVIEWO(td);
            } catch (Exception ex){
                Log.d("GG", "ReviewsViewModel Deserializer try-catcherr:" + ex.getMessage());
            }
            return values;
        }
    }

    @NonNull
    public LiveData<List<TagSaleReviewObject>> getTagSaleReviewObjectLiveData() {
        return reviewObjectLiveData;
    }

}
