package com.example.android.tagsalenow;

public class FriendRelationObject {
    private  String friendId1;
    private  String friendId2;

    public FriendRelationObject(){

    }
    public FriendRelationObject(String fid1, String fid2){
        this.friendId1 = fid1;
        this.friendId2 = fid2;
    }

    public String getFriendId1() { return friendId1; }
    public void setFriendId1(String friendId1){ this.friendId1 = friendId1;}

    public String getFriendId2() { return friendId2; }
    public void setFriendId2(String friendId2){ this.friendId2 = friendId2;}
}
