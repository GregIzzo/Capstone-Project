package com.example.android.tagsalenow;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class FriendRequestObject {
    private String fromUserId;
    private String toUserId;
    private String friendemail;
    private String dbkey;


    public FriendRequestObject(){}
    public FriendRequestObject(String fromUserId, String toUserId,String friendemail, String dbkey){
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.friendemail = friendemail;
        this.dbkey = dbkey;
    }

    public String getFromUserId() { return fromUserId;}
    public void setFromUserId(String fromUserId){this.fromUserId = fromUserId;}

    public String getToUserId() { return toUserId;}
    public void setToUserId(String ToUserId){this.toUserId = toUserId;}

    public String getFriendemail(){return friendemail;}
    public void setFriendemail(String friendemail){this.friendemail = friendemail;}

    public String getDbkey(){ return dbkey;}
    public void setDbkey(String dbkey){this.dbkey = dbkey;}

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("fromUserId", fromUserId);
        result.put("toUserId", toUserId);
        result.put("friendemail", friendemail);
        result.put("dbkey", dbkey);

        return result;
    }
}
