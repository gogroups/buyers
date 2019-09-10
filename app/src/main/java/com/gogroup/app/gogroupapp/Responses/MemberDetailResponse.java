package com.gogroup.app.gogroupapp.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 7/29/2017.
 */

public class MemberDetailResponse {
    @SerializedName("status")
    @Expose
    boolean status;
    @SerializedName("messages")
    @Expose
    String message;
    @SerializedName("data")
    @Expose
    MemberDetailResponse data;

    public MemberDetailResponse getData() {
        return data;
    }

    public void setData(MemberDetailResponse data) {
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

    @SerializedName("member_details")
    @Expose
    UserResponse memberDetails;
    @SerializedName("created_groups")
    @Expose
    List<GroupListResponse> createdGroups=new ArrayList<>();
    @SerializedName("joined_groups")
    @Expose
    List<GroupListResponse> joinedGroups=new ArrayList<>();

    public UserResponse getMemberDetails() {
        return memberDetails;
    }

    public void setMemberDetails(UserResponse memberDetails) {
        this.memberDetails = memberDetails;
    }

    public List<GroupListResponse> getCreatedGroups() {
        return createdGroups;
    }

    public void setCreatedGroups(List<GroupListResponse> createdGroups) {
        this.createdGroups = createdGroups;
    }

    public List<GroupListResponse> getJoinedGroups() {
        return joinedGroups;
    }

    public void setJoinedGroups(List<GroupListResponse> joinedGroups) {
        this.joinedGroups = joinedGroups;
    }
}
