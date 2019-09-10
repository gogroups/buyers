package com.gogroup.app.gogroupapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zabius on 9/18/17.
 */

public class TwitterModel implements Parcelable {

    String name,photoUrl,email,id,location,screenName;

    public void setValues(String id, String email,String name,String screenName,String photoUrl,String location)
    {
        this.name=name;
        this.id=id;
        this.email=email;
        this.screenName=screenName;
        this.photoUrl=photoUrl;
        this.location=location;
    }

    public String getName() {
        return name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public String getScreenName() {
        return screenName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.photoUrl);
        dest.writeString(this.email);
        dest.writeString(this.id);
        dest.writeString(this.location);
        dest.writeString(this.screenName);
    }

    public TwitterModel() {
    }

    protected TwitterModel(Parcel in) {
        this.name = in.readString();
        this.photoUrl = in.readString();
        this.email = in.readString();
        this.id = in.readString();
        this.location = in.readString();
        this.screenName = in.readString();
    }

    public static final Parcelable.Creator<TwitterModel> CREATOR = new Parcelable.Creator<TwitterModel>() {
        @Override
        public TwitterModel createFromParcel(Parcel source) {
            return new TwitterModel(source);
        }

        @Override
        public TwitterModel[] newArray(int size) {
            return new TwitterModel[size];
        }
    };
}
