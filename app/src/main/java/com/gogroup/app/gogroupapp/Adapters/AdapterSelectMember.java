package com.gogroup.app.gogroupapp.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.CallBackListeners.CallBackOnLoadMore;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.UserResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zabius on 7/27/17.
 */

public class AdapterSelectMember extends RecyclerView.Adapter {

    List<UserResponse> list = new ArrayList<>();
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private boolean isLoading;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private CallBackOnLoadMore onLoadMoreListener;
    Activity activity;
    boolean isCreatedGroupList;

    public AdapterSelectMember() {
    }

    public void setOnLoadMoreListener(CallBackOnLoadMore mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    public void setData(RecyclerView recyclerView, List<UserResponse> list, Activity activity, boolean isCreatedGroupList) {
        this.list.addAll(list);
        this.activity = activity;
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

    public void filterList(List<UserResponse> list)
    {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(R.layout.card_select_member, parent, false);
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
            final UserResponse item = list.get(position);
            myViewHolder.tvName.setText(item.getName() != null ? item.getName() : "N/A");
            myViewHolder.tvEmail.setText(item.getEmail() != null ? item.getEmail() : "N/A");
            myViewHolder.tvLocation.setText(item.getLocation() != null ? item.getLocation() : "N/A");
            if (item.getProfileImage() != null && !item.getProfileImage().trim().equals("")) {
                Picasso.with(activity).load(item.getProfileImage()).into(myViewHolder.imgProfile);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(activity, ConversationActivity.class);
//                    intent.putExtra(ConversationUIService.TAKE_ORDER, true);
//
//                    intent.putExtra(ConversationUIService.USER_ID, item.getUserId());
//                    intent.putExtra(ConversationUIService.DISPLAY_NAME, item.getName());
//                    activity.startActivity(intent);
//                    activity.finish();
                }
            });


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
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvEmail)
        TextView tvEmail;
        @BindView(R.id.tvLocation)
        TextView tvLocation;

        @BindView(R.id.imgProfile)
        CircleImageView imgProfile;


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
