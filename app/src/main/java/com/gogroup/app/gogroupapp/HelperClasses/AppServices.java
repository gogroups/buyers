package com.gogroup.app.gogroupapp.HelperClasses;

import com.gogroup.app.gogroupapp.Responses.CategoryResponse;
import com.gogroup.app.gogroupapp.Responses.CommonResponse;
import com.gogroup.app.gogroupapp.Responses.DetailResponse;
import com.gogroup.app.gogroupapp.Responses.GoogleAddressResponse;
import com.gogroup.app.gogroupapp.Responses.GooglePlaceApiResponse;
import com.gogroup.app.gogroupapp.Responses.GroupDetailResponse;
import com.gogroup.app.gogroupapp.Responses.GroupResponse;
import com.gogroup.app.gogroupapp.Responses.GroupListResponse;
import com.gogroup.app.gogroupapp.Responses.MemberDetailResponse;
import com.gogroup.app.gogroupapp.Responses.MemberResponse;
import com.gogroup.app.gogroupapp.Responses.MesiboDetailResponse;
import com.gogroup.app.gogroupapp.Responses.NotificationResponse;
import com.gogroup.app.gogroupapp.Responses.PostResponse;
import com.gogroup.app.gogroupapp.Responses.RegisterResponse;
import com.gogroup.app.gogroupapp.Responses.SellerAdvertisementResponse;
import com.gogroup.app.gogroupapp.Responses.UserResponse;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by zabius on 9/8/17.
 */

public interface AppServices {


    @GET("http://maps.googleapis.com/maps/api/geocode/json?")
    Call<GoogleAddressResponse> getGoogleAddress(@Query("address") String addressId);

    @GET("https://maps.googleapis.com/maps/api/place/autocomplete/json?")
    Call<GooglePlaceApiResponse> checkGooglePlaceApi(@Query("key") String addkeyressId,
                                                     @Query("input") String input);

    @Multipart
    @POST("register")
    Call<RegisterResponse> registerSeller(@Part MultipartBody.Part multiImages,
                                          @PartMap Map<String, RequestBody> field);

    @FormUrlEncoded
    @POST("verify_otp")
    Call<PostResponse> verifyOtp(@Field("otp_text") String otp_text,
                                 @Field("token") String token);

    @FormUrlEncoded
    @POST("login")
    Call<UserResponse> login(@Field("username") String username,
                             @Field("password") String password,
                             @Field("device_token") String device_token,
                             @Field("userType") String userType);

    @FormUrlEncoded
    @POST("updateotp")
    Call<UserResponse> updateOtp(@Field("token") String token,
                                 @Field("email") String username,
                                 @Field("id") String password);

    @GET("getAllCategories")
    Call<DetailResponse> getAllCategories(@Header("token") String token);

    @FormUrlEncoded
    @POST("getCategoryWisedGroups")
    Call<GroupListResponse> getGroupList(@Header("token") String token,
                                         @FieldMap Map<String, String> field);

    @GET("getFavouriteGroups")
    Call<GroupListResponse> getFavouriteGroups(@Header("token") String token);


    @GET("getCategories")
    Call<CategoryResponse> getCategory1(@Header("token") String token);

    @FormUrlEncoded
    @POST("getAllSellerAdvertisement")
    Call<SellerAdvertisementResponse> getAllSellerAdvertisement(@Header("token") String token,
                                                                @FieldMap Map<String, String> field);

    @FormUrlEncoded
    @POST("getAllAdvertisements")
    Call<SellerAdvertisementResponse> getAllUserAdvertisement(@Header("token") String token,
                                                              @FieldMap Map<String, String> field);

    @POST("getAllFavAdvertisements")
    Call<SellerAdvertisementResponse> getFavoriteAds(@Header("token") String token);

    @FormUrlEncoded
    @POST("getAdvertisement")
    Call<SellerAdvertisementResponse> getAdvertisement(@Header("token") String token,
                                                       @Field("advertisement_id") String field);

    @FormUrlEncoded
    @POST("getSubcategories")
    Call<CategoryResponse> getCategory2(@Header("token") String token,
                                        @Field("category_id") String category_id);

    @FormUrlEncoded
    @POST("getSubcategories2")
    Call<CategoryResponse> getCategory3(@Header("token") String token,
                                        @Field("subcategory_id") String subcategory_id2);

    @FormUrlEncoded
    @POST("getSubcategories3")
    Call<CategoryResponse> getCategory4(@Header("token") String token,
                                        @Field("subcategory_id2") String subcategory_id3);

    @FormUrlEncoded
    @POST("getSubcategories4")
    Call<CategoryResponse> getCategory5(@Header("token") String token,
                                        @Field("subcategory_id3") String category_id);

    @FormUrlEncoded
    @POST("getSubcategories5")
    Call<CategoryResponse> getCategory6(@Header("token") String token,
                                        @Field("subcategory_id4") String category_id);

    @Multipart
    @POST("createAdvertisement")
    Call<PostResponse> createAdvertisement(@Header("token") String token,
                                           @Part List<MultipartBody.Part> multiImages,
                                           @PartMap Map<String, RequestBody> field);

    @FormUrlEncoded
    @POST("editAdvertisement")
    Call<PostResponse> editAdvertisement(@Header("token") String token,
                                         @FieldMap Map<String, String> fieldMap);

    @Multipart
    @POST("createGroup")
    Call<PostResponse> createGroup(@Header("token") String token,
                                   @Part MultipartBody.Part multiImages,
                                   @PartMap Map<String, RequestBody> field);

    @FormUrlEncoded
    @POST("editGroup")
    Call<PostResponse> editGroup(@Header("token") String token,
                                 @FieldMap Map<String, String> field);

    @Multipart
    @POST("editProfile")
    Call<UserResponse> editProfile(@Header("token") String token,
                                   @Part List<MultipartBody.Part> multiImages,
                                   @PartMap Map<String, RequestBody> field);

    @GET("profile")
    Call<UserResponse> getProfile(@Header("token") String token);

    @FormUrlEncoded
    @POST("getGroupDetails")
    Call<GroupDetailResponse> getGroupDetails(@Header("token") String token,
                                              @Field("category_id") String category_id,
                                              @Field("group_id") String group_id);
    @FormUrlEncoded
    @POST("getGroupDetails")
    Call<CommonResponse> getGroupDetailsNew(@FieldMap Map<String, String> field);


    @FormUrlEncoded
    @POST("joinGroup")
    Call<PostResponse> joinGroup(@Header("token") String token,
                                 @Field("group_id") String group_id);

    @FormUrlEncoded
    @POST("exitGroup")
    Call<PostResponse> exitGroup(@Header("token") String token,
                                 @Field("group_id") String group_id);

    @FormUrlEncoded
    @POST("report")
    Call<PostResponse> report(@Header("token") String token,
                              @Field("comments") String comments,
                              @Field("reportTo") String reportTo,
                              @Field("report_type") String report_type);

    @FormUrlEncoded
    @POST("forgotPassword")
    Call<PostResponse> forgotPassword(
            @Field("username") String emailId);

    @FormUrlEncoded
    @POST("changeSetting")
    Call<PostResponse> updateNotification(@Header("token") String token,
                                          @Field("notify_status") String notify_status);

    @FormUrlEncoded
    @POST("changePassword")
    Call<PostResponse> changePassword(@Header("token") String token,
                                      @Field("old_password") String old_password,
                                      @Field("new_password") String new_password,
                                      @Field("confirm_new_password") String confirm_new_password);

    @GET("getCreateGroups")
    Call<GroupListResponse> getCreatedGroups(@Header("token") String token);

    @GET("getJoinedGroups")
    Call<GroupListResponse> getJoinedGroups(@Header("token") String token);


    @FormUrlEncoded
    @POST("groupMemberDetail")
    Call<MemberDetailResponse> getMemberDetail(@Header("token") String token,
                                               @Field("member_id") String member_id);

    @FormUrlEncoded
    @POST("updateChannelKey")
    Call<PostResponse> updateChannelKey(@Header("token") String token,
                                        @Field("group_id") String group_id,
                                        @Field("channelKey") Integer channelKey);

    @GET("getUserList")
    Call<MemberResponse> getUserList(@Header("token") String token);

    @FormUrlEncoded
    @POST("getGroupsByID")
    Call<GroupDetailResponse> getGroupDetailForEdit(@Header("token") String token,
                                                    @Field("group_id") String group_id);

    @FormUrlEncoded
    @POST("getSellerAdvertisement")
    Call<SellerAdvertisementResponse> getSellerAdvertisement(@Header("token") String token,
                                                             @Field("advertisement_id") String advertisement_id);

    @FormUrlEncoded
    @POST("MarkFavouriteGroup")
    Call<PostResponse> markFavouriteGroup(@Header("token") String token,
                                          @Field("status") int status,
                                          @Field("group_id") String group_id);

    @FormUrlEncoded
    @POST("MarkFavouriteAds")
    Call<PostResponse> markFavouriteAds(@Header("token") String token,
                                        @Field("status") int status,
                                        @Field("advertisement_id") String group_id);

    @FormUrlEncoded
    @POST("notification")
    Call<NotificationResponse> getNotifications(@Header("token") String token,
                                                @Field("user_type") String user_type);

    @FormUrlEncoded
    @POST("readNotification")
    Call<PostResponse> readNotification(@Header("token") String token,
                                        @Field("user_type") String user_type);

    @FormUrlEncoded
    @POST("editContact")
    Call<PostResponse> editContact(@Header("token") String token,
                                   @Field("user_id") String user_id,
                                   @Field("email") String email,
                                   @Field("contact_number") String contact_number);

    @FormUrlEncoded
    @POST("send_mail")
    Call<PostResponse> sendEmail(@Header("token") String token, @Field("email") String email, @Field("advertisement_id") String advertisement_id);


    @FormUrlEncoded
    @POST("getGroupByID")
    Call<GroupResponse> getTotalGroup(@Header("token") String token, @Field("category_id") String id);

    @FormUrlEncoded
    @POST("generateCouponCode")
    Call<PostResponse> generateCouponCode(@FieldMap Map<String, String> field);

    @FormUrlEncoded
    @POST("getPurchaseList")
    Call<DetailResponse> getPurchaseList(@FieldMap Map<String, String> field);

    @FormUrlEncoded
    @POST("purchaseAdvertisement")
    Call<PostResponse> purchaseAdd(@FieldMap Map<String, String> field);

    @FormUrlEncoded
    @POST("getPendingUser")
    Call<DetailResponse> getPendingUser(@FieldMap Map<String, String> field);

    @FormUrlEncoded
    @POST("getPurchasedUser")
    Call<DetailResponse> getPurchasedUser(@FieldMap Map<String, String> field);

    @FormUrlEncoded
    @POST("getSellerFeed")
    Call<DetailResponse> getSellerFeed(@FieldMap Map<String, String> field);

    @FormUrlEncoded
    @POST("validateOffer")
    Call<PostResponse> validateOffer(@FieldMap Map<String, String> field);

    @FormUrlEncoded
    @POST("insert_like")
    Call<PostResponse> setLike(@FieldMap Map<String, String> field);

    @FormUrlEncoded
    @POST("insert_rating")
    Call<PostResponse> insertRating(@FieldMap Map<String, String> field);

    @POST("https://mesibo.com/api/api.php")
    Call<MesiboDetailResponse> mesiboUserAdd(@QueryMap Map<String, String> field);
}
