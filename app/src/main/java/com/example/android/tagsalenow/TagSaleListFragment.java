package com.example.android.tagsalenow;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.android.tagsalenow.data.TagSaleEventsViewModel;
import com.example.android.tagsalenow.utils.Utilities;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class TagSaleListFragment extends Fragment implements TagSaleListRecyclerAdapter.TagSaleListAdapterOnClickHandler {

    private static final String TAG = "GGG";
    private RecyclerView recyclerView;
    private TagSaleListRecyclerAdapter tagSaleListRecyclerAdapter;
    private Button btn_AddTagSale;

    private Context mContext;
    //Setup Click listener to report the event to anyone listening
    OnButtonClickListener mCallback;

    public interface OnButtonClickListener {
        void onAddButtonClicked();
    }


    public TagSaleListFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tagsalelist, container, false);
        recyclerView = rootView.findViewById(R.id.rv_tagsalelist);
        assert recyclerView != null;
        Log.d(TAG, "onCreateView: ZZZZ recyclerview=" + recyclerView.getId());
        btn_AddTagSale = (Button) rootView.findViewById(R.id.button_addtagsale);
        btn_AddTagSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launch add-tag-sale-activity
                mCallback.onAddButtonClicked();
            }
        });
/*
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("tagsaleevents")
                .limitToLast(50);


        FirebaseRecyclerOptions<TagSaleEventObject> options =
                new FirebaseRecyclerOptions.Builder<TagSaleEventObject>()
                        .setQuery(query, TagSaleEventObject.class)
                        .build();


        TagSaleEventViewAdapter tsAdapter = new TagSaleEventViewAdapter(options);
        recyclerView.setAdapter(tsAdapter);
*/

        tagSaleListRecyclerAdapter = new TagSaleListRecyclerAdapter(this);
        Log.d(TAG, "onCreateView: TEST** 2ND ** Step");
        recyclerView.setAdapter(tagSaleListRecyclerAdapter);
        Log.d(TAG, "onCreateView: TEST** 3RD ** Step");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        Log.d(TAG, "onCreateView: TEST** 4TH ** Step");
        if (mLayoutManager == null) Log.d(TAG, "onCreate: LAYOUTMANAGER IS NULL");
        recyclerView.setLayoutManager(mLayoutManager);
        Log.d(TAG, "onCreateView: TEST** 5TH ** Step");

        // tsAdapter.dataChanged();

        //Listen to data source
        // Obtain a new or prior instance of HotStockViewModel from the
        // ViewModelProviders utility class.
        TagSaleEventsViewModel viewModel = ViewModelProviders.of(this).get(TagSaleEventsViewModel.class);
        Log.d(TAG, "onCreateView: TEST** 6TH ** Step");
        LiveData<DataSnapshot> liveData = viewModel.getDataSnapshotLiveData();
        Log.d(TAG, "onCreateView: TEST** 7TH ** Step");
        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    // update the UI here with values in the snapshot
                    String place =  dataSnapshot.toString();
                    Log.d(TAG, "onChanged: dataSnapshot::::" + place);
                    Toast toast = Toast.makeText(mContext, place, Toast.LENGTH_SHORT);//, "Data Changed");
                    toast.show();

                    Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
                    try {
                        Log.d(TAG, "onChanged: TRYING td.values="+td.values());
                        List<TagSaleEventObject> values = Utilities.MapToTSEO(td);
                        tagSaleListRecyclerAdapter.addItems(values);
                    } catch (Exception ex){
                        Log.d(TAG, "onChanged: TRY CATCH FAIL:" + ex.getMessage());
                    }
                    /*
                    String ticker = dataSnapshot.child("ticker").getValue(String.class);
                    tvTicker.setText(ticker);
                    Float price = dataSnapshot.child("price").getValue(Float.class);
                    tvPrice.setText(String.format(Locale.getDefault(), "%.2f", price));
                    */
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
