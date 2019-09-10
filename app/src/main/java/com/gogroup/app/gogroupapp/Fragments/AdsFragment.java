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

import com.gogroup.app.gogroupapp.Adapters.AdapterSellerAdvertisement;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.RestClient;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.Models.FilterModel;
import com.gogroup.app.gogroupapp.R;
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
public class AdsFragment extends Fragment {
    GridLayoutManager gridLayoutManager;
    AdapterSellerAdvertisement adapterSeller;
    List<SellerAdvertisementResponse> advertiseList;
    private static final int GRID_COLUMN_COUNT = 2;
    PopupWindow popupWindow;

    public FilterModel filterModel;
    boolean isFavorite,isSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_user_group, container, false);
        ButterKnife.bind(this, view);
        try {
            filterModel = ((CategoryDetailUser) getActivity()).filterAds;
            isFavorite = false;
        } catch (Exception e) {
            isFavorite = true;
        }
        gridLayoutManager = new GridLayoutManager(getActivity(), GRID_COLUMN_COUNT);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterSeller = new AdapterSellerAdvertisement();
        recycler.setAdapter(adapterSeller);
        tvEmpty.setVisibility(View.VISIBLE);

        if ( advertiseList == null)

        {
            advertiseList = new ArrayList<>();
            if (isFavorite) {
                apiGetFavoriteAds();
            } else {
                getSellerAdds(false);
            }

        }
        return view;
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

    public void apiGetFavoriteAds() {
        if (Utils.isInterNetConnected(getActivity())) {
            ((BaseActivity) getActivity()).showProgressbar();
            RestClient.get().getFavoriteAds(UserPreferences.getInstance().getToken()).
                    enqueue(new retrofit2.Callback<SellerAdvertisementResponse>() {
                        @Override
                        public void onResponse(Call<SellerAdvertisementResponse> call, Response<SellerAdvertisementResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {

                                    advertiseList = response.body().getData();
                                    adapterSeller.setData(recycler, advertiseList, getActivity(),true);
                                } else {
                                    Utils.showShortToast(getActivity(), response.body().getMessage());
                                }
                                tvEmpty.setVisibility(advertiseList.size() > 0 ? View.GONE : View.VISIBLE);

                            } else {
                                Utils.showServerError(getActivity());
                            }
                            ((BaseActivity) getActivity()).hideProgressBar();

                        }

                        @Override
                        public void onFailure(final Call<SellerAdvertisementResponse> call, Throwable t) {
                            ((BaseActivity) getActivity()).hideProgressBar();
                            Utils.showShortToast(getActivity(), t.getMessage());
                        }
                    });

        }
    }

    public void getSellerAdds(boolean isSearch) {
        this.isSearch=isSearch;
        if (Utils.isInterNetConnected(getActivity())) {
            ((BaseActivity) getActivity()).showProgressbar();
            Map<String, String> postFields = new HashMap<>();
            postFields.put("start_from_date", isSearch?"":filterModel.getStartFromDate() != null ? filterModel.getStartFromDate() : "");
            postFields.put("start_to_date", isSearch?"":filterModel.getStartToDate() != null ? filterModel.getStartToDate() : "");
            postFields.put("end_from_date", isSearch?"":filterModel.getEndFromDate() != null ? filterModel.getEndFromDate() : "");
            postFields.put("end_to_date", isSearch?"":filterModel.getEndToDate() != null ? filterModel.getEndToDate() : "");
            postFields.put("advertisement_name", isSearch?((CategoryDetailUser) getActivity()).postSearchOffer != null ? ((CategoryDetailUser) getActivity()).postSearchOffer : "":filterModel.getDealName() != null ? filterModel.getDealName() : "");
            postFields.put("category_id", ((CategoryDetailUser) getActivity()).postCategoryId);
            postFields.put("location", isSearch?"":filterModel.getLocation() != null ? filterModel.getLocation() : "");
            postFields.put("subcategory_id", isSearch?"":filterModel.getCategoryId2() != null ? filterModel.getCategoryId2() : "");


            RestClient.get().getAllUserAdvertisement(UserPreferences.getInstance().getToken(), postFields).
                    enqueue(new retrofit2.Callback<SellerAdvertisementResponse>() {
                        @Override
                        public void onResponse(Call<SellerAdvertisementResponse> call, Response<SellerAdvertisementResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {

                                    advertiseList = response.body().getData();

                                    adapterSeller.setData(recycler, advertiseList, getActivity(),false);
                                } else {
                                    Utils.showShortToast(getActivity(), response.body().getMessage());
                                }
                                tvEmpty.setVisibility(advertiseList.size() > 0 ? View.GONE : View.VISIBLE);

                            } else {
                                Utils.showServerError(getActivity());
                            }
                            ((BaseActivity) getActivity()).hideProgressBar();

                        }

                        @Override
                        public void onFailure(final Call<SellerAdvertisementResponse> call, Throwable t) {
                            ((BaseActivity) getActivity()).hideProgressBar();
                            Utils.showShortToast(getActivity(), t.getMessage());
                        }
                    });

        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @BindView(R.id.tvEmpty)
    TextView tvEmpty;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    @Override
    public void onResume() {
        super.onResume();
        if(BaseActivity.isRefresh) {
            if (isFavorite) {
                apiGetFavoriteAds();
            } else {
                getSellerAdds(isSearch);
            }
            BaseActivity.isRefresh=false;
        }

    }
}
