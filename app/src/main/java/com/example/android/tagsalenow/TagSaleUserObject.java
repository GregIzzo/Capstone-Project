package com.example.android.tagsalenow;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/*
Database contains it's own user data

User_id
Active - 1=active, 0=inactive (omit from searches)
first name
last name
email
join date

 */
public class TagSaleUserObject {
    private String userId;
    private boolean active;
    private String joinDate;
    private String displayName;
    private String email;
    private String photoUrl;

    public TagSaleUserObject(){    }
    public TagSaleUserObject(String userId, String joinDate, String displayName, String email, String photoUrl){
        this.userId = userId;
        this.active = true;
        this.joinDate = joinDate;
        this.displayName = displayName;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    public String getUserId() { return userId;  }
    public void setUserId(String userId) {  this.userId = userId; }

    public boolean isActive() {  return active; }
    public void setActive(boolean active) {   this.active = active;    }

    public String getJoinDate() {    return joinDate;   }
    public void setJoinDate(String joinDate) {  this.joinDate = joinDate; }

    public String getDisplayName(){ return this.displayName;}
    public void setDisplayName(String displayName){ this.displayName = displayName;}

    public String getEmail(){ return this.email;}
    public void setEmail(String email){ this.email = email;}

    public String getPhotoUrl(){ return this.photoUrl;}
    public void setPhotoUrl(String photoUrl){ this.photoUrl = photoUrl;   }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("active", active);
        result.put("joinDate", joinDate);
        result.put("displayName", displayName);
        result.put("email", email);
        result.put("photoUrl", photoUrl);

        return result;
    }
}
