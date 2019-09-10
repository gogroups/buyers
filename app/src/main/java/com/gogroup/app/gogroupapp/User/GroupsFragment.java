package com.gogroup.app.gogroupapp.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.Adapters.AdapterUserGroups;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.RestClient;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.Models.FilterModel;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.GroupListResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class GroupsFragment extends Fragment {

    List<GroupListResponse> groupList;
    public AdapterUserGroups mAdapter;
    LinearLayoutManager layoutManager;
    public FilterModel filterModel;
    boolean isFavorite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_group, container, false);
        ButterKnife.bind(this, view);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new AdapterUserGroups();
        recyclerView.setAdapter(mAdapter);
        tvEmpty.setVisibility(View.GONE);


        return view;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && groupList == null)

        {
            groupList = new ArrayList<>();
            try {
                filterModel = ((CategoryDetailUser) getActivity()).filterGroup;
                apiGetGroupList();
                isFavorite = false;

            } catch (Exception e) {
                isFavorite = true;
                apiGetFavouriteGroups();

            }
        }
    }

    public void apiGetFavouriteGroups() {
        if (Utils.isInterNetConnected(getActivity())) {
            ((BaseActivity) getActivity()).showProgressbar();
            RestClient.get().getFavouriteGroups(UserPreferences.getInstance().getToken()).
                    enqueue(new retrofit2.Callback<GroupListResponse>() {
                        @Override
                        public void onResponse(Call<GroupListResponse> call, Response<GroupListResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {

                                    groupList = response.body().getData();
                                    mAdapter.setData(recyclerView, groupList, getActivity(), true);
                                } else {
                                    Utils.showShortToast(getActivity(), response.body().getMessage());
                                }
                                tvEmpty.setVisibility(groupList.size() > 0 ? View.GONE : View.VISIBLE);

                            } else {
                                Utils.showServerError(getActivity());
                            }
                            ((BaseActivity) getActivity()).hideProgressBar();

                        }

                        @Override
                        public void onFailure(final Call<GroupListResponse> call, Throwable t) {
                            ((BaseActivity) getActivity()).hideProgressBar();
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
                            Utils.showShortToast(getActivity(), t.getMessage());
                        }
                    });

        }


    }

    public void apiGetGroupList() {
        if (Utils.isInterNetConnected(getActivity())) {
            ((BaseActivity) getActivity()).showProgressbar();
            Map<String, String> postFields = new HashMap<>();
            postFields.put("start_from_date", filterModel.getStartFromDate() != null ? filterModel.getStartFromDate() : "");
            postFields.put("start_to_date", filterModel.getStartToDate() != null ? filterModel.getStartToDate() : "");
            postFields.put("end_from_date", filterModel.getEndFromDate() != null ? filterModel.getEndFromDate() : "");
            postFields.put("end_to_date", filterModel.getEndToDate() != null ? filterModel.getEndToDate() : "");
            postFields.put("category_id", ((CategoryDetailUser) getActivity()).postCategoryId);
            postFields.put("location", filterModel.getLocation() != null ? filterModel.getLocation() : "");
//            postFields.put("cost_range", filterModel.getCostRange() != null && !filterModel.getCostRange().equals("0") ? filterModel.getCostRange() : "");
            postFields.put("cost_range_from", filterModel.getCostRangeFrom() != null ? filterModel.getCostRangeFrom() : "");
            postFields.put("cost_range_to", filterModel.getCostRangeTo() != null ? filterModel.getCostRangeTo() : "");
            postFields.put("subcategory_id", filterModel.getCategoryId2() != null ? filterModel.getCategoryId2() : "");
            postFields.put("group_name", filterModel.getGroupName() != null ? filterModel.getGroupName() : "");
            postFields.put("search_text", ((CategoryDetailUser) getActivity()).postSearchInterest != null ? ((CategoryDetailUser) getActivity()).postSearchInterest : "");
            RestClient.get().getGroupList(UserPreferences.getInstance().getToken(), postFields).
                    enqueue(new retrofit2.Callback<GroupListResponse>() {
                        @Override
                        public void onResponse(Call<GroupListResponse> call, Response<GroupListResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
                                    groupList = response.body().getData();
                                    mAdapter.setData(recyclerView, groupList, getActivity(), false);
                                } else {
                                    Utils.showShortToast(getActivity(), response.body().getMessage());
                                }
                                tvEmpty.setVisibility(groupList.size() > 0 ? View.GONE : View.VISIBLE);

                            } else {
                                Utils.showServerError(getActivity());
                            }
                            try {
                                ((BaseActivity) getActivity()).hideProgressBar();
                            } catch (Exception e) {
                            }

                        }

                        @Override
                        public void onFailure(final Call<GroupListResponse> call, Throwable t) {
                            try {
                                ((BaseActivity) getActivity()).hideProgressBar();

                            } catch (Exception e) {
                            }
                            Utils.showShortToast(getActivity(), t.getMessage());
                        }
                    });

        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(filterModel!=null) {
            if (requestCode == resultCode) {
                if (isFavorite) {
                    apiGetFavouriteGroups();
                } else {
                    apiGetGroupList();
                }
            }
        }


    }

    @BindView(R.id.tvEmpty)
    TextView tvEmpty;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;


}
