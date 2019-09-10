package com.gogroup.app.gogroupapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gogroup.app.gogroupapp.Adapters.UserGroupChatListAdapter;
import com.gogroup.app.gogroupapp.Adapters.UserPersonalChatListAdapter;
import com.gogroup.app.gogroupapp.Models.UserChatModel;
import com.gogroup.app.gogroupapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserGroupChatListFragment extends Fragment {

    ListView userPersonalChatListView;
    ArrayList<UserChatModel> arrayList = new ArrayList<>();
    UserGroupChatListAdapter adapter;
    public UserGroupChatListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_group_chat_list, container, false);
        initIds(view);
        for(int i=0;i<5;i++){
            UserChatModel model = new UserChatModel("","Flat for Rent","lorem Ipsum is a dummy text");
            arrayList.add(model);
        }

        adapter = new UserGroupChatListAdapter(getActivity(),arrayList);
        userPersonalChatListView.setAdapter(adapter);
        return view;
    }
    public void initIds(View view){
        userPersonalChatListView = (ListView)view.findViewById(R.id.listViewUserGroupChat);
    }


}
