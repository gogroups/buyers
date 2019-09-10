package com.gogroup.app.gogroupapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gogroup.app.gogroupapp.CallBackListeners.CallBackApi;
import com.gogroup.app.gogroupapp.CallBackListeners.CallBackRefresh;
import com.gogroup.app.gogroupapp.HelperClasses.ApiCalls;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.ListResponse;
import com.gogroup.app.gogroupapp.Responses.PostResponse;
import com.gogroup.app.gogroupapp.Seller.BuyerActivity;
import com.gogroup.app.gogroupapp.Seller.SellerAddDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;


public class AdapterFeeds extends RecyclerView.Adapter {
    List<ListResponse> list = new ArrayList<>();
    Context context;
    boolean isLoader;

    public AdapterFeeds(Context context, List<ListResponse> list) {
        this.list = list;
        this.context = context;
        this.isLoader = list.size() == 10;
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_feed, parent, false);
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
            final ListResponse item = list.get(position);


            switch ("" + item.getStatus().trim()) {
                case "pending":
                    holder.tvStatus.setText(Utils.fromHtml("<b><font color='" + ContextCompat.getColor(context, R.color.red_color) + "'>Coupon generated :</font></b> " + item.getBuyerName() + " has generated coupon for your offer <u><font color='" + ContextCompat.getColor(context, R.color.blue2) + "'>" + item.getAdvertisementName() + "</font></u>"));
                    break;
                case "orderPlaced":
                    holder.tvStatus.setText(Utils.fromHtml("<b><font color='" + ContextCompat.getColor(context, R.color.red_color) + "'>Order placed :</font></b> " + Utils.fromHtml(item.getBuyerName() + " has placed an order for your offer <u><font color='" + ContextCompat.getColor(context, R.color.blue2) + "'>" + item.getAdvertisementName() + "</font></u>")));
                    break;
                case "purchased":
                    holder.tvStatus.setText(Utils.fromHtml("<b><font color='" + ContextCompat.getColor(context, R.color.red_color) + "'>Order purchased :</font></b> " + item.getBuyerName() + " has purchased your offer <u><font color='" + ContextCompat.getColor(context, R.color.blue2) + "'>" + item.getAdvertisementName() + "</font></u>"));
                    break;
                default:
                    holder.tvStatus.setText("N/A");
                    break;
            }

            holder.tvPlacedOn.setText(item.getOrderPlacedDate() != null ? item.getOrderPlacedDate() : "N/A");
            holder.tvPurchasedOn.setText(item.getPurchasedDate() != null ? item.getPurchasedDate() : "N/A");
            holder.tvPrice.setText(item.getOfferPrice());
            holder.tvOrderRefNo.setText(item.getOrderRefId());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SellerAddDetailActivity.class);
                    intent.putExtra(SellerAddDetailActivity.DATA, item.getAdvertisementId());
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

        @BindView(R.id.tvStatus)
        TextView tvStatus;
        @BindView(R.id.tvPlacedOn)
        TextView tvPlacedOn;
        @BindView(R.id.tvPurchasedOn)
        TextView tvPurchasedOn;
        @BindView(R.id.tvPrice)
        TextView tvPrice;
        //        @BindView(R.id.tvDesc)
//        TextView tvDesc;
        @BindView(R.id.tvOrderRefNo)
        TextView tvOrderRefNo;

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