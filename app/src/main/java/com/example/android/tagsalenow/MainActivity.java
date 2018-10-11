package com.example.android.tagsalenow;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.tagsalenow.data.TagSaleEventsViewModel;
import com.example.android.tagsalenow.sync.SunshineSyncUtils;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private AdView mAdView;//Needed for AdMob Ad

    private static final String TAG = "MainActivity";

    public static final String ANONYMOUS = "anonymous";
    public static final int RC_SIGN_IN = 1;


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private DatabaseReference mTagSaleEventsDatabaseReference;
    private ChildEventListener mTagSaleEvent_ChildEventListener;

    TagSaleListFragment tagSaleListFragment;

    private String mUsername;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages");
        mTagSaleEventsDatabaseReference = mFirebaseDatabase.getReference().child("tagsaleevents");

//

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        ///

        Log.d(TAG, "onCreate: ***** CHECK API KEYS: ["+BuildConfig.MY_OPENWEATHERMAPORG_API_KEY+"]");

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    // Name, email address, and profile photo Url
                    String name = user.getDisplayName();
                    String email = user.getEmail();
                    Uri photoUrl = user.getPhotoUrl();

                    // Check if user's email is verified
                    boolean emailVerified = user.isEmailVerified();

                    // The user's ID, unique to the Firebase project. Do NOT use this value to
                    // authenticate with your backend server, if you have one. Use
                    // FirebaseUser.getToken() instead.
                    String uid = user.getUid();

                    Log.d(TAG, "onAuthStateChanged: *** name["+name+"] emailverified["+emailVerified+"] email["+email+"] photourl["+photoUrl+"]");


                    onSignedInInitialize(user.getDisplayName());

                    showTagSaleList();
                } else {
                    // User is signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .setIsSmartLockEnabled(false)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    // Create Ad
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        SunshineSyncUtils.initialize(this);
    }
    private void showTagSaleList() {
        //connect DB
        TagSaleEventsViewModel viewModel = ViewModelProviders.of(this).get(TagSaleEventsViewModel.class);
        LiveData<DataSnapshot> liveData = viewModel.getDataSnapshotLiveData();

        tagSaleListFragment = new TagSaleListFragment();
        Log.d(TAG, "showTagSaleList: TTTTT tagSaleListFragment =" + tagSaleListFragment);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.tagsalelist_container, tagSaleListFragment,getString(R.string.TAG_FRAGMENT_TAGSALELIST))
                .commit();
/*
        TagSaleEventObject tobj = new TagSaleEventObject("200 Hill Top Dr.", "Mr. Jones",
                "Nov 30", "8 am", "3 pm", "family stuff", "" );
        mTagSaleEventsDatabaseReference.push().setValue(tobj);
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
       //TODO mMessageAdapter.clear();
        detachDatabaseReadListener();
    }
    private void onSignedInInitialize(String username) {
        mUsername = username;
        Log.d(TAG, "onSignedInInitialize: -- username="+username);
        attachDatabaseReadListener();
    }
    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
      //TODO  mMessageAdapter.clear();
        detachDatabaseReadListener();
    }
    private void attachDatabaseReadListener() {
        Log.d(TAG, "attachDatabaseReadListener: Start");
        if (mTagSaleEvent_ChildEventListener == null) {
            Log.d(TAG, "attachDatabaseReadListener: Setting up  mTagSaleEvent_ChildEventListener");
            mTagSaleEvent_ChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    TagSaleEventObject tagSaleEventObject = dataSnapshot.getValue(TagSaleEventObject.class);
                    Log.d(TAG, "onChildAdded: !!!!");
                    //NEED TO SEND THE CHANGE INFO TO fragment
                    // mMessageAdapter.add(tagSaleEventObject);
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Log.d(TAG, "onChildChanged: !!!!");
                }
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            mMessagesDatabaseReference.addChildEventListener(mTagSaleEvent_ChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mTagSaleEvent_ChildEventListener != null) {
            mMessagesDatabaseReference.removeEventListener(mTagSaleEvent_ChildEventListener);
            mTagSaleEvent_ChildEventListener = null;
        }
    }

}
