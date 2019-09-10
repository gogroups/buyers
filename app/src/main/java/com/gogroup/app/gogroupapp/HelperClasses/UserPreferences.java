package com.gogroup.app.gogroupapp.HelperClasses;

import android.content.Context;
import android.content.SharedPreferences;

import com.gogroup.app.gogroupapp.Responses.MesiboDetailResponse;
import com.gogroup.app.gogroupapp.Responses.UserResponse;

/**
 * Created by dmlabs-storage on 22/3/17.
 */

public class UserPreferences {

    private static final String TAG = "UserPreferences";
    public SharedPreferences userPrefs;
    public static final String USER_DETAILS = "user_details";
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "username";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String MOBILE = "mobile";
    public static final String GENDER = "gender";
    public static final String NOTIFY_STATUS = "notify_status";
    public static final String TOKEN = "token";
    public static final String AGE = "age";
    public static final String LOCATION = "location";
    public static final String CUSTOM_ADDRESS = "customAddress";
    public static final String PROFILE_IMAGE = "profile_image";
    public static final String USER_TYPE = "userType";
    public static final String IS_LOGIN = "is_login";
    public static final String PERMISSION = "permission";
    public static final String IS_VERIFY_OTP = "is_verify_otp";
    public static final String MESIBO_USER_ID  = "mesiboUserId";
    public static final String MESIBO_USER_TOKEN = "mesiboUserToken";
    public static final String IS_ACTIVE = "is_active";
    public static final String IS_DEEPLINK = "is_deepLink";
    public static final String ZIP_CODE = "zip_code";
    public static final String DEVICE_TOKEN = "device_token";
    public static final String DEVICE_TOKEN_SENT_TO_SERVER = "device_token_sent_to_server";


    public String userId, userName, age, email, mobile, location, profileImage, userType, token, deviceToken, zipCode;
    public Boolean notifyStatus, isAskedForPermissions;


    private static UserPreferences instance;

    public static UserPreferences getInstance() {
        if (instance == null)
            instance = new UserPreferences();

        return instance;
    }

    private UserPreferences() {
        initPrefs();
    }

    private void initPrefs() {
        if (null != userPrefs) return;
        userPrefs = GoGroup.getInstance().getSharedPreferences(USER_DETAILS, Context.MODE_PRIVATE);
    }
    public void setDeviceToken(String token) {
        userPrefs.edit().putString(DEVICE_TOKEN, token).commit();
    }
    public String getDeviceToken() {
        return userPrefs.getString(DEVICE_TOKEN,null);
    }


    public void clearUserDetails() {
        userPrefs.edit().clear().commit();
    }

    //    public void loadPreference(Context context) {
//        preferences = context.getSharedPreferences(appName, ActivityDashboardResponse.MODE_PRIVATE);
//        userId=preferences.getString(USER_ID, null);
//    }
//
    public void savePreference(UserResponse user) {
//        preferences = context.getSharedPreferences(appName, ActivityDashboardResponse.MODE_PRIVATE);
//        editor = preferences.edit();

        userPrefs.edit().putString(TOKEN, user.getToken()).commit();
        UserResponse userDetail = user.getUserDetail();
        userPrefs.edit().putString(USER_ID, userDetail.getUserId()).commit();
        userPrefs.edit().putString(ZIP_CODE, userDetail.getZipCode()).commit();
        userPrefs.edit().putString(USER_TYPE, userDetail.getUserType()).commit();
        userPrefs.edit().putString(USER_NAME, userDetail.getName()).commit();
        userPrefs.edit().putString(AGE, userDetail.getAge()).commit();
        userPrefs.edit().putString(EMAIL, userDetail.getEmail()).commit();
        userPrefs.edit().putString(MOBILE, userDetail.getContactNumber()).commit();
        userPrefs.edit().putString(CUSTOM_ADDRESS, userDetail.getAddress()).commit();
        userPrefs.edit().putString(LOCATION, userDetail.getLocation()).commit();
        userPrefs.edit().putString(PROFILE_IMAGE, userDetail.getProfileImage()).commit();
        userPrefs.edit().putBoolean(NOTIFY_STATUS, userDetail.isNotifyStatus()).commit();
        userPrefs.edit().putBoolean(IS_LOGIN, true).commit();
        userPrefs.edit().putBoolean(IS_VERIFY_OTP, userDetail.isVerifyOtp()).commit();
        userPrefs.edit().putInt(IS_ACTIVE, userDetail.getIsActive() != null ? userDetail.getIsActive() : 0).commit();


    }
    public void saveMesiboInfo(MesiboDetailResponse body) {

        userPrefs.edit().putString(MESIBO_USER_ID, body.getUser().getUid()).commit();
        userPrefs.edit().putString(MESIBO_USER_TOKEN, body.getUser().getToken()).commit();

    }
    public void updateProfile(String name, String age, String phone, String address, String location, String profileImage, String zipCode) {
        userPrefs.edit().putString(USER_NAME, name).commit();
        userPrefs.edit().putString(AGE, age).commit();
        userPrefs.edit().putString(ZIP_CODE, zipCode).commit();
        userPrefs.edit().putString(MOBILE, phone).commit();
        userPrefs.edit().putString(CUSTOM_ADDRESS, address).commit();
        userPrefs.edit().putString(LOCATION, location).commit();
        userPrefs.edit().putString(PROFILE_IMAGE, profileImage).commit();

    }

    public void updateNotifyStatus(boolean isNotify) {
        userPrefs.edit().putBoolean(NOTIFY_STATUS, isNotify).commit();

    }

    public void setIsDeepLink(boolean isDeeplink) {
        userPrefs.edit().putBoolean(IS_DEEPLINK, isDeeplink).commit();
    }

    public boolean getIsDeepLink() {
        return userPrefs.getBoolean(IS_DEEPLINK, false);
    }

    public void setCategoryId(String groupId, String categoryId) {
        userPrefs.edit().putString(groupId, categoryId).commit();

    }

    public String getCategoryId(String groupId) {
        return userPrefs.getString(groupId, null);
    }


    public String getUserId() {
        return userPrefs.getString(USER_ID, null);
    }
    public void setIsVerifyOtp(boolean isVerifyOtp) {
        userPrefs.edit().putBoolean(IS_VERIFY_OTP, isVerifyOtp).commit();
    }
    public void setEmail(String email) {
        userPrefs.edit().putString(EMAIL, email).commit();
    }




    public void setMobile(String mobile) {
        userPrefs.edit().putString(MOBILE, mobile).commit();
    }
    public Boolean getIsVerifyOtp() {
        return userPrefs.getBoolean(IS_VERIFY_OTP,false);
    }

    public String getUserName() {
        return userPrefs.getString(USER_NAME, null);
    }

    public String getAge() {
        return userPrefs.getString(AGE, null);
    }

    public String getEmail() {
        return userPrefs.getString(EMAIL, null);
    }
    public String getMesiboUserId() {
        return userPrefs.getString(MESIBO_USER_ID, null);
    }
    public String getMesiboUserToken() {
        return userPrefs.getString(MESIBO_USER_TOKEN, null);
    }

    public String getMobile() {
        return userPrefs.getString(MOBILE, null);
    }

    public String getLocation() {
        return userPrefs.getString(LOCATION, null);
    }

    public String getCustomAddress() {
        return userPrefs.getString(CUSTOM_ADDRESS, null);
    }

    public String getProfileImage() {
        return userPrefs.getString(PROFILE_IMAGE, null);
    }

    public String getUserType() {
        return userPrefs.getString(USER_TYPE, null);
    }

    public String getToken() {
        return userPrefs.getString(TOKEN, null);
    }

    public Boolean getNotifyStatus() {
        return userPrefs.getBoolean(NOTIFY_STATUS, false);
    }

    public Boolean isLogin() {
        return userPrefs.getBoolean(IS_LOGIN, false);
    }

    public Integer getIsActive() {
        return userPrefs.getInt(IS_ACTIVE, 0);
    }

    public Boolean isFirstTimeVisible() {
        return userPrefs.getBoolean(PERMISSION, true);
    }

    public void setUserValue() {
        userPrefs.edit().putBoolean(PERMISSION, false).commit();

    }


}
