package com.gogroup.app.gogroupapp.Fragments;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.Adapters.AdapterShowPhoto;
import com.gogroup.app.gogroupapp.HelperClasses.RestClient;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.GroupDetailResponse;
import com.gogroup.app.gogroupapp.Responses.GroupListResponse;
import com.gogroup.app.gogroupapp.Responses.ImageResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;


public class GroupChatAddsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String CATEGORY_ID = "categoryId";
    public static final String GROUP_ID = "groupId";
    private static Activity activity;
    String postCategoryId, postGroupId;
    List<ImageResponse> listAdvertisementExpand = new ArrayList<>();
    List<ImageResponse> listAdvertisementCollapse = new ArrayList<>();
    boolean isCollapse = true,isHideKeyboard=true;

    public static GroupChatAddsFragment newInstance(String postCategoryId, String postGroupId, Activity parentAct) {
        GroupChatAddsFragment fragment = new GroupChatAddsFragment();
        Bundle args = new Bundle();
        args.putString(CATEGORY_ID, postCategoryId);
        args.putString(GROUP_ID, postGroupId);
        fragment.setArguments(args);
        activity = parentAct;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postCategoryId = getArguments().getString(CATEGORY_ID);
            postGroupId = getArguments().getString(GROUP_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_group_chat_adds, container, false);
        ButterKnife.bind(this, view);
        layoutParent.setVisibility(View.GONE);
//        imgOption.setVisibility(View.GONE);
        apiGetGroupDetail();

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                view.getWindowVisibleDisplayFrame(r);
                if (view.getRootView().getHeight() - (r.bottom - r.top) > 500) { // if more than 100 pixels, its probably a keyboard...
                    if (!isCollapse&&isHideKeyboard) {
                        isHideKeyboard=false;
//                        recycler.setAdapter(new AdapterShowPhoto(activity, listAdvertisementCollapse, true));//comment
                        isCollapse = true;
                        tvViewAll.setText("View all");
                    }
                } else {
                    isHideKeyboard=true;
                }
            }
        });
        return view;
    }

    private void apiGetGroupDetail() {
        if (Utils.isInterNetConnected(activity)) {
            // showProgressbar();

            RestClient.get().getGroupDetails(UserPreferences.getInstance().getToken(), postCategoryId, postGroupId).
                    enqueue(new retrofit2.Callback<GroupDetailResponse>() {
                        @Override
                        public void onResponse(Call<GroupDetailResponse> call, Response<GroupDetailResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {

                                    if (response.body().getData() != null) {
                                        setValues(response.body().getData());
                                    }


                                } else {
                                    Utils.showShortToast(activity, response.body().getMessage());
                                }

                            } else {
                                Utils.showServerError(activity);
                            }
                            //   hideProgressBar();

                        }

                        @Override
                        public void onFailure(final Call<GroupDetailResponse> call, Throwable t) {
                            //          hideProgressBar();
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
                            Utils.showShortToast(activity, t.getMessage());
                        }
                    });

        }

    }


    private void setValues(GroupDetailResponse data) {

        GroupListResponse item = data.getGroupDetails();

//        tvMembers.setText("Total Member(s): " + (item.getJoinMember() != null ? item.getJoinMember() : "0"));
        //tvGroupName.setText(item.getGroupName() != null ? item.getGroupName() : "N/A");


        if (item.getGroupImage() != null && item.getGroupImage().trim() != "") {
//            Picasso.with(activity).load(item.getGroupImage()).into(imgProfile);
        }

//        LinearLayoutManager layoutManager= new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
//        recyclerMembers.setLayoutManager(layoutManager);
//        recyclerMembers.setAdapter(new AdapterMember(activity,data.getMemberList()));

        GridLayoutManager layoutManager2 = new GridLayoutManager(activity, 3);
        recycler.setLayoutManager(layoutManager2);
        listAdvertisementExpand = data.getAdvertisementList();

        layoutParent.setVisibility(listAdvertisementExpand.size() > 0 ? View.VISIBLE : View.GONE);

        listAdvertisementCollapse = new ArrayList<>(listAdvertisementExpand.size() > 3 ?
                listAdvertisementExpand.subList(0, 3) : listAdvertisementExpand);
//        recycler.setAdapter(new AdapterShowPhoto(activity, listAdvertisementCollapse, true));//comment
        tvViewAll.setVisibility(listAdvertisementExpand.size() > 3 ? View.VISIBLE : View.GONE);
        tvTotalOffer.setText("Total " + listAdvertisementExpand.size() + " Offers");
        tvTotalOffer.setVisibility(listAdvertisementExpand.size() == 0 ? View.GONE : View.VISIBLE);

//        imgOption.setVisibility(item.getCreatedBy() != null && !item.getCreatedBy().trim().equals(UserPreferences.getInstance().getUserId())
//                ? View.VISIBLE : View.GONE);


    }

    @OnClick(R.id.tvViewAll)
    void viewAll() {
        if (isCollapse) {
//            recycler.setAdapter(new AdapterShowPhoto(activity, listAdvertisementExpand, true));//comment
            isCollapse = false;
            tvViewAll.setText("View less");
            Utils.hideKeyboard(getActivity());
        } else {
//            recycler.setAdapter(new AdapterShowPhoto(activity, listAdvertisementCollapse, true));//comment
            isCollapse = true;
            tvViewAll.setText("View all");


        }

    }


    @BindView(R.id.layoutParent)
    LinearLayout layoutParent;


    @BindView(R.id.tvTotalOffer)
    TextView tvTotalOffer;
    @BindView(R.id.tvViewAll)
    TextView tvViewAll;


    @BindView(R.id.recycler)
    RecyclerView recycler;


}
