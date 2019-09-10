package com.gogroup.app.gogroupapp.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.gogroup.app.gogroupapp.Adapters.AdapterAdsPager;
import com.gogroup.app.gogroupapp.Adapters.AdapterSellerAdvertisement;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.RestClient;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.Models.FilterModel;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.ListResponse;
import com.gogroup.app.gogroupapp.Responses.SellerAdvertisementResponse;
import com.gogroup.app.gogroupapp.User.CategoryDetailUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdsPagerFragment extends Fragment {


    ListResponse item;

    public static AdsPagerFragment newInstance(ListResponse listResponse) {
        AdsPagerFragment frag = new AdsPagerFragment();
        Bundle b = new Bundle();
        b.putParcelable(BaseActivity.DATA, listResponse);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(BaseActivity.DATA)) {
            item = getArguments().getParcelable(BaseActivity.DATA);
        }

    }

    //    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//
//        if (isVisibleToUser && advertiseList == null)
//
//        {
//            advertiseList = new ArrayList<>();
//            if (isFavorite) {
//                apiGetFavoriteAds();
//            } else {
//                getSellerAdds();
//            }
//
//        }
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_ads_pager, container, false);
        ButterKnife.bind(this, view);
        setAdapter();
        return view;
    }


    private void setAdapter() {
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setAdapter(new AdapterAdsPager(getActivity(), item.getAdvertisementList()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @BindView(R.id.recycler)
    RecyclerView recycler;


}
