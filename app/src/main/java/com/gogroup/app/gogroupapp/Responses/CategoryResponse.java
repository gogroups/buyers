package com.gogroup.app.gogroupapp.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 7/29/2017.
 */

public class CategoryResponse
{
    @SerializedName("status")
    @Expose
    boolean status;
    @SerializedName("messages")
    @Expose
    String message;
    @SerializedName("data")
    @Expose
    List<CategoryResponse> data=new ArrayList<>();

    public List<CategoryResponse> getData() {
        return data;
    }

    public void setData(List<CategoryResponse> data) {
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    @SerializedName("category_id")
    @Expose
    String categoryId;
    @SerializedName("category_title")
    @Expose
    String categoryTitle;
    @SerializedName("group_count")
    @Expose
    String groupCount;
    @SerializedName("groupIDs")
    @Expose
    String[] groupIDs;
    @SerializedName("posts")
    @Expose
    String posts;
    @SerializedName("images")
    @Expose
     String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGroupCount() {
        return groupCount;
    }

    public String[] getGroupIDs() {
        return groupIDs;
    }

    public void setGroupIDs(String[] groupIDs) {
        this.groupIDs = groupIDs;
    }

    public void setGroupCount(String groupCount) {
        this.groupCount = groupCount;
    }

    public String getPosts() {
        return posts;
    }

    public void setPosts(String posts) {
        this.posts = posts;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }
}
