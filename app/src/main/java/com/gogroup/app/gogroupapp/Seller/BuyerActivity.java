package com.gogroup.app.gogroupapp.Seller;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.Adapters.AdapterBuyers;
import com.gogroup.app.gogroupapp.Adapters.AdapterMyPurchases;
import com.gogroup.app.gogroupapp.CallBackListeners.CallBackApi;
import com.gogroup.app.gogroupapp.CallBackListeners.CallBackRefresh;
import com.gogroup.app.gogroupapp.HelperClasses.ApiCalls;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.EndlessParentScrollListener;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.DetailResponse;
import com.gogroup.app.gogroupapp.Responses.ListResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class BuyerActivity extends BaseActivity implements CallBackRefresh {

    AdapterBuyers adapter;
    int pageIndex = 0;
    boolean isLoadMore, isPurchasedUsers;
    String status="both",advertisementId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);
        ButterKnife.bind(this);
        isPurchasedUsers = getIntent().getBooleanExtra(BaseActivity.IS_PURCHASED, false);
        tvTitle.setText(isPurchasedUsers?getString(R.string.purchasedBy):getString(R.string.couponGeneratedBy));

        advertisementId=getIntent().getStringExtra(BaseActivity.ID);
        radioGroup.setVisibility(isPurchasedUsers ? View.VISIBLE : View.GONE);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbBoth:
                        pageIndex = 0;
                        adapter = null;
                        status = "both";
                        break;
                    case R.id.rbPurchased:
                        pageIndex = 0;
                        adapter = null;
                        status = "purchased";
                        break;
                    case R.id.rbOrderPlaced:
                        pageIndex = 0;
                        adapter = null;
                        status = "orderPlaced";
                        break;
                    default:
                        break;
                }
                apiGetPurchasedUsers(true);
            }
        });


        if (!isPurchasedUsers) {
            apiGetPendingUsers(true);
        } else {
            apiGetPurchasedUsers(true);
        }
    }


    private void apiGetPendingUsers(final boolean isProgressBar) {

        if (isProgressBar) {
            showProgressbar();
        }
        Map<String, String> postFields = new HashMap<>();
        postFields.put("advertisement_id", getIntent().getStringExtra(BaseActivity.ID));
        postFields.put("pageIndex", "" + pageIndex);
        new ApiCalls(this).apiGetPendingUsers(postFields, new CallBackApi() {
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
                apiGetPendingUsers(isProgressBar);
            }

            @Override
            public void onRetryNo() {
            }
        });

    }

    private void apiGetPurchasedUsers(final boolean isProgressBar) {

        if (isProgressBar) {
            showProgressbar();
        }
        Map<String, String> postFields = new HashMap<>();
        postFields.put("advertisement_id", advertisementId);
        postFields.put("status", status);
        postFields.put("pageIndex", "" + pageIndex);
        new ApiCalls(this).apiGetPurchasedUsers(postFields, new CallBackApi() {
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
                apiGetPurchasedUsers(isProgressBar);
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
            adapter = new AdapterBuyers(this, list,this,advertisementId);
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
        if (isPurchasedUsers) {
            apiGetPurchasedUsers(false);
        } else {
            apiGetPendingUsers(false);
        }
    }


    @BindView(R.id.parentScroll)
    NestedScrollView parentScroll;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.rbBoth)
    RadioButton rbBoth;


    @Override
    public void onRefresh(ListResponse item) {
        pageIndex=0;
        adapter=null;
        apiGetPurchasedUsers(true);

    }
}
