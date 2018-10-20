package com.example.android.tagsalenow;

import android.icu.text.TimeZoneNames;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.android.tagsalenow.ui.DatePickerFragment;
import com.example.android.tagsalenow.ui.TimePickerFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTagSaleActivity extends AppCompatActivity implements TimePickerFragment.TimePickedListener, DatePickerFragment.DatePickerListener {

    private static String TIMESTARTTAG = "timeStartTag";
    private static String TIMEENDTAG = "timeEndTag";
    private static String DATETAG = "date";
    @BindView(R.id.te_address) TextView te_address;
    @BindView(R.id.te_city) TextView te_city;
    @BindView(R.id.te_state) TextView te_state;
    @BindView(R.id.te_zip) TextView te_zip;
    @BindView(R.id.te_description) TextView te_description;
    @BindView(R.id.te_date) TextView te_date;
    @BindView(R.id.te_time_start) TextView te_time_start;
    @BindView(R.id.te_time_end) TextView te_time_end;
    @BindView(R.id.button_cancel) Button button_cancel;
    @BindView(R.id.button_add) Button button_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtagsale);
        ButterKnife.bind(this);
        te_date.setOnClickListener(new View.OnClickListener() {
            //when clicked, launch the picker dialog
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), DATETAG);
            }
        });
        te_time_start.setOnClickListener(new View.OnClickListener() {
            //when clicked, launch the picker dialog
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), TIMESTARTTAG);
            }
        });
        te_time_end.setOnClickListener(new View.OnClickListener() {
            //when clicked, launch the picker dialog
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), TIMEENDTAG);
            }
        });
        button_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //if fields are valid then save record
                 FirebaseDatabase mFirebaseDatabase;
                DatabaseReference mTagSaleEventsDatabaseReference;
                mFirebaseDatabase = FirebaseDatabase.getInstance();
                mTagSaleEventsDatabaseReference = mFirebaseDatabase.getReference().child("tagsaleevents");
                Log.d("ADDDD", "onClick: ABOUT TO ADD RECORD");
                StringBuilder location = new StringBuilder();
                location.append(te_address.getText());
                location.append(", ");
                location.append(te_city.getText());
                location.append(", ");
                location.append(te_state.getText());
                location.append(", ");
                location.append(te_zip.getText());
                String temporaryTags = "";
                TagSaleEventObject tso = new TagSaleEventObject(location.toString(),
                        "Fred",
                        te_date.getText().toString(),
                        te_time_start.getText().toString(),
                        te_time_end.getText().toString(),
                        te_description.getText().toString(),
                        temporaryTags);
                //mMessagesDatabaseReference.push().setValue(friendlyMessage);
                mTagSaleEventsDatabaseReference.push().setValue(tso);
                finish();
            }
        });
        button_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onTimePicked(String time) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        //Determine which field was using the TimePicker
        if (fragmentManager.findFragmentByTag(TIMESTARTTAG) != null)
        {
            te_time_start.setText(time);
        }
        if (fragmentManager.findFragmentByTag(TIMEENDTAG) != null)
        {
            te_time_end.setText(time);
        }
    }

    @Override
    public void onDatePicked(String dateString) {
        te_date.setText(dateString);
    }
}
