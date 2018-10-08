package com.example.android.tagsalenow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.tagsalenow.ui.TagSaleEventViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;

public class TagSaleEventAdapter extends FirebaseRecyclerAdapter<TagSaleEventObject, TagSaleEventViewHolder> {

    /**
     * Initialize a {@link TagSaleEventAdapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private Context context;

    public TagSaleEventAdapter(@NonNull FirebaseRecyclerOptions<TagSaleEventObject> options) {
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


}
