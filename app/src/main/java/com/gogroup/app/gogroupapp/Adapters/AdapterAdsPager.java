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
import com.gogroup.app.gogroupapp.Seller.SellerAddDetailActivity;
import com.gogroup.app.gogroupapp.User.CategoryDetailUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AdapterAdsPager extends RecyclerView.Adapter {
    List<ListResponse> list = new ArrayList<>();
    Context context;
    boolean isLoader;
    CallBackRefresh callBackRefresh;

    public AdapterAdsPager(Context context, List<ListResponse> list) {
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_ads_pager, parent, false);
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

            myViewHolder.tvDealName.setText(item.getAdvertisementName());
            myViewHolder.tvName.setText(item.getName());
            myViewHolder.tvDate.setText("Expiring on "+Utils.convertDateFormat(item.getEndDate(),Utils.dateFormat1));
            myViewHolder.tvActualPrice.setText(item.getActualPrice());
            myViewHolder.tvOfferPrice.setText(item.getOfferPrice());

            Utils.picasso(context,item.getImage(),myViewHolder.imgAdvertise);

            myViewHolder.view.setVisibility(position==list.size()-1?View.GONE:View.VISIBLE);


            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SellerAddDetailActivity.class);
                    intent.putExtra(SellerAddDetailActivity.DATA, item.getAdvertisementId());
                    context.startActivity(intent);

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

        @BindView(R.id.tvDealName)
        TextView tvDealName;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvActualPrice)
        TextView tvActualPrice;
        @BindView(R.id.tvOfferPrice)
        TextView tvOfferPrice;
        @BindView(R.id.imgAdvertise)
        ImageView imgAdvertise;
        @BindView(R.id.view)
        View view;

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