package com.gogroup.app.gogroupapp.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gogroup.app.gogroupapp.CallBackListeners.CallBackOnLoadMore;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.RestClient;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.GroupListResponse;
import com.gogroup.app.gogroupapp.Responses.PostResponse;
import com.gogroup.app.gogroupapp.User.GroupDetails;
import com.gogroup.app.gogroupapp.User.JoinedGroupDetail;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by zabius on 7/28/17.
 */

public class AdapterUserGroups extends RecyclerView.Adapter
{
    List<GroupListResponse> list = new ArrayList<>();
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private boolean isLoading;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private CallBackOnLoadMore onLoadMoreListener;
    Activity activity;
    boolean isFavorite;
    List<Uri> uriList = new ArrayList<>();

    public void setOnLoadMoreListener(CallBackOnLoadMore mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    public AdapterUserGroups() {
    }

    public void setData(RecyclerView recyclerView, List<GroupListResponse> list, Activity activity, boolean isFavorite) {
        this.list = list;
        uriList = new ArrayList<>(list.size());
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
            View view = LayoutInflater.from(activity).inflate(R.layout.card_group, parent, false);
            return new MyViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity).inflate(R.layout.card_load_more, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MyViewHolder) {
            final MyViewHolder myViewHolder = (MyViewHolder) holder;
            final GroupListResponse item = list.get(position);

            if (item.getJoinStatus().equals("1")) {
                myViewHolder.btnJoin.setText(activity.getString(R.string.joined));
            } else {
                myViewHolder.btnJoin.setVisibility(View.GONE);
            }

            if (item.getFavourite() != null) {
                myViewHolder.cbLike.setChecked(item.getFavourite() == 1 ? true : false);
            }

            myViewHolder.tvGroupName.setText(item.getGroupName() != null ? item.getGroupName() : "N/A");
            myViewHolder.tvLocation.setText(item.getGroupLocation() != null ? item.getGroupLocation() : "N/A");
            myViewHolder.tvCategory1.setText(item.getCategory1() != null ? item.getCategory1() : "N/A");
            String c2 = item.getCategory2() != null ? item.getCategory2() : "N/A", c3 = item.getCategory3() != null ? "->" + item.getCategory3() : "",
                    c4 = item.getCategory4() != null ? "->" + item.getCategory4() : "", c5 = item.getCategory5() != null ? "->" + item.getCategory5() : "",
                    c6 = item.getCategory6() != null ? "->" + item.getCategory6() : "";
            myViewHolder.tvSubCategory.setText(c2 + c3 + c4 + c5 + c6);

            myViewHolder.tvRating.setText(item.getRating() != null && !item.getRating().trim().equals("0") ? item.getRating() : "0.0");
            try {

                myViewHolder.ratingBar.setRating(Float.parseFloat(item.getRating() != null ? item.getRating() : "0.0"));
            } catch (Exception e) {
            }

            myViewHolder.tvCostRange.setText(item.getCostRange() != null ? item.getCostRange() : "0");
            myViewHolder.tvEndDate.setText(item.getEndDate() != null ? item.getEndDate() : "N/A");
            myViewHolder.tvMembers.setText(item.getJoinMember() != null ? item.getJoinMember() : "0");
            myViewHolder.tvDescription.setText(item.getDescription() != null ? item.getDescription() : "N/A");


            if (item.getGroupImage() != null && !item.getGroupImage().trim().equals("")) {
                Utils.picasso(activity, item.getGroupImage(), myViewHolder.imgProfile);
                getUri(item.getGroupImage(), position);

            }


            myViewHolder.tvCreatedBy.setText(item.getName() != null ? "Created by " + item.getName() : "");

            myViewHolder.layoutCB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myViewHolder.cbLike.setAlpha(0.5f);
                    if (myViewHolder.cbLike.isChecked()) {
                        setFavouriteGroup(myViewHolder.cbLike, 0, item.getGroupId(), item);
                    } else {
                        setFavouriteGroup(myViewHolder.cbLike, 1, item.getGroupId(), item);

                    }
                }
            });


            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (item.getJoinStatus() != null) {
                        if (item.getJoinStatus().equals("0")) {
                            Intent intent = new Intent(activity, GroupDetails.class);
                            intent.putExtra(GroupDetails.CATEGORY_ID, item.getCategoryId());
                            intent.putExtra(GroupDetails.GROUP_ID, item.getGroupId());
                            activity.startActivityForResult(intent, BaseActivity.REFRESH_ACTIVITY);
                        } else if (item.getJoinStatus().equals("1")) {
                                /*Intent intent = new Intent(activity, JoinedGroupChat.class);
                                intent.putExtra(JoinedGroupChat.CATEGORY_ID, postCategoryId);
                                intent.putExtra(JoinedGroupChat.GROUP_ID, item.getGroupId());
                                activity.startActivityForResult(intent, BaseActivity.REFRESH_LIST);*/

                               /* if (ApplozicClient.getInstance(UserDashboard.this.getApplicationContext()).isContextBasedChat()) {
                                    intent.putExtra(ConversationUIService.CONTEXT_BASED_CHAT, true);
                                }
                                intent.putExtra(TAKE_ORDER, true);*/

//                            final Channel channel = ChannelDatabaseService.getInstance(activity).getChannelByClientGroupId(item.getGroupId());
//                            if (channel != null) {
//                                Intent intent = new Intent(activity, CustomConversationActivity.class);
//                                intent.putExtra("category_id", item.getCategoryId());
//                                intent.putExtra("group_id", item.getGroupId());
//                                intent.putExtra(ConversationUIService.GROUP_ID, channel.getKey());
//                                intent.putExtra(ConversationUIService.GROUP_NAME, channel.getName());
//                                activity.startActivityForResult(intent, BaseActivity.REFRESH_LIST);
//
//                            } else {
//                                Utils.showShortToast(activity, activity.getString(R.string.errorNotRegisterGroup));
//                            }

                            Intent intent = new Intent(activity, JoinedGroupDetail.class);
                            intent.putExtra("category_id", item.getCategoryId());
                            intent.putExtra("group_id", item.getGroupId());
                            activity.startActivityForResult(intent, BaseActivity.REFRESH_LIST);
                        }
                    }
                }
            });
            myViewHolder.imgShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_SUBJECT, "GoGroup App");
//                        String sAux = "\nLet me recommend you this application\n\n";
                        String sAux = "\n" + item.getGroupName() +"\n"+item.getDescription()+ "\n\n";
                        sAux = sAux + "https://m.gogroup.com/details?category_id=" + item.getCategory1() + "&group_id=" + item.getGroupId() + "\n\n";
                        i.putExtra(Intent.EXTRA_TEXT, sAux);
                        if (uriList.get(position) != null) {
                            i.putExtra(Intent.EXTRA_STREAM, uriList.get(position));
                            i.setType("image/*");
                            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                        activity.startActivity(Intent.createChooser(i, "Share via"));
                    } catch (Exception e) {
                        Toast.makeText(activity, "2"+e.getMessage(), Toast.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            Toast.makeText(activity, activity.getString(R.string.validationPermission), Toast.LENGTH_LONG);
                            ((BaseActivity) activity).isPermissionsAllowed();
                            getUri(item.getGroupImage(), position);

                        }


                    }
                }
            });


        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    void getUri(String path, final int position) {
        if (path == null) {
            uriList.add(null);
            return;
        }

        Picasso.with(activity)
                .load(path)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                        try {
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, "Title", null);
                            uriList.add(position, Uri.parse(path));

                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });
    }

    private void setFavouriteGroup(final CheckBox cb, final int status, String groupId, final GroupListResponse item) {
        if (Utils.isInterNetConnected(activity)) {

            // ((BaseActivity) activity).showProgressbar();
            RestClient.get().markFavouriteGroup(UserPreferences.getInstance().getToken(), status, groupId)
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

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tvGroupName)
        TextView tvGroupName;
        @BindView(R.id.tvLocation)
        TextView tvLocation;
        @BindView(R.id.tvCategory1)
        TextView tvCategory1;
        @BindView(R.id.tvSubCategory)
        TextView tvSubCategory;
        @BindView(R.id.tvRating)
        TextView tvRating;
        @BindView(R.id.tvCostRange)
        TextView tvCostRange;
        @BindView(R.id.tvCreatedBy)
        TextView tvCreatedBy;
        @BindView(R.id.tvEndDate)
        TextView tvEndDate;
        @BindView(R.id.tvMembers)
        TextView tvMembers;
        @BindView(R.id.tvDescription)
        TextView tvDescription;
        @BindView(R.id.imgProfile)
        ImageView imgProfile;
        @BindView(R.id.btnJoin)
        Button btnJoin;
        @BindView(R.id.imgShare)
        ImageView imgShare;
        @BindView(R.id.ratingBar)
        RatingBar ratingBar;
        @BindView(R.id.cbLike)
        CheckBox cbLike;
        @BindView(R.id.layoutCB)
        LinearLayout layoutCB;

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


