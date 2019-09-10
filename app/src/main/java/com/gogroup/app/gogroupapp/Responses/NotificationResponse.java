package com.gogroup.app.gogroupapp.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 7/29/2017.
 */

public class NotificationResponse {

    @SerializedName("status")
    @Expose
    boolean status;
    @SerializedName("messages")
    @Expose
    String message;
    @SerializedName("count")
    @Expose
    Integer count;
    @SerializedName("data")
    @Expose
    List<NotificationResponse> data=new ArrayList<>();

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

    public List<NotificationResponse> getData() {
        return data;
    }

    public void setData(List<NotificationResponse> data) {
        this.data = data;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @SerializedName("created_date")
    @Expose
    String createdDate;
    @SerializedName("notify")
    @Expose
    String notify;
    @SerializedName("notify_date")
    @Expose
    String notifyDate;
    @SerializedName("group_name")
    @Expose
    String groupName;

    public String getNotifyDate() {
        return notifyDate;
    }

    public void setNotifyDate(String notifyDate) {
        this.notifyDate = notifyDate;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }
}
