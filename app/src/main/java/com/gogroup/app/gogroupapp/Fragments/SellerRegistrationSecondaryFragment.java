package com.gogroup.app.gogroupapp.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
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
import com.gogroup.app.gogroupapp.SellerRegistration;
import com.gogroup.app.gogroupapp.Responses.RegisterResponse;
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
public class SellerRegistrationSecondaryFragment extends Fragment {

    Context context;
    TextView registration_seller_name_icon, registration_seller_contact, registration_seller_email;

    public SellerRegistrationSecondaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_registration_secondary_contact, container, false);
        ButterKnife.bind(this, view);
        initIds(view);
        setIcons();
        return view;
    }

    private void setIcons() {
        registration_seller_name_icon.setText(R.string.icon_ionicon_var_ios_person_outline);
        registration_seller_name_icon.setTypeface(FontManager.getTypeFaceFromFontName(getActivity(), FontManager.IONICONFONT));
        registration_seller_name_icon.setTextSize(20);
        registration_seller_name_icon.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_color));

        registration_seller_email.setText(R.string.icon_ionicon_var_ios_email_outline);
        registration_seller_email.setTypeface(FontManager.getTypeFaceFromFontName(getActivity(), FontManager.IONICONFONT));
        registration_seller_email.setTextSize(20);
        registration_seller_email.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_color));

        registration_seller_contact.setText(R.string.icon_ionicon_var_android_call);
        registration_seller_contact.setTypeface(FontManager.getTypeFaceFromFontName(getActivity(), FontManager.IONICONFONT));
        registration_seller_contact.setTextSize(20);
        registration_seller_contact.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_color));

    }

    private void initIds(View view) {
        registration_seller_name_icon = view.findViewById(R.id.registration_seller_name_icon);
        registration_seller_contact = view.findViewById(R.id.registration_seller_contact);
        registration_seller_email = view.findViewById(R.id.registration_seller_email);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @OnClick(R.id.btnSignUp)
    void signUp() {

        if(isValid())
        {
            SellerRegistration.postFields.put("secondary_name", Utils.requestBody(etName.getText().toString().trim()));
            SellerRegistration.postFields.put("secondary_contact", Utils.requestBody(etContact.getText().toString().trim()));
            SellerRegistration.postFields.put("secondary_email", Utils.requestBody(etEmail.getText().toString().trim()));
            ViewPager viewPager = getActivity().findViewById(R.id.viewpager);
            viewPager.setCurrentItem(2);
        }


//        if (isValid()) {
//            SellerRegistration.postFields.put("secondary_name", Utils.requestBody(etName.getText().toString().trim()));
//            SellerRegistration.postFields.put("secondary_contact", Utils.requestBody(etContact.getText().toString().trim()));
//            SellerRegistration.postFields.put("secondary_email", Utils.requestBody(etEmail.getText().toString().trim()));
//            if (Utils.isInterNetConnected(getActivity())) {
//                ((BaseActivity) getActivity()).showProgressbar();
//                RestClient.get().registerSeller(((SellerRegistration) getActivity()).multipartImg, ((SellerRegistration) getActivity()).postFields).
//                        enqueue(new Callback<RegisterResponse>() {
//                            @Override
//                            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
//                                if (response.body() != null) {
//                                    if (response.body().isSuccess()) {
//                                        Intent i = new Intent(getActivity(), OtpActivity.class);
//                                        i.putExtra(OtpActivity.DATA, response.body().getData());
//                                        ((SellerRegistration) getActivity()).startActivityForResult(i, 7);
//                                        Utils.showShortToast(getActivity(), ""+response.body().getMessage());
//
//                                    } else {
//
//                                        try {
//                                            Gson gson = new Gson();
//                                            gson.toJson(response.body().getMessage());
//                                            JSONObject a = new JSONObject(gson.toJson(response.body().getMessage()));
//                                            try {
//                                                Utils.showShortToast(getActivity(), a.getString("contact_number"));
//
//                                            } catch (JSONException e) {
//
//                                            }
//                                            try {
//                                                Utils.showShortToast(getActivity(), a.getString("email"));
//
//                                            } catch (JSONException e) {
//
//                                            }
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                    }
//
//                                } else {
//                                    Utils.showServerError(getActivity());
//                                }
//                                ((BaseActivity) getActivity()).hideProgressBar();
//
//
//                            }
//
//                            @Override
//                            public void onFailure(Call<RegisterResponse> call, Throwable t) {
//                                ((BaseActivity) getActivity()).hideProgressBar();
//                                Utils.showShortToast(getActivity(), t.getMessage());
//                            }
//                        });
//
//            }
//        }

    }


    private boolean isValid() {
        if (etContact.getText().toString().trim().length() > 0 && etContact.getText().toString().trim().length() < 10) {
            Utils.setEditTextError(etContact, getString(R.string.validationValidContact));
            return false;
        } else if (etEmail.getText().toString().trim().length() > 0 && !android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
            Utils.setEditTextError(etEmail, getString(R.string.validationValidEmail));
            return false;
        }
        return true;
    }

    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etContact)
    EditText etContact;
    @BindView(R.id.etEmail)
    EditText etEmail;
}
