package com.example.android.tagsalenow;

public class AttendingObject {
    private String id;
    private String userId;
    private String eventId;

    public AttendingObject(){}
    public AttendingObject (String id, String userId, String eventId){
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
    }
    public String getId(){ return id;}
    public void setId(String id){ this.id = id;}

    public String getUserId(){ return userId;}
    public void setUserId(String userId){ this.userId = userId;}

    public String getEventId(){ return eventId;}
    public void setEventId(String eventId){ this.eventId = eventId;}
}