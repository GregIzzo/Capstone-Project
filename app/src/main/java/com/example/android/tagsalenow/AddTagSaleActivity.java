package com.example.android.tagsalenow;

import android.icu.text.TimeZoneNames;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.android.tagsalenow.data.CurrentInfo;
import com.example.android.tagsalenow.data.LatLng;

import com.example.android.tagsalenow.ui.DatePickerFragment;
import com.example.android.tagsalenow.ui.TimePickerFragment;
import com.example.android.tagsalenow.utils.Utilities;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTagSaleActivity extends AppCompatActivity implements TimePickerFragment.TimePickedListener, DatePickerFragment.DatePickerListener {

    private static String TAG = "ADDTAGSALEACTIVITY";
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
    @BindView(R.id.button_update) Button button_update;
    private Boolean updateRecord = false;
    private TagSaleEventObject tseToUpdate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Check for passed in TagSaleID. If it exists, then treat this activity as an UPDATE
        setContentView(R.layout.activity_addtagsale);
        ButterKnife.bind(this);
        if (getIntent().hasExtra("TAGSALEID")){
            updateRecord = true;
            tseToUpdate = CurrentInfo.getCurrentTagSaleEventObject();
            te_address.setText(tseToUpdate.getAddress());
            te_city.setText(tseToUpdate.getCity());
            te_state.setText(tseToUpdate.getState());
            te_zip.setText(tseToUpdate.getZip());
            te_description.setText(tseToUpdate.getDescription());
            te_date.setText(tseToUpdate.getDate());
            te_time_start.setText(tseToUpdate.getStartTime());
            te_time_end.setText(tseToUpdate.getEndTime());
            button_update.setVisibility(View.VISIBLE);
            button_add.setVisibility(View.GONE);
            button_update.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    //if fields are valid then save record
                    
                    
                    Log.d("UPDATE", "onClick: ABOUT TO ADD RECORD");
                    StringBuilder location = new StringBuilder();
                    location.append(te_address.getText());
                    location.append(", ");
                    location.append(te_city.getText());
                    location.append(", ");
                    location.append(te_state.getText());
                    location.append(", ");
                    location.append(te_zip.getText());

                    String temporaryTags = "";
                    
                    //Step 1: Get Key of new record
                   // DatabaseReference opRef =  mTagSaleEventsDatabaseReference.push();
                   // final String key = opRef.getKey();
                    final String key = tseToUpdate.getId();
                    
                    tseToUpdate.setLocationId(location.toString());
                    tseToUpdate.setAddress(te_address.getText().toString());
                    tseToUpdate.setCity(te_city.getText().toString());
                    tseToUpdate.setState(te_state.getText().toString());
                    tseToUpdate.setZip(te_zip.getText().toString());
                    tseToUpdate.setDate(te_date.getText().toString());
                    tseToUpdate.setStartTime(te_time_start.getText().toString());
                    tseToUpdate.setEndTime(te_time_end.getText().toString());
                    tseToUpdate.setDescription(te_description.getText().toString());

                    
                    DatabaseReference mTagSaleEventsDatabaseReference;
                    mTagSaleEventsDatabaseReference = FirebaseDatabase.getInstance().getReference().child("tagsaleevents");
                     Map<String, Object> childUpdates = new HashMap<>();
                    final String addrString = location.toString();
                    childUpdates.put(tseToUpdate.getId(), tseToUpdate);
                    mTagSaleEventsDatabaseReference.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            Log.d(TAG, "onComplete: UPDATEDTAGSALERECORD ");
                            Utilities.setTagSaleLocationData(getApplicationContext(),key,addrString);
                        }
                    });
 

                    finish();
                }
            });

        } else {
            button_update.setVisibility(View.GONE);
            //ADD BUTTON FOR NEW RECORDS
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
                    //Step 1: Get Key of new record
                    DatabaseReference opRef =  mTagSaleEventsDatabaseReference.push();
                    final String key = opRef.getKey();

                    TagSaleEventObject tso = new TagSaleEventObject(key, location.toString(),
                            te_address.getText().toString(),
                            te_city.getText().toString(),
                            te_state.getText().toString(),
                            te_zip.getText().toString(),
                            CurrentInfo.getCurrentUser().getUserId(),
                            te_date.getText().toString(),
                            te_time_start.getText().toString(),
                            te_time_end.getText().toString(),
                            te_description.getText().toString(),
                            temporaryTags,
                            0.01,
                            0.01
                    );
                    //mMessagesDatabaseReference.push().setValue(friendlyMessage);
                    // tso.setLocationId(key);
                    //mTagSaleEventsDatabaseReference.push().setValue(tso);
                    final String addrString = location.toString();
                    opRef.setValue(tso, new DatabaseReference.CompletionListener() {
                                //on completion, fetch geolocation
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    //-----------GET GEOLOCATION
                                    // LatLng whatGeoLatLng = Utilities.getLocationFromAddress(getApplicationContext(), location.toString());
                                    Utilities.setTagSaleLocationData(getApplicationContext(),key,addrString);
                                    //--------------------------------------
                                }
                            }
                    );


                    finish();
                }
            });

        }
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
