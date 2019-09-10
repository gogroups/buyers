package com.gogroup.app.gogroupapp.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.FontManager;
import com.gogroup.app.gogroupapp.HelperClasses.RestClient;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.OtpActivity;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.RegisterResponse;
import com.gogroup.app.gogroupapp.SellerRegistration;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerRegistrationAccountFragment extends Fragment {


    public SellerRegistrationAccountFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_details, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.btnSignUp)
    void signUp() {
        if (isValid()) {
            SellerRegistration.postFields.put("account_number", Utils.requestBody(etAccountNum.getText().toString().trim()));
            SellerRegistration.postFields.put("ac_holder_name", Utils.requestBody(etHolderName.getText().toString().trim()));
            SellerRegistration.postFields.put("ifsc", Utils.requestBody(etIfsc.getText().toString().trim()));
            SellerRegistration.postFields.put("bank_name", Utils.requestBody(etBankName.getText().toString().trim()));
            SellerRegistration.postFields.put("paytm_no", Utils.requestBody(etPaytmNum.getText().toString().trim()));
            if (Utils.isInterNetConnected(getActivity())) {
                ((BaseActivity) getActivity()).showProgressbar();
                RestClient.get().registerSeller(((SellerRegistration) getActivity()).multipartImg, ((SellerRegistration) getActivity()).postFields).
                        enqueue(new Callback<RegisterResponse>() {
                            @Override
                            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                                if (response.body() != null) {
                                    if (response.body().isSuccess()) {
                                        Intent i = new Intent(getActivity(), OtpActivity.class);
                                        i.putExtra(OtpActivity.DATA, response.body().getData());
                                        ((SellerRegistration) getActivity()).startActivityForResult(i, 7);
                                        Utils.showShortToast(getActivity(), "" + response.body().getMessage());

                                    } else {

                                        try {
                                            Gson gson = new Gson();
                                            gson.toJson(response.body().getMessage());
                                            JSONObject a = new JSONObject(gson.toJson(response.body().getMessage()));
                                            try {
                                                Utils.showShortToast(getActivity(), a.getString("contact_number"));

                                            } catch (JSONException e) {

                                            }
                                            try {
                                                Utils.showShortToast(getActivity(), a.getString("email"));

                                            } catch (JSONException e) {

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                } else {
                                    Utils.showServerError(getActivity());
                                }
                                ((BaseActivity) getActivity()).hideProgressBar();


                            }

                            @Override
                            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                                ((BaseActivity) getActivity()).hideProgressBar();
                                Utils.showShortToast(getActivity(), t.getMessage());
                            }
                        });

            }
        }

    }

    void clearAccountDetail() {
        etAccountNum.setText("");
        etBankName.setText("");
        etIfsc.setText("");
        etHolderName.setText("");
        etAccountNum.setError(null);
        etBankName.setError(null);
        etIfsc.setError(null);
        etHolderName.setError(null);

    }
    private boolean isValid() {
            if (etAccountNum.getText().toString().trim().length() == 0) {
                if (etPaytmNum.getText().toString().trim().length() == 0) {
                    Utils.setEditTextError(etAccountNum, getString(R.string.validationAccountNum));
                    return false;
                } else {
                    clearAccountDetail();
                    return true;
                }
            } else if (etHolderName.getText().toString().trim().length() == 0) {
                Utils.setEditTextError(etHolderName, getString(R.string.validationHolderName));
                return false;
            } else if (etIfsc.getText().toString().trim().length() == 0) {
                Utils.setEditTextError(etIfsc, getString(R.string.validationIfsc));
                return false;
            } else if (etBankName.getText().toString().trim().length() == 0) {
                Utils.setEditTextError(etBankName, getString(R.string.validationBankName));
                return false;
            }

            return true;
    }

    @BindView(R.id.etAccountNum)
    EditText etAccountNum;
    @BindView(R.id.etHolderName)
    EditText etHolderName;
    @BindView(R.id.etIfsc)
    EditText etIfsc;
    @BindView(R.id.etBankName)
    EditText etBankName;
    @BindView(R.id.etPaytmNumber)
    EditText etPaytmNum;
}
