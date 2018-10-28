package com.example.android.tagsalenow;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Friends {
    // {<userIdString> : { <FriendUserId>:TRUE, <FriendUserId>:TRUE,...}... }
    public String userId;
    public ArrayList<String> friendList = new ArrayList<String>();
    public Friends(){

    }
    public Friends(String userId, ArrayList<String> friendList){
        this.userId = userId;
        this.friendList =  friendList;
     }

    public String getUserId() { return userId; }
    public void setUserId(String userId){ this.userId = userId;}

    public ArrayList<String> getFriendList(){return friendList;}
    public void setFriendList(ArrayList<String> friendList) { this.friendList = friendList;}
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("friendList", friendList);
        return result;
    }
}
