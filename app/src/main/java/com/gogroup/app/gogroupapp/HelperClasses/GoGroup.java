package com.gogroup.app.gogroupapp.HelperClasses;

import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lenovo on 08-Aug-17.
 */

public class GoGroup extends MultiDexApplication {

    public String categoryId, groupId,advertisementId,groupIdCamp;
    public Map<String,String> notificationMap = new HashMap<>();

    private static GoGroup instance;

    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        instance = this;
    }

    public static GoGroup getInstance() {
        return instance;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getGroupId() {

        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupIdCamp() {
        return groupIdCamp;
    }

    public void setGroupIdCamp(String groupIdCamp) {
        this.groupIdCamp = groupIdCamp;
    }

    public String getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(String advertisementId) {
        this.advertisementId = advertisementId;
    }
}
