package com.gogroup.app.gogroupapp.User;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.FontManager;
import com.gogroup.app.gogroupapp.HelperClasses.RestClient;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.OptionLoginScreen;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.PostResponse;
import com.gogroup.app.gogroupapp.Seller.UserProfile;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class SettingsActivity extends BaseActivity {

    int REFRESH_PROFILE = -1;
    Dialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        ButterKnife.bind(this);
        setIcons();
        customDialog = new Dialog(SettingsActivity.this);
        switchComat.setChecked(UserPreferences.getInstance().getNotifyStatus());

        switchComat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isInterNetConnected(SettingsActivity.this)) {
                    showProgressbar();
                    RestClient.get().updateNotification(UserPreferences.getInstance().getToken(), String.valueOf(switchComat.isChecked())).
                            enqueue(new retrofit2.Callback<PostResponse>() {
                                @Override
                                public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                                    if (response.body() != null) {

                                        if (response.body().isStatus()) {
                                            UserPreferences.getInstance().updateNotifyStatus(switchComat.isChecked());
                                        } else {
                                            switchComat.setChecked(!switchComat.isChecked());

                                        }
                                        Utils.showShortToast(SettingsActivity.this, response.body().getMessage());

                                    } else {
                                        Utils.showServerError(SettingsActivity.this);
                                        switchComat.setChecked(!switchComat.isChecked());

                                    }
                                    hideProgressBar();

                                }

                                @Override
                                public void onFailure(Call<PostResponse> call, Throwable t) {
                                    switchComat.setChecked(!switchComat.isChecked());

                                    hideProgressBar();
                                    Utils.showShortToast(SettingsActivity.this, t.getMessage());
                                }
                            });

                } else {
                    switchComat.setChecked(!switchComat.isChecked());

                }


            }
        });

    }


    @OnClick(R.id.layoutChangePswd)
    void changePswd() {
        customDialog.setContentView(R.layout.layout_change_password);
        final EditText etOldPswd = (EditText) customDialog.findViewById(R.id.etOldPswd);
        final EditText etNewPswd = (EditText) customDialog.findViewById(R.id.etNewPswd);
        final EditText etConfirmPswd = (EditText) customDialog.findViewById(R.id.etConfirmPswd);

        TextView cancel = (TextView) customDialog.findViewById(R.id.tv_cancel);
        TextView ok = (TextView) customDialog.findViewById(R.id.tv_ok);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = customDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        customDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (etOldPswd.getText().toString().trim().isEmpty()) {
                    Utils.setEditTextError(etOldPswd, getResources().getString(R.string.validationPassword));

                } else if (etNewPswd.getText().toString().trim().isEmpty()) {
                    Utils.setEditTextError(etNewPswd, getResources().getString(R.string.validationPassword));

                } else if (etConfirmPswd.getText().toString().trim().isEmpty()) {
                    Utils.setEditTextError(etConfirmPswd, getResources().getString(R.string.validationPassword));

                } else if (!etConfirmPswd.getText().toString().trim().equals(etNewPswd.getText().toString().trim())) {
                    Utils.setEditTextError(etConfirmPswd, getString(R.string.validationMatchingPassword));
                } else {
                    apiChangePswd(etOldPswd.getText().toString(), etNewPswd.getText().toString(), etConfirmPswd.getText().toString());

                }
            }

        });
        customDialog.show();
    }

    private void apiChangePswd(String oldPswd, String newPswd, String confirmPswd) {

        if (Utils.isInterNetConnected(SettingsActivity.this)) {
            showProgressbar();
            RestClient.get().changePassword(UserPreferences.getInstance().getToken(), oldPswd, newPswd, confirmPswd).
                    enqueue(new retrofit2.Callback<PostResponse>() {
                        @Override
                        public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                            if (response.body() != null) {

                                if (response.body().isStatus()) {
                                    customDialog.dismiss();
                                }
                                Utils.showShortToast(SettingsActivity.this, response.body().getMessage());

                            } else {
                                Utils.showServerError(SettingsActivity.this);
                            }
                            hideProgressBar();

                        }

                        @Override
                        public void onFailure(Call<PostResponse> call, Throwable t) {
                            hideProgressBar();
                            Utils.showShortToast(SettingsActivity.this, t.getMessage());
                        }
                    });

        }

    }


    private void setIcons() {
        btnBack.setText(R.string.icon_ionicon_var_chevron_left);
        btnBack.setTypeface(FontManager.getTypeFaceFromFontName(getApplicationContext(), FontManager.IONICONFONT));
        btnBack.setTextSize(20);
        btnBack.setTextColor(ContextCompat.getColor(this, R.color.white_color));

    }

    @OnClick(R.id.btnBack)
    void back() {
        Intent intent = new Intent();
        setResult(REFRESH_PROFILE, intent);
        finish();
    }

    @BindView(R.id.btnBack)
    Button btnBack;

    @OnClick(R.id.layoutEditProfile)
    void openEditProfile() {

        Intent i = new Intent(SettingsActivity.this, UserProfile.class);
        startActivityForResult(i, BaseActivity.REFRESH_PROFILE);
    }

    @OnClick(R.id.layoutLogout)
    void logout() {
        UserPreferences.getInstance().clearUserDetails();
        Intent goToOptionsScreen = new Intent(SettingsActivity.this, OptionLoginScreen.class);
        startActivity(goToOptionsScreen);
        ActivityCompat.finishAffinity(SettingsActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {

            case BaseActivity.REFRESH_PROFILE:
                REFRESH_PROFILE = BaseActivity.REFRESH_PROFILE;
                break;

            default:
                break;

        }

    }

    @BindView(R.id.compatSwitch)
    Switch switchComat;
}