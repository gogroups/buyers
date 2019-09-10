package com.gogroup.app.gogroupapp.Models;

/**
 * Created by zabius on 7/28/17.
 */

public class GroupModel {
    public GroupModel(String group_name, String created_by, String category_name, String subcategory_name, String start_date, String end_date, String members, String cost_range) {
        this.group_name = group_name;
        this.created_by = created_by;
        this.category_name = category_name;
        this.subcategory_name = subcategory_name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.members = members;
        this.cost_range = cost_range;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getSubcategory_name() {
        return subcategory_name;
    }

    public void setSubcategory_name(String subcategory_name) {
        this.subcategory_name = subcategory_name;
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

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getCost_range() {
        return cost_range;
    }

    public void setCost_range(String cost_range) {
        this.cost_range = cost_range;
    }

    String group_name,created_by,category_name,subcategory_name,start_date,end_date,members,cost_range;

}
