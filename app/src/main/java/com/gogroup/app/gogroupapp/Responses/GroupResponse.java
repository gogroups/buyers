package com.gogroup.app.gogroupapp.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zabius-android on 2/6/2018.
 */

public class GroupResponse {
    @SerializedName("status")
    @Expose
    Boolean isStatus;
    @SerializedName("messages")
    @Expose
    String message;
    @SerializedName("data")
    @Expose
    List<GroupResponse> listData = new ArrayList<>();

    public Boolean getStatus() {
        return isStatus;
    }

    public void setStatus(Boolean status) {
        this.isStatus = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<GroupResponse> getListData() {
        return listData;
    }

    public void setListData(List<GroupResponse> listData) {
        this.listData = listData;
    }
    @SerializedName("category_id")
    @Expose
    Integer categoryId1;
    @SerializedName("subcategory_id")
    @Expose
    Integer categoryId2;
    @SerializedName("subcategory2_id")
    @Expose
    Integer categoryId3;
    @SerializedName("subcategory3_id")
    @Expose
    Integer categoryId4;
    @SerializedName("subcategory4_id")
    @Expose
    Integer categoryId5;
    @SerializedName("subcategory5_id")
    @Expose
    Integer categoryId6;

    public Integer getCategoryId1() {
        return categoryId1;
    }

    public void setCategoryId1(Integer categoryId1) {
        this.categoryId1 = categoryId1;
    }

    public Integer getCategoryId2() {
        return categoryId2;
    }

    public void setCategoryId2(Integer categoryId2) {
        this.categoryId2 = categoryId2;
    }

    public Integer getCategoryId3() {
        return categoryId3;
    }

    public void setCategoryId3(Integer categoryId3) {
        this.categoryId3 = categoryId3;
    }

    public Integer getCategoryId4() {
        return categoryId4;
    }

    public void setCategoryId4(Integer categoryId4) {
        this.categoryId4 = categoryId4;
    }

    public Integer getCategoryId5() {
        return categoryId5;
    }

    public void setCategoryId5(Integer categoryId5) {
        this.categoryId5 = categoryId5;
    }

    public Integer getCategoryId6() {
        return categoryId6;
    }

    public void setCategoryId6(Integer categoryId6) {
        this.categoryId6 = categoryId6;
    }
}
