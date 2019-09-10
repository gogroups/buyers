package com.gogroup.app.gogroupapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gogroup.app.gogroupapp.CallBackListeners.CallBackApi;
import com.gogroup.app.gogroupapp.CallBackListeners.CallBackOnAddressConfirm;
import com.gogroup.app.gogroupapp.CallBackListeners.CallBackRefresh;
import com.gogroup.app.gogroupapp.Fragments.AddressDialog;
import com.gogroup.app.gogroupapp.HelperClasses.ApiCalls;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.ListResponse;
import com.gogroup.app.gogroupapp.Responses.PostResponse;
import com.gogroup.app.gogroupapp.Seller.SellerAddDetailActivity;
import com.gogroup.app.gogroupapp.User.MyPurchases;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;


public class AdapterTotalCashback extends RecyclerView.Adapter {
    List<ListResponse> list = new ArrayList<>();
    Context context;
    boolean isLoader;
    CallBackRefresh callBackRefresh;
    ListResponse selectedOrder;
    String formattedDate;
    public int quant=1;

    public AdapterTotalCashback(Context context, List<ListResponse> list, CallBackRefresh callBackRefresh) {
        this.list = list;
        this.context = context;
        this.isLoader = list.size() == 10;
        this.callBackRefresh = callBackRefresh;

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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_my_purchase, parent, false);
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

            if (item.getCouponStatus().equalsIgnoreCase("purchased")) {

                holder.tvAddName.setText(item.getAdvertisementName());
                holder.tvSellerName.setText(item.getSellerName());
                holder.tvLocation.setText(item.getSellerLocation());
                holder.tvTotalPurchased.setText(item.getPurchasedCount());
                holder.tvCouponCode.setText(item.getCouponCode());
                holder.tvSequenceNo.setText(item.getSequenceNo());
                holder.tvOrderRefNo.setText(item.getOrderRefId() != null ? item.getOrderRefId() : "N/A");

//          holder.layoutOrder.setVisibility(item.getCouponStatus()!=null&&!item.getCouponStatus().equalsIgnoreCase("pending")?View.VISIBLE:View.GONE);
                holder.tvActualPrice.setText(item.getActualPrice());
                holder.tvOfferPrice.setText(item.getOfferPrice());
                holder.tvPurchase.setVisibility(View.GONE);
                if (item.getImageList().size() > 0) {
                    Utils.picasso(context, item.getImageList().get(0).getImagePath(), holder.imgAdd);
                }
            }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
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

        @BindView(R.id.tvAddName)
        TextView tvAddName;
        @BindView(R.id.tvCouponCode)
        TextView tvCouponCode;
        @BindView(R.id.tvSellerName)
        TextView tvSellerName;
        @BindView(R.id.tvLocation)
        TextView tvLocation;
        @BindView(R.id.tvOfferPrice)
        TextView tvOfferPrice;
        @BindView(R.id.tvActualPrice)
        TextView tvActualPrice;
        @BindView(R.id.tvStatus)
        TextView tvStatus;
        @BindView(R.id.imgAdd)
        ImageView imgAdd;
        @BindView(R.id.tvPurchase)
        TextView tvPurchase;
        @BindView(R.id.tvTotalPurchased)
        TextView tvTotalPurchased;
        @BindView(R.id.tvSequenceNo)
        TextView tvSequenceNo;
        @BindView(R.id.tvOrderRefNo)
        TextView tvOrderRefNo;
        @BindView(R.id.layoutOrder)
        LinearLayout layoutOrder;


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