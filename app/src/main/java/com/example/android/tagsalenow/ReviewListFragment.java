package com.example.android.tagsalenow;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.android.tagsalenow.data.ReviewsViewModel;

import java.util.List;

public class ReviewListFragment extends Fragment implements ReviewListRecyclerAdapter.ReviewListAdapterOnClickHandler {

    private static final String TAG = "REVIEWLISTFRAGMENT";
    private RecyclerView recyclerView;
    private ReviewListRecyclerAdapter ReviewListRecyclerAdapter;
    private Button btn_AddReview;

    private Context mContext;
    //Setup Click listener to report the event to anyone listening
    OnButtonClickListener mCallback;

    public interface OnButtonClickListener {
        void onAddButtonClicked(String tag);
    }

    public ReviewListFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reviewlist, container, false);
        recyclerView = rootView.findViewById(R.id.rv_reviewlist);
        assert recyclerView != null;
        Log.d(TAG, "onCreateView: ZZZZ ReviewListFragment.recyclerview=" + recyclerView.getId());
        btn_AddReview = rootView.findViewById(R.id.button_addreview);
        btn_AddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launch add-review-activity
                mCallback.onAddButtonClicked(getString(R.string.TAG_FRAGMENT_REVIEWLIST));
            }
        });

        ReviewListRecyclerAdapter = new ReviewListRecyclerAdapter(this);
        recyclerView.setAdapter(ReviewListRecyclerAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        if (mLayoutManager == null) Log.d(TAG, "onCreate: LAYOUTMANAGER IS NULL");
        recyclerView.setLayoutManager(mLayoutManager);

        // tsAdapter.dataChanged();

        //Listen to data source
        // Obtain a new or prior instance of ReviewsViewModel from the
        // ViewModelProviders utility class.
        ReviewsViewModel viewModel = ViewModelProviders.of(this).get(ReviewsViewModel.class);
        LiveData<List<TagSaleReviewObject>> liveData = viewModel.getTagSaleReviewObjectLiveData();

        liveData.observe(this, new Observer<List<TagSaleReviewObject>>() {
            @Override
            public void onChanged(@Nullable List<TagSaleReviewObject> TagSaleReviewObjects) {
                if (TagSaleReviewObjects != null) {
                    Log.d(TAG, "onChanged: REVIEW  Data changed :"+TagSaleReviewObjects.toString());
                    try{
                        ReviewListRecyclerAdapter.addItems(TagSaleReviewObjects);
                    } catch (Exception ex){
                        Log.d(TAG, "ReviewListFragment - onChange - Exception: "+ex.getMessage());
                    }
                }
            }
        });

        //add record
        //String locationId, String ownerId, String date, String startTime, String endTime, String description, String tags
        return rootView;
    }

    @Override
    public void onClick(int listPosition) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

        try {
            mCallback = (OnButtonClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " Must implement OnImageClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }
}

