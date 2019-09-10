package com.gogroup.app.gogroupapp.Models;

/**
 * Created by zabius on 9/28/17.
 */

public class FilterModel {


    String startFromDate, startToDate, endFromDate, endToDate, categoryId1, categoryId2, location,costRangeFrom,costRangeTo, dealName,groupName, joinStatus;


    public void setSellerFilterValues(String startFromDate, String startToDate, String endFromDate, String endToDate, String categoryId1, String categoryId2, String location,String advertisementName) {
        this.startFromDate = startFromDate;
        this.endFromDate = endFromDate;
        this.startToDate = startToDate;
        this.endToDate = endToDate;
        this.categoryId1 = categoryId1;
        this.categoryId2 = categoryId2;
        this.location = location;
        this.dealName = advertisementName;
    }
    public void setFeedFilterValues(String startFromDate, String startToDate, String endFromDate, String endToDate) {
        this.startFromDate = startFromDate;
        this.endFromDate = endFromDate;
        this.startToDate = startToDate;
        this.endToDate = endToDate;
//        this.location = location;
    }

    public void setUserFilterValues(String startFromDate, String startToDate, String endFromDate, String endToDate, String categoryId1, String categoryId2, String location, String costRangeFrom, String costRangeTo,String groupName) {
        this.startFromDate = startFromDate;
        this.endFromDate = endFromDate;
        this.startToDate = startToDate;
        this.endToDate = endToDate;
        this.categoryId1 = categoryId1;
        this.categoryId2 = categoryId2;
        this.location = location;
        this.costRangeFrom = costRangeFrom;
        this.costRangeTo = costRangeTo;
        this.groupName=groupName;
        this.joinStatus = joinStatus;
    }

    public String getDealName() {
        return dealName;
    }

    public String getCostRangeFrom() {
        return costRangeFrom;
    }

    public String getCostRangeTo() {
        return costRangeTo;
    }

    public String getStartFromDate() {
        return startFromDate;
    }

    public String getEndFromDate() {
        return endFromDate;
    }

    public String getCategoryId1() {
        return categoryId1;
    }

    public String getCategoryId2() {
        return categoryId2;
    }

    public String getLocation() {
        return location;
    }

    public String getStartToDate() {
        return startToDate;
    }

    public String getEndToDate() {
        return endToDate;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getJoinStatus(){return joinStatus;}

    public void clearValues() {
        startFromDate = null;
        startToDate = null;
        endToDate = null;
        endFromDate = null;
        categoryId1 = null;
        categoryId2 = null;
        location = null;
        costRangeFrom = null;
        costRangeTo = null;
        dealName = null;
        groupName = null;
    }
}

