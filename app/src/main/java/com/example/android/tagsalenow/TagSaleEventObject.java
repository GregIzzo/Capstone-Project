package com.example.android.tagsalenow;

/*
event_id
location_id
user_id (tag sale owner)
date
start time
end time
description
Tags
event_change_counter - increments when owner makes a change

 */
public class TagSaleEventObject {
    private String locationId;
    private String ownerId;
    private String date;
    private String startTime;
    private String endTime;
    private String description;
    private String tags;
    //private String friendsAttending;

    private int changeCounter =0;

    public TagSaleEventObject(){
    }
    public TagSaleEventObject(String locationId, String ownerId, String date, String startTime, String endTime, String description, String tags){
        this.locationId = locationId;
        this.ownerId = ownerId;
        this.date= date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.tags = tags;
        changeCounter = 0;
    }
    public String getLocationId() { return locationId;}

    public void setLocationId(String locationId) { this.locationId = locationId;  }

    public String getOwnerId() { return ownerId; }

    public void setOwnerId(String ownerId) {  this.ownerId = ownerId; }

    public String getDate() { return date;  }

    public void setDate(String date) {  this.date = date;  }

    public String getStartTime() {  return startTime; }

    public void setStartTime(String startTime) {  this.startTime = startTime; }

    public String getEndTime() { return endTime;  }

    public void setEndTime(String endTime) {  this.endTime = endTime;  }

    public String getDescription() {  return description; }

    public void setDescription(String description) {   this.description = description;  }

    public String getTags() {   return tags;  }

    public void setTags(String tags) {   this.tags = tags;   }

    //public String getFriendsAttending() {   return friendsAttending;  }

    //public void setFriendsAttending(String friendsAttending) {   this.friendsAttending = friendsAttending;   }

    public int getChangeCounter() {    return changeCounter;   }

    public void setChangeCounter(int changeCounter) {   this.changeCounter = changeCounter;  }
}
