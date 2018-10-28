package com.example.android.tagsalenow;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OneFriend {
    public String userId;
    public OneFriend(){

    }
    public OneFriend(String userId){
        this.userId = userId;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId){ this.userId = userId;}
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        return result;
    }
}
