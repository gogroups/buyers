package com.gogroup.app.gogroupapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.RestClient;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.Responses.PostResponse;
import com.gogroup.app.gogroupapp.Responses.RegisterResponse;
import com.gogroup.app.gogroupapp.Seller.SellerDashboard;
import com.gogroup.app.gogroupapp.User.DeepLinking;
import com.gogroup.app.gogroupapp.User.UserDashboard;
import com.gogroup.app.gogroupapp.User.UserDashboardNew;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends BaseActivity {
    public static final String DATA = "data";
    @BindView(R.id.otp_submit_button)
    Button submitButton;
    @BindView(R.id.etOtp)
    EditText etOtp;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etCode)
    EditText etCode;
    RegisterResponse data;
    String token, email, phone, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);

        try {

            data = getIntent().getParcelableExtra(DATA);
            token = data.getToken();
            email = data.getEmail();
            phone = data.getContact();
            userId = data.getUserId();


        } catch (NullPointerException e) {

        }

        if (data == null) {
            token = UserPreferences.getInstance().getToken();
            email = UserPreferences.getInstance().getEmail();
            phone = UserPreferences.getInstance().getMobile();
            userId = UserPreferences.getInstance().getUserId();

        }
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {
                    if (Utils.isInterNetConnected(OtpActivity.this)) {
                        showProgressbar();
                        RestClient.get().verifyOtp(etOtp.getText().toString().trim(), token).
                                enqueue(new Callback<PostResponse>() {
                                    @Override
                                    public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {

                                        if (response.body() != null) {
                                            if (response.body().isStatus()) {
                                                if (data != null) {
                                                    Intent intent = new Intent();
                                                    setResult(7, intent);
                                                    finish();
                                                } else {
                                                    UserPreferences.getInstance().setIsVerifyOtp(true);
                                                    UserPreferences.getInstance().setEmail(etEmail.getText().toString());
                                                    UserPreferences.getInstance().setMobile(etCode.getText().toString() + etPhone.getText().toString());
                                                    if (UserPreferences.getInstance().getUserType().toLowerCase().trim().equals("user")) {

                                                        if (UserPreferences.getInstance().getIsDeepLink()) {
                                                            Intent i = new Intent(OtpActivity.this, DeepLinking.class);
                                                            startActivity(i);
                                                            ActivityCompat.finishAffinity(OtpActivity.this);

                                                        } else {

                                                            Intent i = new Intent(OtpActivity.this, UserDashboardNew.class);
                                                            startActivity(i);
                                                            ActivityCompat.finishAffinity(OtpActivity.this);
                                                        }

                                                    } else {

                                                        if (UserPreferences.getInstance().getIsActive() != null && UserPreferences.getInstance().getIsActive() != 0) {
                                                            Intent i = new Intent(OtpActivity.this, SellerDashboard.class);
                                                            startActivity(i);
                                                            ActivityCompat.finishAffinity(OtpActivity.this);
                                                        } else {
                                                            UserPreferences.getInstance().clearUserDetails();
                                                            Intent i = new Intent(OtpActivity.this, Login.class);
                                                            startActivity(i);
                                                            ActivityCompat.finishAffinity(OtpActivity.this);
                                                            Toast.makeText(OtpActivity.this, getString(R.string.inActiveStatus), Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                }
                                                Utils.showShortToast(OtpActivity.this, response.body().getMessage());

                                            } else {
                                                Utils.showShortToast(OtpActivity.this, response.body().getMessage());
                                            }
                                        } else {
                                            Utils.showServerError(OtpActivity.this);
                                        }
                                        hideProgressBar();
                                    }

                                    @Override
                                    public void onFailure(Call<PostResponse> call, Throwable t) {
                                        hideProgressBar();
                                        Utils.showShortToast(OtpActivity.this, t.getMessage());
                                    }
                                });

                    }


                }


            }
        });

        etEmail.setText(email);

        String num = phone != null ? phone : "";
        switch (num.length()) {
            case 11:
                etCode.setText(num.substring(0, 1));
                etPhone.setText(num.substring(1, num.length()));
                break;
            case 12:
                etCode.setText(num.substring(0, 2));
                etPhone.setText(num.substring(2, num.length()));
                break;
            case 13:
                etCode.setText(num.substring(0, 3));
                etPhone.setText(num.substring(3, num.length()));
                break;
            default:
                etPhone.setText(num);
                break;

        }

    }


    @OnClick(R.id.btnEdit)
    void edit(final Button btn) {
        if (btn.getText().toString().toLowerCase().trim().equals(getString(R.string.edit).toLowerCase())) {
            btn.setText(getString(R.string.update));
            etCode.setEnabled(true);
            etPhone.setEnabled(true);
            etEmail.setEnabled(true);
        } else {
            if (isValidEdit()) {
                if (Utils.isInterNetConnected(this)) {
                    showProgressbar();
                    RestClient.get().editContact(token, userId, etEmail.getText().toString(), etCode.getText().toString() + etPhone.getText().toString()).
                            enqueue(new retrofit2.Callback<PostResponse>() {
                                @Override
                                public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                                    if (response.body() != null) {
                                        if (response.body().isStatus()) {
                                            btn.setText(getString(R.string.edit));
                                            etCode.setEnabled(false);
                                            etPhone.setEnabled(false);
                                            etEmail.setEnabled(false);
                                        }
                                        Utils.showShortToast(OtpActivity.this, response.body().getMessage());

                                    } else {
                                        Utils.showServerError(OtpActivity.this);
                                    }
                                    hideProgressBar();

                                }

                                @Override
                                public void onFailure(Call<PostResponse> call, Throwable t) {
                                    hideProgressBar();
                                    Utils.showShortToast(OtpActivity.this, t.getMessage());
                                }
                            });

                }
            }

        }
    }

    public boolean isValid() {
        if (TextUtils.isEmpty(etOtp.getText().toString().trim())) {
            Utils.setEditTextError(etOtp, getResources().getString(R.string.otp_required_error));
            return false;
        } else if (etOtp.getText().toString().trim().length() != 4) {
            Utils.setEditTextError(etOtp, getResources().getString(R.string.otp_length_error));
            return false;
        }
        return true;
    }

    public boolean isValidEdit() {

        if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
            Utils.setEditTextError(etEmail, getString(R.string.user_email_error));
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches()) {
            Utils.setEditTextError(etEmail, getString(R.string.user_email_pattern_error));
            return false;
        } else if (TextUtils.isEmpty(etCode.getText().toString().trim())) {
            Utils.showShortToast(getApplicationContext(), getString(R.string.validationCode));
            return false;
        } else if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
            Utils.setEditTextError(etPhone, getString(R.string.validationContact));
            return false;
        } else if (etPhone.getText().toString().trim().length() < 10) {
            Utils.setEditTextError(etPhone, getString(R.string.validationValidContact));
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed()
    {
        Intent i=new Intent(OtpActivity.this, Login.class);
        startActivity(i);
    }
}
