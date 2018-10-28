package com.example.android.tagsalenow;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class FriendRequestObject {
    private String friendemail;
    private Boolean sent;

    public FriendRequestObject(){}
    public FriendRequestObject(String friendemail, Boolean sent){
        this.friendemail = friendemail;
        this.sent = sent;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("friendemail", friendemail);
        result.put("sent", sent);
        return result;
    }
}
