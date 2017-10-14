package com.example.owner.petbetter.classes;

/**
 * Created by owner on 4/10/2017.
 */

public class PostRep {
    private long id, userId;
    private int postId, parentId;
    private String repContent;
    private String datePerformed;
    private int isDeleted;
    private String userName;

    public PostRep(long id, long userId, int postId, String repContent, String repUser) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.repContent = repContent;
    }

    public PostRep(long id, long userId, int postId, int parentId, String repContent, String datePerformed, int isDeleted,
                   String firstName, String lastName) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.parentId = parentId;
        this.repContent = repContent;
        this.datePerformed = datePerformed;
        this.isDeleted = isDeleted;
        this.userName = firstName +" "+ lastName;
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
