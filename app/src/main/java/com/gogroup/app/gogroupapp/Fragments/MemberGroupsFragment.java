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
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.GroupListResponse;
import com.gogroup.app.gogroupapp.User.MemberProfile;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MemberGroupsFragment extends Fragment {

    public static final String POSITION = "position";
    AdapterMemberGroup mAdapter;
    LinearLayoutManager layoutManager;
    List<GroupListResponse> list = new ArrayList<>();

    public static MemberGroupsFragment newInstance(int someInt) {
        MemberGroupsFragment myFragment = new MemberGroupsFragment();

        Bundle args = new Bundle();
        args.putInt(POSITION, someInt);
        myFragment.setArguments(args);

        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_group, container, false);
        ButterKnife.bind(this, view);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new AdapterMemberGroup(false);
        recyclerView.setAdapter(mAdapter);
        //apiGetGroupList();
        if (getArguments().getInt(POSITION) == 0) {
            list = ((MemberProfile) getActivity()).createdList;
        } else {
            list = ((MemberProfile) getActivity()).joinedList;
        }
        mAdapter.setData(recyclerView, list, MemberGroupsFragment.this);
        tvEmpty.setVisibility(list.size() > 0 ? View.GONE : View.VISIBLE);

        return view;
    }


    @BindView(R.id.tvEmpty)
    TextView tvEmpty;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

}
