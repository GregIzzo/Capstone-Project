package com.example.android.tagsalenow;

import android.content.Context;

import com.example.android.tagsalenow.ui.TagSaleEventViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter_LifecycleAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

public class TagSaleEventViewAdapter extends FirebaseRecyclerAdapter<TagSaleEventObject, TagSaleEventViewHolder> {
    private static final String TAG = "TSEVA";
    /**
     * Initialize a {@link TagSaleEventViewAdapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private Context context;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TagSaleEventViewAdapter(@NonNull FirebaseRecyclerOptions<TagSaleEventObject> options) {
        super(options);
    }
     @Override
    protected void onBindViewHolder(@NonNull TagSaleEventViewHolder holder, int position, @NonNull TagSaleEventObject model) {

        holder.placeName.setText(model.getLocationId());
        holder.tagSaleDate.setText(model.getDate());
         Log.d(TAG, "onBindViewHolder: ^^^ locationid="+model.getLocationId()+" date="+model.getDate());
        //friendsAttending will need to be computed from another table
         // attending - needs to be found from another table
         //distance - computed
    }

    @NonNull
    @Override
    public TagSaleEventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroupParent, int i) {
        // Create a new instance of the ViewHolder, in this case we are using a custom
        // layout called R.layout.message for each item
        View view = LayoutInflater.from(viewGroupParent.getContext())
                .inflate(R.layout.tagsalelistitem, viewGroupParent, false);
        Log.d(TAG, "onCreateViewHolder: onCreateviewHolder i="+i);
        return new TagSaleEventViewHolder(view);
    }

/*
    public TagSaleEventViewAdapter(@NonNull FirebaseRecyclerOptions<TagSaleEventObject> options) {
        super(options);
    }

    @NonNull
    @Override
    public TagSaleEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tagsalelistitem, parent, false);
        return new TagSaleEventViewHolder(view);
    }
    @Override
    protected void onBindViewHolder(@NonNull TagSaleEventViewHolder holder, int position, @NonNull TagSaleEventObject model) {

        holder.imHereIndicator.setVisibility(View.VISIBLE);
        holder.placeName.setText(R.string.fake_place);
        holder.tagSaleDistance.setText(R.string.fake_distance);
        holder.tagSaleDate.setText(R.string.fake_date);
        holder.friendsAttending.setText(R.string.fake_friendsattending);

    }
*/

}
