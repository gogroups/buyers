package com.gogroup.app.gogroupapp.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Lenovo on 7/29/2017.
 */

public class UserResponse {
    @SerializedName("status")
    @Expose
    boolean status;
    @SerializedName("messages")
    @Expose
    String message;
    @SerializedName("data")
    @Expose
    UserResponse data;

    public UserResponse getData() {
        return data;
    }

    public void setData(UserResponse data) {
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    @SerializedName("user_details")
    @Expose
    UserResponse userDetail;
    @SerializedName("token")
    @Expose
    String token;

    public UserResponse getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserResponse userDetail) {
        this.userDetail = userDetail;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("account_number")
    @Expose
    private String accountNum;
    @SerializedName("ac_holder_name")
    @Expose
    private String acHolderName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("ifsc")
    @Expose
    private String Ifsc;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("paytm_no")
    @Expose
    private String paytmNum;
    @SerializedName("isActive")
    @Expose
    private Integer isActive;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("isVerifyOtp")
    @Expose
    private boolean isVerifyOtp;

    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("zipCode")
    @Expose
    private String zipCode;
    @SerializedName("contact_number")
    @Expose
    private String contactNumber;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("notify_status")
    @Expose
    private boolean notifyStatus;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("usp")
    @Expose
    private String usp;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("pan_gst")
    @Expose
    private String panGst;

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsp() {
        return usp;
    }

    public void setUsp(String usp) {
        this.usp = usp;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPanGst() {
        return panGst;
    }

    public void setPanGst(String panGst) {
        this.panGst = panGst;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean isVerifyOtp() {
        return isVerifyOtp;
    }

    public void setVerifyOtp(boolean verifyOtp) {
        isVerifyOtp = verifyOtp;
    }
    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public boolean isNotifyStatus() {
        return notifyStatus;
    }

    public void setNotifyStatus(boolean notifyStatus) {
        this.notifyStatus = notifyStatus;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getAddress() {
        return address;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public String getAcHolderName() {
        return acHolderName;
    }

    public String getIfsc() {
        return Ifsc;
    }

    public String getBankName() {
        return bankName;
    }

    public String getPaytmNum() {
        return paytmNum;
    }
}
