package com.gogroup.app.gogroupapp.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Lenovo on 7/29/2017.
 */

public class PostResponse {

    @SerializedName("status")
    @Expose
    boolean status;
    @SerializedName("messages")
    @Expose
    String message;
    @SerializedName("data")
    @Expose
    PostResponse data;
    @SerializedName("couponCode")
    @Expose
    String couponCode;
    @SerializedName("likedcount")
    @Expose
    String likedCount;
    @SerializedName("rating")
    @Expose
    Float rating;


    public Float getRating() {
        return rating;
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

    public PostResponse getData() {
        return data;
    }

    public void setData(PostResponse data) {
        this.data = data;
    }

    public String getCouponCode() {
        return couponCode;
    }

    @SerializedName("end_date")
    @Expose
    String endDate;
    @SerializedName("group_id")
    @Expose
    String groupId;

    @SerializedName("group_image")
    @Expose
    String groupImage;

    @SerializedName("order_ref_id")
    @Expose
    private String orderRefId;
    @SerializedName("sequence_of_order")
    @Expose
    private String SequenceNo;
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public String getOrderRefId() {
        return orderRefId;
    }

    public String getLikedCount() {
        return likedCount;
    }

    public String getSequenceNo() {
        return SequenceNo;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }
}
