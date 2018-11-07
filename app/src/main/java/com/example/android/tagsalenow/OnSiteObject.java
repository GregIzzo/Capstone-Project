package com.example.android.tagsalenow;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/*
OnSite DB :
<TagSaleId> { <userid>:true, <userid>:true, ...}
OnSiteObject is: <userid>
 */
public class OnSiteObject {
    private String userId;

    public OnSiteObject(){}
    public OnSiteObject ( String userId){
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
