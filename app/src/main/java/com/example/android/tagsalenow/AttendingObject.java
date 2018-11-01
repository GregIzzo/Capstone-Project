package com.example.android.tagsalenow;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/*
Attending DB :
<TagSaleId> { <userid>:true, <userid>:true, ...}
AttendingObject is: <userid>
 */
public class AttendingObject {
    private String userId;

    public AttendingObject(){}
    public AttendingObject ( String userId){
        this.userId = userId;
    }

    public String getUserId(){ return userId;}
    public void setUserId(String userId){ this.userId = userId;}
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        return result;
    }

}
