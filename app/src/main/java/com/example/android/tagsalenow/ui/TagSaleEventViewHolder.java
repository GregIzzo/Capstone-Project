package com.example.android.tagsalenow.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.tagsalenow.R;

public class TagSaleEventViewHolder extends RecyclerView.ViewHolder {

    public ImageView imHereIndicator;
    public TextView placeName;
    public TextView friendsAttending;
    public TextView tagSaleDate;
    public TextView tagSaleDistance;
    public TextView planningToAttend;

    public TagSaleEventViewHolder(@NonNull View itemView) {
        super(itemView);
        imHereIndicator =  itemView.findViewById(R.id.onsite_indicator_iv);
        placeName = itemView.findViewById(R.id.ts_placetv);
        friendsAttending = itemView.findViewById(R.id.ts_friendsattendingtv);
        tagSaleDate = itemView.findViewById(R.id.ts_datetv);
        tagSaleDistance = itemView.findViewById(R.id.ts_distancetv);
        planningToAttend = itemView.findViewById(R.id.ts_planningtoattendcb);



    }
}
