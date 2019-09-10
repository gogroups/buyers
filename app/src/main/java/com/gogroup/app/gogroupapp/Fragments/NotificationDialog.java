package com.gogroup.app.gogroupapp.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.Adapters.AdapterNotification;
import com.gogroup.app.gogroupapp.HelperClasses.RestClient;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.NotificationResponse;
import com.gogroup.app.gogroupapp.Responses.PostResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by atinderpalsingh on 12/13/17.
 */

public class NotificationDialog extends AppCompatDialogFragment {

    private static NotificationResponse notifications;
    AdapterNotification adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_notification_new, container);
        ButterKnife.bind(this, view);
//        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        tvTitle.setText(getActivity().getString(R.string.notifications));
        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);

        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AdapterNotification(getActivity());
        recycler.setAdapter(adapter);


        if (notifications != null) {
            adapter.setData(recycler, notifications.getData());
            tvEmpty.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            tvEmpty.setVisibility(notifications.getData().size() > 0 ? View.GONE : View.VISIBLE);

        }

        return view;
    }

    public static NotificationDialog getInstance(NotificationResponse notificationResponse) {
        notifications = notificationResponse;
        return new NotificationDialog();

    }
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @OnClick(R.id.btnBack)
    void close() {
        getDialog().dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(),R.color.white)));
        }
    }
}
