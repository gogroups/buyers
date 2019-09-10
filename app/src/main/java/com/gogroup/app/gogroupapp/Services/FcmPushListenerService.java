package com.gogroup.app.gogroupapp.Services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by zabius on 11/20/17.
 */

public class FcmPushListenerService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";

    @Override
    public void onTokenRefresh() {
//        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("deviceToken", "Refreshed token: " + refreshedToken);


        //sendTokenToServer();
    }


}