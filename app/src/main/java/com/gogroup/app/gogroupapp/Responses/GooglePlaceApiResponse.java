package com.gogroup.app.gogroupapp.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dmlabs-storage on 28/3/17.
 */

public class GooglePlaceApiResponse {


    @SerializedName("predictions")
    @Expose
    private List<GooglePlaceApiResponse> predictions = null;

    public List<GooglePlaceApiResponse> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<GooglePlaceApiResponse> predictions) {
        this.predictions = predictions;
    }

    @SerializedName("description")
    @Expose
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


























