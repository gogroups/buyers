package com.gogroup.app.gogroupapp.User;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.gogroup.app.gogroupapp.Adapters.AdapterCategory;
import com.gogroup.app.gogroupapp.Adapters.AdapterCustomUserHomeNavigationViewListView;
import com.gogroup.app.gogroupapp.Fragments.NotificationDialog;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.FontManager;
import com.gogroup.app.gogroupapp.HelperClasses.GoGroup;
import com.gogroup.app.gogroupapp.HelperClasses.RestClient;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.OptionLoginScreen;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.CategoryResponse;
import com.gogroup.app.gogroupapp.Responses.GroupListResponse;
import com.gogroup.app.gogroupapp.Responses.NotificationResponse;
import com.gogroup.app.gogroupapp.Responses.PostResponse;
import com.gogroup.app.gogroupapp.Seller.SellerDashboard;
import com.gogroup.app.gogroupapp.Seller.UserProfile;
import com.gogroup.app.gogroupapp.SplashActivity;
import com.squareup.picasso.Picasso;

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

public class UserDashboard extends BaseActivity {

    ArrayList<String> mArraysMenus = new ArrayList<>();
    AdapterCustomUserHomeNavigationViewListView adapterCustomUserNavigationView;
    AdapterCategory mAdapter;
    GridLayoutManager gridLayoutManager;
    List<CategoryResponse> categoryList = new ArrayList<>();
    private static final int GRID_COLUMN_COUNT = 2;
    public static final String TAKE_ORDER = "takeOrder";
    NotificationResponse notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_user_dashboard);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0); // this disable the animation
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                super.onDrawerSlide(drawerView, 0); // this disables the arrow @ completed state
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, findViewById(R.id.layoutFilter));
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            LinearLayout header_linear = navigationView.findViewById(R.id.nav_header_linear);
            header_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(UserDashboard.this, UserProfile.class);
                    startActivityForResult(i, BaseActivity.REFRESH_PROFILE);
                }
            });

        }
        setNavigationView();

        //navigationView.setNavigationItemSelectedListener(this);

        setUserProfile();


        gridLayoutManager = new GridLayoutManager(UserDashboard.this, GRID_COLUMN_COUNT);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AdapterCategory();
        recyclerView.setAdapter(mAdapter);
        tvEmpty.setVisibility(View.GONE);
        layoutNotify.setVisibility(View.GONE);

//        mAdapter.setOnLoadMoreListener(SellerDashboard.this);
        apiGetNotifications();
//        getAllCategory();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                apiGetNotifications();
//                getAllCategory();
            }
        });

        openPage(GoGroup.getInstance().notificationMap);

    }

    private void openPage(Map<String, String> notificationMap) {


        if (notificationMap == null) {
            return;
        }

        GroupListResponse body = new GroupListResponse();

        body.setTitle(notificationMap.get("title"));
        body.setDescription(notificationMap.get("description"));
        body.setUserId(notificationMap.get("user_id"));
        body.setType(notificationMap.get("type"));


        try {
            body.setGroupId(notificationMap.get("group_id"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            body.setCategoryId(notificationMap.get("category_id"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Class detailClass = null;

        final Bundle bundle = new Bundle();

        try {
            switch ("" + body.getType()) {

                case "user":
                    if (body.getCategoryId() != null && body.getGroupId() != null) {
                        bundle.putString(JoinedGroupDetails.CATEGORY_ID, body.getCategoryId());
                        bundle.putString(JoinedGroupDetails.GROUP_ID, body.getGroupId());
                        detailClass = JoinedGroupDetails.class;
                    } else {
                        detailClass = UserDashboard.class;
                    }
                    break;


            }


            Intent intent = new Intent(this, detailClass);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            GoGroup.getInstance().notificationMap.clear();

        } catch (Exception e) {
        }


    }

    private void setUserProfile()
    {

        UserPreferences user = UserPreferences.getInstance();
        tvName.setText(user.getUserName());
        tvCity.setText(user.getLocation());
        Utils.picasso(getApplicationContext(), user.getProfileImage(), imgUser);
    }

//    private void getAllCategory() {
//        if (Utils.isInterNetConnected(UserDashboard.this)) {
//            showProgressbar();
//
//
//            Map<String, String> postFields = new HashMap<>();
//
////            postFields.put("start_date", "");
////            postFields.put("end_date", "");
////            postFields.put("category_id", "");
////            postFields.put("subcategory_id", "");
////            postFields.put("member_count", "");
//            RestClient.get().getAllCategories(UserPreferences.getInstance().getToken()).
//                    enqueue(new retrofit2.Callback<CategoryResponse>() {
//                        @Override
//                        public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
//                            if (response.body() != null) {
//                                if (response.body().isStatus()) {
//                                    categoryList = response.body().getData();
//                                    mAdapter.setData(recyclerView, categoryList, UserDashboard.this);
//                                } else {
//                                    Utils.showShortToast(getApplicationContext(), response.body().getMessage());
//                                }
//                                tvEmpty.setVisibility(categoryList.size() > 0 ? View.GONE : View.VISIBLE);
//                            } else {
//                                Utils.showServerError(getApplicationContext());
//                            }
//                            hideProgressBar();
//                            swipeRefreshLayout.setRefreshing(false);
//                        }
//
//                        @Override
//                        public void onFailure(final Call<CategoryResponse> call, Throwable t) {
//                            hideProgressBar();
//                            swipeRefreshLayout.setRefreshing(false);
//                            if (t.getMessage().toLowerCase().contains("failed to connect")) {
////                                showAlert();
////                                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
////                                    @Override
////                                    public void onClick(View view) {
////                                        call.request();
////                                        alertDialog.dismiss();
////                                    }
////                                });
//                            }
//                            Utils.showShortToast(getApplicationContext(), t.getMessage());
//                        }
//                    });
//        }
//    }


    private void setNavigationView()
    {
        mArraysMenus.add(getString(R.string.shortlisted));
        mArraysMenus.add(getString(R.string.myGroups));
        mArraysMenus.add(getString(R.string.myPurchases));
        //mArraysMenus.add(getString(R.string.chat));
        mArraysMenus.add("My Orders");
        mArraysMenus.add(getString(R.string.settings));
        mArraysMenus.add(getString(R.string.contactUs));
//        mArraysMenus.add(getString(R.string.about));
        //  mArraysMenus.add(getString(R.string.share));

        mTextViewIconLogout.setText(R.string.icon_ionicon_var_log_out);
        mTextViewIconLogout.setTypeface(FontManager.getTypeFaceFromFontName(getApplicationContext(), FontManager.IONICONFONT));
        mTextViewIconLogout.setTextSize(20);
        mTextViewIconLogout.setTextColor(ContextCompat.getColor(this, R.color.white_color));

        adapterCustomUserNavigationView = new AdapterCustomUserHomeNavigationViewListView(mArraysMenus, UserDashboard.this);
        mListViewMenus.setAdapter(adapterCustomUserNavigationView);

        mListViewMenus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (position == 0) {
                    Intent i = new Intent(UserDashboard.this, ShortListed.class);
                    startActivity(i);
                    //drawer.closeDrawer(mLinearLayoutDrawer);

                } else if (position == 1) {
                    Intent i = new Intent(UserDashboard.this, MyGroups.class);
                    startActivity(i);
                } else if (position == 2) {
                    Intent i = new Intent(UserDashboard.this, MyPurchases.class);
                    startActivity(i);
                } else if (position == 3) {
                    Intent i = new Intent(UserDashboard.this, TotalCashback.class);
                    startActivity(i);
                }
//                else if (position == 3) {
//                    Intent i = new Intent(UserDashboard.this, AppLogicChat.class);
//                    startActivity(i);
//
//                }

                else if (position == 5) {
                    Intent i = new Intent(UserDashboard.this, SettingsActivity.class);
                    startActivityForResult(i, BaseActivity.REFRESH_PROFILE);

                } else if (position == 6) {
                    Intent i = new Intent(UserDashboard.this, ContactUs.class);
                    startActivity(i);

                }


            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @OnClick(R.id.imgNotification)
    void actionNotification() {
        apiReadNotification();
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        NotificationDialog dialogFragment = NotificationDialog.getInstance(notifications);
        dialogFragment.show(manager, "notifications");
//        startActivity(new Intent(this,TestActivity.class));
    }


    private void apiGetNotifications() {
        if (Utils.isInterNetConnected(this)) {

            // ((BaseActivity) activity).showProgressbar();
            RestClient.get().getNotifications(UserPreferences.getInstance().getToken(), UserPreferences.getInstance().getUserType().toLowerCase())
                    .enqueue(new retrofit2.Callback<NotificationResponse>() {
                        @Override
                        public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {

                                    notifications = response.body();
                                    if (notifications != null && notifications.getCount() != null && notifications.getCount() > 0) {
                                        layoutNotify.setVisibility(View.VISIBLE);
                                        tvNotify.setText(notifications.getCount() > 99 ? "99+" : "" + notifications.getCount());
                                    }
                                } else {
                                    Utils.showShortToast(UserDashboard.this, response.body().getMessage());
                                }
                            } else {
                                Utils.showServerError(UserDashboard.this);
                            }
                        }

                        @Override
                        public void onFailure(final Call<NotificationResponse> call, Throwable t) {
                            Utils.showShortToast(UserDashboard.this, t.getMessage());
                        }
                    });
        }

    }

    private void apiReadNotification() {
        if (Utils.isInterNetConnected(this)) {

            // ((BaseActivity) activity).showProgressbar();
            RestClient.get().readNotification(UserPreferences.getInstance().getToken(), UserPreferences.getInstance().getUserType().toLowerCase())
                    .enqueue(new retrofit2.Callback<PostResponse>() {
                        @Override
                        public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {

                                    if (response.body().isStatus()) {
                                        layoutNotify.setVisibility(View.GONE);
                                        tvNotify.setText("0");
                                    }
                                } else {
                                    Utils.showShortToast(UserDashboard.this, response.body().getMessage());
                                }
                            } else {
                                Utils.showServerError(UserDashboard.this);
                            }
                        }

                        @Override
                        public void onFailure(final Call<PostResponse> call, Throwable t) {
                            Utils.showShortToast(UserDashboard.this, t.getMessage());
                        }
                    });
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case BaseActivity.REFRESH_DASHBOARD:
//                getAllCategory();
                break;
            case BaseActivity.REFRESH_PROFILE:
                setUserProfile();
                break;

            default:
                break;

        }

    }

    @OnClick({R.id.navigation_linear_layout_logout, R.id.navigation_vendor_home_icon_logout_icon, R.id.vendor_home_logout_button})
    void logOut() {
        UserPreferences.getInstance().clearUserDetails();
        Intent goToOptionsScreen = new Intent(UserDashboard.this, OptionLoginScreen.class);
        startActivity(goToOptionsScreen);
        ActivityCompat.finishAffinity(UserDashboard.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        apiGetNotifications();
    }

    @BindView(R.id.navigation_vendor_home_icon_logout_icon)
    TextView mTextViewIconLogout;
    @BindView(R.id.vendor_home_logout_button)
    Button mButtonLogout;
    @BindView(R.id.vendor_home_navigation_listview)
    ListView mListViewMenus;
    @BindView(R.id.drawer_layout)
    public DrawerLayout drawer;
    @BindView(R.id.imgUser)
    CircleImageView imgUser;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvCity)
    TextView tvCity;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.layoutNotify)
    LinearLayout layoutNotify;
    @BindView(R.id.tvNotify)
    TextView tvNotify;
}
