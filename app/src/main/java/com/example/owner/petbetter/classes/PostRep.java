package com.example.owner.petbetter.classes;

/**
 * Created by owner on 4/10/2017.
 */

public class PostRep {
    private long id, userId;
    private int postId;
    private String repContent;
    private String repUser;

    public PostRep(long id, long userId, int postId, String repContent, String repUser) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.repContent = repContent;
        this.repUser = repUser;
    }

    public PostRep(long id, long userId, int postId, String repContent) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.repContent = repContent;
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

    public String getRepContent() {
        return repContent;
    }

    public void setRepContent(String repContent) {
        this.repContent = repContent;
    }

    public String getRepUser() {
        return repUser;
    }

    public void setRepUser(String repUser) {
        this.repUser = repUser;
    }
}
