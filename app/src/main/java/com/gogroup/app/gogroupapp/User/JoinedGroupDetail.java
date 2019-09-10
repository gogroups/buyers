package com.gogroup.app.gogroupapp.User;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.Adapters.AdapterAdsPager;
import com.gogroup.app.gogroupapp.CallBackListeners.CallBackApi;
import com.gogroup.app.gogroupapp.CallBackListeners.CallBackReport;
import com.gogroup.app.gogroupapp.Fragments.ReportDialog;
import com.gogroup.app.gogroupapp.HelperClasses.ApiCalls;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.RestClient;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.CommonResponse;
import com.gogroup.app.gogroupapp.Responses.DetailResponse;
import com.gogroup.app.gogroupapp.Responses.ListResponse;
import com.gogroup.app.gogroupapp.Responses.PostResponse;
import com.gogroup.app.gogroupapp.Seller.AddAdvertisement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class JoinedGroupDetail extends BaseActivity implements CallBackReport {

    String postCategoryId, postGroupId;
    boolean isDeepLinking;
    Dialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_group_detail);
        ButterKnife.bind(this);

        postCategoryId = getIntent().getStringExtra("category_id");
        postGroupId = getIntent().getStringExtra("group_id");

        try {
            isDeepLinking = getIntent().getBooleanExtra(BaseActivity.DEEPLINK_DATA, false);
        } catch (NullPointerException e) {
            isDeepLinking = false;
        }
        customDialog = new Dialog(this);

        apiGetGroupDetails();


    }


    private void apiGetGroupDetails() {
        showProgressbar();
        Map<String, String> postFields = new HashMap<>();
        postFields.put("category_id", postCategoryId);
        postFields.put("group_id", postGroupId);
        new ApiCalls(this).apiGetGroupDetails(postFields, new CallBackApi() {
            @Override
            public void onSuccess(Response response) {
                CommonResponse body = (CommonResponse) response.body();
                try {
                    tvTitle.setText(body.getDetail().getGroupDetails().getGroupName());
                } catch (Exception e){
                    tvTitle.setText("Interest");
                }
                setAdapter(body.getDetail().getAdvertisementList());
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

    private void setAdapter(List<ListResponse> dataList) {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new AdapterAdsPager(this, dataList));
        tvEmpty.setVisibility(dataList.size() > 0 ? View.GONE : View.VISIBLE);
    }

    @OnClick(R.id.layoutToolbar)
    void openGroupDetail() {
        Intent i = new Intent(this, JoinedGroupDetails.class);
        i.putExtra(JoinedGroupDetails.CATEGORY_ID, postCategoryId);
        i.putExtra(JoinedGroupDetails.GROUP_ID, postGroupId);
        startActivity(i);
    }

    @OnClick(R.id.imgOption)
    void openOption(View v) {

        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.joined_group_menu, popup.getMenu());

//        if (UserPreferences.getInstance().getUserType().equalsIgnoreCase("user")) {
//            popup.getMenu().findItem(R.id.exit).setVisible(false);
//        } else {
//            popup.getMenu().findItem(R.id.report).setVisible(false);
//
//        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.report:
                        ReportDialog reportDialog = ReportDialog.instance(JoinedGroupDetail.this);
                        reportDialog.show(getSupportFragmentManager(), "Report");

                        break;
                    case R.id.exit:

                        apiExitGroup();
                        break;
                    default:
                        break;

                }
                return true;
            }
        });

        popup.show();

    }

    private void apiExitGroup() {
        if (com.gogroup.app.gogroupapp.HelperClasses.Utils.isInterNetConnected(this)) {
            //showProgressbar();
            RestClient.get().exitGroup(UserPreferences.getInstance().getToken(), postGroupId).
                    enqueue(new retrofit2.Callback<PostResponse>() {
                        @Override
                        public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
                                    Intent intent = new Intent();
                                    setResult(BaseActivity.REFRESH_LIST, intent);
                                    finish();
                                } else {
                                    com.gogroup.app.gogroupapp.HelperClasses.Utils.showShortToast(getApplicationContext(), response.body().getMessage());
                                }
                            } else {
                                com.gogroup.app.gogroupapp.HelperClasses.Utils.showServerError(getApplicationContext());
                            }
                            //  hideProgressBar();

                        }

                        @Override
                        public void onFailure(final Call<PostResponse> call, Throwable t) {
                            //     hideProgressBar();
                            com.gogroup.app.gogroupapp.HelperClasses.Utils.showShortToast(getApplicationContext(), t.getMessage());
                        }
                    });

        }
    }

    @Override
    public void onClickReportSubmit(String report) {
        apiReport(report);
    }

    private void apiReport(String comment) {
        if (com.gogroup.app.gogroupapp.HelperClasses.Utils.isInterNetConnected(this)) {
            //showProgressbar();
            RestClient.get().report(UserPreferences.getInstance().getToken(), comment, postGroupId, "group").
                    enqueue(new retrofit2.Callback<PostResponse>() {
                        @Override
                        public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {

                                    customDialog.dismiss();


                                }

                                com.gogroup.app.gogroupapp.HelperClasses.Utils.showShortToast(getApplicationContext(), response.body().getMessage());

                            } else {
                                com.gogroup.app.gogroupapp.HelperClasses.Utils.showServerError(getApplicationContext());
                            }
                            //  hideProgressBar();

                        }

                        @Override
                        public void onFailure(final Call<PostResponse> call, Throwable t) {
                            //     hideProgressBar();
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
                            com.gogroup.app.gogroupapp.HelperClasses.Utils.showShortToast(getApplicationContext(), t.getMessage());
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {

        if (isDeepLinking) {
            Intent intent = new Intent(this, UserDashboardNew.class);
            startActivity(intent);
            ActivityCompat.finishAffinity(this);
        } else {
            Intent intent = new Intent();
            setResult(BaseActivity.REFRESH_ACTIVITY, intent);
            finish();
        }

    }

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;
    @BindView(R.id.recycler)
    RecyclerView recycler;

}
