package com.gogroup.app.gogroupapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.CallBackListeners.CallBackRefresh;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.ListResponse;
import com.gogroup.app.gogroupapp.User.CategoryDetailUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AdapterCategoryNew extends RecyclerView.Adapter {
    List<ListResponse> list = new ArrayList<>();
    Context context;
    boolean isLoader;
    CallBackRefresh callBackRefresh;

    public AdapterCategoryNew(Context context, List<ListResponse> list) {
        this.list = list;
        this.context = context;
//        this.isLoader = list.size() == 10;
//        this.callBackRefresh = callBackRefresh;

    }

    public void addData(List<ListResponse> list, boolean isLoader) {
        this.list.addAll(list);
        this.isLoader = isLoader;
        notifyDataSetChanged();
    }


    public void removeLoadingView() {
        isLoader = false;
        notifyItemChanged(list.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == BaseActivity.VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user_dashboard_new, parent, false);
            return new ItemHolder(view);
        } else if (viewType == BaseActivity.VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_load_more, parent, false);
            return new LoadingHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder myHolder, final int position) {

        if (myHolder instanceof ItemHolder) {
            final ItemHolder holder = (ItemHolder) myHolder;
            ItemHolder myViewHolder = (ItemHolder) holder;
            final ListResponse item = list.get(position);
            myViewHolder.tvGroupNum.setText(context.getString(R.string.Groups)+" " + (item.getGroupCount()));
            myViewHolder.tvCategoryName.setText(item.getCategory1() != null ? item.getCategory1() : "");
            myViewHolder.tvPost.setText(context.getString(R.string.offers)+" " + item.getPosts());

            Utils.picasso(context,item.getImages(),myViewHolder.imgProfile);

            if (item.getGroupIdArr() != null) {
                for (int i = 0; i < item.getGroupIdArr().length; i++) {
                    UserPreferences.getInstance().setCategoryId(item.getGroupIdArr()[i], item.getCategoryId());
                }
            }

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CategoryDetailUser.class);
                    intent.putExtra(CategoryDetailUser.DATA, item.getCategoryId());
                    intent.putExtra(CategoryDetailUser.CATEGORY_NAME, item.getCategory1() != null ? item.getCategory1() : "");
                    ((AppCompatActivity) context).startActivityForResult(intent, BaseActivity.REFRESH_DASHBOARD);

                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return isLoader ? list.size() + 1 : list.size();

    }


    @Override
    public int getItemViewType(int position) {
        return list.size() == position ? BaseActivity.VIEW_TYPE_LOADING : BaseActivity.VIEW_TYPE_ITEM;

    }

    class ItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvGroupNum)
        TextView tvGroupNum;
        @BindView(R.id.tvPost)
        TextView tvPost;
        @BindView(R.id.tvCategoryName)
        TextView tvCategoryName;
        @BindView(R.id.imgProfile)
        ImageView imgProfile;

        public ItemHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);


        }
    }

    class LoadingHolder extends RecyclerView.ViewHolder {

        public LoadingHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}