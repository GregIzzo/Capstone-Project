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

import com.example.android.tagsalenow.data.FriendRequestViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AcceptFriendFragment extends Fragment  implements AcceptFriendListRecyclerAdapter.AcceptFriendListAdapterOnClickHandler {
    private static final String TAG = "ACCEPTFRIENDLISTFRAGMENT";
    private RecyclerView recyclerView;
    private AcceptFriendListRecyclerAdapter acceptFriendListRecyclerAdapter;
    private Context mContext;

    private FriendRequestViewModel viewModel;
    private LiveData<List<FriendRequestObject>> liveData;

    AcceptFriendFragment.OnButtonClickListener mCallback;
    public interface OnButtonClickListener {
        void onAddButtonClicked(String tag);
    }
    public AcceptFriendFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_acceptfriend, container, false);
        recyclerView = rootView.findViewById(R.id.rv_friendrequestlist);
        assert recyclerView != null;

        acceptFriendListRecyclerAdapter = new AcceptFriendListRecyclerAdapter(this);
        recyclerView.setAdapter(acceptFriendListRecyclerAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        if (mLayoutManager == null) Log.d(TAG, "onCreate: LAYOUTMANAGER IS NULL");
        recyclerView.setLayoutManager(mLayoutManager);

        viewModel = ViewModelProviders.of(this).get(FriendRequestViewModel.class);
        liveData = viewModel.getFriendRequestObjectLiveData();

        liveData.observe(this, new Observer<List<FriendRequestObject>>() {
            @Override
            public void onChanged(@Nullable List<FriendRequestObject> friendRequest) {
                if (friendRequest != null) {
                    Log.d(TAG, "onChanged: FRIENDRELATAION  Data changed :"+ friendRequest.toString());
                    try{
                        acceptFriendListRecyclerAdapter.addItems(friendRequest);
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
        Log.d(TAG, "onClick: ADD FRIEND position="+listPosition);
        FriendRequestObject fro = acceptFriendListRecyclerAdapter.getAtOffset(listPosition);
        if (fro != null) {
            Log.d(TAG, "onClick: friendreq::" +fro.toString());
            //Create friend record
            DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
            String key = mDatabaseReference.child("friends").push().getKey();
            Map<String, Boolean> fromUserValues;
            fromUserValues = new HashMap<String, Boolean>();
            fromUserValues.put(fro.getFromUserId(), true);
            Map<String, Boolean> toUserValues;
            toUserValues = new HashMap<String, Boolean>();
            toUserValues.put(fro.getToUserId(), true);

            Map<String, Object> childUpdates = new HashMap<>();

            childUpdates.put("/friends/" +
                    fro.getToUserId()+"/"+
                    fro.getFromUserId(), "true");

            childUpdates.put("/friends/" +
                    fro.getFromUserId()+"/" +
                    fro.getToUserId(), "true");
            //delete the friend request record
            childUpdates.put("/friendrequest/"+fro.getDbkey(),null);

            Log.d(TAG, "onClick: ACCEPTFRIEND childupdates:"+childUpdates.toString());
            mDatabaseReference.updateChildren(childUpdates);


        } else {
            Log.d(TAG, "onClick: From Adapter - FriendRequetsObject is null:" );

        }

    }
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

        try {
            mCallback = (AcceptFriendFragment.OnButtonClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " Must implement OnImageClickListener");
        }
    }
    */

}
