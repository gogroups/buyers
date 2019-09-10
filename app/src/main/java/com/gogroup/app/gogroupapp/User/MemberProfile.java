package com.gogroup.app.gogroupapp.User;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.Adapters.ViewPagerAdapter;
import com.gogroup.app.gogroupapp.CallBackListeners.CallBackReport;
import com.gogroup.app.gogroupapp.Fragments.MemberGroupsFragment;
import com.gogroup.app.gogroupapp.Fragments.ReportDialog;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.FontManager;
import com.gogroup.app.gogroupapp.HelperClasses.RestClient;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.GroupListResponse;
import com.gogroup.app.gogroupapp.Responses.MemberDetailResponse;
import com.gogroup.app.gogroupapp.Responses.PostResponse;
import com.gogroup.app.gogroupapp.Responses.UserResponse;
import com.gogroup.app.gogroupapp.Seller.SellerAddDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

public class MemberProfile extends BaseActivity implements CallBackReport {
    public static final String MEMBER_ID = "memberId";
    public static final String GROUP_ID = "groupId";
    Dialog customDialog;

    int selectedPage;
    String postMemberId, posGroupId;
    public List<GroupListResponse> createdList = new ArrayList<>();
    public List<GroupListResponse> joinedList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_joined_group_member);
        ButterKnife.bind(this);
        posGroupId = getIntent().getStringExtra(GROUP_ID);
        postMemberId = getIntent().getStringExtra(MEMBER_ID);
        setIcons();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(MemberProfile.this, MemberChat.class);
//                startActivity(i);
            /*    Intent intent = new Intent(MemberProfile.this, CustomConversationActivity.class);
                intent.putExtra(ConversationUIService.USER_ID, postMemberId);*/
//                startActivity(intent);

            }
        });
        customDialog = new Dialog(MemberProfile.this);

        layoutParent.setVisibility(View.GONE);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        apiGetMemberDetail();
    }

    private void apiGetMemberDetail() {
        if (Utils.isInterNetConnected(MemberProfile.this)) {
            showProgressbar();
            RestClient.get().getMemberDetail(UserPreferences.getInstance().getToken(), postMemberId).
                    enqueue(new retrofit2.Callback<MemberDetailResponse>() {
                        @Override
                        public void onResponse(Call<MemberDetailResponse> call, Response<MemberDetailResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {

                                    setValues(response.body().getData());

                                } else {
                                    Utils.showShortToast(getApplicationContext(), response.body().getMessage());
                                }

                            } else {
                                Utils.showServerError(getApplicationContext());
                            }
                            hideProgressBar();

                        }

                        @Override
                        public void onFailure(final Call<MemberDetailResponse> call, Throwable t) {
                            hideProgressBar();
                            if (t.getMessage().toLowerCase().contains("failed to connect")) {
//                                showAlert();
//                                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        call.request();
//
//                                        alertDialog.dismiss();
//                                    }
//                                });
                            }
                            Utils.showShortToast(getApplicationContext(), t.getMessage());
                        }
                    });

        }
    }

    private void setValues(MemberDetailResponse data) {
        createdList = data.getCreatedGroups();
        joinedList = data.getJoinedGroups();

        UserResponse user = data.getMemberDetails();
        tvName.setText(user.getName() != null ? user.getName() : "N/A");
        tvLocation.setText(user.getLocation() != null ? user.getLocation() : "N/A");
        tvEmail.setText(user.getEmail() != null ? user.getEmail() : "N/A");
        tvNumber.setText(user.getContactNumber() != null ? "+" + user.getContactNumber() : "N/A");

        Utils.picasso(getApplicationContext(),user.getProfileImage(),imgProfile);


        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

//        setUpTabLayout();
        setUpTabs();

        layoutParent.setVisibility(View.VISIBLE);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(new MemberCreatedGroupListingFragment(), "Created Groups");
//        adapter.addFragment(new MemberJoinedGroupListingFragment(), "Joined Groups");
        adapter.addFragment(MemberGroupsFragment.newInstance(0), "Created Groups");
        adapter.addFragment(MemberGroupsFragment.newInstance(1), "Joined Groups");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(selectedPage);
    }


    private void setIcons() {
        btnBack.setText(R.string.icon_ionicon_var_chevron_left);
        btnBack.setTypeface(FontManager.getTypeFaceFromFontName(getApplicationContext(), FontManager.IONICONFONT));
        btnBack.setTextSize(20);
        btnBack.setTextColor(ContextCompat.getColor(this, R.color.white_color));
    }



    private void setUpTabs() {
        LinearLayout custom_tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.tab_custom, null);
        TextView text1 = custom_tab.findViewById(R.id.text_number);
        text1.setText("" + createdList.size());
        TextView text2 = custom_tab.findViewById(R.id.tab_name);
        text2.setText("Created Groups");
        tabLayout.getTabAt(0).setCustomView(custom_tab);
        LinearLayout custom_tab2 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.tab_custom, null);
        TextView text3 = custom_tab2.findViewById(R.id.text_number);
        text3.setText("" + joinedList.size());
        TextView text4 = custom_tab2.findViewById(R.id.tab_name);
        text4.setText("Joined Groups");
        tabLayout.getTabAt(1).setCustomView(custom_tab2);
    }
    @OnClick(R.id.imgOption)
    void menu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.report_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.report:
                        ReportDialog reportDialog =  ReportDialog.instance(MemberProfile.this);
                        reportDialog.show(getSupportFragmentManager(), "Report");
                        break;
                    default:
                        break;

                }
                return true;
            }
        });

        popup.show();
    }

    private void apiReport(String comment) {
        if (Utils.isInterNetConnected(MemberProfile.this)) {
            showProgressbar();

            RestClient.get().report(UserPreferences.getInstance().getToken(), comment, postMemberId, "member").
                    enqueue(new retrofit2.Callback<PostResponse>() {
                        @Override
                        public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {

                                    customDialog.dismiss();


                                }

                                Utils.showShortToast(MemberProfile.this, response.body().getMessage());

                            } else {
                                Utils.showServerError(MemberProfile.this);
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
//
//                                        alertDialog.dismiss();
//                                    }
//                                });
                            }
                            Utils.showShortToast(MemberProfile.this, t.getMessage());
                        }
                    });
        }
    }

    @OnClick(R.id.btnBack)
    void back() {
        finish();
    }

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.btnBack)
    Button btnBack;
    @BindView(R.id.imgProfile)
    CircleImageView imgProfile;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvLocation)
    TextView tvLocation;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.layoutParent)
    RelativeLayout layoutParent;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode) {
            apiGetMemberDetail();
        }
    }

    @Override
    public void onClickReportSubmit(String report) {
        apiReport(report);
    }
}
