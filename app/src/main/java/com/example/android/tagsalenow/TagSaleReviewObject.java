package com.example.android.tagsalenow;

public class TagSaleReviewObject {
    private String id;
    private String tagSaleID;
    private String reviewerID;
    private String description;
    private int fiveStarRating;

    public TagSaleReviewObject(){    }

    public TagSaleReviewObject(String id, String tagSaleID, String reviewerID, String description, int fiveStarRating){
        this.id = id;
        this.tagSaleID = tagSaleID;
        this.reviewerID = reviewerID;
        this.description = description;
        this.fiveStarRating = fiveStarRating;
    }

    public String getId(){ return id;}
    public void setId(String id){ this.id = id;}
    public String getTagSaleID() {        return tagSaleID;    }
    public void setTagSaleID(String tagSaleID) {        this.tagSaleID = tagSaleID;    }

    public String getReviewerID() {        return reviewerID;    }
    public void setReviewerID(String reviewerID) {        this.reviewerID = reviewerID;    }

    public String getDescription() {        return description;    }
    public void setDescription(String description) {        this.description = description;    }

    public int getFiveStarRating() {        return fiveStarRating;    }
    public void setFiveStarRating(int fiveStarRating) {        this.fiveStarRating = fiveStarRating;
    }



}
