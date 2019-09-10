package com.gogroup.app.gogroupapp.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommonResponse {


    @SerializedName("status")
    @Expose
    boolean status;
    @SerializedName("messages")
    @Expose
    String message;
    @SerializedName("data")
    @Expose
    ListResponse detail;

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ListResponse getDetail() {
        return detail;
    }
}
