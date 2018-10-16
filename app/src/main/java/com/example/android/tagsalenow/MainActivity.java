package com.example.android.tagsalenow;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.tagsalenow.data.TagSaleEventsViewModel;
import com.example.android.tagsalenow.data.WeatherContract;
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

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, TagSaleListFragment.OnButtonClickListener {
    private AdView mAdView;//Needed for AdMob Ad

    //NOTE : WEATHER DB AND LOADING IS FROM SUNSHINE APP, FROM UDACITY ANDROID NANO DEGREE COURSE
    private static final int ID_FORECAST_LOADER = 47;
    /*
     * The columns of data that we are interested in displaying within our MainActivity's list of
     * weather data.
     */
    public static final String[] MAIN_FORECAST_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.WeatherEntry.COLUMN_DESCRIPTION
    };

    /*
     * We store the indices of the values in the array of Strings above to more quickly be able to
     * access the data from our query. If the order of the Strings above changes, these indices
     * must be adjusted to match the order of the Strings.
     */
    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_MAX_TEMP = 1;
    public static final int INDEX_WEATHER_MIN_TEMP = 2;
    public static final int INDEX_WEATHER_CONDITION_ID = 3;
    ///////////////////////////////////////////
    private static final String TAG = "MainActivity";

    public static final String ANONYMOUS = "anonymous";
    public static final int RC_SIGN_IN = 1;


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private DatabaseReference mTagSaleEventsDatabaseReference;
    private ChildEventListener mTagSaleEvent_ChildEventListener;

    TagSaleListFragment tagSaleListFragment;
    WeatherFragment weatherFragment;

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
        Context myContext = this;
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

                 //   getSupportLoaderManager().initLoader(ID_FORECAST_LOADER, null, myContext);

                    SunshineSyncUtils.initialize(getApplicationContext());
                    showTagSaleList();
                    showWeatherDataView();
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

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {


        switch (loaderId) {

            case ID_FORECAST_LOADER:
                /* URI for all rows of weather data in our weather table */
                Uri forecastQueryUri = WeatherContract.WeatherEntry.CONTENT_URI;
                /* Sort order: Ascending by date */
                String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
                /*
                 * A SELECTION in SQL declares which rows you'd like to return. In our case, we
                 * want all weather data from today onwards that is stored in our weather table.
                 * We created a handy method to do that in our WeatherEntry class.
                 */
                String selection = WeatherContract.WeatherEntry.getSqlSelectForTodayOnwards();

                return new CursorLoader(this,
                        forecastQueryUri,
                        MAIN_FORECAST_PROJECTION,
                        selection,
                        null,
                        sortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    /**
     * Called when a Loader has finished loading its data.
     *
     * NOTE: There is one small bug in this code. If no data is present in the cursor do to an
     * initial load being performed with no access to internet, the loading indicator will show
     * indefinitely, until data is present from the ContentProvider. This will be fixed in a
     * future version of the course.
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
       // mForecastAdapter.swapCursor(data);
       // if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
      //  mRecyclerView.smoothScrollToPosition(mPosition);
       // if (data.getCount() != 0)
            updateWeatherData(data);
    }

    /**
     * Called when a previously created loader is being reset, and thus making its data unavailable.
     * The application should at this point remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        /*
         * Since this Loader's data is now invalid, we need to clear the Adapter that is
         * displaying the data.
         */
       // mForecastAdapter.swapCursor(null);
    }
    private void showWeatherDataView() {
        /* First, hide the loading indicator */
        //mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Finally, make sure the weather data is visible */
        weatherFragment = new WeatherFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.weather_container, weatherFragment,getString(R.string.TAG_FRAGMENT_WEATHERPANEL))
                .commit();


    }
    private void updateWeatherData(Cursor data){
        Log.d(TAG, "updateWeatherData: --- WeatherData In MAIN:"+data.toString());
    }

    @Override
    public void onAddButtonClicked() {
        Intent intent = new Intent(this, AddTagSaleActivity.class);
        startActivity(intent);
    }
}
