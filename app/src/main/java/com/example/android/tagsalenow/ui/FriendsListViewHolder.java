package com.example.android.tagsalenow.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.tagsalenow.R;

public class FriendsListViewHolder extends RecyclerView.ViewHolder  {
    public TextView friendName;
    //friend_nametv
    public FriendsListViewHolder(@NonNull View itemView) {
        super(itemView);
        friendName =  itemView.findViewById(R.id.friend_nametv);
    }
}
