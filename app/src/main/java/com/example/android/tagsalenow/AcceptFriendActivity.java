package com.example.android.tagsalenow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class AcceptFriendActivity extends AppCompatActivity {
    private static final String TAG = "ACCEPTFRIENDLISTFRAGMENT";
    private RecyclerView recyclerView;
    private FriendsListRecyclerAdapter friendsListRecyclerAdapter;
    private AcceptFriendFragment acceptFriendFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptfriendrequest);//friendrequest_container

        acceptFriendFragment = new AcceptFriendFragment();
        Log.d(TAG, "Create: TTTTT acceptFriendFragment =" + acceptFriendFragment);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.acceptfriend_container, acceptFriendFragment,getString(R.string.TAG_FRAGMENT_TAGSALELIST))
                .commit();

    }
}
