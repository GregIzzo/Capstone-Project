package com.example.android.tagsalenow;

/*
Database contains it's own user data

User_id
Active - 1=active, 0=inactive (omit from searches)
first name
last name
email
join date

 */
public class UserData {
    private String userId;
    private boolean active;
    private String joinDate;

    public String getUserId() { return userId;  }

    public void setUserId(String userId) {  this.userId = userId; }

    public boolean isActive() {  return active; }

    public void setActive(boolean active) {   this.active = active;    }

    public String getJoinDate() {    return joinDate;   }

    public void setJoinDate(String joinDate) {  this.joinDate = joinDate; }


}
