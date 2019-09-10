package com.gogroup.app.gogroupapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.Adapters.AdapterMyGroups;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.RestClient;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.GroupListResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyCreatedGroupsFragment extends Fragment {

    AdapterMyGroups mAdapter;
    LinearLayoutManager layoutManager;
    List<GroupListResponse> groupList=new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_group, container, false);
        ButterKnife.bind(this,view);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new AdapterMyGroups();
        recyclerView.setAdapter(mAdapter);
        tvEmpty.setVisibility(View.GONE);
        apiGetGroupList();

        return view;
    }


    public void apiGetGroupList() {
        if (Utils.isInterNetConnected(getActivity())) {
            ((BaseActivity) getActivity()).showProgressbar();
          RestClient.get().getCreatedGroups(UserPreferences.getInstance().getToken()).
                    enqueue(new retrofit2.Callback<GroupListResponse>() {
                        @Override
                        public void onResponse(Call<GroupListResponse> call, Response<GroupListResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {

                                    groupList = response.body().getData();
                                    mAdapter.setData(recyclerView, groupList, MyCreatedGroupsFragment.this,true);
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


    @BindView(R.id.tvEmpty)
    TextView tvEmpty;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

}
