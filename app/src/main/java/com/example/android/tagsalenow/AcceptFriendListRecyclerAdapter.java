package com.example.android.tagsalenow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.tagsalenow.data.CurrentInfo;

import java.util.List;

public class AcceptFriendListRecyclerAdapter extends RecyclerView.Adapter<AcceptFriendListRecyclerAdapter.AcceptFriendListAdapterViewHolder > {

    private static final String TAG = "ACCEPTFRIENDADDAPTER";
    private List<FriendRequestObject> FRObjectList;
    private final AcceptFriendListAdapterOnClickHandler mClickHandler;
    private Context viewGroupContext;
    private Button acceptButton;

    public interface AcceptFriendListAdapterOnClickHandler {
        void onClick(int listPosition);//
    }
    public AcceptFriendListRecyclerAdapter(AcceptFriendListAdapterOnClickHandler mClick) {
        mClickHandler = mClick;
    }
    public class AcceptFriendListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView fr_name;
        public AcceptFriendListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            fr_name = itemView.findViewById(R.id.friend_nametv);
            acceptButton = itemView.findViewById(R.id.button_acceptFriend);
            Log.d(TAG, "AcceptFriendListAdapterViewHolder: *** fr_name["+fr_name+"]  ");
           // itemView.setOnClickListener(this);
            acceptButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();

            mClickHandler.onClick(adapterPosition);
        }
    }

    @NonNull
    @Override
    public AcceptFriendListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        viewGroupContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.acceptfriendlistitem;
        LayoutInflater inflater = LayoutInflater.from(viewGroupContext);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        Log.d(TAG, "onCreateViewHolder: xxxx view=" + view);
        return new AcceptFriendListAdapterViewHolder(view);
    }



    /**
     * OnBindViewHolder is called by RecyclerView to display the data at the specified
     * position. In this method, update the contents of the ViewHolder using the "position" argument that is conveniently
     * passed into us.
     *
     * @param AcceptFriendListAdapterViewHolder The AcceptFriendListAdapterViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull AcceptFriendListAdapterViewHolder AcceptFriendListAdapterViewHolder, int position) {
        Log.d(TAG, "onBindViewHolder: ACCEPTFRIEND viewholder=" + AcceptFriendListAdapterViewHolder);
        FriendRequestObject fr = FRObjectList.get(position);
        if (fr == null) return;
        TagSaleUserObject tso = CurrentInfo.getUserByKey(fr.getFromUserId());
        if (tso == null) return;
        AcceptFriendListAdapterViewHolder.fr_name
                .setText(tso.getDisplayName());
    }
    public void addItems(List<FriendRequestObject> FRObjectList) {
        Log.d(TAG, "addItems: ADDING LIST OF ONEFRIEND:" + FRObjectList.toString());
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
    public FriendRequestObject getAtOffset(int position){
        if (position >= FRObjectList.size()) return null;
        return FRObjectList.get(position);
    }
}
