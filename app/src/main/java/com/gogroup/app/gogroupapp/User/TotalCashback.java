package com.gogroup.app.gogroupapp.User;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.Adapters.AdapterMyPurchases;
import com.gogroup.app.gogroupapp.Adapters.AdapterTotalCashback;
import com.gogroup.app.gogroupapp.CallBackListeners.CallBackApi;
import com.gogroup.app.gogroupapp.CallBackListeners.CallBackRefresh;
import com.gogroup.app.gogroupapp.HelperClasses.ApiCalls;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.EndlessParentScrollListener;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.DetailResponse;
import com.gogroup.app.gogroupapp.Responses.ListResponse;
import com.gogroup.app.gogroupapp.Responses.SellerAdvertisementResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class TotalCashback extends BaseActivity implements CallBackRefresh{
    AdapterTotalCashback adapter;
    int pageIndex = 0;
    boolean isLoadMore;
    int cashback = 0;
    List<SellerAdvertisementResponse> sellerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_cashback);
        ButterKnife.bind(this);
        apiGetPurchaseList(true);
    }


    private void apiGetPurchaseList(final boolean isProgressBar) {

        if (isProgressBar) {
            showProgressbar();
        }
        Map<String, String> postFields = new HashMap<>();
        postFields.put("pageIndex", "" + pageIndex);
        new ApiCalls(this).apiGetPurchaseList(postFields, new CallBackApi() {
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
                apiGetPurchaseList(isProgressBar);
            }

            @Override
            public void onRetryNo() {
            }
        });

    }


    private void setAdapter(List<ListResponse> list) {

        ArrayList<ListResponse> newList = new ArrayList<>();

        for(int i=0;i<list.size();i++)
        {
            ListResponse item = list.get(i);
            if(item.getCouponStatus().equalsIgnoreCase("purchased"))
                newList.add(item);
        }

        if (adapter == null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recycler.setLayoutManager(linearLayoutManager);
            adapter = new AdapterTotalCashback(this, newList,this);
            recycler.setAdapter(adapter);
            recycler.setNestedScrollingEnabled(false);
            recycler.setHasFixedSize(true);
            tvEmpty.setVisibility(newList.size() == 0 ? View.VISIBLE : View.GONE);
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

        cashback = calculateCashback(newList);

        tvCashback.setText("Rs. "+cashback);
    }


    private void LoadMoreData() {
        ++pageIndex;
        apiGetPurchaseList(false);
    }



    public int calculateCashback(ArrayList<ListResponse> list)
    {
        for(int i=0;i<list.size();i++)
        {
         ListResponse item = list.get(i);
         int pendingusers = Integer.parseInt(item.getPurchasedCount());
         switch(pendingusers)
         {
             case 1:
                 cashback = cashback+Integer.parseInt(item.getActualPrice())-Integer.parseInt(item.getOfferPrice());
                 break;
             case 2:
                 cashback = cashback+Integer.parseInt(item.getActualPrice())-Integer.parseInt(item.getOfferfortwo());
                 break;
                 default:
                     cashback = cashback+Integer.parseInt(item.getActualPrice())-Integer.parseInt(item.getCostforx());
         }
        }
        return cashback;
    }

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;
    @BindView(R.id.cashback)
    TextView tvCashback;
    @BindView(R.id.parentScroll)
    NestedScrollView parentScroll;

    @Override
    public void onRefresh(ListResponse item) {
        pageIndex=0;
        adapter=null;
        apiGetPurchaseList(true);
    }
}
