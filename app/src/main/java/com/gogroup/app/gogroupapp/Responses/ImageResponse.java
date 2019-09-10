package com.gogroup.app.gogroupapp.Responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zabius on 9/19/17.
 */

public class ImageResponse implements Parcelable {


    @SerializedName("advertisement_id")
    @Expose
    String advertisementId;
    @SerializedName("image_id")
    @Expose
    String imageId;
    @SerializedName("image_path")
    @Expose
    String imagePath;

    public String getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(String advertisementId) {
        this.advertisementId = advertisementId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.advertisementId);
        dest.writeString(this.imageId);
        dest.writeString(this.imagePath);
    }

    public ImageResponse() {
    }

    protected ImageResponse(Parcel in) {
        this.advertisementId = in.readString();
        this.imageId = in.readString();
        this.imagePath = in.readString();
    }

    public static final Parcelable.Creator<ImageResponse> CREATOR = new Parcelable.Creator<ImageResponse>() {
        @Override
        public ImageResponse createFromParcel(Parcel source) {
            return new ImageResponse(source);
        }

        @Override
        public ImageResponse[] newArray(int size) {
            return new ImageResponse[size];
        }
    };
}
