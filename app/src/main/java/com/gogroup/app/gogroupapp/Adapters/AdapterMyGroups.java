package com.gogroup.app.gogroupapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gogroup.app.gogroupapp.CallBackListeners.CallBackOnLoadMore;
import com.gogroup.app.gogroupapp.Fragments.MyJoinedGroupsFragment;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.RestClient;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.GroupListResponse;
import com.gogroup.app.gogroupapp.Responses.PostResponse;
import com.gogroup.app.gogroupapp.User.JoinedGroupDetail;
import com.gogroup.app.gogroupapp.User.JoinedGroupDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by zabius on 7/27/17.
 */

public class AdapterMyGroups extends RecyclerView.Adapter {

    List<GroupListResponse> list = new ArrayList<>();
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private boolean isLoading;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private CallBackOnLoadMore onLoadMoreListener;
    Fragment fragment;
    boolean isCreatedGroupList;

    public AdapterMyGroups() {
    }

    public void setOnLoadMoreListener(CallBackOnLoadMore mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    public void setData(RecyclerView recyclerView, List<GroupListResponse> list, Fragment fragment, boolean isCreatedGroupList) {
        this.list = list;
        this.fragment = fragment;
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
            View view = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.card_user_created_group, parent, false);
            return new MyViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.card_load_more, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            final GroupListResponse item = list.get(position);
            myViewHolder.tvGroupName.setText(item.getGroupName() != null ? item.getGroupName() : "N/A");
            myViewHolder.tvEndDate.setText(item.getEndDate() != null ? item.getEndDate() : "N/A");
            myViewHolder.tvStartDate.setText(item.getEndDate() != null ? item.getStartDate() : "N/A");
            myViewHolder.tvMembers.setText(item.getJoinMember() != null ? item.getJoinMember() : "0");
            if (item.getGroupImage() != null && item.getGroupImage().trim() != "") {
                Picasso.with(fragment.getActivity()).load(item.getGroupImage()).into(myViewHolder.imgGroup);
            }
            myViewHolder.tvStatus.setText(item.getGroupStatus() != null ? item.getGroupStatus() : "N/A");
            if (myViewHolder.tvStatus.getText().toString().trim().equalsIgnoreCase(fragment.getString(R.string.approved))) {
                myViewHolder.tvStatus.setTextColor(ContextCompat.getColor(fragment.getActivity(), R.color.green_color));

            } else {
                myViewHolder.tvStatus.setTextColor(ContextCompat.getColor(fragment.getActivity(), R.color.redAppColor));
            }
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.getGroupStatus() != null && item.getGroupStatus().toString().equalsIgnoreCase(fragment.getString(R.string.approved))) {

//                        Intent intent = new Intent(fragment.getActivity(), JoinedGroupChat.class);
//                        intent.putExtra(JoinedGroupChat.CATEGORY_ID, item.getCategoryId());
//                        intent.putExtra(JoinedGroupChat.GROUP_ID, item.getGroupId());
//                        fragment.getActivity().startActivityForResult(intent, BaseActivity.REFRESH_LIST);


//                        Intent intent = new Intent(fragment.getActivity(), CustomConversationActivity.class);
//                        final Channel channel = ChannelDatabaseService.getInstance(fragment.getActivity()).getChannelByClientGroupId(item.getGroupId());
//                        if (channel != null) {
//                            intent.putExtra("category_id", item.getCategoryId());
//                            intent.putExtra("group_id", item.getGroupId());
//                            intent.putExtra(ConversationUIService.GROUP_ID, channel.getKey());
//                            intent.putExtra(ConversationUIService.GROUP_NAME, channel.getName());
//                            fragment.getActivity().startActivityForResult(intent, BaseActivity.REFRESH_ACTIVITY);
//                        } else {
//                            Utils.showShortToast(fragment.getActivity(), fragment.getActivity().getString(R.string.errorNotRegisterGroup));
//                        }
                        Intent intent = new Intent(fragment.getActivity(), JoinedGroupDetail.class);
                        intent.putExtra("category_id", item.getCategoryId());
                        intent.putExtra("group_id", item.getGroupId());
                        fragment.getActivity().startActivityForResult(intent, BaseActivity.REFRESH_LIST);
                    } else {

                        Intent i = new Intent(fragment.getActivity(), JoinedGroupDetails.class);
                        i.putExtra(JoinedGroupDetails.CATEGORY_ID, item.getCategoryId());
                        i.putExtra(JoinedGroupDetails.GROUP_ID, item.getGroupId());
                        fragment.getActivity().startActivity(i);


                    }
                }
            });
            if (!isCreatedGroupList) {
                myViewHolder.tvExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Utils.isInterNetConnected(fragment.getActivity())) {
                            ((BaseActivity) fragment.getActivity()).showProgressbar();
                            RestClient.get().exitGroup(UserPreferences.getInstance().getToken(), item.getGroupId()).
                                    enqueue(new retrofit2.Callback<PostResponse>() {
                                        @Override
                                        public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                                            if (response.body() != null) {
                                                if (response.body().isStatus()) {
//                                                    applozicLeaveGroup(item.getGroupId());
                                                    MyJoinedGroupsFragment JoinedGroupsFragment = (MyJoinedGroupsFragment) fragment;
                                                    JoinedGroupsFragment.apiGetGroupList();
                                                    ((BaseActivity) fragment.getActivity()).hideProgressBar();

                                                } else {
                                                    Utils.showShortToast(fragment.getActivity(), response.body().getMessage());
                                                    ((BaseActivity) fragment.getActivity()).hideProgressBar();

                                                }

                                            } else {
                                                Utils.showServerError(fragment.getActivity());
                                                ((BaseActivity) fragment.getActivity()).hideProgressBar();

                                            }

                                        }

                                        @Override
                                        public void onFailure(final Call<PostResponse> call, Throwable t) {
                                            ((BaseActivity) fragment.getActivity()).hideProgressBar();
                                            if (t.getMessage().toLowerCase().contains("failed to connect")) {
//                                showAlert();
//                                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        call.request();
//
//                                        alertDialog.dismiss();
//                                    }
//                                });
                                            }
                                            Utils.showShortToast(fragment.getActivity(), t.getMessage());
                                        }
                                    });
                        }
                    }
                });
            }

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
        @BindView(R.id.tvGroupName)
        TextView tvGroupName;
        @BindView(R.id.tvEndDate)
        TextView tvEndDate;
        @BindView(R.id.tvMembers)
        TextView tvMembers;
        @BindView(R.id.tvStartDate)
        TextView tvStartDate;
        @BindView(R.id.tvStatus)
        TextView tvStatus;
        @BindView(R.id.tvExit)
        TextView tvExit;
        @BindView(R.id.imgGroup)
        CircleImageView imgGroup;


        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            tvExit.setVisibility(isCreatedGroupList ? View.GONE : View.VISIBLE);
            tvStatus.setVisibility(isCreatedGroupList ? View.VISIBLE : View.GONE);
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
