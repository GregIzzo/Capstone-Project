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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);
        ButterKnife.bind(this);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "onClick: ");

                DatabaseReference mDatabaseReference;

               // FirebaseDatabase mFirebaseDatabase;
                //DatabaseReference mFriendRequestDatabaseReference;
               // mFirebaseDatabase = FirebaseDatabase.getInstance();
                /*
                //Step 1: Get Key of new record

                mFriendRequestDatabaseReference = mFirebaseDatabase.getReference().child("friendrequest");
                DatabaseReference opRef =  mFriendRequestDatabaseReference.push();
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
                */
                // Create new post at /user-posts/$userid/$postid and at
                // /posts/$postid simultaneously

                String friendEmail = te_friendemail.getText().toString();
                String emailKey = friendEmail.replaceAll("\\.", ",");
                mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                String key = mDatabaseReference.child("friendrequest").push().getKey();
                FriendRequestObject fro = new FriendRequestObject(friendEmail, false);
                Map<String, Object> frValues = fro.toMap();
                Log.d(TAG, "onClick: FRVALUES="+frValues.toString());
                Map<String, Object> childUpdates = new HashMap<>();
                Log.d(TAG, "onClick: ADDINGFRIEND");

                childUpdates.put("/friendrequest/" +
                        CurrentInfo.getCurrentUser().getUserId() +
                        "/" + emailKey, frValues);
/*
                childUpdates.put("/tagsaleusers/" +
                        CurrentInfo.getCurrentUser().getUserId()
                        + "/friendrequests/"
                        + emailKey, frValues);
*/
                mDatabaseReference.updateChildren(childUpdates);
               // childUpdates.put();
               //ORIGINAL mFriendRequestDatabaseReference.updateChildren(childUpdates);
/*
                mFriendRequestDatabaseReference.child("friendrequest")
                        .child(CurrentInfo.getCurrentUser().getUserId())
                        .child(emailKey)
                        .setValue(frValues);
                mFriendRequestDatabaseReference.child("tagsaleusers")
                        .child(CurrentInfo.getCurrentUser().getUserId())
                        .child("friendrequests")
                        .child(emailKey)
                        .setValue(frValues);
*/
                Log.d(TAG, "onClick: ADDINGFRIENDREQUEST DONE");
/*
                Post post = new Post(userId, username, title, body);
                Map<String, Object> postValues = post.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/posts/" + key, postValues);
                childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

                mDatabase.updateChildren(childUpdates);
*/
                finish();

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
                Log.d(TAG, "onClick: TESTCLICKED friendemail="+friendEmail);
                DatabaseReference mDatabaseReference;
                mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("tagsaleusers");
                Log.d(TAG, "onClick: TESTCLICKED mDatabaseReference="+mDatabaseReference);
                Query queryRef = mDatabaseReference.orderByChild("email").startAt(friendEmail).endAt(friendEmail);
                Log.d(TAG, "onClick: TESTCLICKED queryRef="+queryRef.toString());
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

    }
}

