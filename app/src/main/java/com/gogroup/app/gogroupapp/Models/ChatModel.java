package com.gogroup.app.gogroupapp.Models;

/**
 * Created by zabius on 8/1/17.
 */

public class ChatModel {
    String username;
    String message;

    public ChatModel(String username, String message, int isMine, String time) {
        this.username = username;
        this.message = message;
        this.isMine = isMine;
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getIsMine() {
        return isMine;
    }

    public void setIsMine(int isMine) {
        this.isMine = isMine;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    int isMine;
    String time;
}
