package com.gogroup.app.gogroupapp.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MesiboDetailResponse {

    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("account")
    @Expose
    private MesiboResponse account;
    @SerializedName("plan")
    @Expose
    private MesiboResponse plan;
    @SerializedName("user")
    @Expose
    private MesiboResponse user;
    @SerializedName("app")
    @Expose
    private MesiboResponse app;
    @SerializedName("op")
    @Expose
    private String op;
    @SerializedName("error")
    @Expose
    private String error;


    public String getError() {
        return error;
    }

    public Boolean getResult() {
        return result;
    }

    public MesiboResponse getAccount() {
        return account;
    }

    public MesiboResponse getPlan() {
        return plan;
    }

    public MesiboResponse getApp() {
        return app;
    }

    public String getOp() {
        return op;
    }

    public MesiboResponse getUser() {
        return user;
    }
}
