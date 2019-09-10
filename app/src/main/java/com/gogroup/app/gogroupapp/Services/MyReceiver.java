package com.gogroup.app.gogroupapp.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.gogroup.app.gogroupapp.HelperClasses.RestClient;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.Responses.PostResponse;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by atinderpalsingh on 12/13/17.
 */

public class MyReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
    }

//
//     if (Utils.isInterNetConnected(activity)) {
//
//        // ((BaseActivity) activity).showProgressbar();
//        RestClient.get().markFavouriteAds(UserPreferences.getInstance().getToken(), status, advertisementId)
//                .enqueue(new retrofit2.Callback<PostResponse>() {
//                    @Override
//                    public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
//                        if (response.body() != null) {
//                            cb.setAlpha(1f);
//                            if (response.body().isStatus()) {
//                                if (isFavorite) {
//                                    list.remove(item);
//                                    notifyDataSetChanged();
//                                }
//                                Utils.showShortToast(activity, response.body().getMessage());
//                                cb.setChecked(status == 0 ? false : true);
//                                item.setFavourite(status);
//                            } else {
//                                Utils.showShortToast(activity, response.body().getMessage());
//                            }
//
//                        } else {
//                            Utils.showServerError(activity);
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(final Call<PostResponse> call, Throwable t) {
//                        cb.setAlpha(1f);
//                        Utils.showShortToast(activity, t.getMessage());
//
//                    }
//                });
//
//    }
}
