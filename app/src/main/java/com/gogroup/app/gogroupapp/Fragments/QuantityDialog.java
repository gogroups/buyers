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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gogroup.app.gogroupapp.Adapters.AdapterNotification;
import com.gogroup.app.gogroupapp.Adapters.GooglePlacesAutocompleteAdapter;
import com.gogroup.app.gogroupapp.CallBackListeners.CallBackOnAddressConfirm;
import com.gogroup.app.gogroupapp.CallBackListeners.CallBackOnQuantityConfirm;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.GroupResponse;
import com.gogroup.app.gogroupapp.Responses.NotificationResponse;
import com.gogroup.app.gogroupapp.Responses.SellerAdvertisementResponse;
import com.gogroup.app.gogroupapp.Seller.SellerAddDetailActivity;
import com.gogroup.app.gogroupapp.UserRegistration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.gogroup.app.gogroupapp.Seller.SellerAddDetailActivity.DATA;

/**
 * Created by atinderpalsingh on 12/13/17.
 */

public class QuantityDialog extends AppCompatDialogFragment {

    static CallBackOnQuantityConfirm listener;
    public static final String DATA = "data";
    public static String quant;
    int initial = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.quantity_dialog, container);
        ButterKnife.bind(this, view);

        SellerAddDetailActivity activity = (SellerAddDetailActivity)getActivity();

        quant= activity.sendItem();

        //SellerAdvertisementResponse item = (SellerAdd).getParentActivityIntent().getParcelableExtra(DATA);



        tvQuantity.setText(""+initial);

        //getItem();

        return view;
    }

   /*public void getItem()
   {
       SellerAdvertisementResponse item = ((SellerAddDetailActivity)getActivity()).sendItem();
       quant = item.getQuantityPerUser();
       tvQuantity.setText(""+quant);
   }*/

    public static QuantityDialog getInstance(CallBackOnQuantityConfirm callBackOnQuantityConfirm) {
        listener = callBackOnQuantityConfirm;
        return new QuantityDialog();
    }


    @BindView(R.id.etQuantity)
    TextView tvQuantity;

    @OnClick(R.id.btnadd)
    void increase()
    {
        int q = Integer.parseInt(quant);
        if(initial+1 <= q)
            initial = initial+1;
        else
            Toast.makeText(getContext(),"Maximum quantity per user is "+quant,Toast.LENGTH_SHORT).show();

        tvQuantity.setText(String.valueOf(initial));

    }

    @OnClick(R.id.btnsubtract)
    void decrease()
    {
        if(initial-1 > 0)
            initial = initial-1;

        tvQuantity.setText(String.valueOf(initial));
    }

    @OnClick(R.id.tvConfirm)
    void confirm()
    {
        String quantity = String.valueOf(initial);
        listener.onQuantityConfirm(quantity);
        getDialog().dismiss();
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



}
