package com.gogroup.app.gogroupapp.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 7/29/2017.
 */

public class GroupDetailResponse {
    @SerializedName("status")
    @Expose
    boolean status;
    @SerializedName("messages")
    @Expose
    String message;
    @SerializedName("data")
    @Expose
    GroupDetailResponse data;

    public void setData(GroupDetailResponse data) {
        this.data = data;
    }

    public GroupDetailResponse getData() {
        return data;
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


    @SerializedName("group_details")
    @Expose
    GroupListResponse groupDetails;
    @SerializedName("member_details")
    @Expose
    List<GroupListResponse> memberList=new ArrayList<>();
    @SerializedName("advertisement_details")
    @Expose
    List<ImageResponse> advertisementList=new ArrayList<>();


    public GroupListResponse getGroupDetails() {
        return groupDetails;
    }

    public void setGroupDetails(GroupListResponse groupDetails) {
        this.groupDetails = groupDetails;
    }

    public List<GroupListResponse> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<GroupListResponse> memberList) {
        this.memberList = memberList;
    }

    public List<ImageResponse> getAdvertisementList() {
        return advertisementList;
    }

    public void setAdvertisementList(List<ImageResponse> advertisementList) {
        this.advertisementList = advertisementList;
    }
}
