package com.gogroup.app.gogroupapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.CallBackListeners.CallBackOnLoadMore;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.NotificationResponse;
import com.gogroup.app.gogroupapp.Responses.UserResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zabius on 7/27/17.
 */

public class AdapterNotification extends RecyclerView.Adapter {

    List<NotificationResponse> list = new ArrayList<>();
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private boolean isLoading;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private CallBackOnLoadMore onLoadMoreListener;
    Activity activity;
    boolean isCreatedGroupList;

    public AdapterNotification(Activity context) {
        activity = context;
    }

    public void setOnLoadMoreListener(CallBackOnLoadMore mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    public void setData(RecyclerView recyclerView, List<NotificationResponse> list) {
        this.list = list;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
        this.isCreatedGroupList = isCreatedGroupList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(R.layout.card_notification, parent, false);
            return new MyViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity).inflate(R.layout.card_load_more, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            final NotificationResponse item = list.get(position);

            String userType = UserPreferences.getInstance().getUserType().equalsIgnoreCase("user") ? "GROUP" : "Ad";
            String title = "N/A";
            String desc1, desc2 = " by GoGroup Admin.", description = "N/A";
            String hasBeen = " has been";

            if (userType.toLowerCase().equals("group")) {
                desc1 = "Your " + item.getGroupName() + " group";
            } else {
                desc1 = "Your Ad created on " + item.getCreatedDate();
            }

            if (item.getNotify() != null) {
                switch (item.getNotify().toLowerCase()) {
                    case "expired":
                        myViewHolder.imgProfile.setImageResource(R.drawable.ic_cross);
                        myViewHolder.layoutDesc.setBackgroundColor(ContextCompat.getColor(activity, R.color.grayNew));
                        title = userType + " " + activity.getString(R.string.expiring).toUpperCase();
                        description = desc1 + " is expiring tomorrow.";
                        break;
                    case "approved":
                        myViewHolder.imgProfile.setImageResource(R.drawable.ic_tick);
                        myViewHolder.layoutDesc.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
                        title = userType + " " + activity.getString(R.string.approved).toUpperCase();
                        description = desc1 + hasBeen + " approved" + desc2;
                        break;
                    case "rejected":
                        myViewHolder.imgProfile.setImageResource(R.drawable.ic_cross);
                        myViewHolder.layoutDesc.setBackgroundColor(ContextCompat.getColor(activity, R.color.grayNew));
                        title = userType + " " + activity.getString(R.string.rejected).toUpperCase();
                        description = desc1 + hasBeen + " rejected" + desc2;
                        break;
                    case "report abuse":
                        myViewHolder.imgProfile.setImageResource(R.drawable.ic_cross);
                        myViewHolder.layoutDesc.setBackgroundColor(ContextCompat.getColor(activity, R.color.grayNew));
                        title = " " + activity.getString(R.string.reportAbuse).toUpperCase();
                        description = desc1 + hasBeen + " reported" + desc2 + " Kindly check your mail for more information.";
                        break;
                    case "enable":
                        myViewHolder.imgProfile.setImageResource(R.drawable.ic_tick);
                        myViewHolder.layoutDesc.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
                        title = userType + " " + activity.getString(R.string.enabled).toUpperCase();
                        description = desc1 + hasBeen + " enabled" + desc2 + "";
                        break;
                    case "disable":
                        myViewHolder.imgProfile.setImageResource(R.drawable.ic_cross);
                        myViewHolder.layoutDesc.setBackgroundColor(ContextCompat.getColor(activity, R.color.grayNew));
                        title = userType + " " + activity.getString(R.string.disabled).toUpperCase();
                        description = desc1 + hasBeen + " disabled" + desc2;
                        break;
                    default:
                        myViewHolder.imgProfile.setImageResource(R.drawable.app_icon);
                        myViewHolder.layoutDesc.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
                        title = "" + item.getNotify().toUpperCase();

                }
            }

            myViewHolder.tvTitle.setText(title);
            myViewHolder.tvDate.setText("" + item.getNotifyDate());
            myViewHolder.tvDesc.setText(description);

//            myViewHolder.tvName.setText(item.getName() != null ? item.getName() : "N/A");
//            myViewHolder.tvEmail.setText(item.getEmail() != null ? item.getEmail() : "N/A");
//            myViewHolder.tvLocation.setText(item.getLocation() != null ? item.getLocation() : "N/A");
//            if (item.getProfileImage() != null && item.getProfileImage().trim() != "") {
//                Picasso.with(activity).load(item.getProfileImage()).into(myViewHolder.imgProfile);
//            }
//
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(activity, ConversationActivity.class);
//                    intent.putExtra(ConversationUIService.TAKE_ORDER, true);
//
//                    intent.putExtra(ConversationUIService.USER_ID, item.getUserId());
//                    intent.putExtra(ConversationUIService.DISPLAY_NAME, item.getName());
//                    activity.startActivity(intent);
//                    activity.finish();
//                }
//            });


        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvDesc)
        TextView tvDesc;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.layoutDesc)
        RelativeLayout layoutDesc;

        @BindView(R.id.imgProfile)
        ImageView imgProfile;


        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.progressBar1)
        ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
