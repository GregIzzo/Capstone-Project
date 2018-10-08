package com.example.android.tagsalenow;

import android.content.Context;

import com.example.android.tagsalenow.ui.TagSaleEventViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter_LifecycleAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

public class TagSaleEventViewAdapter extends FirebaseRecyclerAdapter<TagSaleEventObject, TagSaleEventViewHolder> {

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

    }

    @NonNull
    @Override
    public TagSaleEventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
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
