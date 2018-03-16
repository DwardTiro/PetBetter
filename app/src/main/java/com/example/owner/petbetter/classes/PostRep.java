package com.example.owner.petbetter.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by owner on 4/10/2017.
 */

public class PostRep {

    @SerializedName("_id")
    private long id;

    @SerializedName("user_id")
    private long userId;

    @SerializedName("post_id")
    private int postId;

    @SerializedName("parent_id")
    private int parentId;

    @SerializedName("rep_content")
    private String repContent;

    @SerializedName("date_performed")
    private String datePerformed;

    @SerializedName("postrep_photo")
    private String postRepPhoto;

    @SerializedName("is_deleted")
    private int isDeleted;

    private String userName;

    public PostRep(long id, long userId, int postId, int parentId, String repContent, String datePerformed,
                   String postRepPhoto, int isDeleted) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.parentId = parentId;
        this.repContent = repContent;
        this.datePerformed = datePerformed;
        this.postRepPhoto = postRepPhoto;
        this.isDeleted = isDeleted;
    }

    public PostRep(long id, long userId, int postId, int parentId, String repContent, String datePerformed, int isDeleted,
                   String firstName, String lastName, String postRepPhoto) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.parentId = parentId;
        this.repContent = repContent;
        this.datePerformed = datePerformed;
        this.isDeleted = isDeleted;
        this.userName = firstName +" "+ lastName;
        this.postRepPhoto = postRepPhoto;
    }

    public String getPostRepPhoto() {
        return postRepPhoto;
    }

    public void setPostRepPhoto(String postRepPhoto) {
        this.postRepPhoto = postRepPhoto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getRepContent() {
        return repContent;
    }

    public void setRepContent(String repContent) {
        this.repContent = repContent;
    }

    public String getDatePerformed() {
        return datePerformed;
    }

    public void setDatePerformed(String datePerformed) {
        this.datePerformed = datePerformed;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}
