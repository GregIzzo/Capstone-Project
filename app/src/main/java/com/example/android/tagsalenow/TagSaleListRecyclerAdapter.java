package com.example.android.tagsalenow;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.tagsalenow.data.CurrentInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagSaleListRecyclerAdapter extends RecyclerView.Adapter<TagSaleListRecyclerAdapter.TagSaleListAdapterViewHolder > {

    private static final String TAG = "ADDAPTER";

    private List<TagSaleEventObject> TSEObjectList;

    private final TagSaleListAdapterOnClickHandler mClickHandler;
    private Context viewGroupContext;

    private final TagSaleListAdapterOnAttendClickHandler mAttendClickHandler;


    public interface TagSaleListAdapterOnClickHandler {
        void onClick(int listPosition);//
    }
    public interface TagSaleListAdapterOnAttendClickHandler {
        void onAttendClick(Map<String, Object> dataMap);//
    }
    public TagSaleListRecyclerAdapter(TagSaleListAdapterOnClickHandler mClick,
                                      TagSaleListAdapterOnAttendClickHandler mAttendClick) {
        mClickHandler = mClick;
        mAttendClickHandler = mAttendClick;
    }



    public class TagSaleListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public final ImageView indicator_iv;
        public final TextView ts_placetv;
        public final TextView ts_friendsattendingtv;
        public final TextView ts_datetv;
        public final TextView ts_distancetv;
        public final CheckBox ts_planningtoattendcb;

        public TagSaleListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            indicator_iv = itemView.findViewById(R.id.indicator_iv);
            ts_placetv = itemView.findViewById(R.id.ts_placetv);
            ts_friendsattendingtv = itemView.findViewById(R.id.ts_friendsattendingtv);
            ts_datetv = itemView.findViewById(R.id.ts_datetv);
            ts_distancetv = itemView.findViewById(R.id.ts_distancetv);
            ts_planningtoattendcb = itemView.findViewById(R.id.ts_planningtoattendcb);
            ts_planningtoattendcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int adapterPosition = getAdapterPosition();
                    Map<String, Object> dataMap = new HashMap<String,Object>();
                    dataMap.put("position", adapterPosition);
                    dataMap.put("state",b );
                    mAttendClickHandler.onAttendClick(dataMap);

                }
            });
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
       tagSaleListAdapterViewHolder.ts_placetv.setText(TSEObjectList.get(position).getLocationId());
       tagSaleListAdapterViewHolder.ts_datetv.setText(TSEObjectList.get(position).getFormattedDate());
       tagSaleListAdapterViewHolder.ts_friendsattendingtv.setText(R.string.fake_friendsattending);
       //DISTANCE
        if (CurrentInfo.getCurrentLocationObject() == null){
            //no location service so show ?
            tagSaleListAdapterViewHolder.ts_distancetv.setText("?");
        } else {
            Location location = CurrentInfo.getCurrentLocationObject();
            float[] results = new float[1];
            Double tsLat = TSEObjectList.get(position).getLat();
            Double tsLon = TSEObjectList.get(position).getLon();
            if (tsLat == 0.01 && tsLon == 0.01){
                //fake lat long
                tagSaleListAdapterViewHolder.ts_distancetv.setText("?");
            } else {
                Double fromlat = location.getLatitude();
                Double fromlong = location.getLongitude();
                Log.d(TAG, "onBindViewHolder: from:" + fromlat + "," + fromlong + " to:" + TSEObjectList.get(position).getLat() + "," + TSEObjectList.get(position).getLon());
                Location.distanceBetween(fromlat,
                        fromlong,
                        TSEObjectList.get(position).getLat(),
                        TSEObjectList.get(position).getLon(),
                        results);
                if (results.length > 0) {
                    double distanceMeters = results[0] * 0.00062137;
                    //distance in meters. Miles= meters * 0.00062137
                    tagSaleListAdapterViewHolder.ts_distancetv.setText(String.format("%.1f", distanceMeters));

                } else {
                    tagSaleListAdapterViewHolder.ts_distancetv.setText("?");
                }
            }
        }
    }
    public void addItems(List<TagSaleEventObject> TSEObjectList) {
        this.TSEObjectList = TSEObjectList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if (TSEObjectList == null) return 0;
        Log.d(TAG, "getItemCount: XXX ITEM COUNT="+TSEObjectList.size());
        return TSEObjectList.size();
    }

    public void dataChanged(){
        notifyDataSetChanged();
    }
    public TagSaleEventObject getItemAt(int position){
        if (this.TSEObjectList.size()< position + 1){
            return null;
        } else {
            //list is large enough for the given position
            return this.TSEObjectList.get(position);
        }
    }
 }
