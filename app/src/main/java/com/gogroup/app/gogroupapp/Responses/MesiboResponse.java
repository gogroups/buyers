package com.gogroup.app.gogroupapp.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Lenovo on 7/29/2017.
 */

public class MesiboResponse {


    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("oid")
    @Expose
    private String oid;
    @SerializedName("msgs")
    @Expose
    private String msgs;
    @SerializedName("users")
    @Expose
    private String users;
    @SerializedName("groups")
    @Expose
    private String groups;
    @SerializedName("conns")
    @Expose
    private String conns;
    @SerializedName("apps")
    @Expose
    private String apps;
    @SerializedName("storage")
    @Expose
    private String storage;
    @SerializedName("pid")
    @Expose
    private String pid;
    @SerializedName("billday")
    @Expose
    private String billday;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("expiry")
    @Expose
    private String expiry;
    @SerializedName("issuetime")
    @Expose
    private String issuetime;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("amount")
    @Expose
    private String amount;

    public String getUid() {
        return uid;
    }

    public String getOid() {
        return oid;
    }

    public String getMsgs() {
        return msgs;
    }

    public String getUsers() {
        return users;
    }

    public String getGroups() {
        return groups;
    }

    public String getConns() {
        return conns;
    }

    public String getApps() {
        return apps;
    }

    public String getStorage() {
        return storage;
    }

    public String getPid() {
        return pid;
    }

    public String getBillday() {
        return billday;
    }

    public String getStatus() {
        return status;
    }

    public String getFlag() {
        return flag;
    }

    public String getExpiry() {
        return expiry;
    }

    public String getIssuetime() {
        return issuetime;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public String getAmount() {
        return amount;
    }
}
