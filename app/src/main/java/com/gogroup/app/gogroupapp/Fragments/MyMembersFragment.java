package com.gogroup.app.gogroupapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.Adapters.AdapterMemberGroup;
import com.gogroup.app.gogroupapp.Adapters.AdapterSelectMember;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.RestClient;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.GroupListResponse;
import com.gogroup.app.gogroupapp.Responses.MemberDetailResponse;
import com.gogroup.app.gogroupapp.Responses.MemberResponse;
import com.gogroup.app.gogroupapp.Responses.UserResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyMembersFragment extends Fragment {

    public static final String POSITION = "position";

    public AdapterSelectMember adapterMembers;
   public AdapterMemberGroup adapterGroups;

    List<UserResponse> userList = new ArrayList<>();
    List<GroupListResponse> groupList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_member, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(layoutManager);
        tvEmpty.setVisibility(View.GONE);
        if (getArguments().getInt(POSITION) == 0) {
            apiGetMembers();
        } else {
            apiGetGroups();
        }


        return view;
    }

    public static MyMembersFragment newInstance(int someInt) {
        MyMembersFragment myFragment = new MyMembersFragment();

        Bundle args = new Bundle();
        args.putInt(POSITION, someInt);
        myFragment.setArguments(args);

        return myFragment;
    }

    public void apiGetMembers() {
        if (Utils.isInterNetConnected(getActivity())) {
            ((BaseActivity) getActivity()).showProgressbar();
            RestClient.get().getUserList(UserPreferences.getInstance().getToken()).
                    enqueue(new retrofit2.Callback<MemberResponse>() {
                        @Override
                        public void onResponse(Call<MemberResponse> call, Response<MemberResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
                                    adapterMembers = new AdapterSelectMember();
                                    recycler.setAdapter(adapterMembers);
                                    userList = response.body().getUserList();
                                    adapterMembers.setData(recycler, userList, getActivity(), true);
                                } else {
                                    Utils.showShortToast(getActivity(), response.body().getMessage());
                                }
                                tvEmpty.setVisibility(userList.size() > 0 ? View.GONE : View.VISIBLE);
                            } else {
                                Utils.showServerError(getActivity());
                            }
                            ((BaseActivity) getActivity()).hideProgressBar();
                        }

                        @Override
                        public void onFailure(final Call<MemberResponse> call, Throwable t) {
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


    private void apiGetGroups() {
        if (Utils.isInterNetConnected(getActivity())) {
            ((BaseActivity) getActivity()).showProgressbar();
            RestClient.get().getMemberDetail(UserPreferences.getInstance().getToken(), UserPreferences.getInstance().getUserId()).
                    enqueue(new retrofit2.Callback<MemberDetailResponse>() {
                        @Override
                        public void onResponse(Call<MemberDetailResponse> call, Response<MemberDetailResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
                                    adapterGroups =new AdapterMemberGroup(true);
                                    recycler.setAdapter(adapterGroups);
                                    groupList = response.body().getData().getCreatedGroups();
                                    groupList.addAll(response.body().getData().getJoinedGroups());
                                    adapterGroups.setData(recycler, groupList, MyMembersFragment.this);
                                    tvEmpty.setVisibility(groupList.size() > 0 ? View.GONE : View.VISIBLE);
                                } else {
                                    Utils.showShortToast(getActivity(), response.body().getMessage());
                                }

                            } else {
                                Utils.showServerError(getActivity());
                            }
                            ((BaseActivity) getActivity()).hideProgressBar();

                        }

                        @Override
                        public void onFailure(final Call<MemberDetailResponse> call, Throwable t) {
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

    public void filterMemberList(String query)
    {
        List<UserResponse> filterList = new ArrayList<>();

        for (int i=0;i<userList.size();i++)
        {
            if(userList.get(i).getName().toLowerCase().contains(query.toLowerCase()))
            {
                filterList.add(userList.get(i));
            }
        }

        adapterMembers.filterList(filterList);

    }
    public void filterGroupList(String query)
    {
        List<GroupListResponse> filterList = new ArrayList<>();

        for (int i=0;i<groupList.size();i++)
        {
            if(groupList.get(i).getGroupName().toLowerCase().contains(query.toLowerCase()))
            {
                filterList.add(groupList.get(i));
            }
        }

        adapterGroups.filterList(filterList);
    }


    @BindView(R.id.tvEmpty)
    TextView tvEmpty;
    @BindView(R.id.recycler)
    RecyclerView recycler;
}
