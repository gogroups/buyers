package com.gogroup.app.gogroupapp.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.Adapters.AdapterNotification;
import com.gogroup.app.gogroupapp.Adapters.GooglePlacesAutocompleteAdapter;
import com.gogroup.app.gogroupapp.CallBackListeners.CallBackOnAddressConfirm;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.NotificationResponse;
import com.gogroup.app.gogroupapp.UserRegistration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by atinderpalsingh on 12/13/17.
 */

public class AddressDialog extends AppCompatDialogFragment {

    static CallBackOnAddressConfirm listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_address, container);
        ButterKnife.bind(this, view);

        etCustomAddress.setText(UserPreferences.getInstance().getCustomAddress());
        etLocation.setText(UserPreferences.getInstance().getLocation());

        Utils.disableEditTextFocus(etLocation);
        Utils.disableEditTextFocus(etCustomAddress);

        return view;
    }

    public static AddressDialog getInstance(CallBackOnAddressConfirm callBackOnAddressConfirm) {
        listener = callBackOnAddressConfirm;
        return new AddressDialog();
    }

    @BindView(R.id.etCustomAddress)
    EditText etCustomAddress;
    @BindView(R.id.etLocation)
    AutoCompleteTextView etLocation;


    @OnClick(R.id.tvChange)
    void editAddress() {
        etLocation.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.spinner_item));
        Utils.enableEditTextFocus(etLocation);
        Utils.enableEditTextFocus(etCustomAddress);
    }

    @OnClick(R.id.tvConfirm)
    void confirm() {
        if (isValid()) {
            getDialog().dismiss();
            listener.onAddressConfirm(etCustomAddress.getText().toString(), etLocation.getText().toString());
        }
    }


    @OnClick(R.id.tvCancel)
    void close() {
        getDialog().dismiss();
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }


    public boolean isValid() {
        if (etCustomAddress.getText().toString().trim().length() == 0) {
            Utils.setEditTextError(etCustomAddress, getString(R.string.validationAddress));
            return false;
        } else if (TextUtils.isEmpty(etLocation.getText().toString().trim())) {
            Utils.setEditTextError(etLocation, getString(R.string.validationLocation));
            return false;
        }
        return true;
    }

}
