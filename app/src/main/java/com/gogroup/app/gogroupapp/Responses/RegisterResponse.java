package com.gogroup.app.gogroupapp.Responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Lenovo on 7/29/2017.
 */

public class RegisterResponse implements Parcelable
{
    @SerializedName("status")
    @Expose
    boolean isSuccess;
    @SerializedName("messages")
    @Expose
    Object message;
    @SerializedName("data")
    @Expose
    RegisterResponse data;

    public RegisterResponse getData() {
        return data;
    }

    public void setData(RegisterResponse data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }


    @SerializedName("opt_text")
    @Expose
    String optText;

    @SerializedName("token")
    @Expose
    String token;
    @SerializedName("email")
    @Expose
    String email;
    @SerializedName("contact")
    @Expose
    String contact;
    @SerializedName("user_id")
    @Expose
    String userId;

    public String getOptText() {
        return optText;
    }

    public void setOptText(String optText) {
        this.optText = optText;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeByte(this.isSuccess ? (byte) 1 : (byte) 0);
        dest.writeValue(this.message);
        dest.writeParcelable(this.data, flags);
        dest.writeString(this.optText);
        dest.writeString(this.token);
        dest.writeString(this.email);
        dest.writeString(this.contact);
        dest.writeString(this.userId);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public RegisterResponse()
    {
    }

    protected RegisterResponse(Parcel in)
    {
        this.isSuccess = in.readByte() != 0;
        this.message = in.readString();
        this.data = in.readParcelable(RegisterResponse.class.getClassLoader());
        this.optText = in.readString();
        this.token = in.readString();
        this.email = in.readString();
        this.contact = in.readString();
        this.userId = in.readString();
    }

    public static final Parcelable.Creator<RegisterResponse> CREATOR = new Parcelable.Creator<RegisterResponse>()
    {
        @Override
        public RegisterResponse createFromParcel(Parcel source)
        {
            return new RegisterResponse(source);
        }

        @Override
        public RegisterResponse[] newArray(int size) {
            return new RegisterResponse[size];
        }
    };
}
