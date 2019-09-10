package com.gogroup.app.gogroupapp.Models;

/**
 * Created by zabius on 7/28/17.
 */

public class UserCreatedGroupModel {
    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    String group_name;
    String group_members;
    String group_pic;
    String start_date;
    String end_date;

    public UserCreatedGroupModel(String group_name, String group_members, String group_pic, String start_date, String end_date, String status) {
        this.group_name = group_name;
        this.group_members = group_members;
        this.group_pic = group_pic;
        this.start_date = start_date;
        this.end_date = end_date;
        this.status = status;
    }

    public String getGroup_members() {

        return group_members;
    }

    public void setGroup_members(String group_members) {
        this.group_members = group_members;
    }

    public String getGroup_pic() {
        return group_pic;
    }

    public void setGroup_pic(String group_pic) {
        this.group_pic = group_pic;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String status;
}
