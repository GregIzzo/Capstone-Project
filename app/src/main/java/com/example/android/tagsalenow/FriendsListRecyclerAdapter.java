package com.example.android.tagsalenow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.tagsalenow.data.CurrentInfo;

import java.util.List;

public class FriendsListRecyclerAdapter extends RecyclerView.Adapter<FriendsListRecyclerAdapter.FriendsListAdapterViewHolder > {

    private static final String TAG = "ADDAPTER";

    private List<Friends> FRObjectList;

    private final FriendsListAdapterOnClickHandler mClickHandler;
    private Context viewGroupContext;

    public interface FriendsListAdapterOnClickHandler {
        void onClick(int listPosition);//
    }
    public FriendsListRecyclerAdapter(FriendsListAdapterOnClickHandler mClick) {
        mClickHandler = mClick;
    }

    public class FriendsListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView fr_name;

        public FriendsListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            fr_name = itemView.findViewById(R.id.friend_nametv);
            Log.d(TAG, "FriendsListAdapterViewHolder: *** fr_name["+fr_name+"]  ");

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
    public FriendsListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        viewGroupContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.friendslistitem;
        LayoutInflater inflater = LayoutInflater.from(viewGroupContext);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        Log.d(TAG, "onCreateViewHolder: xxxx view=" + view);
        return new FriendsListAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by RecyclerView to display the data at the specified
     * position. In this method, update the contents of the ViewHolder using the "position" argument that is conveniently
     * passed into us.
     *
     * @param FriendsListAdapterViewHolder The FriendsListAdapterViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull FriendsListAdapterViewHolder FriendsListAdapterViewHolder, int position) {
        Log.d(TAG, "onBindViewHolder: BBBBBB viewholder=" + FriendsListAdapterViewHolder);

        FriendsListAdapterViewHolder.fr_name.setText(CurrentInfo.getUserByKey(FRObjectList.get(position).getUserId()).getDisplayName());

    }
    public void addItems(List<Friends> FRObjectList) {
        this.FRObjectList = FRObjectList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if (FRObjectList == null) return 0;
        Log.d(TAG, "getItemCount: XXX ITEM COUNT="+FRObjectList.size());
        return FRObjectList.size();
    }

    public void dataChanged(){
        notifyDataSetChanged();
    }
}
