package com.example.android.tagsalenow;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class ViewTagSaleActivity extends AppCompatActivity {

    TagSaleViewFragment tagSaleViewFragment;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewtagsale);

        //EXPECT CurrentInfo.currentTagSaleEventObject to contain info

        tagSaleViewFragment = new TagSaleViewFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.tagsaledetail_container, tagSaleViewFragment,getString(R.string.TAG_FRAGMENT_VIEWTAGSALE))
                .commit();

    }
}
