package com.example.android.tagsalenow;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.tagsalenow.data.CurrentInfo;
import com.example.android.tagsalenow.data.WeatherContract;
import com.example.android.tagsalenow.sync.SunshineSyncUtils;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, TagSaleListFragment.OnButtonClickListener,
        FriendsListFragment.OnButtonClickListener{
    private AdView mAdView;//Needed for AdMob Ad

    //NOTE : WEATHER DB AND LOADING IS FROM SUNSHINE APP, FROM UDACITY ANDROID NANO DEGREE COURSE
    private static final int ID_FORECAST_LOADER = 47;

    private static final String REQUEST_LOCATION_UPDATES_KEY= "requestlocationupdateskey";
    private static final String PERMISSION_SEQUENCE_GOING= "permissionsequencegoing";

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
    public static final String MAINACTIVITYKEY = "mainactivity";
    public static final int RC_SIGN_IN = 1;

    //LOCATION PERMISSION REQUEST CONSTANT:
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 25;
    private boolean permissionSequenceGoing = true;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private DatabaseReference mTagSaleEventsDatabaseReference;
    private DatabaseReference mTagSaleUsersDatabaseReference;
    private DatabaseReference mFriendsDatabaseReference;
    private DatabaseReference mReviewsDatabaseReference;

    private ChildEventListener mTagSaleEvent_ChildEventListener;
    private ChildEventListener mTagSaleUser_ChildEventListener;
    private ChildEventListener mFriends_ChildEventListener;
    private ChildEventListener mReviews_ChildEventListener;

    TagSaleListFragment tagSaleListFragment;
    FriendsListFragment friendsListFragment;
    WeatherFragment weatherFragment;

    private String mUsername;
    private TagSaleUserObject mCurrentUserObject;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private static FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    //private boolean mRequestingLocationUpdates=false;//check permissions first, then auth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateValuesFromBundle(savedInstanceState);//restore saved
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: FirebaseDatabase.getInstance()");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        Log.d(TAG, "onCreate: FirebaseAuth.getInstance()");
        mFirebaseAuth = FirebaseAuth.getInstance();

        Log.d(TAG, "onCreate: mFirebaseDatabase.getReference(messages)");
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages");
        Log.d(TAG, "onCreate: mFirebaseDatabase.getReference(tagsaleevents)");
        mTagSaleEventsDatabaseReference = mFirebaseDatabase.getReference().child("tagsaleevents");
        Log.d(TAG, "onCreate: mFirebaseDatabase.getReference(tagsaleusers)");
        mTagSaleUsersDatabaseReference = mFirebaseDatabase.getReference().child("tagsaleusers");
       // mFriendsDatabaseReference= mFirebaseDatabase.getReference().child("friends");
        Log.d(TAG, "onCreate: mFirebaseDatabase.getReference(reviews)");
        mReviewsDatabaseReference = mFirebaseDatabase.getReference().child("reviews");
//

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //setup Location Services:

        // code from Google: https://developer.android.com/training/location/retrieve-current
        Log.d(TAG, "onCreate: LocationServices.getFusedLocationProviderClient(this)");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //Get Periodic updates about location
        //routine below is from : https://developer.android.com/training/location/receive-location-updates
        Context myContext = this;

        //Snippet below based on code from : https://developer.android.com/training/permissions/requesting


     // Create Ad
        mAdView = findViewById(R.id.adView);
        Log.d(TAG, "onCreate: new AdRequest.Builder().build();");

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //GetLocationInfo will check location permissions
        Log.d(TAG, "onCreate: getLocationInfo();");
        getLocationInfo();

    }
    private void startAuth(){
        Log.d(TAG, " startAuth() start");

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    // Name, email address, and profile photo Url
                    String uid = user.getUid();
                    String name = user.getDisplayName();
                    String email = user.getEmail();
                    Uri photoUrl = user.getPhotoUrl();

                    // Check if user's email is verified
                    boolean emailVerified = user.isEmailVerified();

                    // The user's ID, unique to the Firebase project. Do NOT use this value to
                    // authenticate with your backend server, if you have one. Use
                    // FirebaseUser.getToken() instead.
                    // String uid = user.getUid();

                    Log.d(TAG, "onAuthStateChanged: *** name["+name+"] emailverified["+emailVerified+"] email["+email+"] photourl["+photoUrl+"]");
                    String currentDateString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    String photostring = "";
                    if (photoUrl != null) photostring =  photoUrl.toString();
                    String onSiteTagSaleId = "";
                    mCurrentUserObject = new TagSaleUserObject(uid, currentDateString, name, email, photostring,onSiteTagSaleId);
                    onSignedInInitialize(mCurrentUserObject);

                    //   getSupportLoaderManager().initLoader(ID_FORECAST_LOADER, null, myContext);

                    SunshineSyncUtils.initialize(getApplicationContext());
                    showTagSaleList();
                    showFriendsList();
                    showWeatherDataFragment();
                } else {
                    // User is signed out
                    onSignedOutCleanup();
                    removeTagSaleList();
                    removeFriendsList();
                    removeWeatherDataFragment();
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

    }
    private void showTagSaleList() {
        //connect DB
        //TagSaleEventsViewModel viewModel = ViewModelProviders.of(this).get(TagSaleEventsViewModel.class);
        //LiveData<TagSaleEventObject> liveData = viewModel.getTagSaleEventObjectLiveData();
        //Remove old fragment if it exists
        removeTagSaleList();
        tagSaleListFragment = new TagSaleListFragment();
        Log.d(TAG, "showTagSaleList: TTTTT tagSaleListFragment =" + tagSaleListFragment);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.tagsalelist_container, tagSaleListFragment,getString(R.string.TAG_FRAGMENT_TAGSALELIST))
                .commit();
    }
    private void removeTagSaleList(){
        TagSaleListFragment prevFragment = (TagSaleListFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.TAG_FRAGMENT_TAGSALELIST));
        if(prevFragment != null)
            getSupportFragmentManager().beginTransaction().remove(prevFragment).commit();
    }
    private void showFriendsList() {
        //Remove old fragment if it exists:
        removeFriendsList();
        friendsListFragment = new FriendsListFragment();
        Log.d(TAG, "showFriendsList: TTTTT friendsListFragment =" + friendsListFragment);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.friendlist_container, friendsListFragment,getString(R.string.TAG_FRAGMENT_FRIENDLIST))
                .commit();
    }
    private void removeFriendsList(){
        FriendsListFragment prevFragment = (FriendsListFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.TAG_FRAGMENT_FRIENDLIST));
        if(prevFragment != null)
            getSupportFragmentManager().beginTransaction().remove(prevFragment).commit();
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
        Log.d(TAG, " onResume() start");
        if (mFirebaseAuth != null && mAuthStateListener != null) {
            mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        }
        //if (mRequestingLocationUpdates) {
        //    startLocationUpdates();
        //}
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        stopLocationUpdates();
        detachDatabaseReadListener();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //outState.putBoolean(REQUEST_LOCATION_UPDATES_KEY,mRequestingLocationUpdates);
        outState.putBoolean(PERMISSION_SEQUENCE_GOING,permissionSequenceGoing);
        // ...
        super.onSaveInstanceState(outState);
    }
    private void onSignedInInitialize(TagSaleUserObject userObject) {
        mUsername = userObject.getDisplayName();
        Log.d(TAG, "onSignedInInitialize: -- username="+mUsername);
        CurrentInfo.setCurrentUser(userObject);
        //add record ifit doesn't exist
        attachDatabaseReadListener();
        if (mCurrentUserObject != null){
            //add/update current user record
            String whatId = mCurrentUserObject.getUserId();
            mTagSaleUsersDatabaseReference.child(whatId).setValue(mCurrentUserObject);

        }

    }
    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
        detachDatabaseReadListener();
    }
    private void attachDatabaseReadListener() {
        //
        // LISTEN FOR TAGSALEEVENT CHANGES
        //
        Log.d(TAG, " attachDatabaseReadListener() start");

        if (mTagSaleEvent_ChildEventListener == null) {
             mTagSaleEvent_ChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    TagSaleEventObject tagSaleEventObject = dataSnapshot.getValue(TagSaleEventObject.class);
                    Log.d(TAG, "TAGSALEEVENT ChildAdded:("+s+") !!!!:" + tagSaleEventObject.getLocationId());
                    CurrentInfo.addEvent(tagSaleEventObject);
                    //NEED TO SEND THE CHANGE INFO TO fragment
                    // mMessageAdapter.add(tagSaleEventObject);
                }
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    TagSaleEventObject tagSaleEventObject = dataSnapshot.getValue(TagSaleEventObject.class);
                    Log.d(TAG, "TAGSALEEVENT ChildChanged: TagSaleEvent !!!!:"+tagSaleEventObject.getLocationId());
                }
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {
                    //ERROR - probably a permission problem
                }
            };
            mTagSaleEventsDatabaseReference.addChildEventListener(mTagSaleEvent_ChildEventListener);
        }
        //
        // LISTEN FOR TAGSALEUSER CHANGES
        //
        if (mTagSaleUser_ChildEventListener == null){
            mTagSaleUser_ChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    //When first attached, this method is called for every object in the DB
                    Log.d(TAG, "TAGSALEUSER onChildAdded: added:" + dataSnapshot.toString());
                    TagSaleUserObject tagSaleUserObject = dataSnapshot.getValue(TagSaleUserObject.class);
                    CurrentInfo.addUser(tagSaleUserObject);
                }
                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    TagSaleUserObject tagSaleUserObject = dataSnapshot.getValue(TagSaleUserObject.class);
                    Log.d(TAG, "TAGSALEUSER ChildChanged: updated:" + dataSnapshot.toString());
                }
                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                }
                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            mTagSaleUsersDatabaseReference.addChildEventListener(mTagSaleUser_ChildEventListener);
        }

         //
        // LISTEN FOR FRIENDS CHANGES
        // NOTE: NEED CURRENT USER ID IN ORDER TO GET FRIEND LIST, SO CREATE FRIENDSDB LINK HERE, AFTER WE
        // KNOW THE USER ID
        //mFriendsDatabaseReference= mFirebaseDatabase.getReference().child("friends").child(CurrentInfo.getCurrentUser().getFromUserId());
        mFriendsDatabaseReference= mFirebaseDatabase.getReference().child("friends").child(CurrentInfo.getCurrentUser().getUserId());
        if (mFriends_ChildEventListener == null){
            mFriends_ChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    //When first attached, this method is called for every object in the DB
                    Log.d(TAG, "FRIENDS-- onChildAdded: added:" + dataSnapshot.toString());
                    OneFriend friends = new OneFriend(dataSnapshot.getKey());
                }
                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    OneFriend friends = dataSnapshot.getValue(OneFriend.class);
                    Log.d(TAG, "FRIENDS onChildAdded: changed:" + friends.getUserId());
                }
                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                }
                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            mFriendsDatabaseReference.addChildEventListener(mFriends_ChildEventListener);
        }
        //
        // LISTEN FOR REVIEWS CHANGES
        //
        if (mReviews_ChildEventListener == null){
            mReviews_ChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    //When first attached, this method is called for every object in the DB
                    TagSaleReviewObject tagSaleReviewObject = dataSnapshot.getValue(TagSaleReviewObject.class);
                    Log.d(TAG, "REVIEWS-- onChildAdded: added:" + dataSnapshot.toString());
                }
                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    TagSaleReviewObject tagSaleReviewObject = dataSnapshot.getValue(TagSaleReviewObject.class);
                    Log.d(TAG, "REVIEWS onChildAdded: changed:" + tagSaleReviewObject.getReviewerID()+" - " + tagSaleReviewObject.getDescription());
                }
                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                }
                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            mReviewsDatabaseReference.addChildEventListener(mReviews_ChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mTagSaleEvent_ChildEventListener != null) {
            mMessagesDatabaseReference.removeEventListener(mTagSaleEvent_ChildEventListener);
            mTagSaleEvent_ChildEventListener = null;
        }
        if (mTagSaleUser_ChildEventListener != null) {
            mTagSaleUsersDatabaseReference.removeEventListener(mTagSaleUser_ChildEventListener);
            mTagSaleUser_ChildEventListener = null;
        }
        if (mFriends_ChildEventListener != null) {
            mFriendsDatabaseReference.removeEventListener(mFriends_ChildEventListener);
            mFriends_ChildEventListener = null;
        }
        if (mReviews_ChildEventListener != null) {
            mReviewsDatabaseReference.removeEventListener(mReviews_ChildEventListener);
            mReviews_ChildEventListener = null;
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        Log.d(TAG, " onCreateLoader() start");

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
        Log.d(TAG, " onLoadFinished() start");

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
    private void showWeatherDataFragment() {
        Log.d(TAG, "showWeatherDataFragment: STARTING permissionSequenceGoing="+permissionSequenceGoing);
        /* First, hide the loading indicator */
        //mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Finally, make sure the weather data is visible */
        //Remove old fragment if it exists:
        removeWeatherDataFragment();
        weatherFragment = new WeatherFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.weather_container, weatherFragment,getString(R.string.TAG_FRAGMENT_WEATHERPANEL))
                .commit();
    }
    private void removeWeatherDataFragment(){
        WeatherFragment prevFragment = (WeatherFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.TAG_FRAGMENT_WEATHERPANEL));
        if(prevFragment != null)
            getSupportFragmentManager().beginTransaction().remove(prevFragment).commit();
    }

    private void updateWeatherData(Cursor data){
        Log.d(TAG, "updateWeatherData: --- WeatherData In MAIN:"+data.toString());
    }

    @Override
    public void onAddButtonClicked(String fragmentTag) {
        Intent intent = null;
        if( fragmentTag ==  getString(R.string.TAG_FRAGMENT_TAGSALELIST)){
             intent = new Intent(this, AddTagSaleActivity.class);

        }
        if( fragmentTag ==  getString(R.string.TAG_FRAGMENT_FRIENDLIST)){
            intent =new Intent(this, AddFriendActivity.class);

        }
        if (intent != null){
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
                startActivity(intent, bundle);
            } else {
                startActivity(intent);
            }

        }
    }
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,int[] grantResults) {
        Log.d(TAG, " onRequestPermissionsResult() start");

        switch(requestCode){
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if(grantResults.length >0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    // We can now safely use the API we requested access to
                    permissionSequenceGoing = false;
                    startAuth();

                    reallyGetLocation();
                } else {
                    // Permission was denied or request was cancelled
                    Toast.makeText(this, R.string.no_location_permission_msg, Toast.LENGTH_LONG).show();
                    permissionSequenceGoing = false;
                    startAuth();

                }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
    public void getLocationInfo(){
        Log.d(TAG, " getLocationInfo() STARTING");

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, " getLocationInfo() permission not granted");

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, R.string.toast_show_permission_explanation, Toast.LENGTH_LONG).show();
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                permissionSequenceGoing = false;
                startAuth();

            } else {
                // No explanation needed; request the permission
                Log.d(TAG, " getLocationInfo() request the permission");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            permissionSequenceGoing = false;
            startAuth();
            reallyGetLocation();
        }

    }
    private void reallyGetLocation(){
        Log.d(TAG, " reallyGetLocation() start");
        if (mFusedLocationClient != null) {

        if( getBaseContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            Log.d(TAG, "reallyGetLocation: PERMISSION GRANTED - getting location");
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Log.d(TAG, "reallyGetLocation.onSuccess: ");
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Log.d(TAG, "reallyGetLocation.onSuccess: Location good:"+location.toString());
                            // Logic to handle location object
                            CurrentInfo.setCurrentLocationObject(location);
                        }
                        //JUST USING THE 'GETLASTLOCATION'. FUTURE ENHANCEMENT - PERIODIC LOCATION UPDATES
                       // mRequestingLocationUpdates = true;
                      //  startLocationUpdates();// start getting location periodically
                    }
                });
        }
    }
    private void stopLocationUpdates() {
        if (mLocationCallback != null && mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }

        // Update the value of mRequestingLocationUpdates from the Bundle.
        //if (savedInstanceState.keySet().contains(REQUEST_LOCATION_UPDATES_KEY)) {
        //    mRequestingLocationUpdates = savedInstanceState.getBoolean(REQUEST_LOCATION_UPDATES_KEY);
        //}
        //permissionSequenceGoing

        if (savedInstanceState.keySet().contains(PERMISSION_SEQUENCE_GOING)) {
            permissionSequenceGoing = savedInstanceState.getBoolean(PERMISSION_SEQUENCE_GOING);
        }
        // ...


    }
}
