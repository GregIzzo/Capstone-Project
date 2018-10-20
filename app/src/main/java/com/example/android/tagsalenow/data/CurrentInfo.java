package com.example.android.tagsalenow.data;

import com.example.android.tagsalenow.TagSaleEventObject;

/*
* Store static (current) information, such as 'currently selected Tag Sale' or 'Currently selected Friend'
 */
public  class CurrentInfo {
    private static String currentTagSaleID = "";
    private static String currentFriendID = "";
    private static TagSaleEventObject currentTagSaleEventObject;

    public static String getCurrentTagSaleID(){ return currentTagSaleID;}
    public static void setCurrentTagSaleID(String incomingTagSaleID){ currentTagSaleID = incomingTagSaleID;}

    public static String getCurrentFriendID(){ return currentFriendID;}
    public static void setCurrentFriendID(String incomingFriendID) {currentFriendID = incomingFriendID;}


    public static TagSaleEventObject getCurrentTagSaleEventObject(){ return currentTagSaleEventObject;}
    public static void setCurrentTagSaleEventObject(TagSaleEventObject tagSaleEventObject) { currentTagSaleEventObject = tagSaleEventObject;}
}
