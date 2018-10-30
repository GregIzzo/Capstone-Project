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


import java.util.List;

public class AcceptFriendFragment extends Fragment  implements AcceptFriendListRecyclerAdapter.AcceptFriendListAdapterOnClickHandler {
    private static final String TAG = "ACCEPTFRIENDLISTFRAGMENT";
    private RecyclerView recyclerView;
    private AcceptFriendListRecyclerAdapter acceptFriendListRecyclerAdapter;
    private Context mContext;
    FriendsListFragment.OnButtonClickListener mCallback;

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

        FriendRequestViewModel viewModel = ViewModelProviders.of(this).get(FriendRequestViewModel.class);
        LiveData<List<FriendRequestObject>> liveData = viewModel.getFriendRequestObjectLiveData();

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

    }
}
