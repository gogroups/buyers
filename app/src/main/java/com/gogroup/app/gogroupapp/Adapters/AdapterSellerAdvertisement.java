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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gogroup.app.gogroupapp.CallBackListeners.CallBackApi;
import com.gogroup.app.gogroupapp.CallBackListeners.CallBackOnLoadMore;
import com.gogroup.app.gogroupapp.HelperClasses.ApiCalls;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.RestClient;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.ListResponse;
import com.gogroup.app.gogroupapp.Responses.PostResponse;
import com.gogroup.app.gogroupapp.Responses.SellerAdvertisementResponse;
import com.gogroup.app.gogroupapp.Seller.SellerAddDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by zabius on 7/24/17.
 */

public class AdapterSellerAdvertisement extends RecyclerView.Adapter {
    List<SellerAdvertisementResponse> list = new ArrayList<>();
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private boolean isLoading;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private CallBackOnLoadMore onLoadMoreListener;
    Activity activity;
    boolean isFavorite, isUser;
    Context context;

    public void setOnLoadMoreListener(CallBackOnLoadMore mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    public AdapterSellerAdvertisement() {

        isUser = UserPreferences.getInstance().getUserType().trim().equalsIgnoreCase("user");


    }

    public void setData(RecyclerView recyclerView, List<SellerAdvertisementResponse> list, Activity activity, boolean isFavorite) {
        this.list.clear();
        this.list.addAll(list);
        this.isFavorite = isFavorite;
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
            View view = LayoutInflater.from(activity).inflate(R.layout.card_adds, parent, false);
            return new MyViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity).inflate(R.layout.card_load_more, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyViewHolder) {
            final MyViewHolder myViewHolder = (MyViewHolder) holder;
            final SellerAdvertisementResponse item = list.get(position);

           // if(item.getAdStatus().equalsIgnoreCase("approved"))
            //{
                if (item.getFavourite() != null) {
                    myViewHolder.cbLike.setChecked(item.getFavourite() == 1 ? true : false);
                }

                myViewHolder.layoutCB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myViewHolder.cbLike.setAlpha(0.5f);
                        if (myViewHolder.cbLike.isChecked()) {
                            setFavouriteGroup(myViewHolder.cbLike, 0, item.getAdvertisementId(), item);
                        } else {
                            setFavouriteGroup(myViewHolder.cbLike, 1, item.getAdvertisementId(), item);

                        }
                    }
                });

                int no_in_group = Integer.parseInt(item.getPendingCodeCount());
                String pfix = "";
                int d = no_in_group % 10;
                int num = no_in_group / 10;
                int dd = num % 10;
                int S = (dd * 10) + d;

                if (S >= 11 && S <= 20)
                    pfix = "th";
                else {
                    switch (d) {
                        case 1:
                            pfix = "st";
                            break;
                        case 2:
                            pfix = "nd";
                            break;
                        case 3:
                            pfix = "rd";
                            break;
                        default:
                            pfix = "th";
                    }
                }

                myViewHolder.tvDealName.setText(item.getAdvertisementName());
                myViewHolder.tvGroupNum.setText(item.getGroupCount() != null ? item.getGroupCount() : "0");
                myViewHolder.tvViewsNum.setText(item.getViewsCount() != null ? item.getViewsCount() : "0");
                myViewHolder.tvEndDate.setText(item.getEndDate() != null ? item.getEndDate() : "");
                myViewHolder.tvCouponCode.setText(item.getPendingCodeCount()+pfix);
                myViewHolder.tvLikes.setText(item.getLikedcount());
                myViewHolder.tvActualPrice.setText(item.getActualPrice());
                myViewHolder.tvOfferPrice.setText(item.getOfferPrice());
                myViewHolder.tvpricefortwo.setText(item.getCostfortwo());

//            myViewHolder.tvTotalPurchased.setText((!isUser ? item.getOrderPlacedCount() + "/" : "") + item.getPurchasedCount());
                myViewHolder.tvTotalPurchased.setText((!isUser ? item.getOrderPlacedCount() : item.getPurchasedCount()));
//            myViewHolder.tvActiveStatus.setText(item.getActiveStatus()!=null?item.getActiveStatus():"");
//            if (myViewHolder.tvActiveStatus.getText().toString().toLowerCase().trim().equals("approved")) {
//                myViewHolder.tvActiveDot.setBackgroundResource( R.drawable.green_circle);
//                myViewHolder.tvActiveStatus.setTextColor(ContextCompat.getColor(activity,R.color.green_color));
//            } else {
//                myViewHolder.tvActiveDot.setBackgroundResource(R.drawable.red_circle);
//                myViewHolder.tvActiveStatus.setTextColor(ContextCompat.getColor(activity,R.color.redAppColor));
//            }


                myViewHolder.tvDescription.setText(item.getAdvertisementDetails() != null ? item.getAdvertisementDetails() : "N/A");
                myViewHolder.imgLike.setImageDrawable(ContextCompat.getDrawable(activity, item.getIsliked() == 0 ? R.drawable.ic_dislike : R.drawable.ic_like));
                myViewHolder.tvLocation.setText(item.getLocation() != null ? item.getLocation() : "N/A");
                myViewHolder.tvCategory1.setText(item.getCategory1() != null ? item.getCategory1() : "N/A");
                String c2 = item.getCategory2() != null ? item.getCategory2() : "N/A", c3 = item.getCategory3() != null ? "->" + item.getCategory3() : "",
                        c4 = item.getCategory4() != null ? "->" + item.getCategory4() : "", c5 = item.getCategory5() != null ? "->" + item.getCategory5() : "",
                        c6 = item.getCategory6() != null ? "->" + item.getCategory6() : "";
                myViewHolder.tvSubCategory.setText(c2 + c3 + c4 + c5 + c6);
                if (item.getImagesDetails() != null && item.getImagesDetails().size() > 0) {
                    Picasso.with(activity).load(item.getImagesDetails().get(0).getImagePath()).into(myViewHolder.imgAdvertise);
                }

                myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(activity, SellerAddDetailActivity.class);
                        intent.putExtra(SellerAddDetailActivity.DATA, item);
                        activity.startActivityForResult(intent, BaseActivity.REFRESH_DASHBOARD);
                    }
                });

                myViewHolder.imgLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!isUser) {
                            return;
                        }

                        if (myViewHolder.imgLike.getAlpha() == 0.7f) {
                            return;
                        }
                        apiSetLike(item, myViewHolder.imgLike);

                    }
                });


            } else if (holder instanceof LoadingViewHolder) {
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }}

        //}

        private void setFavouriteGroup ( final CheckBox cb, final int status, String advertisementId,
        final SellerAdvertisementResponse item){
            if (Utils.isInterNetConnected(activity)) {

                // ((BaseActivity) activity).showProgressbar();
                RestClient.get().markFavouriteAds(UserPreferences.getInstance().getToken(), status, advertisementId)
                        .enqueue(new retrofit2.Callback<PostResponse>() {
                            @Override
                            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                                if (response.body() != null) {
                                    cb.setAlpha(1f);
                                    if (response.body().isStatus()) {
                                        if (isFavorite) {
                                            list.remove(item);
                                            notifyDataSetChanged();
                                        }
                                        Utils.showShortToast(activity, response.body().getMessage());
                                        cb.setChecked(status == 0 ? false : true);
                                        item.setFavourite(status);
                                    } else {
                                        Utils.showShortToast(activity, response.body().getMessage());
                                    }

                                } else {
                                    Utils.showServerError(activity);
                                }

                            }

                            @Override
                            public void onFailure(final Call<PostResponse> call, Throwable t) {
                                cb.setAlpha(1f);
                                Utils.showShortToast(activity, t.getMessage());

                            }
                        });
            }


        }

        private void apiSetLike ( final SellerAdvertisementResponse item, final ImageView imgLike){

            imgLike.setAlpha(0.7f);
            final boolean isLiked = item.getIsliked() == 1;
            Map<String, String> postFields = new HashMap<>();
            postFields.put("status", "" + (isLiked ? 0 : 1));
            postFields.put("advertisement_id", item.getAdvertisementId());
            new ApiCalls(activity).apiSetLike(postFields, new CallBackApi() {
                @Override
                public void onSuccess(Response response) {
                    PostResponse body = (PostResponse) response.body();
                    item.setIsliked(isLiked ? 0 : 1);
                    item.setLikedcount(body.getLikedCount());
                    imgLike.setAlpha(1f);
                    notifyDataSetChanged();
                }

                @Override
                public void onFailure(String message) {
                    imgLike.setAlpha(1f);

                }

                @Override
                public void onRetryYes() {
                    apiSetLike(item, imgLike);
                }

                @Override
                public void onRetryNo() {
                }
            });

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
        @BindView(R.id.tvViewsNum)
        TextView tvViewsNum;
        @BindView(R.id.imgAdvertise)
        ImageView imgAdvertise;

        @BindView(R.id.tvDealName)
        TextView tvDealName;
        @BindView(R.id.tvLocation)
        TextView tvLocation;
        @BindView(R.id.tvCategory1)
        TextView tvCategory1;
        @BindView(R.id.tvSubCategory)
        TextView tvSubCategory;
        @BindView(R.id.tvEndDate)
        TextView tvEndDate;
        @BindView(R.id.tvDescription)
        TextView tvDescription;
        @BindView(R.id.tvCouponCode)
        TextView tvCouponCode;
        @BindView(R.id.tvTotalPurchased)
        TextView tvTotalPurchased;
        @BindView(R.id.tvPurchaseTitle)
        TextView tvPurchaseTitle;
        @BindView(R.id.cbLike)
        CheckBox cbLike;
        @BindView(R.id.layoutCB)
        LinearLayout layoutCB;
        @BindView(R.id.layoutCouponCode)
        LinearLayout layoutCouponCode;
        @BindView(R.id.layoutPurchased)
        LinearLayout layoutPurchased;
        @BindView(R.id.tvLikes)
        TextView tvLikes;
        @BindView(R.id.tvActualPrice)
        TextView tvActualPrice;
        @BindView(R.id.tvOfferPrice)
        TextView tvOfferPrice;
        @BindView(R.id.tvpricefortwo)
        TextView tvpricefortwo;
        @BindView(R.id.tvpriceforthree)
        TextView tvpriceforthree;
        @BindView(R.id.tvpriceforx)
        TextView tvpriceforx;
        @BindView(R.id.imgLike)
        ImageView imgLike;
        @BindView(R.id.view)
        View view;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            if (!isUser) {
                layoutCB.setVisibility(View.GONE);
            } else {
                //layoutPurchased.setVisibility(View.GONE);
                this.view.setVisibility(View.VISIBLE);
            }
            tvPurchaseTitle.setText(!isUser ? view.getContext().getString(R.string.ordered) : view.getContext().getString(R.string.purchased2));
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
