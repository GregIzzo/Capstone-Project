package com.example.android.tagsalenow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class TagSaleListRecyclerAdapter extends RecyclerView.Adapter<TagSaleListRecyclerAdapter.TagSaleListAdapterViewHolder > {

    private static final String TAG = "ADDAPTER";

    private final TagSaleListAdapterOnClickHandler mClickHandler;
    private Context viewGroupContext;

    public interface TagSaleListAdapterOnClickHandler {
        void onClick(int listPosition);//
    }
    public TagSaleListRecyclerAdapter(TagSaleListAdapterOnClickHandler mClick) {
        mClickHandler = mClick;
    }

    public class TagSaleListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView indicator_iv;
        public final TextView ts_placetv;
        public final TextView ts_friendsattendingtv;
        public final TextView ts_datetv;
        public final TextView ts_distancetv;
        public final TextView ts_planningtoattendcb;

        public TagSaleListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            indicator_iv = itemView.findViewById(R.id.indicator_iv);
            ts_placetv = itemView.findViewById(R.id.ts_placetv);
            ts_friendsattendingtv = itemView.findViewById(R.id.ts_friendsattendingtv);
            ts_datetv = itemView.findViewById(R.id.ts_datetv);
            ts_distancetv = itemView.findViewById(R.id.ts_distancetv);
            ts_planningtoattendcb = itemView.findViewById(R.id.ts_planningtoattendcb);
            Log.d(TAG, "TagSaleListAdapterViewHolder: *** itemView["+itemView+"] ["+indicator_iv+"]  ["+ts_placetv+"]  ["+ts_friendsattendingtv+"]  ["+ts_datetv+"]   ["+ts_distancetv+"]  ["+ts_planningtoattendcb+"]");

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);

        }
    }

    @NonNull
    @Override
    public TagSaleListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        viewGroupContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.tagsalelistitem;
        LayoutInflater inflater = LayoutInflater.from(viewGroupContext);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        Log.d(TAG, "onCreateViewHolder: xxxx view=" + view);
        return new TagSaleListAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by RecyclerView to display the data at the specified
     * position. In this method, update the contents of the ViewHolder using the "position" argument that is conveniently
     * passed into us.
     *
     * @param tagSaleListAdapterViewHolder The tagSaleListAdapterViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull TagSaleListAdapterViewHolder tagSaleListAdapterViewHolder, int position) {
        Log.d(TAG, "onBindViewHolder: BBBBBB viewholder=" + tagSaleListAdapterViewHolder);
       tagSaleListAdapterViewHolder.indicator_iv.setVisibility(View.VISIBLE);
       tagSaleListAdapterViewHolder.ts_placetv.setText(R.string.fake_place);
       tagSaleListAdapterViewHolder.ts_distancetv.setText(R.string.fake_distance);
       tagSaleListAdapterViewHolder.ts_datetv.setText(R.string.fake_date);
       tagSaleListAdapterViewHolder.ts_friendsattendingtv.setText(R.string.fake_friendsattending);

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public void dataChanged(){
        notifyDataSetChanged();
    }
 }
