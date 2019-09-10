package com.gogroup.app.gogroupapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gogroup.app.gogroupapp.Adapters.MemberCreatedGroupAdapter;
import com.gogroup.app.gogroupapp.Models.UserCreatedGroupModel;
import com.gogroup.app.gogroupapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
 import java.util.ArrayList;

public class MemberCreatedGroupListingFragment extends Fragment {


    ListView userCreatedListView;
    ArrayList<UserCreatedGroupModel> arrayList = new ArrayList<>();
    MemberCreatedGroupAdapter adapter;
    public MemberCreatedGroupListingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_member_created_group_listing, container, false);

        initIds(view);
        for(int i=0;i<10;i++){
            UserCreatedGroupModel model = new UserCreatedGroupModel("Flat for rent","20","R.drawable.add","20 June 2017","21 July 2017","Active");
            arrayList.add(model);
        }

        adapter = new MemberCreatedGroupAdapter(getActivity(),arrayList);

        userCreatedListView.setAdapter(adapter);
        return view;
    }
    public void initIds(View view){
        userCreatedListView = (ListView)view.findViewById(R.id.listViewMemberCreatedGroup);
    }


}
