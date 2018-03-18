package com.example.owner.petbetter.classes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Kristian on 3/18/2018.
 */

public class Bookmark {

    @SerializedName("user_id")
    private long user_id;

    @SerializedName("bookmark_type")
    private int bookmark_type;

    @SerializedName("_id")
    private long _id;

    @SerializedName("item_id")
    private long item_id;

    public Bookmark(long _id, long item_id, int bookmark_type, long user_id){
        this._id = _id;
        this.item_id = item_id;
        this.bookmark_type = bookmark_type;
        this.user_id = user_id;
    }

    public long getUserId() {
        return user_id;
    }
    public long getItemId() {
        return item_id;
    }
    public int getBookmarkType() {
        return bookmark_type;
    }

    public long getBookmarkId() {
        return _id;
    }

    public void setUserId(long user_id){
        this.user_id = user_id;
    }
    public void setItemId(long item_id){
        this.item_id = item_id;
    }
    public void setBookmarkType(int bookmark_type){
        this.bookmark_type = bookmark_type;
    }
    public void setBookmarkId(long _id){
        this._id = _id;
    }
}
