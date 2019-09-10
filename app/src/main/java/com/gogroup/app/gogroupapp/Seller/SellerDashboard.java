package com.gogroup.app.gogroupapp.Seller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gogroup.app.gogroupapp.Adapters.AdapterCustomSellerHomeNavigationViewListView;
import com.gogroup.app.gogroupapp.Adapters.AdapterSellerAdvertisement;
import com.gogroup.app.gogroupapp.CallBackListeners.CallBackOnLoadMore;
import com.gogroup.app.gogroupapp.Fragments.NotificationDialog;
import com.gogroup.app.gogroupapp.Fragments.SellerFilterFragment;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.FontManager;
import com.gogroup.app.gogroupapp.HelperClasses.GoGroup;
import com.gogroup.app.gogroupapp.HelperClasses.RestClient;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.Models.FilterModel;
import com.gogroup.app.gogroupapp.OptionLoginScreen;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.GroupListResponse;
import com.gogroup.app.gogroupapp.Responses.NotificationResponse;
import com.gogroup.app.gogroupapp.Responses.PostResponse;
import com.gogroup.app.gogroupapp.Responses.SellerAdvertisementResponse;
import com.gogroup.app.gogroupapp.SplashActivity;
import com.gogroup.app.gogroupapp.User.ContactUs;
import com.gogroup.app.gogroupapp.User.SettingsActivity;
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

public class SellerDashboard extends BaseActivity implements CallBackOnLoadMore {

    ArrayList<String> mArraysMenus = new ArrayList<>();
    AdapterCustomSellerHomeNavigationViewListView adapterCustomsellerNavigationView;
    private static final int GRID_COLUMN_COUNT = 2;
    ListView mListViewMenus;
    TextView mTextViewIconLogout;
    GridLayoutManager gridLayoutManager;
    AdapterSellerAdvertisement adapterSeller;
    List<SellerAdvertisementResponse> advertiseList = new ArrayList<>();
    NotificationResponse notifications;
    public FilterModel filterModel = new FilterModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dashboard);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initIds();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0); // this disable the animation
                Utils.hideKeyboard(SellerDashboard.this);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Utils.hideKeyboard(SellerDashboard.this);
                super.onDrawerOpened(drawerView);
                super.onDrawerSlide(drawerView, 0); // this disables the arrow @ completed state
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            //LinearLayout header_linear_h = (LinearLayout) navigationView.getHeaderView(0);
            LinearLayout header_linear = navigationView.findViewById(R.id.nav_header_linear);
            header_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(SellerDashboard.this, UserProfile.class);
                    startActivityForResult(i, BaseActivity.REFRESH_PROFILE);
                }
            });

        }
        setNavigationView();
//        navigationView.setNavigationItemSelectedListener(SellerDashboard.this);

        gridLayoutManager = new GridLayoutManager(SellerDashboard.this, GRID_COLUMN_COUNT);
        recyclerAdvertisement.setLayoutManager(new LinearLayoutManager(this));
        adapterSeller = new AdapterSellerAdvertisement();
        recyclerAdvertisement.setAdapter(adapterSeller);
        tvEmpty.setVisibility(View.VISIBLE);
//        adapterSeller.setOnLoadMoreListener(SellerDashboard.this);

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, findViewById(R.id.layoutFilter));

        layoutNotify.setVisibility(View.GONE);
        setSellerProfile();
        apiGetNotifications();
        getSellerAdds();
        addFilterFragment();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                apiGetNotifications();
                setSellerProfile();
                getSellerAdds();
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
        body.setNotificationType(notificationMap.get("notificationType"));

        try {
            body.setGroupId(notificationMap.get("group_id"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            body.setAdvertisementId(notificationMap.get("advertisement_id"));
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


            switch ("" + body.getNotificationType().trim()) {
                case "myOrders":
                    detailClass = SellerFeed.class;
                    break;
                case "offer":
                    detailClass = SellerAddDetailActivity.class;
                    bundle.putString(SellerAddDetailActivity.DATA, body.getAdvertisementId());
                    break;
                case "dashboard":
                    detailClass = SellerDashboard.class;
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
                                    Utils.showShortToast(SellerDashboard.this, response.body().getMessage());
                                }
                            } else {
                                Utils.showServerError(SellerDashboard.this);
                            }
                        }

                        @Override
                        public void onFailure(final Call<NotificationResponse> call, Throwable t) {
                            Utils.showShortToast(SellerDashboard.this, t.getMessage());
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
                                    Utils.showShortToast(SellerDashboard.this, response.body().getMessage());
                                }
                            } else {
                                Utils.showServerError(SellerDashboard.this);
                            }
                        }

                        @Override
                        public void onFailure(final Call<PostResponse> call, Throwable t) {
                            Utils.showShortToast(SellerDashboard.this, t.getMessage());
                        }
                    });
        }

    }

    @OnClick({R.id.navigation_linear_layout_logout, R.id.navigation_vendor_home_icon_logout_icon, R.id.vendor_home_logout_button})
    void logOut() {


        UserPreferences.getInstance().clearUserDetails();
        Intent i = new Intent(SellerDashboard.this, OptionLoginScreen.class);
        startActivity(i);
        ActivityCompat.finishAffinity(SellerDashboard.this);
    }

    private void setSellerProfile() {

        UserPreferences user = UserPreferences.getInstance();
        tvName.setText(user.getUserName());
        tvCity.setText(user.getLocation());
        if (user.getProfileImage() != null && !user.getProfileImage().trim().equals("")) {
            Picasso.with(getApplicationContext()).load(user.getProfileImage()).into(imgUser);
        }
    }

    public void getSellerAdds() {
        if (Utils.isInterNetConnected(SellerDashboard.this)) {
            showProgressbar();
            Map<String, String> postFields = new HashMap<>();
            postFields.put("start_from_date", filterModel.getStartFromDate() != null ? filterModel.getStartFromDate() : "");
            postFields.put("start_to_date", filterModel.getStartToDate() != null ? filterModel.getStartToDate() : "");
            postFields.put("end_from_date", filterModel.getEndFromDate() != null ? filterModel.getEndFromDate() : "");
            postFields.put("end_to_date", filterModel.getEndToDate() != null ? filterModel.getEndToDate() : "");
            postFields.put("advertisement_name", filterModel.getDealName() != null ? filterModel.getDealName() : "");
            postFields.put("category_id", filterModel.getCategoryId1() != null ? filterModel.getCategoryId1() : "");
            postFields.put("subcategory_id", filterModel.getCategoryId2() != null ? filterModel.getCategoryId2() : "");
            postFields.put("location", filterModel.getLocation() != null ? filterModel.getLocation() : "");
            RestClient.get().getAllSellerAdvertisement(UserPreferences.getInstance().getToken(), postFields).
                    enqueue(new retrofit2.Callback<SellerAdvertisementResponse>() {
                        @Override
                        public void onResponse(Call<SellerAdvertisementResponse> call, Response<SellerAdvertisementResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {

                                    advertiseList = response.body().getData();

                                    setSelection(tvActive, false);
                                    setSelection(tvExpired, false);
                                    setSelection(tvPending, false);
                                    switch (selectedStatus) {
                                        case 1:
                                            showActiveAdds();
                                            break;
                                        case 2:
                                            showExpiredAdds();
                                            break;
                                        case 3:
                                            showPendingAdds();
                                            break;
                                        default:
                                            showAllAdds();
                                            adapterSeller.setData(recyclerAdvertisement, advertiseList, SellerDashboard.this, false);
                                            break;
                                    }


                                } else {
                                    Utils.showShortToast(getApplicationContext(), response.body().getMessage());
                                }
                                tvEmpty.setVisibility(advertiseList.size() > 0 ? View.GONE : View.VISIBLE);

                            } else {
                                Utils.showServerError(getApplicationContext());
                            }
                            hideProgressBar();
                            swipeRefreshLayout.setRefreshing(false);

                        }

                        @Override
                        public void onFailure(final Call<SellerAdvertisementResponse> call, Throwable t) {
                            hideProgressBar();
                            swipeRefreshLayout.setRefreshing(false);

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

    private void initIds() {
        mTextViewIconLogout = (TextView) findViewById(R.id.navigation_vendor_home_icon_logout_icon);
        mListViewMenus = (ListView) findViewById(R.id.vendor_home_navigation_listview);
    }

    private void setNavigationView() {
        mArraysMenus.add(getString(R.string.settings));
        mArraysMenus.add(getString(R.string.myOrders));
        mArraysMenus.add(getString(R.string.contactUs));
//        mArraysMenus.add(getString(R.string.about));
//        mArraysMenus.add(getString(R.string.share));

        mTextViewIconLogout.setText(R.string.icon_ionicon_var_log_out);
        mTextViewIconLogout.setTypeface(FontManager.getTypeFaceFromFontName(getApplicationContext(), FontManager.IONICONFONT));
        mTextViewIconLogout.setTextSize(20);
        mTextViewIconLogout.setTextColor(ContextCompat.getColor(this, R.color.white_color));

        adapterCustomsellerNavigationView = new AdapterCustomSellerHomeNavigationViewListView(mArraysMenus, SellerDashboard.this);
        mListViewMenus.setAdapter(adapterCustomsellerNavigationView);

        mListViewMenus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (position == 0) {
                    Intent i = new Intent(SellerDashboard.this, SettingsActivity.class);
                    startActivityForResult(i, BaseActivity.REFRESH_PROFILE);

                } else if (position == 1) {
                    Intent i = new Intent(SellerDashboard.this, SellerFeed.class);
                    startActivity(i);
                } else if (position == 2) {
                    Intent i = new Intent(SellerDashboard.this, ContactUs.class);
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


    @OnClick(R.id.imgFilter)
    void actionFilter() {
        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);

            //drawerLayout.
        } else {
            drawer.openDrawer(Gravity.RIGHT);
        }
    }

    @OnClick(R.id.imgNotification)
    void actionNotification() {
        apiReadNotification();
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        NotificationDialog dialogFragment = NotificationDialog.getInstance(notifications);
        dialogFragment.show(manager, "notifications");
    }

    @OnClick(R.id.imgAdd)
    void actionAdd() {
        if (Utils.isInterNetConnected(SellerDashboard.this)) {
            Intent i = new Intent(SellerDashboard.this, AddAdvertisement.class);
            startActivityForResult(i, BaseActivity.REFRESH_DASHBOARD);
        }
    }


    @Override
    public void onLoadMore() {
        if (advertiseList.size() <= 10) {
            advertiseList.add(null);
            adapterSeller.notifyItemInserted(advertiseList.size() - 1);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    advertiseList.remove(advertiseList.size() - 1);
                    adapterSeller.notifyItemRemoved(advertiseList.size());

                    //Generating more data
                    int index = advertiseList.size();
                    int end = index + 10;
                    for (int i = index; i < end; i++) {
//                        advertiseList.add("ads");
                    }
                    adapterSeller.notifyDataSetChanged();
                    adapterSeller.setLoaded();
                }
            }, 5000);
        } else {
            Toast.makeText(this, "Loading data completed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        apiGetNotifications();
        if (BaseActivity.isRefresh) {
            getSellerAdds();
            BaseActivity.isRefresh = false;
        }
    }

    @Override
    public void onVisibleBehindCanceled() {
        super.onVisibleBehindCanceled();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case BaseActivity.REFRESH_DASHBOARD:
                getSellerAdds();
                break;
            case BaseActivity.REFRESH_PROFILE:
                setSellerProfile();
                break;

            default:
                break;
        }
    }

    private void addFilterFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.layoutFilter, new SellerFilterFragment()).addToBackStack(null).commit();
    }


    @OnClick(R.id.tvActive)
    void showActiveAdds() {
        filterAdds(tvActive, 1);
    }

    @OnClick(R.id.tvExpired)
    void showExpiredAdds() {
        filterAdds(tvExpired, 2);
    }

    @OnClick(R.id.tvPending)
    void showPendingAdds() {
        filterAdds(tvPending, 3);
    }

    @OnClick(R.id.tvAll)
    void showAllAdds() {
        filterAdds(tvAll, 0);
    }


    int selectedStatus;

    void filterAdds(TextView textView, int status) {
        selectedStatus = status;
//        boolean isSelect = textView.getCurrentTextColor() == ContextCompat.getColor(this, R.color.white);
        boolean isAllSelect = textView == tvAll;
        setSelection(tvActive, false);
        setSelection(tvExpired, false);
        setSelection(tvPending, false);
        setSelection(tvAll, false);
        setSelection(textView, true);
        List<SellerAdvertisementResponse> filterList = new ArrayList<>();
        for (int i = 0; i < advertiseList.size(); i++) {
            if (advertiseList.get(i).getActiveStatus().trim().equalsIgnoreCase(status == 1 ? "approved" : status == 2 ? "expired" : "unapproved")) {
                filterList.add(advertiseList.get(i));
            }
        }
        if (adapterSeller != null) {
            adapterSeller.setData(recyclerAdvertisement, !isAllSelect ? filterList : advertiseList, SellerDashboard.this, false);
        }
        tvEmpty.setVisibility((!isAllSelect ? filterList : advertiseList).size() > 0 ? View.GONE : View.VISIBLE);
    }

    void setSelection(TextView textView, boolean isSelect) {
        textView.setTextColor(ContextCompat.getColor(this, isSelect ? R.color.white : R.color.gray6));
        textView.setBackground(ContextCompat.getDrawable(this, isSelect ? R.drawable.bg_round_fill2 : R.drawable.bg_round_fill3));
    }


    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.imgUser)
    CircleImageView imgUser;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvActive)
    TextView tvActive;
    @BindView(R.id.tvAll)
    TextView tvAll;
    @BindView(R.id.tvExpired)
    TextView tvExpired;
    @BindView(R.id.tvPending)
    TextView tvPending;
    @BindView(R.id.layoutNotify)
    LinearLayout layoutNotify;
    @BindView(R.id.tvNotify)
    TextView tvNotify;
    @BindView(R.id.tvCity)
    TextView tvCity;
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;
    @BindView(R.id.recyclerAdvertisement)
    RecyclerView recyclerAdvertisement;
    @BindView(R.id.drawer_layout)
    public DrawerLayout drawer;
}
