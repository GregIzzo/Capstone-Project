package com.example.android.tagsalenow;

import android.app.ActivityOptions;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
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

import com.example.android.tagsalenow.data.CurrentInfo;
import com.example.android.tagsalenow.data.PrefHandler;
import com.example.android.tagsalenow.data.TagSaleEventsViewModel;
import com.example.android.tagsalenow.utils.Utilities;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class TagSaleListFragment extends Fragment
        implements TagSaleListRecyclerAdapter.TagSaleListAdapterOnClickHandler,
         TagSaleListRecyclerAdapter.TagSaleListAdapterOnAttendClickHandler,
        TagSaleListRecyclerAdapter.TagSaleListAdapterOnOnSiteClickhandler
{

    private static final String TAG = "TAGSALELISTFRAGMENT";
    private RecyclerView recyclerView;
    private TagSaleListRecyclerAdapter tagSaleListRecyclerAdapter;
    private Button btn_AddTagSale;

    private TagSaleEventsViewModel viewModel ;
    private LiveData<List<TagSaleEventObject>> liveData;


    private Context mContext;
    //Setup Click listener to report the event to anyone listening
    OnButtonClickListener mCallback;


    public interface OnButtonClickListener {
        void onAddButtonClicked(String tag);
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
        btn_AddTagSale = rootView.findViewById(R.id.button_addtagsale);
        btn_AddTagSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCallback.onAddButtonClicked(getString(R.string.TAG_FRAGMENT_TAGSALELIST));
            }
        });

        tagSaleListRecyclerAdapter = new TagSaleListRecyclerAdapter(this,this, this
        );
        recyclerView.setAdapter(tagSaleListRecyclerAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        if (mLayoutManager == null) Log.d(TAG, "onCreate: LAYOUTMANAGER IS NULL");
        recyclerView.setLayoutManager(mLayoutManager);

        // tsAdapter.dataChanged();

        //Listen to data source
        // Obtain a new or prior instance of TagSaleEventsViewModel from the
        // ViewModelProviders utility class.
         viewModel = ViewModelProviders.of(this).get(TagSaleEventsViewModel.class);
         liveData = viewModel.getTagSaleEventObjectLiveData();

        liveData.observe(this, new Observer<List<TagSaleEventObject>>() {
            @Override
            public void onChanged(@Nullable List<TagSaleEventObject> tagSaleEventObjects) {
                if (tagSaleEventObjects != null) {
                    Log.d(TAG, "onChanged: TAGSALELISTFRAGMENT TAGSALEEVENTS Data changed");
                   try{
                       tagSaleListRecyclerAdapter.addItems(tagSaleEventObjects);
                       //
                       // FOR WIDGET, SAVE TOP 3
                       List<TagSaleEventObject> top3List = new ArrayList<TagSaleEventObject>();
                       TagSaleEventObject ts1 = tagSaleListRecyclerAdapter.getItemAt(0);
                       if (ts1 != null) top3List.add(ts1);
                       TagSaleEventObject ts2 = tagSaleListRecyclerAdapter.getItemAt(1);
                       if (ts2 != null) top3List.add(ts2);
                       TagSaleEventObject ts3 = tagSaleListRecyclerAdapter.getItemAt(2);
                       if (ts3 != null) top3List.add(ts3);

                       String outString = "";
                       Gson gson = new Gson();
                       outString = gson.toJson(top3List);
                        PrefHandler.setTop3(mContext,outString);
                       //Update Widget
                       Log.d(TAG, "onClick: About to call WidgetUpdateService.startActionUpdateTagSales ");
                       WidgetUpdateService.startActionUpdateTagSales(mContext);

                   } catch (Exception ex){

                   }
                }
            }
/*
            @Override
            public void onChanged(@Nullable TagSaleEventObject tagSaleEventObject) {
                if (tagSaleEventObject != null) {
                    // update the UI here with values in the snapshot
                    String place =  tagSaleEventObject.toString();
                    Log.d(TAG, "onChanged: dataSnapshot::::" + tagSaleEventObject.getLocationId());
                    Toast toast = Toast.makeText(mContext, place, Toast.LENGTH_SHORT);//, "Data Changed");
                   toast.show();

                    //Map<String, Object> td = (HashMap<String,Object>) tagSaleEventObject.getValue();
                    try {
                        Log.d(TAG, "onChanged:TTT tagSaleEventObject="+tagSaleEventObject.toString());
                        ///FIXME List<TagSaleEventObject> values = Utilities.MapToTSEO(td);
                       ///FIXME tagSaleListRecyclerAdapter.addItems(values);
                    } catch (Exception ex){
                        Log.d(TAG, "onChanged: TRY CATCH FAIL:" + ex.getMessage());
                    }
                }
            }

            */
        });

        //add record
        //String locationId, String ownerId, String date, String startTime, String endTime, String description, String tags
          return rootView;
    }

    @Override
    public void onClick(int listPosition) {
        Log.d(TAG, "onClick: CLICK POSITION:" + listPosition+" ["+liveData.getValue().get(listPosition).getAddress()+"]");
        //launch TagSaleViewFragment

        CurrentInfo.setCurrentTagSaleEventObject(liveData.getValue().get(listPosition));
        Intent intent = new Intent(getActivity(), ViewTagSaleActivity.class);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle();
            startActivity(intent, bundle);
        } else {
            startActivity(intent);
        }


    }
    @Override
    public void onAttendClick(Map<String,Object> dataMap) {
        int listPosition = (int) dataMap.get("position");
        Boolean checkState = (Boolean) dataMap.get("state");
        if (checkState == null) checkState  = false;
   //Add Attending record
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        String key = mDatabaseReference.child("attending").push().getKey();
        TagSaleEventObject tso = liveData.getValue().get(listPosition);
        Map<String, Object> childUpdates = new HashMap<>();
        if (checkState) {
            //Add Attending Record
            childUpdates.put("/attending/" +
                    tso.getId() + "/" +
                    CurrentInfo.getCurrentUser().getUserId(), "true");
           // mDatabaseReference.updateChildren(childUpdates);
        } else {
            //Remove Attending Record
            childUpdates.put("/attending/" +
                    tso.getId() + "/" +
                    CurrentInfo.getCurrentUser().getUserId(), null);
        }
        mDatabaseReference.updateChildren(childUpdates);
    }

    @Override
    public void onOnSiteClick(Map<String, Object> dataMap) {
        int listPosition = (int) dataMap.get("position");
        Boolean checkState = (Boolean) dataMap.get("state");
        if (checkState == null) checkState  = false;
//Add OnSite record
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        String key = mDatabaseReference.child("onsite").push().getKey();
        TagSaleEventObject tso = liveData.getValue().get(listPosition);
        Map<String, Object> childUpdates = new HashMap<>();
        //Change 'onSiteTagSaleId' in user record

        if (checkState) {
            //Add onsite Record
            Log.d(TAG, "onOnSiteClick: Adding onsite :"+tso.getId()+" : "+CurrentInfo.getCurrentUser().getUserId());
            childUpdates.put("/onsite/" +
                    tso.getId() + "/" +
                    CurrentInfo.getCurrentUser().getUserId(), "true");
            Log.d(TAG, "onOnSiteClick: CURRENT USER ONSITE:"+CurrentInfo.getCurrentUser().getOnSiteTagSaleId());
            if (CurrentInfo.getCurrentUser().getOnSiteTagSaleId() != ""){
                //remove old onsite record from 'onsite' table
                childUpdates.put("/onsite/" +
                        CurrentInfo.getCurrentUser().getOnSiteTagSaleId() + "/" +
                        CurrentInfo.getCurrentUser().getUserId(), null);
                Log.d(TAG, "onOnSiteClick: Deleting onsite :"+
                        CurrentInfo.getCurrentUser().getOnSiteTagSaleId()+
                        " : "+CurrentInfo.getCurrentUser().getUserId());

            }
            //Change user record
            childUpdates.put("/tagsaleusers/" +
                            CurrentInfo.getCurrentUser().getUserId() + "/" +
                  "onSiteTagSaleId" , tso.getId());

        } else {
            //Remove onsite Record
            childUpdates.put("/onsite/" +
                    tso.getId() + "/" +
                    CurrentInfo.getCurrentUser().getUserId(), null);
            //Change user record
            childUpdates.put("/tagsaleusers/" +
                    CurrentInfo.getCurrentUser().getUserId() + "/" +
                    "onSiteTagSaleId" ,"");
        }
        Log.d(TAG, "onOnSiteClick: UPDATES::"+
                childUpdates.toString());
        mDatabaseReference.updateChildren(childUpdates);
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
        /*
        try {
            mCallback = (OnButtonClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " Must implement OnImageClickListener");
        }
        */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }
}
