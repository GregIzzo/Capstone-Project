package com.example.android.tagsalenow;
/*
Display info about a given Tag sale.
Tag Sale info is passed in with the Bundle
 */
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.tagsalenow.data.CurrentInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TagSaleViewFragment extends Fragment {
    private static final String TAG = "TAGSALEVIEWFRAGMENT";
    @BindView(R.id.te_address) TextView te_address;
    @BindView(R.id.te_city) TextView te_city;
    @BindView(R.id.te_state) TextView te_state;
    @BindView(R.id.te_zip) TextView te_zip;
    @BindView(R.id.te_description) TextView te_description;
    @BindView(R.id.te_date) TextView te_date;
    @BindView(R.id.te_time_start) TextView te_time_start;
    @BindView(R.id.te_time_end) TextView te_time_end;
    @BindView(R.id.button_update) Button button_update;

    private Context mContext;
    private TagSaleEventObject tagSaleEventObject;
    //Setup Click listener to report the event to anyone listening
    TagSaleViewFragment.OnButtonClickListener mCallback;

    public interface OnButtonClickListener {
        void onUpdateButtonClicked(String tag);
    }

    public TagSaleViewFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tagsaledetails, container, false);
        ButterKnife.bind(this, rootView);
        //if (getArguments() != null){
        //    tagSaleEventObject = getArguments().getParcelable(MainActivity.MAINACTIVITYKEY);
        //}
        tagSaleEventObject = CurrentInfo.getCurrentTagSaleEventObject();
        Log.d(TAG, "onCreateView: CURRENTTAGSALEOBJECT:" + tagSaleEventObject.getAddress()+" city="+ tagSaleEventObject.getCity());
        if (tagSaleEventObject != null){

            te_address.setText(tagSaleEventObject.getAddress());
            te_city.setText(tagSaleEventObject.getCity());
            te_state.setText(tagSaleEventObject.getState());
            te_zip.setText(tagSaleEventObject.getZip());
            te_description.setText(tagSaleEventObject.getDescription());
            te_date.setText(tagSaleEventObject.getFormattedDate());
            te_time_start.setText(tagSaleEventObject.getStartTime());
            te_time_end.setText(tagSaleEventObject.getEndTime());
        }
        button_update.setVisibility(View.VISIBLE);
        button_update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mCallback.onUpdateButtonClicked(getString(R.string.TAG_FRAGMENT_VIEWTAGSALE));
            }
        });
        return rootView;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

        try {
            mCallback = (TagSaleViewFragment.OnButtonClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " Must implement OnButtonClickListener");
        }
    }
}
