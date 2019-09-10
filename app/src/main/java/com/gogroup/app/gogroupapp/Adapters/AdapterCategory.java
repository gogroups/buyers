package com.gogroup.app.gogroupapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.CallBackListeners.CallBackOnLoadMore;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.CategoryResponse;
import com.gogroup.app.gogroupapp.User.CategoryDetailUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zabius on 7/27/17.
 */

public class AdapterCategory extends RecyclerView.Adapter {
    List<CategoryResponse> list = new ArrayList<>();
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private boolean isLoading;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private CallBackOnLoadMore onLoadMoreListener;

    public void setOnLoadMoreListener(CallBackOnLoadMore mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    Context activity;

    public AdapterCategory() {
    }

    public void setData(RecyclerView recyclerView, List<CategoryResponse> list, Context activity) {
        this.list = list;
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
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(R.layout.card_user_dashboard, parent, false);
            return new MyViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity).inflate(R.layout.card_load_more, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            final CategoryResponse item = list.get(position);
            myViewHolder.tvGroupNum.setText((item.getGroupCount() != null ? item.getGroupCount() : "0") + " Group(s)");
            myViewHolder.tvCategoryName.setText(item.getCategoryTitle() != null ? item.getCategoryTitle() : "");
            myViewHolder.tvPost.setText(item.getPosts() != null ? item.getPosts() : "");
            if (item.getImage() != null && item.getImage().trim() != "") {
                Picasso.with(activity).load(item.getImage()).into(myViewHolder.imgProfile);
            }

            if (item.getGroupIDs() != null) {
                for (int i = 0; i < item.getGroupIDs().length; i++) {
                    UserPreferences.getInstance().setCategoryId(item.getGroupIDs()[i], item.getCategoryId());
                }
            }

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, CategoryDetailUser.class);
                    intent.putExtra(CategoryDetailUser.DATA, item.getCategoryId());
                    intent.putExtra(CategoryDetailUser.CATEGORY_NAME, item.getCategoryTitle() != null ? item.getCategoryTitle() : "");
                    ((AppCompatActivity) activity).startActivityForResult(intent, BaseActivity.REFRESH_DASHBOARD);

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
        @BindView(R.id.tvGroupNum)
        TextView tvGroupNum;
        @BindView(R.id.tvPost)
        TextView tvPost;
        @BindView(R.id.tvCategoryName)
        TextView tvCategoryName;
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
