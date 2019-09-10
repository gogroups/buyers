package com.gogroup.app.gogroupapp.Services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.GroupListResponse;
import com.gogroup.app.gogroupapp.Seller.SellerAddDetailActivity;
import com.gogroup.app.gogroupapp.Seller.SellerDashboard;
import com.gogroup.app.gogroupapp.Seller.SellerFeed;
import com.gogroup.app.gogroupapp.SplashActivity;
import com.gogroup.app.gogroupapp.User.GroupDetails;
import com.gogroup.app.gogroupapp.User.JoinedGroupDetail;
import com.gogroup.app.gogroupapp.User.JoinedGroupDetails;
import com.gogroup.app.gogroupapp.User.MyPurchases;
import com.gogroup.app.gogroupapp.User.UserDashboard;
import com.gogroup.app.gogroupapp.User.UserDashboardNew;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rohit.singh on 07/03/17.
 */

public class FireMsgService extends FirebaseMessagingService {

    GroupListResponse body;
    int notifyId = 2891;

    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        body = new GroupListResponse();
        Log.e("Msg", "Messagereceived [" + remoteMessage.getData() + "]");


        JSONObject jsonObject = new JSONObject(remoteMessage.getData());

        try {
            body.setDescription(jsonObject.getString("description"));
            body.setUserId(jsonObject.getString("user_id"));
            body.setTitle(jsonObject.getString("title"));
            body.setType(jsonObject.getString("type"));
            body.setNotificationType(jsonObject.getString("notificationType"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            body.setGroupId(jsonObject.getString("group_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            body.setAdvertisementId(jsonObject.getString("advertisement_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            body.setCategoryId(jsonObject.getString("category_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (body == null) {
            return;
        }
        NotificationChannel mChannel;
        Notification.Builder mBuilder;
        NotificationManager mNotifyManager;
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new Notification.Builder(this);
        mBuilder.setContentTitle("" + body.getTitle())
                .setContentText("" + body.getDescription())
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.app_icon)
                .setContentIntent(getPendingIntent())
//                .setOngoing(true)
        ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setChannelId("" + notifyId);
            mChannel = new NotificationChannel("" + notifyId, remoteMessage.getNotification().getBody(), NotificationManager.IMPORTANCE_LOW);
            mNotifyManager.createNotificationChannel(mChannel);
        }

        if(UserPreferences.getInstance().isLogin()) {
            mNotifyManager.notify(notifyId, mBuilder.build());
        }
    }


    private PendingIntent getPendingIntent() {
        Class detailClass = null;
        final Bundle bundle = new Bundle();
        try {
            switch ("" + body.getType()) {
                case "user":

                    switch ("" + body.getNotificationType().trim()) {
                        case "myPurchase":
                            detailClass = MyPurchases.class;
                            break;
                        default:
                            if (body.getCategoryId() != null && body.getGroupId() != null) {
                                bundle.putString(JoinedGroupDetails.CATEGORY_ID, body.getCategoryId());
                                bundle.putString(JoinedGroupDetails.GROUP_ID, body.getGroupId());
                                detailClass = JoinedGroupDetails.class;
                            } else {
                                detailClass = UserDashboardNew.class;
                            }
                            break;
                    }
                    break;
                case "seller":
                    switch ("" + body.getNotificationType().trim()) {
                        case "myOrders":
                            detailClass = SellerFeed.class;
                            break;
                        case "offer":
                            detailClass = SellerAddDetailActivity.class;
                            bundle.putString(SellerAddDetailActivity.DATA, body.getAdvertisementId());
                            break;
                        default:
                            detailClass = SellerDashboard.class;
                            break;
                    }
                    break;
                default:
                    detailClass = SplashActivity.class;
                    break;
            }
        } catch (Exception e) {
        }
        Intent intent = new Intent(this, detailClass);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return PendingIntent.getActivity(this, notifyId, intent, PendingIntent.FLAG_ONE_SHOT);
    }
//[{description=Manpreet has generated coupon code 28ed4cc for  your advertisement - New AUG Deal, advertisement_id=29, user_id=1,
//            type=seller, title=Coupon Code Generated, category_id=1}]


}

