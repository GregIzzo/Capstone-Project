package com.example.android.tagsalenow;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.tagsalenow.data.CurrentInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddFriendActivity extends AppCompatActivity {

    @BindView(R.id.te_friendemail)
    TextView te_friendemail;
    @BindView(R.id.button_cancel)
    Button button_cancel;
    @BindView(R.id.button_add)
    Button button_add;
    @BindView(R.id.button_test)
    Button button_test;

    private static String TAG = "AddFriendActivity";
    private AcceptFriendFragment acceptFriendFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);
        ButterKnife.bind(this);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                String friendEmail = te_friendemail.getText().toString();
                DatabaseReference mDatabaseReference
                        = FirebaseDatabase.getInstance().getReference().child("tagsaleusers");
                Query queryRef = mDatabaseReference.orderByChild("email").startAt(friendEmail).endAt(friendEmail);
                queryRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                        Log.d(TAG, "onChildAdded: EMAIL RESULTS:" + snapshot.toString());
                        TagSaleUserObject u = snapshot.getValue(TagSaleUserObject.class);
                        Log.d(TAG, "onChildAdded: EMAIL SEARCH:" + u.getDisplayName() + ", " + u.getEmail());
                        Toast.makeText(AddFriendActivity.this, "Found:" + u.getDisplayName() + "," + u.getEmail(), Toast.LENGTH_SHORT).show();
//-------------------------------------------------------------------

                        DatabaseReference mDatabaseReference;

                        // Create new post at /user-posts/$userid/$postid and at
                        // /posts/$postid simultaneously

                        String friendEmail = te_friendemail.getText().toString();
                        String emailKey = friendEmail.replaceAll("\\.", ",");
                        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                        String key = mDatabaseReference.child("friendrequest").push().getKey();
                        FriendRequestObject fro =
                                new FriendRequestObject(
                                        CurrentInfo.getCurrentUser().getUserId(),
                                        u.getUserId(),
                                        friendEmail,
                                        key
                                );
                        Map<String, Object> frValues = fro.toMap();
                        Map<String, Object> childUpdates = new HashMap<>();

                        childUpdates.put("/friendrequest/" +
                                key, frValues);//NOTE: Key is random. Queries will search using 'fromuserid' and 'touserid'
                        //FriendRequest is structured:
                        //  ToBeFriendUserId: {
                        //     FromUserId

                        childUpdates.put("/tagsaleusers/" +
                                CurrentInfo.getCurrentUser().getUserId()
                                + "/friendrequests/"
                                + u.getUserId(), frValues);
                        Log.d(TAG, "onChildAdded: CHILDUPDATES:"+childUpdates.toString());
                        mDatabaseReference.updateChildren(childUpdates);
                        finish();
//--------------------------------------------------------------------
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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


                    // childUpdates.put();
                    //ORIGINAL mFriendRequestDatabaseReference.updateChildren(childUpdates);
/*
                mFriendRequestDatabaseReference.child("friendrequest")
                        .child(CurrentInfo.getCurrentUser().getFromUserId())
                        .child(emailKey)
                        .setValue(frValues);
                mFriendRequestDatabaseReference.child("tagsaleusers")
                        .child(CurrentInfo.getCurrentUser().getFromUserId())
                        .child("friendrequests")
                        .child(emailKey)
                        .setValue(frValues);
*/

/*
                Post post = new Post(userId, username, title, body);
                Map<String, Object> postValues = post.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/posts/" + key, postValues);
                childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

                mDatabase.updateChildren(childUpdates);
*/
                }   );

            }
        });
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        button_test.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                String friendEmail = te_friendemail.getText().toString();
                DatabaseReference mDatabaseReference
                 = FirebaseDatabase.getInstance().getReference().child("tagsaleusers");
                Query queryRef = mDatabaseReference.orderByChild("email").startAt(friendEmail).endAt(friendEmail);
                queryRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                        Log.d(TAG, "onChildAdded: EMAIL RESULTS:"+snapshot.toString());
                        TagSaleUserObject u = snapshot.getValue(TagSaleUserObject.class);
                        Log.d(TAG, "onChildAdded: EMAIL SEARCH:"+u.getDisplayName()+", "+u.getEmail());
                        Toast.makeText(AddFriendActivity.this, "Found:"+u.getDisplayName()+","+u.getEmail(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
                    // ....
                });
/*
                mDatabaseReference.child("tagsaleusers")
                        .child("email")
                        .child(friendEmail).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot whatData) {
                        Toast.makeText(AddFriendActivity.this, "GOT"+whatData.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // ...
                    }
                });
*/
            }
        });
        acceptFriendFragment = new AcceptFriendFragment();
        Log.d(TAG, "Create: TTTTT acceptFriendFragment =" + acceptFriendFragment);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.acceptfriend_container, acceptFriendFragment,getString(R.string.TAG_FRAGMENT_TAGSALELIST))
                .commit();

    }
}

