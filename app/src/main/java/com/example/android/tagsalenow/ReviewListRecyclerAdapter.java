package com.example.android.tagsalenow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.tagsalenow.data.CurrentInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ReviewListRecyclerAdapter extends RecyclerView.Adapter<ReviewListRecyclerAdapter.ReviewsListAdapterViewHolder > {

    private static final String TAG = "ADDAPTER";

    private List<TagSaleReviewObject> RevObjectList;

    private final ReviewListAdapterOnClickHandler mClickHandler;
    private Context viewGroupContext;



    public interface ReviewListAdapterOnClickHandler {
        void onClick(int listPosition);//
    }
    public ReviewListRecyclerAdapter(ReviewListAdapterOnClickHandler mClick) {
        mClickHandler = mClick;

    }

    public class ReviewsListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView tv_reviewauthor;
        public final TextView tv_reviewdescription;
        public final RatingBar ratingBar;


        public ReviewsListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_reviewauthor = itemView.findViewById(R.id.tv_reviewauthor);
            tv_reviewdescription = itemView.findViewById(R.id.tv_reviewdescription);
            ratingBar = itemView.findViewById(R.id.ratingBar);

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
    public ReviewsListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        viewGroupContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.reviewlistitem;
        LayoutInflater inflater = LayoutInflater.from(viewGroupContext);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        Log.d(TAG, "onCreateViewHolder: xxxx view=" + view);
        return new ReviewsListAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by RecyclerView to display the data at the specified
     * position. In this method, update the contents of the ViewHolder using the "position" argument that is conveniently
     * passed into us.
     *
     * @param ReviewsListAdapterViewHolder The ReviewsListAdapterViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ReviewsListAdapterViewHolder ReviewsListAdapterViewHolder, int position) {
        Log.d(TAG, "onBindViewHolder: BBBBBB viewholder=" + ReviewsListAdapterViewHolder);

        //Get Author NAme:


        ReviewsListAdapterViewHolder.tv_reviewauthor.setText(CurrentInfo.getUserByKey(RevObjectList.get(position).getReviewerID()).getDisplayName());
        ReviewsListAdapterViewHolder.tv_reviewdescription.setText(RevObjectList.get(position).getDescription());
        ReviewsListAdapterViewHolder.ratingBar.setRating(RevObjectList.get(position).getFiveStarRating());

    }
    public void addItems(List<TagSaleReviewObject> RevObjectList) {
        this.RevObjectList = RevObjectList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if (RevObjectList == null) return 0;
        Log.d(TAG, "getItemCount: XXX ITEM COUNT="+RevObjectList.size());
        return RevObjectList.size();
    }

    public void dataChanged(){
        notifyDataSetChanged();
    }
}
