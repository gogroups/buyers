package com.gogroup.app.gogroupapp.User;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.gogroup.app.gogroupapp.Responses.ListResponse;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

public class JoinedGroupDetails extends BaseActivity {


    public static final String CATEGORY_ID = "categoryId";
    public static final String GROUP_ID = "groupId";

    String postCategoryId, postGroupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_joined_group_detail);
        ButterKnife.bind(this);
        setIcons();
        postCategoryId = getIntent().getStringExtra(CATEGORY_ID);
        postGroupId = getIntent().getStringExtra(GROUP_ID);
        layoutParent.setVisibility(View.GONE);
        imgOption.setVisibility(View.GONE);
//        member.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(JoinedGroupDetails.this, MemberProfile.class);
//                startActivity(i);
//            }
//        });
//        getGroupDetailApi();
        apiGetGroupDetails();
    }

//    private void getGroupDetailApi() {
//        if (Utils.isInterNetConnected(JoinedGroupDetails.this)) {
//            showProgressbar();
//            RestClient.get().getGroupDetails(UserPreferences.getInstance().getToken(), postCategoryId, postGroupId).
//                    enqueue(new retrofit2.Callback<GroupDetailResponse>() {
//                        @Override
//                        public void onResponse(Call<GroupDetailResponse> call, Response<GroupDetailResponse> response) {
//                            if (response.body() != null) {
//                                if (response.body().isStatus()) {
//                                    setValues(response.body().getData());
//                                } else {
//                                    Utils.showShortToast(JoinedGroupDetails.this, response.body().getMessage());
//                                }
//                            } else {
//                                Utils.showServerError(JoinedGroupDetails.this);
//                            }
//                            hideProgressBar();
//                        }
//
//                        @Override
//                        public void onFailure(final Call<GroupDetailResponse> call, Throwable t) {
//                            hideProgressBar();
//                            Utils.showShortToast(JoinedGroupDetails.this, t.getMessage());
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

        ListResponse item = data.getGroupDetails();
        layoutJoined.setVisibility(item.getJoinStatus()!=null&&item.getJoinStatus().equals("1")?View.VISIBLE:View.GONE);
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

        Utils.picasso(getApplicationContext(),item.getProfileImage(),imgProfile);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerMembers.setLayoutManager(layoutManager);
        recyclerMembers.setAdapter(new AdapterMember(JoinedGroupDetails.this, data.getMemberList(),true,postGroupId));

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerAdvertisement.setLayoutManager(layoutManager2);
        recyclerAdvertisement.setAdapter(new AdapterShowPhoto(JoinedGroupDetails.this, data.getAdvertisementList(),true));
        tvTopOffer.setText("Top " + data.getAdvertisementList().size() + " Offers");
        tvTopOffer.setVisibility(data.getAdvertisementList().size() == 0 ? View.GONE : View.VISIBLE);
        imgOption.setVisibility(item.getCreatedBy().equalsIgnoreCase(UserPreferences.getInstance().getUserId())?View.VISIBLE:View.GONE);
        layoutParent.setVisibility(View.VISIBLE);
    }
    @OnClick(R.id.imgOption)
    void menu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.group_detail_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.editGroup:

                        Intent i = new Intent(JoinedGroupDetails.this, CreateGroup.class);
                        i.putExtra(CreateGroup.EDIT,true);
                        i.putExtra(CreateGroup.GROUP_ID,postGroupId);
                        startActivityForResult(i,BaseActivity.REFRESH_ACTIVITY);
                        break;
                    default:
                        break;

                }
                return true;
            }
        });

        popup.show();
    }

    @BindView(R.id.imgOption)
    ImageView imgOption;
    @BindView(R.id.btnBack)
    Button btnBack;
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
    @BindView(R.id.layoutJoined)
    LinearLayout layoutJoined;

    @OnClick(R.id.btnBack)
    void back() {

        finish();
    }

    private void setIcons() {
        btnBack.setText(R.string.icon_ionicon_var_chevron_left);
        btnBack.setTypeface(FontManager.getTypeFaceFromFontName(getApplicationContext(), FontManager.IONICONFONT));
        btnBack.setTextSize(20);
        btnBack.setTextColor(ContextCompat.getColor(this, R.color.white_color));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case BaseActivity.REFRESH_ACTIVITY:
                apiGetGroupDetails();
                break;
            default:
                break;
        }
    }
}
