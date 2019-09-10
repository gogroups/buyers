package com.gogroup.app.gogroupapp.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetailResponse {


    @SerializedName("status")
    @Expose
    boolean status;
    @SerializedName("messages")
    @Expose
    String message;
    @SerializedName("data")
    @Expose
    List<ListResponse> dataList;
    @SerializedName("detail")
    @Expose
    ListResponse detail;

    public ListResponse getDetail() {
        return detail;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<ListResponse> getDataList() {
        return dataList;
    }
}
