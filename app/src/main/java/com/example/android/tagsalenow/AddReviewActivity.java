package com.example.android.tagsalenow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

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

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");

            }
        });
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}
