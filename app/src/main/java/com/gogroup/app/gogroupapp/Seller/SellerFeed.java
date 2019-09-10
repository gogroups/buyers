package com.gogroup.app.gogroupapp.Seller;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gogroup.app.gogroupapp.Adapters.AdapterFeeds;
import com.gogroup.app.gogroupapp.CallBackListeners.CallBackApi;
import com.gogroup.app.gogroupapp.Fragments.FeedFilterFragment;
import com.gogroup.app.gogroupapp.HelperClasses.ApiCalls;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.EndlessParentScrollListener;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.Models.FilterModel;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.DetailResponse;
import com.gogroup.app.gogroupapp.Responses.ListResponse;
import com.gogroup.app.gogroupapp.User.CategoryDetailUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class SellerFeed extends BaseActivity {

    public AdapterFeeds adapter;
    public int pageIndex = 0;
    boolean isLoadMore;
    String status = "placed", advertisementId;
    public FeedFilterFragment fragmentFeedFilter;
    public FilterModel filterFeed = new FilterModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_feed);
        ButterKnife.bind(this);

        fragmentFeedFilter = new FeedFilterFragment();
        addFilterFragment(fragmentFeedFilter);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0); // this disable the animation
                Utils.hideKeyboard(SellerFeed.this);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Utils.hideKeyboard(SellerFeed.this);
                super.onDrawerOpened(drawerView);
                super.onDrawerSlide(drawerView, 0); // this disables the arrow @ completed state
            }
        };

        apiGetFeeds(true, tvPlaced);

    }


    public void apiGetFeeds(final boolean isProgressBar, final TextView textView) {

        if (textView != null) {
            setSelection(tvPlaced, false);
            setSelection(tvPurchased, false);
            setSelection(tvGenerated, false);
            setSelection(textView, true);
            status = textView.getTag().toString();
        }
        if (isProgressBar) {
            showProgressbar();
        }
        Map<String, String> postFields = new HashMap<>();
        postFields.put("pageIndex", "" + pageIndex);
        postFields.put("order_placed_from_date", filterFeed.getStartFromDate() != null ? filterFeed.getStartFromDate() : "");
        postFields.put("order_placed_to_date", filterFeed.getStartToDate() != null ? filterFeed.getStartToDate() : "");
        postFields.put("purchased_from_date", filterFeed.getEndFromDate() != null ? filterFeed.getEndFromDate() : "");
        postFields.put("purchased_to_date", filterFeed.getEndToDate() != null ? filterFeed.getEndToDate() : "");
        postFields.put("status", status);

        new ApiCalls(this).apiGetSellerFeed(postFields, new CallBackApi() {
            @Override
            public void onSuccess(Response response) {
                DetailResponse body = (DetailResponse) response.body();
                isLoadMore = body != null && body.getDataList().size() > 0;
                setAdapter(body.getDataList());
                hideProgressBar();
            }

            @Override
            public void onFailure(String message) {
                hideProgressBar();
            }

            @Override
            public void onRetryYes() {
                apiGetFeeds(isProgressBar, textView);
            }

            @Override
            public void onRetryNo() {
            }
        });

    }


    private void setAdapter(List<ListResponse> list) {

        if (adapter == null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recycler.setLayoutManager(linearLayoutManager);
            adapter = new AdapterFeeds(this, list);
            recycler.setAdapter(adapter);
            recycler.setNestedScrollingEnabled(false);
            recycler.setHasFixedSize(true);
            tvEmpty.setVisibility(list.size() == 0 ? View.VISIBLE : View.GONE);
            parentScroll.setOnScrollChangeListener(new EndlessParentScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    if (isLoadMore) {
                        LoadMoreData();
                    } else {
                        adapter.removeLoadingView();
                    }
                }
            });

        } else {
            adapter.addData(list, isLoadMore);

        }
    }


    private void LoadMoreData() {
        ++pageIndex;
        apiGetFeeds(false, null);
    }

    private void addFilterFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.findFragmentByTag("FeedFilter") != null) {
            //if the fragment exists, show it.
            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("FeedFilter")).commit();
        } else {
            //if the fragment does not exist, add it to fragment manager.
            fragmentManager.beginTransaction().add(R.id.layoutFilter, fragment, "FeedFilter").commit();
        }
//                if (fragmentManager.findFragmentByTag(ADS_TAG) != null) {
//                    //if the other fragment is visible, hide it.
//                    fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(ADS_TAG)).commit();
//                }


    }

    @OnClick(R.id.filter)
    void openFilter() {
        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);
        } else {
            drawer.openDrawer(Gravity.RIGHT);
        }
    }

    @OnClick(R.id.btnBack)
    void back() {
        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);
        } else {
            finish();
        }
    }

    @OnClick(R.id.tvPlaced)
    void showPlaced() {
        pageIndex = 0;
        adapter = null;
        apiGetFeeds(true, tvPlaced);
    }

    @OnClick(R.id.tvPurchased)
    void showPurchased() {
        pageIndex = 0;
        adapter = null;
        apiGetFeeds(true, tvPurchased);
    }

    @OnClick(R.id.tvGenerated)
    void showGenerated() {
        pageIndex = 0;
        adapter = null;
        apiGetFeeds(true, tvGenerated);
    }

    void setSelection(TextView textView, boolean isSelect) {
        textView.setTextColor(ContextCompat.getColor(this, isSelect ? R.color.white : R.color.gray6));
        textView.setBackground(ContextCompat.getDrawable(this, isSelect ? R.drawable.bg_round_fill2 : R.drawable.bg_round_fill3));
    }

    @BindView(R.id.parentScroll)
    NestedScrollView parentScroll;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;
    @BindView(R.id.tvPlaced)
    TextView tvPlaced;
    @BindView(R.id.tvPurchased)
    TextView tvPurchased;
    @BindView(R.id.tvGenerated)
    TextView tvGenerated;


    @BindView(R.id.drawer_layout)
    public DrawerLayout drawer;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == REFRESH_DASHBOARD) {
            pageIndex = 0;
            adapter = null;
            apiGetFeeds(true, null);
        }
    }
}
