package com.gogroup.app.gogroupapp.Models;

/**
 * Created by zabius on 7/28/17.
 */

public class UserChatModel {
    String image;

    public UserChatModel(String image, String user_name, String message) {
        this.image = image;
        this.user_name = user_name;
        this.message = message;
    }

    String user_name;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    String message;
}
