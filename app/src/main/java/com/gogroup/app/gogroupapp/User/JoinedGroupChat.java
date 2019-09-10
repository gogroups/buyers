package com.gogroup.app.gogroupapp.User;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.Adapters.AdapterShowPhoto;
import com.gogroup.app.gogroupapp.Adapters.GalleryAdapter;
import com.gogroup.app.gogroupapp.Adapters.GroupchatAdapter;
import com.gogroup.app.gogroupapp.CallBackListeners.CallBackReport;
import com.gogroup.app.gogroupapp.Fragments.ReportDialog;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.FontManager;
import com.gogroup.app.gogroupapp.HelperClasses.RestClient;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.Models.ChatModel;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.GroupDetailResponse;
import com.gogroup.app.gogroupapp.Responses.GroupListResponse;
import com.gogroup.app.gogroupapp.Responses.ImageResponse;
import com.gogroup.app.gogroupapp.Responses.PostResponse;
import com.gogroup.app.gogroupapp.Seller.SellerAddDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

public class JoinedGroupChat extends BaseActivity implements CallBackReport{

    public static final String CATEGORY_ID = "categoryId";
    public static final String GROUP_ID = "groupId";
    String postCategoryId, postGroupId;
    Dialog customDialog;
    List<ImageResponse> listAdvertisementExpand = new ArrayList<>();
    List<ImageResponse> listAdvertisementCollapse = new ArrayList<>();
    boolean isCollapse = true;

    ArrayList<Integer> image3 = new ArrayList<>();
    GalleryAdapter galleryAdapter;
    GroupchatAdapter groupchatAdapter;
    ArrayList<ChatModel> chatlist = new ArrayList<ChatModel>();
    boolean isDeepLinking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_joined_group_chat);
        ButterKnife.bind(this);
        setIcons();
        customDialog = new Dialog(JoinedGroupChat.this);
        postCategoryId = getIntent().getStringExtra(CATEGORY_ID);
        postGroupId = getIntent().getStringExtra(GROUP_ID);
        layoutParent.setVisibility(View.GONE);
        imgOption.setVisibility(View.GONE);
        apiGetGroupDetail();


        galleryAdapter = new GalleryAdapter(JoinedGroupChat.this, image3);

        messageListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        messageListView.setStackFromBottom(true);
        getChat();

        groupchatAdapter = new GroupchatAdapter(JoinedGroupChat.this, chatlist);
        messageListView.setAdapter(groupchatAdapter);
        try {
            isDeepLinking = getIntent().getBooleanExtra(DEEPLINK_DATA, false);
        } catch (NullPointerException e) {
            isDeepLinking = false;
        }

    }

    private void setIcons() {
        btnBack.setText(R.string.icon_ionicon_var_chevron_left);
        btnBack.setTypeface(FontManager.getTypeFaceFromFontName(getApplicationContext(), FontManager.IONICONFONT));
        btnBack.setTextSize(25);
        btnBack.setTextColor(ContextCompat.getColor(this, R.color.white_color));
    }

    public void getChat() {

        chatlist.add(new ChatModel("Sachin", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s", 0, "4:05 PM"));
        chatlist.add(new ChatModel("Vipul", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.", 0, "5:05 PM"));
        chatlist.add(new ChatModel("Me", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s", 1, "5:05 PM"));
        chatlist.add(new ChatModel("Sachin", "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s", 0, "5:15 PM"));
        chatlist.add(new ChatModel("Me", " has been the industry's standard dummy text ever since the 1500s", 1, "7:05 PM"));
    }

    private void apiGetGroupDetail() {
        if (Utils.isInterNetConnected(JoinedGroupChat.this)) {
            showProgressbar();

            RestClient.get().getGroupDetails(UserPreferences.getInstance().getToken(), postCategoryId, postGroupId).
                    enqueue(new retrofit2.Callback<GroupDetailResponse>() {
                        @Override
                        public void onResponse(Call<GroupDetailResponse> call, Response<GroupDetailResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {

                                    setValues(response.body().getData());


                                } else {
                                    Utils.showShortToast(JoinedGroupChat.this, response.body().getMessage());
                                }

                            } else {
                                Utils.showServerError(JoinedGroupChat.this);
                            }
                            hideProgressBar();

                        }

                        @Override
                        public void onFailure(final Call<GroupDetailResponse> call, Throwable t) {
                            hideProgressBar();
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
                            Utils.showShortToast(JoinedGroupChat.this, t.getMessage());
                        }
                    });

        }

    }


    private void setValues(GroupDetailResponse data) {

        GroupListResponse item = data.getGroupDetails();

        tvMembers.setText("Total Member(s): " + (item.getJoinMember() != null ? item.getJoinMember() : "0"));
        tvGroupName.setText(item.getGroupName() != null ? item.getGroupName() : "N/A");


        if (item.getGroupImage() != null && item.getGroupImage().trim() != "") {
            Picasso.with(getApplicationContext()).load(item.getGroupImage()).into(imgProfile);
        }

//        LinearLayoutManager layoutManager= new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
//        recyclerMembers.setLayoutManager(layoutManager);
//        recyclerMembers.setAdapter(new AdapterMember(JoinedGroupChat.this,data.getMemberList()));

        GridLayoutManager layoutManager2 = new GridLayoutManager(getApplicationContext(), 3);
        recycler.setLayoutManager(layoutManager2);
        listAdvertisementExpand = data.getAdvertisementList();
        listAdvertisementCollapse = new ArrayList<>(listAdvertisementExpand.size() > 3 ?
                listAdvertisementExpand.subList(0, 3) : listAdvertisementExpand);
//        recycler.setAdapter(new AdapterShowPhoto(JoinedGroupChat.this, listAdvertisementCollapse, true));//comment
        tvViewAll.setVisibility(listAdvertisementExpand.size() > 3 ? View.VISIBLE : View.GONE);
        tvTotalOffer.setText("Total " + listAdvertisementExpand.size() + " Offers");
        tvTotalOffer.setVisibility(listAdvertisementExpand.size() == 0 ? View.GONE : View.VISIBLE);

        imgOption.setVisibility(item.getCreatedBy() != null && !item.getCreatedBy().trim().equals(UserPreferences.getInstance().getUserId())
                ? View.VISIBLE : View.GONE);

        layoutParent.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tvViewAll)
    void viewAll() {
        if (isCollapse) {
//            recycler.setAdapter(new AdapterShowPhoto(JoinedGroupChat.this, listAdvertisementExpand, true));//comment
            isCollapse = false;
            tvViewAll.setText("View less");
        } else {
//            recycler.setAdapter(new AdapterShowPhoto(JoinedGroupChat.this, listAdvertisementCollapse, true)); //comment
            isCollapse = true;
            tvViewAll.setText("View all");


        }

    }

    @OnClick(R.id.imgOption)
    void menu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.group_chat_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.exitGroup:
                        apiExitGroup();
                        break;
                    case R.id.report:

                        ReportDialog reportDialog =  ReportDialog.instance(JoinedGroupChat.this);
                        reportDialog.show(getSupportFragmentManager(), "Report");
                        break;
                    default:
                        break;

                }
                return true;
            }
        });

        popup.show();
    }


    private void apiReport(String comment) {
        if (Utils.isInterNetConnected(JoinedGroupChat.this)) {
            showProgressbar();

            RestClient.get().report(UserPreferences.getInstance().getToken(), comment, postGroupId, "group").
                    enqueue(new retrofit2.Callback<PostResponse>() {
                        @Override
                        public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {

                                    customDialog.dismiss();


                                }

                                Utils.showShortToast(JoinedGroupChat.this, response.body().getMessage());

                            } else {
                                Utils.showServerError(JoinedGroupChat.this);
                            }
                            hideProgressBar();

                        }

                        @Override
                        public void onFailure(final Call<PostResponse> call, Throwable t) {
                            hideProgressBar();
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
                            Utils.showShortToast(JoinedGroupChat.this, t.getMessage());
                        }
                    });
        }
    }

    private void apiExitGroup() {
        if (Utils.isInterNetConnected(JoinedGroupChat.this)) {
            showProgressbar();

            RestClient.get().exitGroup(UserPreferences.getInstance().getToken(), postGroupId).
                    enqueue(new retrofit2.Callback<PostResponse>() {
                        @Override
                        public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {

                                    Intent intent = new Intent();
                                    setResult(BaseActivity.REFRESH_LIST, intent);
                                    finish();


                                } else {
                                    Utils.showShortToast(JoinedGroupChat.this, response.body().getMessage());
                                }

                            } else {
                                Utils.showServerError(JoinedGroupChat.this);
                            }
                            hideProgressBar();

                        }

                        @Override
                        public void onFailure(final Call<PostResponse> call, Throwable t) {
                            hideProgressBar();
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
                            Utils.showShortToast(JoinedGroupChat.this, t.getMessage());
                        }
                    });

        }
    }

    @OnClick(R.id.btnBack)
    void back() {
        if (isDeepLinking) {
            Intent intent = new Intent(this, UserDashboardNew.class);
            startActivity(intent);
            ActivityCompat.finishAffinity(this);
        } else {
            Intent intent = new Intent();
            setResult(BaseActivity.REFRESH_ACTIVITY, intent);
            finish();
        }
    }

    @OnClick(R.id.group_name_layout)
    void groupInfoLayout() {
        Intent i = new Intent(JoinedGroupChat.this, JoinedGroupDetails.class);
        i.putExtra(JoinedGroupDetails.CATEGORY_ID, postCategoryId);
        i.putExtra(JoinedGroupDetails.GROUP_ID, postGroupId);
        startActivity(i);
    }

    ;
    @BindView(R.id.btnBack)
    Button btnBack;
    @BindView(R.id.messagesContainer)
    ListView messageListView;
    @BindView(R.id.layoutParent)
    LinearLayout layoutParent;
    @BindView(R.id.tvGroupName)
    TextView tvGroupName;
    @BindView(R.id.tvTotalOffer)
    TextView tvTotalOffer;
    @BindView(R.id.tvViewAll)
    TextView tvViewAll;
    @BindView(R.id.tvMembers)
    TextView tvMembers;
    @BindView(R.id.imgProfile)
    CircleImageView imgProfile;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.imgOption)
    ImageView imgOption;

    @Override
    public void onClickReportSubmit(String report) {
        apiReport(report);
    }
}
