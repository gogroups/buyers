package com.gogroup.app.gogroupapp.User;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.gogroup.app.gogroupapp.Adapters.AdapterMember;
import com.gogroup.app.gogroupapp.Adapters.AdapterShowPhoto;
import com.gogroup.app.gogroupapp.CallBackListeners.CallBackApi;
import com.gogroup.app.gogroupapp.HelperClasses.ApiCalls;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.FontManager;
import com.gogroup.app.gogroupapp.HelperClasses.RestClient;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.CommonResponse;
import com.gogroup.app.gogroupapp.Responses.GroupDetailResponse;
import com.gogroup.app.gogroupapp.Responses.GroupListResponse;
import com.gogroup.app.gogroupapp.Responses.ImageResponse;
import com.gogroup.app.gogroupapp.Responses.ListResponse;
import com.gogroup.app.gogroupapp.Responses.PostResponse;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

public class GroupDetails extends BaseActivity {

    public static final String CATEGORY_ID = "categoryId";
    public static final String GROUP_ID = "groupId";
    boolean isDeepLinking;
    String postCategoryId, postGroupId;
    GroupListResponse apiGroupDataOld;
    ListResponse apiGroupData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_category_group);
        ButterKnife.bind(this);
        setIcons();
        postCategoryId = getIntent().getStringExtra(CATEGORY_ID);
        postGroupId = getIntent().getStringExtra(GROUP_ID);
        layoutParent.setVisibility(View.GONE);

        apiGetGroupDetails();

        try {
            isDeepLinking = getIntent().getBooleanExtra(DEEPLINK_DATA, false);
        } catch (NullPointerException e) {
            isDeepLinking = false;
        }
    }

//    private void getGroupDetailApi() {
//        if (Utils.isInterNetConnected(GroupDetails.this)) {
//            showProgressbar();
//            RestClient.get().getGroupDetails(UserPreferences.getInstance().getToken(), postCategoryId, postGroupId).
//                    enqueue(new retrofit2.Callback<GroupDetailResponse>() {
//                        @Override
//                        public void onResponse(Call<GroupDetailResponse> call, Response<GroupDetailResponse> response) {
//                            if (response.body() != null) {
//                                if (response.body().isStatus()) {
//
//                                    setValues(response.body().getData());
//
//                                } else {
//                                    Utils.showShortToast(GroupDetails.this, response.body().getMessage());
//                                }
//
//                            } else {
//                                Utils.showServerError(GroupDetails.this);
//                            }
//                            hideProgressBar();
//
//                        }
//
//                        @Override
//                        public void onFailure(final Call<GroupDetailResponse> call, Throwable t) {
//                            hideProgressBar();
//                            Utils.showShortToast(GroupDetails.this, t.getMessage());
//                        }
//                    });
//
//        }
//
//    }
    private void apiGetGroupDetails() {
        showProgressbar();
        Map<String, String> postFields = new HashMap<>();
        postFields.put("category_id", postCategoryId);
        postFields.put("group_id", postGroupId);
        new ApiCalls(this).apiGetGroupDetails(postFields, new CallBackApi() {
            @Override
            public void onSuccess(Response response) {
                CommonResponse body = (CommonResponse) response.body();

                setValues(body.getDetail());

                hideProgressBar();
            }

            @Override
            public void onFailure(String message) {
                hideProgressBar();
            }

            @Override
            public void onRetryYes() {
                apiGetGroupDetails();
            }

            @Override
            public void onRetryNo() {
            }
        });

    }
    private void setValues(ListResponse data) {
        apiGroupData = data;
        ListResponse item = data.getGroupDetails();
        tvStartDate.setText(item.getStartDate() != null ? item.getStartDate() : "");
        tvEndDate.setText(item.getEndDate() != null ? item.getEndDate() : "");

        String joined = item.getJoinMember() != null && !TextUtils.isEmpty(""+item.getJoinMember()) ? ""+item.getJoinMember() : "0";
        String total = item.getMembersCount() != null && !TextUtils.isEmpty(item.getMembersCount()) ? item.getMembersCount() : "0";

        tvMembers.setText(joined + "/" + total + " Member(s)");
        tvCategory.setText(item.getCategory1() != null ? item.getCategory1() : "");
        tvSubCategory.setText(item.getCategory2() != null ? item.getCategory2() : "");
        tvGroupName.setText(item.getGroupName() != null ? item.getGroupName() : "");
        tvCreatedBy.setText(item.getName() != null ? item.getName() : "");
        tvLocation.setText(item.getGroupLocation() != null ? item.getGroupLocation() : "");
        tvDescription.setText(item.getDescription() != null ? item.getDescription() : "N/A");
        tvCostRange.setText(item.getCostRange() != null ? item.getCostRange() : "");

        Utils.picasso(getApplicationContext(),item.getProfileImage(),imgProfile);

        if (item.getGroupImage() != null && !item.getGroupImage().trim().equals("")) {

            Picasso.with(this).load(item.getGroupImage()).into(new Target() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    imgGroup.setBackground(new BitmapDrawable(bitmap));

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerMembers.setLayoutManager(layoutManager);
        recyclerMembers.setAdapter(new AdapterMember(GroupDetails.this, data.getMemberList(), false, postGroupId));

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerAdvertisement.setLayoutManager(layoutManager2);
        List<ListResponse> list = new ArrayList<>(data.getAdvertisementList().size() > 20 ?
                data.getAdvertisementList().subList(0, 20) : data.getAdvertisementList());
        recyclerAdvertisement.setAdapter(new AdapterShowPhoto(GroupDetails.this, list, false));
        tvTopOffer.setText("Top " + list.size() + " Offers");
        tvTopOffer.setVisibility(list.size() == 0 ? View.GONE : View.VISIBLE);

        layoutParent.setVisibility(View.VISIBLE);

        if (item.getJoinMember() != null && item.getMembersCount() != null) {
            btnJoin.setVisibility((""+item.getJoinMember()).trim().equals(item.getMembersCount().trim()) ? View.GONE : View.VISIBLE);
        }

    }


    @OnClick(R.id.vendor_home_filter_left_arrow)
    void back() {
        if (isDeepLinking) {
            Intent intent = new Intent(this, UserDashboardNew.class);
            startActivity(intent);
            ActivityCompat.finishAffinity(this);
        } else {
            finish();
        }
    }

    @OnClick(R.id.join_group)
    void join() {
        if (Utils.isInterNetConnected(GroupDetails.this)) {
            showProgressbar();
            RestClient.get().joinGroup(UserPreferences.getInstance().getToken(), postGroupId).
                    enqueue(new retrofit2.Callback<PostResponse>() {
                        @Override
                        public void onResponse(Call<PostResponse> call, final Response<PostResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
//                                    Intent i = new Intent(GroupDetails.this, JoinedGroupChat.class);
//                                    i.putExtra(JoinedGroupChat.CATEGORY_ID, postCategoryId);
//                                    i.putExtra(JoinedGroupChat.GROUP_ID, postGroupId);
//                                    startActivityForResult(i, BaseActivity.REFRESH_ACTIVITY);

                                    Intent intent = new Intent(GroupDetails.this, JoinedGroupDetail.class);
                                    intent.putExtra("category_id", postCategoryId);
                                    intent.putExtra("group_id", postGroupId);
                                    startActivityForResult(intent, BaseActivity.REFRESH_ACTIVITY);
                                    Utils.showShortToast(GroupDetails.this, response.body().getMessage());

//                                    ApplozicChannelAddMemberTask.ChannelAddMemberListener channelAddMemberListener = new ApplozicChannelAddMemberTask.ChannelAddMemberListener() {
//                                        @Override
//                                        public void onSuccess(String appRes, Context context) {
//                                            //Response will be "success" if user is added successfully
//                                            Intent intent = new Intent(GroupDetails.this, CustomConversationActivity.class);
//                                            intent.putExtra("category_id", postCategoryId);
//                                            intent.putExtra("group_id", postGroupId);
//                                            intent.putExtra(ConversationUIService.GROUP_ID, apiGroupData.getChannelKey());
//                                            intent.putExtra(ConversationUIService.GROUP_NAME, apiGroupData.getName());
//                                            startActivityForResult(intent, BaseActivity.REFRESH_ACTIVITY);
//                                            Utils.showShortToast(GroupDetails.this, response.body().getMessage());
//
//                                        }
//
//                                        @Override
//                                        public void onFailure(String response, Exception e, Context context) {
//                                            Toast.makeText(context, getString(R.string.failedTojoin), Toast.LENGTH_LONG).show();
//                                        }
//                                    };
//                                    ApplozicChannelAddMemberTask applozicChannelAddMemberTask = new ApplozicChannelAddMemberTask(GroupDetails.this, apiGroupData.getChannelKey(), UserPreferences.getInstance().getUserId(), channelAddMemberListener);//pass channel key and userId whom you want to add to channel
//                                    applozicChannelAddMemberTask.execute((Void) null);
                                } else {
                                    Utils.showShortToast(GroupDetails.this, response.body().getMessage());
                                }
                            } else {
                                Utils.showServerError(GroupDetails.this);
                            }
                            hideProgressBar();
                        }

                        @Override
                        public void onFailure(final Call<PostResponse> call, Throwable t) {
                            hideProgressBar();
                            if (t.getMessage().toLowerCase().contains("failed to connect")) {
//                                showAlert();
//                                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        call.request();
//                                        alertDialog.dismiss();
//                                    }
//                                });
                            }
                            Utils.showShortToast(GroupDetails.this, t.getMessage());
                        }
                    });
        }
    }


    @BindView(R.id.vendor_home_filter_left_arrow)
    Button mButtonLeftArrow;
    @BindView(R.id.tvStartDate)
    TextView tvStartDate;
    @BindView(R.id.tvEndDate)
    TextView tvEndDate;
    @BindView(R.id.tvCostRange)
    TextView tvCostRange;
    @BindView(R.id.tvMembers)
    TextView tvMembers;
    @BindView(R.id.tvCategory)
    TextView tvCategory;
    @BindView(R.id.tvSubCategory)
    TextView tvSubCategory;
    @BindView(R.id.tvGroupName)
    TextView tvGroupName;
    @BindView(R.id.tvCreatedBy)
    TextView tvCreatedBy;
    @BindView(R.id.tvLocation)
    TextView tvLocation;
    @BindView(R.id.tvDescription)
    ExpandableTextView tvDescription;
    @BindView(R.id.recyclerMembers)
    RecyclerView recyclerMembers;
    @BindView(R.id.recyclerAdvertisement)
    RecyclerView recyclerAdvertisement;
    @BindView(R.id.imgProfile)
    CircleImageView imgProfile;
    @BindView(R.id.imgGroup)
    RelativeLayout imgGroup;
    @BindView(R.id.tvTopOffer)
    TextView tvTopOffer;
    @BindView(R.id.layoutParent)
    LinearLayout layoutParent;
    @BindView(R.id.join_group)
    Button btnJoin;

    private void setIcons() {
        mButtonLeftArrow.setText(R.string.icon_ionicon_var_chevron_left);
        mButtonLeftArrow.setTypeface(FontManager.getTypeFaceFromFontName(getApplicationContext(), FontManager.IONICONFONT));
        mButtonLeftArrow.setTextSize(20);
        mButtonLeftArrow.setTextColor(ContextCompat.getColor(this, R.color.white_color));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BaseActivity.REFRESH_ACTIVITY) {
            Intent intent = new Intent();
            setResult(BaseActivity.REFRESH_ACTIVITY, intent);
            finish();

        }
    }
}
