package com.example.android.tagsalenow;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.tagsalenow.data.CurrentInfo;

public class ViewTagSaleActivity extends AppCompatActivity implements  FriendsListFragment.OnButtonClickListener,
ReviewListFragment.OnButtonClickListener, TagSaleViewFragment.OnButtonClickListener{

    private static final String TAG = "ViewTagSaleActivity";
    TagSaleViewFragment tagSaleViewFragment;
    ReviewListFragment reviewListFragment;


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewtagsale);

        //EXPECT CurrentInfo.currentTagSaleEventObject to contain info
        Log.d(TAG, "CHOSEN TAGSALEID:"+CurrentInfo.getCurrentTagSaleID());
        tagSaleViewFragment = new TagSaleViewFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.tagsaledetail_container, tagSaleViewFragment,getString(R.string.TAG_FRAGMENT_VIEWTAGSALE))
                .commit();

        reviewListFragment = new ReviewListFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.tagsalereviews_container, reviewListFragment,getString(R.string.TAG_FRAGMENT_REVIEWLIST))
                .commit();
    }

    @Override
    public void onAddButtonClicked(String tag)  {
        Log.d(TAG, "onAddButtonClicked: TAG=" + tag);
         if(tag == getString(R.string.TAG_FRAGMENT_REVIEWLIST)){
             Intent intent = new Intent( this, AddReviewActivity.class);
             startActivity(intent);

         }

    }

    @Override
    public void onUpdateButtonClicked(String tag) {
        //launch Update Tag Sale
        if(tag == getString(R.string.TAG_FRAGMENT_VIEWTAGSALE)){
            Intent intent = new Intent( this, AddTagSaleActivity.class);
            intent.putExtra("TAGSALEID", CurrentInfo.getCurrentTagSaleID());

            startActivity(intent);

        }
    }
}
