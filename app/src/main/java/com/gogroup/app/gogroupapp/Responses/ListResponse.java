package com.gogroup.app.gogroupapp.Responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListResponse implements Parcelable {



    public ListResponse(List<ListResponse> advertisementList)
    {
        this.advertisementList=advertisementList;
    }
    @SerializedName("advertisement_id")
    @Expose
    private String advertisementId;
    @SerializedName("advertisement_name")
    @Expose
    private String advertisementName;
    @SerializedName("advertisement_Status")
    @Expose
    private String advertisementStatus;

    @SerializedName("is_approved")
    @Expose
    private int is_approved;

    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("buyer_name")
    @Expose
    private String buyerName;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("order_ref_id")
    @Expose
    private String orderRefId;
    @SerializedName("order_placed_date")
    @Expose
    private String orderPlacedDate;
    @SerializedName("purchased_date")
    @Expose
    private String purchasedDate;
    @SerializedName("couponGenerated_date")
    @Expose
    private String couponGeneratedDate;
    @SerializedName("sequence_of_order")
    @Expose
    private String SequenceNo;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("advertisement_details")
    @Expose
    private String advertisementDetails;
    @SerializedName("seller_contact")
    @Expose
    private String sellerContact;
    @SerializedName("contact_number")
    @Expose
    private String contactNumber;
    @SerializedName("seller_email")
    @Expose
    private String sellerEmail;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("seller_name")
    @Expose
    private String sellerName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("category_title")
    @Expose
    private String category1;
    @SerializedName("subcategory_title")
    @Expose
    private String category2;
    @SerializedName("subcategory_title2")
    @Expose
    private String category3;
    @SerializedName("subcategory_title3")
    @Expose
    private String category4;
    @SerializedName("subcategory_title4")
    @Expose
    private String category5;
    @SerializedName("subcategory_title5")
    @Expose
    private String category6;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("min_user_count")
    @Expose
    private String minUserCount;
    @SerializedName("quantity_per_user")
    @Expose
    private String quantity_per_user;
    @SerializedName("actual_price")
    @Expose
    private String actualPrice;

    @SerializedName("offerfortwo")
    @Expose
    private String offerfortwo;

    @SerializedName("offerforx")
    @Expose
    private String offerforx;

    @SerializedName("offer_price")
    @Expose
    private String offerPrice;
    @SerializedName("coupon_id")
    @Expose
    private String couponId;
    @SerializedName("coupon_code")
    @Expose
    private String couponCode;
    @SerializedName("seller_id")
    @Expose
    private String sellerId;
    @SerializedName("coupon_status")
    @Expose
    private String couponStatus;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("seller_location")
    @Expose
    private String sellerLocation;

    @SerializedName("purchasedCount")
    @Expose
    String purchasedCount;
    @SerializedName("orderPlacedCount")
    @Expose
    String orderPlacedCount;
    @SerializedName("seller_image")
    @Expose
    private String sellerImage;
    @SerializedName("image_id")
    @Expose
    private String imageId;
    @SerializedName("image_path")
    @Expose
    private String imagePath;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("modify_date")
    @Expose
    private String modifyDate;

    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("category_image")
    @Expose
    private String categoryImage;
    @SerializedName("groupIDs")
    @Expose
    private String[] groupIdArr;
    @SerializedName("group_count")
    @Expose
    private Integer groupCount;
    @SerializedName("posts")
    @Expose
    private Integer posts;
    @SerializedName("images")
    @Expose
    private String images;
    @SerializedName("ID")
    @Expose
    private String iD;
    @SerializedName("image_name")
    @Expose
    private String imageName;
    @SerializedName("order_number")
    @Expose
    private String orderNumber;

    @SerializedName("image")
    @Expose
    private String image;




    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("channelKey")
    @Expose
    private String channelKey;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("groupLocation")
    @Expose
    private String groupLocation;
    @SerializedName("subcategory_id")
    @Expose
    private String subcategoryId;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("group_image")
    @Expose
    private String groupImage;
    @SerializedName("cost_range")
    @Expose
    private String costRange;
    @SerializedName("members_count")
    @Expose
    private String membersCount;
    @SerializedName("join_Member")
    @Expose
    private Integer joinMember;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("join_status")
    @Expose
    private String joinStatus;

    @SerializedName("images_details")
    @Expose
    private List<ListResponse> imageList;
    @SerializedName("categories")
    @Expose
    private List<ListResponse> categoryList;
    @SerializedName("banner")
    @Expose
    private List<ListResponse> bannerList;
    @SerializedName("advertisement")
    @Expose
    private List<ListResponse> advertisementList;
    @SerializedName("member_details")
    @Expose
    private List<ListResponse> memberList;
    @SerializedName("group_details")
    @Expose
    private ListResponse groupDetails;

    @SerializedName("quantity")
    @Expose
    private String quantity;


    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public String[] getGroupIdArr() {
        return groupIdArr;
    }

    public Integer getGroupCount() {
        return groupCount;
    }

    public Integer getPosts() {
        return posts;
    }

    public String getImages() {
        return images;
    }

    public String getiD() {
        return iD;
    }

    public String getImageName() {
        return imageName;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getImage() {
        return image;
    }

    public List<ListResponse> getCategoryList() {
        return categoryList;
    }

    public List<ListResponse> getBannerList() {
        return bannerList;
    }

    public List<ListResponse> getAdvertisementList() {
        return advertisementList;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getAdvertisementId() {
        return advertisementId;
    }

    public String getAdvertisementName() {
        return advertisementName;
    }

    public String getAdvertisementStatus() {
        return advertisementStatus;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getAdvertisementDetails() {
        return advertisementDetails;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getChannelKey() {
        return channelKey;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupLocation() {
        return groupLocation;
    }

    public String getSubcategoryId() {
        return subcategoryId;
    }

    public String getRating() {
        return rating;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public String getCostRange() {
        return costRange;
    }

    public String getMembersCount() {
        return membersCount;
    }

    public Integer getJoinMember() {
        return joinMember;
    }

    public String getDescription() {
        return description;
    }

    public String getJoinStatus() {
        return joinStatus;
    }

    public List<ListResponse> getMemberList() {
        return memberList;
    }

    public ListResponse getGroupDetails() {
        return groupDetails;
    }

    public String getSellerContact() {
        return sellerContact;
    }

    public String getSequenceNo() {
        return SequenceNo;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getAddress() {
        return address;
    }

    public String getCategory1() {
        return category1;
    }

    public String getCategory2() {
        return category2;
    }

    public String getCategory3() {
        return category3;
    }

    public String getCategory4() {
        return category4;
    }

    public String getCategory5() {
        return category5;
    }

    public String getStatus() {
        return status;
    }

    public String getCategory6() {
        return category6;
    }

    public String getUserId() {
        return userId;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getMinUserCount() {
        return minUserCount;
    }

    public String getQuantityPerUser(){return quantity_per_user;}

    public String getActualPrice() {
        return actualPrice;
    }

    public String getOfferPrice() {
        return offerPrice;
    }



    public String getCouponId() {
        return couponId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public String getOrderRefId() {
        return orderRefId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getCouponStatus() {
        return couponStatus;
    }

    public String getSellerLocation() {
        return sellerLocation;
    }

    public String getPurchasedCount() {
        return purchasedCount;
    }

    public String getOrderPlacedCount() {
        return orderPlacedCount;
    }

    public String getSellerImage() {
        return sellerImage;
    }

    public String getImageId() {
        return imageId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public String getOrderPlacedDate() {
        return orderPlacedDate;
    }

    public String getPurchasedDate() {
        return purchasedDate;
    }

    public String getCouponGeneratedDate() {
        return couponGeneratedDate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public String getCostforx() { return  offerforx;}

    public String getOfferfortwo() { return  offerfortwo;}

    public int getIs_approved() { return  is_approved;}

    public List<ListResponse> getImageList() {
        return imageList;
    }

    public String getQuantity() { return quantity;}

    public ListResponse() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.advertisementId);
        dest.writeString(this.advertisementName);
        dest.writeString(this.advertisementStatus);
        dest.writeString(this.endDate);
        dest.writeString(this.buyerName);
        dest.writeString(this.startDate);
        dest.writeString(this.userId);
        dest.writeString(this.orderRefId);
        dest.writeString(this.orderPlacedDate);
        dest.writeString(this.purchasedDate);
        dest.writeString(this.couponGeneratedDate);
        dest.writeString(this.SequenceNo);
        dest.writeString(this.profileImage);
        dest.writeString(this.name);
        dest.writeString(this.advertisementDetails);
        dest.writeString(this.sellerContact);
        dest.writeString(this.contactNumber);
        dest.writeString(this.sellerEmail);
        dest.writeString(this.email);
        dest.writeString(this.sellerName);
        dest.writeString(this.address);
        dest.writeString(this.category1);
        dest.writeString(this.category2);
        dest.writeString(this.category3);
        dest.writeString(this.category4);
        dest.writeString(this.category5);
        dest.writeString(this.category6);
        dest.writeString(this.location);
        dest.writeString(this.minUserCount);
        dest.writeString(this.quantity_per_user);
        dest.writeString(this.actualPrice);
        dest.writeString(this.offerPrice);
        dest.writeString(this.couponId);
        dest.writeString(this.couponCode);
        dest.writeString(this.sellerId);
        dest.writeString(this.couponStatus);
        dest.writeString(this.status);
        dest.writeString(this.sellerLocation);
        dest.writeString(this.purchasedCount);
        dest.writeString(this.orderPlacedCount);
        dest.writeString(this.sellerImage);
        dest.writeString(this.imageId);
        dest.writeString(this.imagePath);
        dest.writeString(this.createdBy);
        dest.writeString(this.createdDate);
        dest.writeString(this.modifyDate);
        dest.writeString(this.categoryId);
        dest.writeString(this.categoryImage);
        dest.writeStringArray(this.groupIdArr);
        dest.writeValue(this.groupCount);
        dest.writeValue(this.posts);
        dest.writeString(this.images);
        dest.writeString(this.iD);
        dest.writeString(this.imageName);
        dest.writeString(this.orderNumber);
        dest.writeString(this.image);
        dest.writeString(this.groupId);
        dest.writeString(this.channelKey);
        dest.writeString(this.groupName);
        dest.writeString(this.groupLocation);
        dest.writeString(this.subcategoryId);
        dest.writeString(this.rating);
        dest.writeString(this.groupImage);
        dest.writeString(this.costRange);
        dest.writeString(this.membersCount);
        dest.writeValue(this.joinMember);
        dest.writeString(this.description);
        dest.writeString(this.joinStatus);
        dest.writeTypedList(this.imageList);
        dest.writeTypedList(this.categoryList);
        dest.writeTypedList(this.bannerList);
        dest.writeTypedList(this.advertisementList);
        dest.writeTypedList(this.memberList);
        dest.writeParcelable(this.groupDetails, flags);
        dest.writeString(this.offerfortwo);
        dest.writeString(this.offerforx);
        dest.writeInt(this.is_approved);
        dest.writeString(this.quantity);
    }

    protected ListResponse(Parcel in) {
        this.advertisementId = in.readString();
        this.advertisementName = in.readString();
        this.advertisementStatus = in.readString();
        this.endDate = in.readString();
        this.buyerName = in.readString();
        this.startDate = in.readString();
        this.userId = in.readString();
        this.orderRefId = in.readString();
        this.orderPlacedDate = in.readString();
        this.purchasedDate = in.readString();
        this.couponGeneratedDate = in.readString();
        this.SequenceNo = in.readString();
        this.profileImage = in.readString();
        this.name = in.readString();
        this.advertisementDetails = in.readString();
        this.sellerContact = in.readString();
        this.contactNumber = in.readString();
        this.sellerEmail = in.readString();
        this.email = in.readString();
        this.sellerName = in.readString();
        this.address = in.readString();
        this.category1 = in.readString();
        this.category2 = in.readString();
        this.category3 = in.readString();
        this.category4 = in.readString();
        this.category5 = in.readString();
        this.category6 = in.readString();
        this.location = in.readString();
        this.minUserCount = in.readString();
        this.quantity_per_user=in.readString();
        this.actualPrice = in.readString();
        this.offerPrice = in.readString();
        this.couponId = in.readString();
        this.couponCode = in.readString();
        this.sellerId = in.readString();
        this.couponStatus = in.readString();
        this.status = in.readString();
        this.sellerLocation = in.readString();
        this.purchasedCount = in.readString();
        this.orderPlacedCount = in.readString();
        this.sellerImage = in.readString();
        this.imageId = in.readString();
        this.imagePath = in.readString();
        this.createdBy = in.readString();
        this.createdDate = in.readString();
        this.modifyDate = in.readString();
        this.categoryId = in.readString();
        this.categoryImage = in.readString();
        this.groupIdArr = in.createStringArray();
        this.groupCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.posts = (Integer) in.readValue(Integer.class.getClassLoader());
        this.images = in.readString();
        this.iD = in.readString();
        this.imageName = in.readString();
        this.orderNumber = in.readString();
        this.image = in.readString();
        this.groupId = in.readString();
        this.channelKey = in.readString();
        this.groupName = in.readString();
        this.groupLocation = in.readString();
        this.subcategoryId = in.readString();
        this.rating = in.readString();
        this.groupImage = in.readString();
        this.costRange = in.readString();
        this.membersCount = in.readString();
        this.joinMember = (Integer) in.readValue(Integer.class.getClassLoader());
        this.description = in.readString();
        this.joinStatus = in.readString();
        this.imageList = in.createTypedArrayList(ListResponse.CREATOR);
        this.categoryList = in.createTypedArrayList(ListResponse.CREATOR);
        this.bannerList = in.createTypedArrayList(ListResponse.CREATOR);
        this.advertisementList = in.createTypedArrayList(ListResponse.CREATOR);
        this.memberList = in.createTypedArrayList(ListResponse.CREATOR);
        this.groupDetails = in.readParcelable(ListResponse.class.getClassLoader());
        this.offerfortwo = in.readString();
        this.offerforx = in.readString();
        this.is_approved = in.readInt();
        this.quantity = in.readString();
    }

    public static final Creator<ListResponse> CREATOR = new Creator<ListResponse>() {
        @Override
        public ListResponse createFromParcel(Parcel source) {
            return new ListResponse(source);
        }

        @Override
        public ListResponse[] newArray(int size) {
            return new ListResponse[size];
        }
    };
}
