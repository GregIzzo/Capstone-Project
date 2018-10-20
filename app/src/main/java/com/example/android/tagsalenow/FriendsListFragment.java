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

    private static final String TAG = "FRIENDSLISTFRAGMENT";
    private RecyclerView recyclerView;
    private FriendsListRecyclerAdapter friendsListRecyclerAdapter;
    private Button btn_AddFriend;

    private Context mContext;
    //Setup Click listener to report the event to anyone listening
    OnButtonClickListener mCallback;

    public interface OnButtonClickListener {
        void onAddButtonClicked(String tag);
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
        Log.d(TAG, "onCreateView: ZZZZ FriendListFragment.recyclerview=" + recyclerView.getId());
        btn_AddFriend = (Button) rootView.findViewById(R.id.button_addfriend);
        btn_AddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launch add-friend-activity
                mCallback.onAddButtonClicked(getString(R.string.TAG_FRAGMENT_FRIENDLIST));
            }
        });

        friendsListRecyclerAdapter = new FriendsListRecyclerAdapter(this);
        recyclerView.setAdapter(friendsListRecyclerAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        if (mLayoutManager == null) Log.d(TAG, "onCreate: LAYOUTMANAGER IS NULL");
        recyclerView.setLayoutManager(mLayoutManager);

        // tsAdapter.dataChanged();

        //Listen to data source
        // Obtain a new or prior instance of HotStockViewModel from the
        // ViewModelProviders utility class.
        FriendsViewModel viewModel = ViewModelProviders.of(this).get(FriendsViewModel.class);
        LiveData<List<FriendRelationObject>> liveData = viewModel.getFriendRelationObjectLiveData();

        liveData.observe(this, new Observer<List<FriendRelationObject>>() {
            @Override
            public void onChanged(@Nullable List<FriendRelationObject> friendRelationObjects) {
                if (friendRelationObjects != null) {
                    Log.d(TAG, "onChanged: FRIENDRELATAION  Data changed :"+friendRelationObjects.toString());
                    try{
                        friendsListRecyclerAdapter.addItems(friendRelationObjects);
                    } catch (Exception ex){
                        Log.d(TAG, "FriendsListFragment - onChange - Exception: "+ex.getMessage());
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
