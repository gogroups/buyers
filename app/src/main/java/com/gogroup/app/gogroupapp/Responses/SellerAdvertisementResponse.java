package com.gogroup.app.gogroupapp.Responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 7/29/2017.
 */

public class SellerAdvertisementResponse implements Parcelable {
    @SerializedName("status")
    @Expose
    boolean status;
    @SerializedName("messages")
    @Expose
    String message;
    @SerializedName("data")
    @Expose
    List<SellerAdvertisementResponse> data=new ArrayList<>();
    @SerializedName("advertisementData")
    @Expose
    SellerAdvertisementResponse advertisementData;


    public SellerAdvertisementResponse getAdvertisementData() {
        return advertisementData;
    }

    public void setAdvertisementData(SellerAdvertisementResponse advertisementData) {
        this.advertisementData = advertisementData;
    }

    public List<SellerAdvertisementResponse> getData() {
        return data;
    }

    public void setData(List<SellerAdvertisementResponse> data) {
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

    @SerializedName("details")
    @Expose
    SellerAdvertisementResponse details;

    public SellerAdvertisementResponse getDetails() {
        return details;
    }

    public void setDetails(SellerAdvertisementResponse details) {
        this.details = details;
    }

    @SerializedName("advertisement_id")
    @Expose
    String advertisementId;
    @SerializedName("end_date")
    @Expose
    String endDate;
    @SerializedName("start_date")
    @Expose
    String startDate;
    @SerializedName("Favourite")
    @Expose
    Integer favourite;
    @SerializedName("advertisement_details")
    @Expose
    String advertisementDetails;
    @SerializedName("actual_price")
    @Expose
    String actualPrice;
    @SerializedName("rating")
    @Expose
    Float rating;
    @SerializedName("selfRating")
    @Expose
    Float selfRating;
    @SerializedName("likedcount")
    @Expose
    String likedcount;
    @SerializedName("isliked")
    @Expose
    int isliked;
    @SerializedName("offer_price")
    @Expose
    String offerPrice;
    @SerializedName("offerfortwo")
    @Expose
    String costfortwo;

    @SerializedName("advertisement_name")
    @Expose
    String advertisementName;
    @SerializedName("pendingCount")
    @Expose
    String pendingCodeCount;
    @SerializedName("purchasedCount")
    @Expose
    String purchasedCount;
    @SerializedName("orderPlacedCount")
    @Expose
    String orderPlacedCount;
    @SerializedName("category_title")
    @Expose
    String category1;
    @SerializedName("category_id")
    @Expose
    String categoryId1;
    @SerializedName("subcategory_id")
    @Expose
    String categoryId2;
    @SerializedName("subcategory2_id")
    @Expose
    String categoryId3;
    @SerializedName("subcategory3_id")
    @Expose
    String categoryId4;
    @SerializedName("subcategory4_id")
    @Expose
    String categoryId5;
    @SerializedName("subcategory5_id")
    @Expose
    String categoryId6;
    @SerializedName("subcategory_title")
    @Expose
    String category2;
    @SerializedName("subcategory_title2")
    @Expose
    String category3;
    @SerializedName("subcategory_title3")
    @Expose
    String category4;
    @SerializedName("subcategory_title4")
    @Expose
    String category5;
    @SerializedName("subcategory_title5")
    @Expose
    String category6;
    @SerializedName("location")
    @Expose
    String location;
    @SerializedName("min_user_count")
    @Expose
    String minUserCount;
    @SerializedName("quantity_per_user")
    @Expose
    String quantity_per_user;
    @SerializedName("views_count")
    @Expose
    String viewsCount;
    @SerializedName("group_count")
    @Expose
    String groupCount;
    @SerializedName("coupon_code")
    @Expose
    String couponCode;
    @SerializedName("seller_contact")
    @Expose
    String sellerContact;
    @SerializedName("seller_name")
    @Expose
    String sellerName;
    @SerializedName("isRatingOption")
    @Expose
    Integer isRatingOption;
    @SerializedName("address")
    @Expose
    String address;
    @SerializedName("seller_id")
    @Expose
    String sellerId;
    @SerializedName("seller_email")
    @Expose
    String email;
    @SerializedName("images_details")
    @Expose
    List<ImageResponse> imagesDetails=new ArrayList<>();
    @SerializedName("activeStatus")
    @Expose
    String activeStatus;


    @SerializedName("offerforx")
    @Expose
    String costforx;


    public Integer getIsRatingOption() {
        return isRatingOption;
    }

    public String getLikedcount() {
        return likedcount;
    }

    public void setIsliked(int isliked) {
        this.isliked = isliked;
    }

    public void setLikedcount(String likedcount) {
        this.likedcount = likedcount;
    }

    public int getIsliked() {
        return isliked;
    }

    public List<ImageResponse> getImagesDetails() {
        return imagesDetails;
    }

    public void setImagesDetails(List<ImageResponse> imagesDetails) {
        this.imagesDetails = imagesDetails;
    }
    public String getStartDate() {
        return startDate;
    }

    public String getCategoryId1() {
        return categoryId1;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public String getPendingCodeCount() {
        return pendingCodeCount;
    }

    public String getPurchasedCount() {
        return purchasedCount;
    }

    public String getAddress() {
        return address;
    }

    public void setCategoryId1(String categoryId1) {
        this.categoryId1 = categoryId1;
    }

    public String getCategoryId2() {
        return categoryId2;
    }

    public String getOrderPlacedCount() {
        return orderPlacedCount;
    }

    public void setCategoryId2(String categoryId2) {
        this.categoryId2 = categoryId2;
    }

    public String getCategoryId3() {
        return categoryId3;
    }

    public void setCategoryId3(String categoryId3) {
        this.categoryId3 = categoryId3;
    }

    public String getCategoryId4() {
        return categoryId4;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setCategoryId4(String categoryId4) {
        this.categoryId4 = categoryId4;
    }

    public String getCategoryId5() {
        return categoryId5;
    }

    public void setCategoryId5(String categoryId5) {
        this.categoryId5 = categoryId5;
    }

    public String getCategoryId6() {
        return categoryId6;
    }

    public void setCategoryId6(String categoryId6) {
        this.categoryId6 = categoryId6;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getAdvertisementDetails() {
        return advertisementDetails;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public String getCostfortwo() {
        return costfortwo;
    }

    public String getCostforx() {return costforx; }

    public void setCostforx() { this.costforx = costforx; }

    public String getAdvertisementName() {
        return advertisementName;
    }

    public void setAdvertisementDetails(String advertisementDetails) {
        this.advertisementDetails = advertisementDetails;
    }

    public Integer getFavourite() {
        return favourite;
    }

    public void setFavourite(Integer favourite) {
        this.favourite = favourite;
    }

    public String getCategory1() {
        return category1;
    }

    public void setCategory1(String category1) {
        this.category1 = category1;
    }

    public String getMinUserCount() {
        return minUserCount;
    }

    public void setMinUserCount(String minUserCount) {
        this.minUserCount = minUserCount;
    }

    public String getQuantityPerUser() {
        return quantity_per_user;
    }



    public String getCategory2() {
        return category2;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    public String getSellerContact() {
        return sellerContact;
    }

    public void setSellerContact(String sellerContact) {
        this.sellerContact = sellerContact;
    }

    public Float getRating() {
        return rating;
    }

    public Float getSelfRating() {
        return selfRating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public void setSelfRating(Float selfRating) {
        this.selfRating = selfRating;
    }

    public String getCategory3() {
        return category3;
    }

    public void setCategory3(String category3) {
        this.category3 = category3;
    }

    public String getCategory4() {
        return category4;
    }

    public void setCategory4(String category4) {
        this.category4 = category4;
    }

    public String getCategory5() {
        return category5;
    }

    public void setCategory5(String category5) {
        this.category5 = category5;
    }

    public String getCategory6() {
        return category6;
    }

    public void setCategory6(String category6) {
        this.category6 = category6;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(String advertisementId) {
        this.advertisementId = advertisementId;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(String viewsCount) {
        this.viewsCount = viewsCount;
    }

    public String getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(String groupCount) {
        this.groupCount = groupCount;
    }


    public SellerAdvertisementResponse() {
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.status ? (byte) 1 : (byte) 0);
        dest.writeString(this.message);
        dest.writeTypedList(this.data);
        dest.writeParcelable(this.advertisementData, flags);
        dest.writeParcelable(this.details, flags);
        dest.writeString(this.advertisementId);
        dest.writeString(this.endDate);
        dest.writeString(this.startDate);
        dest.writeValue(this.favourite);
        dest.writeString(this.advertisementDetails);
        dest.writeString(this.actualPrice);
        dest.writeString(this.costfortwo);
        dest.writeValue(this.rating);
        dest.writeValue(this.selfRating);
        dest.writeString(this.likedcount);
        dest.writeInt(this.isliked);
        dest.writeString(this.offerPrice);
        dest.writeString(this.advertisementName);
        dest.writeString(this.pendingCodeCount);
        dest.writeString(this.purchasedCount);
        dest.writeString(this.orderPlacedCount);
        dest.writeString(this.category1);
        dest.writeString(this.categoryId1);
        dest.writeString(this.categoryId2);
        dest.writeString(this.categoryId3);
        dest.writeString(this.categoryId4);
        dest.writeString(this.categoryId5);
        dest.writeString(this.categoryId6);
        dest.writeString(this.category2);
        dest.writeString(this.category3);
        dest.writeString(this.category4);
        dest.writeString(this.category5);
        dest.writeString(this.category6);
        dest.writeString(this.location);
        dest.writeString(this.minUserCount);
        dest.writeString(this.quantity_per_user);
        dest.writeString(this.viewsCount);
        dest.writeString(this.groupCount);
        dest.writeString(this.couponCode);
        dest.writeString(this.sellerContact);
        dest.writeString(this.sellerName);
        dest.writeValue(this.isRatingOption);
        dest.writeString(this.address);
        dest.writeString(this.sellerId);
        dest.writeString(this.email);
        dest.writeTypedList(this.imagesDetails);
        dest.writeString(this.activeStatus);
       // dest.writeString(this.adStatus);
        dest.writeString(this.costforx);
    }

    protected SellerAdvertisementResponse(Parcel in) {
        this.status = in.readByte() != 0;
        this.message = in.readString();
        this.data = in.createTypedArrayList(SellerAdvertisementResponse.CREATOR);
        this.advertisementData = in.readParcelable(SellerAdvertisementResponse.class.getClassLoader());
        this.details = in.readParcelable(SellerAdvertisementResponse.class.getClassLoader());
        this.advertisementId = in.readString();
        this.endDate = in.readString();
        this.startDate = in.readString();
        this.favourite = (Integer) in.readValue(Integer.class.getClassLoader());
        this.advertisementDetails = in.readString();
        this.actualPrice = in.readString();
        this.costfortwo = in.readString();
        this.rating = (Float) in.readValue(Float.class.getClassLoader());
        this.selfRating = (Float) in.readValue(Float.class.getClassLoader());
        this.likedcount = in.readString();
        this.isliked = in.readInt();
        this.offerPrice = in.readString();
        this.advertisementName = in.readString();
        this.pendingCodeCount = in.readString();
        this.purchasedCount = in.readString();
        this.orderPlacedCount = in.readString();
        this.category1 = in.readString();
        this.categoryId1 = in.readString();
        this.categoryId2 = in.readString();
        this.categoryId3 = in.readString();
        this.categoryId4 = in.readString();
        this.categoryId5 = in.readString();
        this.categoryId6 = in.readString();
        this.category2 = in.readString();
        this.category3 = in.readString();
        this.category4 = in.readString();
        this.category5 = in.readString();
        this.category6 = in.readString();
        this.location = in.readString();
        this.minUserCount = in.readString();
        this.quantity_per_user = in.readString();
        this.viewsCount = in.readString();
        this.groupCount = in.readString();
        this.couponCode = in.readString();
        this.sellerContact = in.readString();
        this.sellerName = in.readString();
        this.isRatingOption = (Integer) in.readValue(Integer.class.getClassLoader());
        this.address = in.readString();
        this.sellerId = in.readString();
        this.email = in.readString();
        this.imagesDetails = in.createTypedArrayList(ImageResponse.CREATOR);
        this.activeStatus = in.readString();
        //this.adStatus = in.readString();
        this.costforx = in.readString();
    }

    public static final Creator<SellerAdvertisementResponse> CREATOR = new Creator<SellerAdvertisementResponse>() {
        @Override
        public SellerAdvertisementResponse createFromParcel(Parcel source) {
            return new SellerAdvertisementResponse(source);
        }

        @Override
        public SellerAdvertisementResponse[] newArray(int size) {
            return new SellerAdvertisementResponse[size];
        }
    };
}
