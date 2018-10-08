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
import android.widget.Toast;

import com.example.android.tagsalenow.data.TagSaleEventsViewModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Locale;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class TagSaleListFragment extends Fragment implements TagSaleListRecyclerAdapter.TagSaleListAdapterOnClickHandler {

    private static final String TAG = "GGG";
    private RecyclerView recyclerView;
    private TagSaleListRecyclerAdapter tagSaleListRecyclerAdapter;

    private Context mContext;

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

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("tagsaleevents")
                .limitToLast(50);

        FirebaseRecyclerOptions<TagSaleEventObject> options =
                new FirebaseRecyclerOptions.Builder<TagSaleEventObject>()
                .setQuery(query, TagSaleEventObject.class)
                .build();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        if (mLayoutManager == null) Log.d(TAG, "onCreate: LAYOUTMANAGER IS NULL");


        recyclerView.setLayoutManager(mLayoutManager);
        tagSaleListRecyclerAdapter = new TagSaleListRecyclerAdapter(this);
        recyclerView.setAdapter(tagSaleListRecyclerAdapter);
        tagSaleListRecyclerAdapter.dataChanged();

        //Listen to data source
        // Obtain a new or prior instance of HotStockViewModel from the
        // ViewModelProviders utility class.
        TagSaleEventsViewModel viewModel = ViewModelProviders.of(this).get(TagSaleEventsViewModel.class);
        LiveData<DataSnapshot> liveData = viewModel.getDataSnapshotLiveData();

        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    // update the UI here with values in the snapshot
                    Toast toast = Toast.makeText(mContext, "Data Changed", Toast.LENGTH_SHORT);//, "Data Changed");
                    toast.show();
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }
}
