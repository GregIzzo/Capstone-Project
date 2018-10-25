package com.example.android.tagsalenow;

import com.example.android.tagsalenow.utils.DateConverter;
import com.example.android.tagsalenow.utils.Utilities;

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
    private String id;
    private String locationId;
    private String address;
    private String city;
    private String state;
    private String zip;
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
    public TagSaleEventObject(String id, String locationId, String address, String city, String state, String zip, String ownerId, String date, String startTime,
                              String endTime, String description, String tags){
        this.id = id;
        this.locationId = locationId;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.ownerId = ownerId;
        this.date= date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.tags = tags;
        changeCounter = 0;
    }
    public String getId(){ return id;}
    public void setId(String id) { this.id = id;}

    public String getLocationId() { return locationId;}
    public void setLocationId(String locationId) { this.locationId = locationId;  }

    public String getAddress() { return address;}
    public void setAddress(String address) { this.address = address;}

    public String getCity() { return city;}
    public void setCity(String city) { this.city = city;}

    public String getState() { return state;}
    public void setState(String state) {this.state = state;}

    public String getZip() { return zip;}
    public void setZip(String zip) { this.zip = zip;}

    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) {  this.ownerId = ownerId; }

    public String getDate() { return date;  }
    public String getFormattedDate(){
        //expect date to be YYYY-mm-dd
        // Convert to simpley "MMM dd"
        return DateConverter.toDisplayFormat(date);
    }

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
