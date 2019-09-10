package com.gogroup.app.gogroupapp.User;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.Adapters.AdapterMyPurchases;
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

public class MyPurchases extends BaseActivity implements CallBackRefresh{
    AdapterMyPurchases adapter;
    int pageIndex = 0;
    boolean isLoadMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_purchases);
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

        /*for(int i=0;i<list.size();i++)
        {
            if((list.get(i).getIs_approved() == 1))
                newList.add(list.get(i));
        }*/

        if (adapter == null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recycler.setLayoutManager(linearLayoutManager);
            adapter = new AdapterMyPurchases(this, list,this);
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
        apiGetPurchaseList(false);
    }


    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;

    @BindView(R.id.parentScroll)
    NestedScrollView parentScroll;

    @Override
    public void onRefresh(ListResponse item) {
        pageIndex=0;
        adapter=null;
        apiGetPurchaseList(true);
    }
}
