package com.gogroup.app.gogroupapp.Models;

/**
 * Created by zabius on 8/2/17.
 */

public class AddViewListModel {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserlocation() {
        return userlocation;
    }

    public void setUserlocation(String userlocation) {
        this.userlocation = userlocation;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    String username;
    String userlocation;

    public AddViewListModel(String username, String userlocation, String category) {
        this.username = username;
        this.userlocation = userlocation;
        this.category = category;
    }

    String category;
}
