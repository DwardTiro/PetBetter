package com.example.owner.petbetter.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by owner on 20/2/2018.
 */

public class Rating {
    @SerializedName("_id")
    long id;

    @SerializedName("rater_id")
    long raterId;

    @SerializedName("rated_id")
    long ratedId;

    @SerializedName("rating")
    float rating;

    @SerializedName("comment")
    String comment;

    @SerializedName("rating_type")
    int ratingType;

    @SerializedName("date_created")
    String dateCreated;

    @SerializedName("is_deleted")
    int isDeleted;

    public Rating(long id, long raterId, long ratedId, float rating, String comment, int ratingType, String dateCreated, int isDeleted) {
        this.id = id;
        this.raterId = raterId;
        this.ratedId = ratedId;
        this.rating = rating;
        this.comment = comment;
        this.ratingType = ratingType;
        this.dateCreated = dateCreated;
        this.isDeleted = isDeleted;
    }

    public long getRatedId() {
        return ratedId;
    }

    public void setRatedId(long ratedId) {
        this.ratedId = ratedId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRaterId() {
        return raterId;
    }

    public void setRaterId(long raterId) {
        this.raterId = raterId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRatingType() {
        return ratingType;
    }

    public void setRatingType(int ratingType) {
        this.ratingType = ratingType;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}
