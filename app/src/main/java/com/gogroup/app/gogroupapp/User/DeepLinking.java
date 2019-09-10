package com.gogroup.app.gogroupapp.User;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;
import com.gogroup.app.gogroupapp.CallBackListeners.CallBackApi;
import com.gogroup.app.gogroupapp.HelperClasses.ApiCalls;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.GoGroup;
import com.gogroup.app.gogroupapp.HelperClasses.RestClient;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.Login;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.CommonResponse;
import com.gogroup.app.gogroupapp.Responses.GroupDetailResponse;
import com.gogroup.app.gogroupapp.Responses.GroupListResponse;
import com.gogroup.app.gogroupapp.Responses.ListResponse;
import com.gogroup.app.gogroupapp.Seller.SellerAddDetailActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class DeepLinking extends BaseActivity {
    String postCategoryId, postGroupId, postAdvertisementId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_linking);
        ButterKnife.bind(this);
        Uri data = getIntent().getData();
        Log.d("DeepLinkData", "" + data);
        try {
            postCategoryId = String.valueOf(data.getQueryParameter("category_id"));
            postGroupId = String.valueOf(data.getQueryParameter("group_id"));
            GoGroup.getInstance().setGroupId(postGroupId);
            GoGroup.getInstance().setCategoryId(postCategoryId);
        } catch (NullPointerException e) {
            postGroupId = GoGroup.getInstance().getGroupId();
            postCategoryId = GoGroup.getInstance().getCategoryId();
        }
        try {
            postAdvertisementId = String.valueOf(data.getQueryParameter("advertisement_id"));
            GoGroup.getInstance().setAdvertisementId(postAdvertisementId);
        } catch (NullPointerException e) {
            postAdvertisementId = GoGroup.getInstance().getAdvertisementId();
        }
        if (UserPreferences.getInstance().isLogin()) {

            if (UserPreferences.getInstance().getUserType().toLowerCase().trim().equals("user")) {
                if (postAdvertisementId == null || postAdvertisementId.equals("null")) {
                    apiGetGroupDetails();
                } else {
                    Intent intent = new Intent(DeepLinking.this, SellerAddDetailActivity.class);
                    intent.putExtra(SellerAddDetailActivity.DATA, postAdvertisementId);
                    intent.putExtra(Login.DEEPLINK_DATA, true);
                    startActivity(intent);
                    ActivityCompat.finishAffinity(DeepLinking.this);


                }
            } else {


                AlertDialog dialog = new AlertDialog.Builder(DeepLinking.this)

                        .setMessage("You are logined as a Seller. You have to login as a User to join the group. Do you want to logout?")
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                UserPreferences.getInstance().clearUserDetails();
                                goToLoginScreen();

                            }
                        }).setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                                finish();

                            }
                        }).setCancelable(false)
                        .show();

            }
        } else {
            goToLoginScreen();
        }

    }

    private void goToLoginScreen() {

        Utils.userType = getResources().getString(R.string.option_login_screen_user);
        Intent intent = new Intent(DeepLinking.this, Login.class);
        intent.putExtra(Login.DEEPLINK_DATA, true);
        startActivity(intent);
        ActivityCompat.finishAffinity(DeepLinking.this);
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

    private void setValues(ListResponse detail) {

        ListResponse groupDetail = detail.getGroupDetails();
        if (groupDetail.getJoinStatus() != null) {
            if (groupDetail.getJoinStatus().equals("0")) {
                Intent intent = new Intent(DeepLinking.this, GroupDetails.class);
                intent.putExtra(GroupDetails.CATEGORY_ID, postCategoryId);
                intent.putExtra(GroupDetails.GROUP_ID, groupDetail.getGroupId());
                intent.putExtra(GroupDetails.DEEPLINK_DATA, true);
                startActivity(intent);
            } else if (groupDetail.getJoinStatus().equals("1")) {

                Intent intent = new Intent(DeepLinking.this, JoinedGroupDetail.class);
                intent.putExtra("category_id", postCategoryId);
                intent.putExtra("group_id", groupDetail.getGroupId());
                startActivity(intent);

            }
        } else {
            Intent intent = new Intent(DeepLinking.this, UserDashboardNew.class);
            startActivity(intent);
        }

        ActivityCompat.finishAffinity(DeepLinking.this);

    }

}
