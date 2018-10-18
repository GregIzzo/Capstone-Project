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

import com.example.android.tagsalenow.data.FriendsViewModel;
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

public class FriendsListFragment extends Fragment implements FriendsListRecyclerAdapter.FriendsListAdapterOnClickHandler {

    private static final String TAG = "GGG";
    private RecyclerView recyclerView;
    private FriendsListRecyclerAdapter FriendsListRecyclerAdapter;
    private Button btn_AddFriend;

    private Context mContext;
    //Setup Click listener to report the event to anyone listening
    OnButtonClickListener mCallback;

    public interface OnButtonClickListener {
        void onAddButtonClicked();
    }


    public FriendsListFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friendlist, container, false);
        recyclerView = rootView.findViewById(R.id.rv_friendslist);
        assert recyclerView != null;
        Log.d(TAG, "onCreateView: ZZZZ recyclerview=" + recyclerView.getId());
        btn_AddFriend = (Button) rootView.findViewById(R.id.button_addfriend);
        btn_AddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launch add-friend-activity
                mCallback.onAddButtonClicked();
            }
        });

        FriendsListRecyclerAdapter = new FriendsListRecyclerAdapter(this);
        Log.d(TAG, "onCreateView: TEST** 2ND ** Step");
        recyclerView.setAdapter(FriendsListRecyclerAdapter);
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
        FriendsViewModel viewModel = ViewModelProviders.of(this).get(FriendsViewModel.class);
        Log.d(TAG, "onCreateView: TEST** 6TH ** Step");
        LiveData<DataSnapshot> liveData = viewModel.getDataSnapshotLiveData();
        Log.d(TAG, "onCreateView: TEST** 7TH ** Step");
        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    // update the UI here with values in the snapshot
                    String friendname =  dataSnapshot.toString();
                    Log.d(TAG, "onChanged: dataSnapshot::::" + friendname);
                    Toast toast = Toast.makeText(mContext, friendname, Toast.LENGTH_SHORT);//, "Data Changed");
                    toast.show();

                    Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
                    try {
                        Log.d(TAG, "onChanged: TRYING td.values="+td.values());
                        List<FriendRelationObject> values = Utilities.MapToFRO(td);
                        FriendsListRecyclerAdapter.addItems(values);
                    } catch (Exception ex){
                        Log.d(TAG, "onChanged: TRY CATCH FAIL:" + ex.getMessage());
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
