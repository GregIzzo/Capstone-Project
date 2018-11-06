package com.example.android.tagsalenow;
/*
I manage the 'attending' state for a single TagSaleEvent and the currentUser
 */
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.android.tagsalenow.data.CurrentInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AttendingFragment extends Fragment {
    private final String TAG = "ATTENDINGFRAGMENT";
    private String myTagSaleId = "";
    private CheckBox cbAttend;

    public AttendingFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_attending, container, false);
        cbAttend = (CheckBox) rootView.findViewById(R.id.ts_planningtoattendcb);

        return rootView;
    }
    private void getData(){
        DatabaseReference mDatabaseReference
                = FirebaseDatabase.getInstance().getReference("attending/"+myTagSaleId);
        Query queryRef = mDatabaseReference.orderByChild(CurrentInfo.getCurrentUser().getUserId()).equalTo(true);
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: ATTENDING DATA:"+dataSnapshot.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: ATTENDING DATA error:"+databaseError.getMessage());
            }
        });

    }
    public void setMyTagSaleId(String tagSaleId){
        myTagSaleId = tagSaleId;
    }

}
