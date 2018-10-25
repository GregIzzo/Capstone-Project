package com.example.android.tagsalenow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.tagsalenow.data.CurrentInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddReviewActivity extends AppCompatActivity {

    @BindView(R.id.te_address)
    TextView te_address;
    @BindView(R.id.te_city)
    TextView te_city;
    @BindView(R.id.te_state)
    TextView te_state;
    @BindView(R.id.te_zip)
    TextView te_zip;
    @BindView(R.id.te_description)
    TextView te_description;
    @BindView(R.id.te_date)
    TextView te_date;
    @BindView(R.id.te_time_start)
    TextView te_time_start;
    @BindView(R.id.te_time_end)
    TextView te_time_end;
    @BindView(R.id.button_cancel)
    Button button_cancel;
    @BindView(R.id.button_add)
    Button button_add;

    @BindView(R.id.te_review)
    TextView te_review;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    private static String TAG = "AddReviewActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addreview);
        ButterKnife.bind(this);
        ratingBar.setRating(3.0f);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "onClick: ");

                FirebaseDatabase mFirebaseDatabase;
                DatabaseReference mTagSaleReviewsDatabaseReference;
                mFirebaseDatabase = FirebaseDatabase.getInstance();
                //Step 1: Get Key of new record



                mTagSaleReviewsDatabaseReference = mFirebaseDatabase.getReference().child("reviews");

                DatabaseReference opRef =  mTagSaleReviewsDatabaseReference.push();
                String key = opRef.getKey();

                TagSaleReviewObject tsr = new TagSaleReviewObject(
                        key,
                        CurrentInfo.getCurrentTagSaleID(),
                        CurrentInfo.getCurrentUser().getUserId(),
                        te_review.getText().toString(),
                        (int)ratingBar.getRating()
                );

                //Add the record and exit
                Log.d(TAG, "ADDING:["+tsr.getTagSaleID()+", "+tsr.getReviewerID()+", "+tsr.getDescription()+", "+tsr.getFiveStarRating()+"");
                opRef.setValue(tsr);
                finish();

            }
        });
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                ratingBar.setRating(rating);

            }
        });


    }
}
