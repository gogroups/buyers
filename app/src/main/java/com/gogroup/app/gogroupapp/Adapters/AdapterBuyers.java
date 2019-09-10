package com.gogroup.app.gogroupapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gogroup.app.gogroupapp.CallBackListeners.CallBackApi;
import com.gogroup.app.gogroupapp.CallBackListeners.CallBackRecyclerItemClick;
import com.gogroup.app.gogroupapp.CallBackListeners.CallBackRefresh;
import com.gogroup.app.gogroupapp.HelperClasses.ApiCalls;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.ListResponse;
import com.gogroup.app.gogroupapp.Responses.PostResponse;
import com.gogroup.app.gogroupapp.Seller.SellerAddDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;


public class AdapterBuyers extends RecyclerView.Adapter {
    List<ListResponse> list = new ArrayList<>();
    Context context;
    boolean isLoader;
    CallBackRefresh callBackRefresh;
    String advertisementId;

    public AdapterBuyers(Context context, List<ListResponse> list, CallBackRefresh callBackRefresh,String advertisementId) {
        this.list = list;
        this.context = context;
        this.isLoader = list.size() == 10;
        this.callBackRefresh = callBackRefresh;
        this.advertisementId=advertisementId;
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_buyer, parent, false);
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
            holder.tvName.setText(item.getName());
            holder.tvContact.setText(item.getContactNumber());
            holder.tvEmail.setText(item.getEmail());
            holder.tvLocation.setText(context.getString(R.string.deliveryAddress)+"     "+item.getAddress()+", "+item.getLocation());
            holder.tvCouponCode.setText(item.getCouponCode());
            holder.tvOrderRefNo.setText(item.getOrderRefId());
            holder.tvSequenceNo.setText(item.getSequenceNo());
            if (!item.getStatus().equalsIgnoreCase("pending")) {
                holder.tvStatus.setText(item.getStatus().equalsIgnoreCase("purchased") ? context.getString(R.string.purchased) : context.getString(R.string.orderPlaced));
            }
            Utils.picasso(context, item.getProfileImage(), holder.imgUser);

            holder.tvValidate.setVisibility(item.getStatus() != null && item.getStatus().equalsIgnoreCase("orderPlaced") ? View.VISIBLE : View.GONE);
            holder.layoutOrder.setVisibility(item.getStatus() != null && !item.getStatus().equalsIgnoreCase("pending") ? View.VISIBLE : View.GONE);

            holder.tvValidate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    validateOffer(item);
                }
            });

//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                    Intent intent = new Intent(context, SellerAddDetailActivity.class);
////                    intent.putExtra(SellerAddDetailActivity.DATA, item.getAdvertisementId());
////                    context.startActivity(intent);
//                }
//            });


        }
    }

    void validateOffer(final ListResponse item) {
        Map<String, String> postFields = new HashMap<>();
        postFields.put("buyer_id", item.getUserId());
        postFields.put("coupon_code", item.getCouponCode());
        postFields.put("advertisement_id", advertisementId);
        ((BaseActivity) context).showProgressbar();
        new ApiCalls(context).apiValidateOffer(postFields, new CallBackApi() {
            @Override
            public void onSuccess(Response response) {
                PostResponse body = (PostResponse) response.body();
                Toast.makeText(context, "" + body.getMessage(), Toast.LENGTH_LONG).show();
                callBackRefresh.onRefresh(item);
                BaseActivity.isRefresh = true;
                ((BaseActivity) context).hideProgressBar();
            }

            @Override
            public void onFailure(String message) {
                ((BaseActivity) context).hideProgressBar();
            }

            @Override
            public void onRetryYes() {
                validateOffer(item);
            }

            @Override
            public void onRetryNo() {
            }
        });
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

        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvCouponCode)
        TextView tvCouponCode;
        @BindView(R.id.tvLocation)
        TextView tvLocation;
        @BindView(R.id.tvStatus)
        TextView tvStatus;
        @BindView(R.id.tvContact)
        TextView tvContact;
        @BindView(R.id.tvEmail)
        TextView tvEmail;
        @BindView(R.id.imgUser)
        CircleImageView imgUser;
        @BindView(R.id.tvValidate)
        TextView tvValidate;
        @BindView(R.id.tvOrderRefNo)
        TextView tvOrderRefNo;
        @BindView(R.id.tvSequenceNo)
        TextView tvSequenceNo;
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