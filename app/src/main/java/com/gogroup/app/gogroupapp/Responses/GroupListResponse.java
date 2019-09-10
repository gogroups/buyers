package com.gogroup.app.gogroupapp.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 7/29/2017.
 */

public class GroupListResponse {


    String title,type,notificationType,advertisementId;

    @SerializedName("status")
    @Expose
    boolean status;
    @SerializedName("messages")
    @Expose
    String message;
    @SerializedName("data")
    @Expose
    List<GroupListResponse> data=new ArrayList<>();

    public List<GroupListResponse> getData() {
        return data;
    }

    public void setData(List<GroupListResponse> data) {
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

    public String getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(String advertisementId) {
        this.advertisementId = advertisementId;
    }

    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("channelKey")
    @Expose
    private Integer channelKey;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("subcategory_id")
    @Expose
    private String subcategoryId;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("category_title")
    @Expose
    String category1;
    @SerializedName("category_id")
    @Expose
    String categoryId;
    @SerializedName("subcategory1_id")
    @Expose
    String categoryId2;
    @SerializedName("subcategory2_id")
    @Expose
    String categoryId3;
    @SerializedName("subcategory3_id")
    @Expose
    String categoryId4;
    @SerializedName("subcategory4_id")
    @Expose
    String categoryId5;
    @SerializedName("subcategory5_id")
    @Expose
    String categoryId6;
    @SerializedName("subcategory_title")
    @Expose
    String category2;
    @SerializedName("subcategory_title2")
    @Expose
    String category3;
    @SerializedName("subcategory_title3")
    @Expose
    String category4;
    @SerializedName("subcategory_title4")
    @Expose
    String category5;
    @SerializedName("subcategory_title5")
    @Expose
    String category6;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("Favourite")
    @Expose
    private Integer favourite;
    @SerializedName("group_image")
    @Expose
    private String groupImage;
    @SerializedName("cost_range")
    @Expose
    private String costRange;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("members_count")
    @Expose
    private String membersCount;
    @SerializedName("join_Member")
    @Expose
    private String joinMember;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("groupLocation")
    @Expose
    private String groupLocation;
    @SerializedName("join_status")
    @Expose
    private String joinStatus;
    @SerializedName("groupstatus")
    @Expose
    private String groupStatus;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getChannelKey() {
        return channelKey;
    }

    public void setChannelKey(Integer channelKey) {
        this.channelKey = channelKey;
    }

    public Integer getFavourite() {
        return favourite;
    }

    public void setFavourite(Integer favourite) {
        this.favourite = favourite;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryId2() {
        return categoryId2;
    }

    public void setCategoryId2(String categoryId2) {
        this.categoryId2 = categoryId2;
    }

    public String getCategoryId3() {
        return categoryId3;
    }

    public void setCategoryId3(String categoryId3) {
        this.categoryId3 = categoryId3;
    }

    public String getCategoryId4() {
        return categoryId4;
    }

    public void setCategoryId4(String categoryId4) {
        this.categoryId4 = categoryId4;
    }

    public String getCategoryId5() {
        return categoryId5;
    }

    public void setCategoryId5(String categoryId5) {
        this.categoryId5 = categoryId5;
    }

    public String getCategoryId6() {
        return categoryId6;
    }

    public void setCategoryId6(String categoryId6) {
        this.categoryId6 = categoryId6;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(String groupStatus) {
        this.groupStatus = groupStatus;
    }

    public String getJoinMember() {
        return joinMember;
    }

    public void setJoinMember(String joinMember) {
        this.joinMember = joinMember;
    }

    public String getUserId() {
        return userId;
    }

    public String getGroupLocation() {
        return groupLocation;
    }

    public void setGroupLocation(String groupLocation) {
        this.groupLocation = groupLocation;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(String subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public String getCategory1() {
        return category1;
    }

    public void setCategory1(String category1) {
        this.category1 = category1;
    }

    public String getCategory2() {
        return category2;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    public String getCategory3() {
        return category3;
    }

    public void setCategory3(String category3) {
        this.category3 = category3;
    }

    public String getCategory4() {
        return category4;
    }

    public void setCategory4(String category4) {
        this.category4 = category4;
    }

    public String getCategory5() {
        return category5;
    }

    public void setCategory5(String category5) {
        this.category5 = category5;
    }

    public String getCategory6() {
        return category6;
    }

    public void setCategory6(String category6) {
        this.category6 = category6;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public String getCostRange() {
        return costRange;
    }

    public void setCostRange(String costRange) {
        this.costRange = costRange;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getMembersCount() {
        return membersCount;
    }

    public void setMembersCount(String membersCount) {
        this.membersCount = membersCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJoinStatus() {
        return joinStatus;
    }

    public void setJoinStatus(String joinStatus) {
        this.joinStatus = joinStatus;
    }
}
