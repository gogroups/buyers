package com.gogroup.app.gogroupapp.Seller;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gogroup.app.gogroupapp.Adapters.AdapterMyPurchases;
import com.gogroup.app.gogroupapp.Adapters.SliderAdapter;
import com.gogroup.app.gogroupapp.CallBackListeners.CallBackApi;
import com.gogroup.app.gogroupapp.CallBackListeners.CallBackOnAddressConfirm;
import com.gogroup.app.gogroupapp.CallBackListeners.CallBackOnQuantityConfirm;
import com.gogroup.app.gogroupapp.CallBackListeners.CallBackReport;
import com.gogroup.app.gogroupapp.Fragments.AddressDialog;
import com.gogroup.app.gogroupapp.Fragments.QuantityDialog;
import com.gogroup.app.gogroupapp.Fragments.ReportDialog;
import com.gogroup.app.gogroupapp.HelperClasses.ApiCalls;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.FontManager;
import com.gogroup.app.gogroupapp.HelperClasses.RestClient;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.GroupResponse;
import com.gogroup.app.gogroupapp.Responses.PostResponse;
import com.gogroup.app.gogroupapp.Responses.SellerAdvertisementResponse;
import com.gogroup.app.gogroupapp.User.MyPurchases;
import com.gogroup.app.gogroupapp.User.UserDashboardNew;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Response;

public class SellerAddDetailActivity extends BaseActivity implements CallBackReport, CallBackOnQuantityConfirm{

    public static final String DATA = "data";
    Dialog customDialog;
    int imagePos;
    SliderAdapter adapterImage;
    boolean isEnableStoragePermission, isRefresh, isUser, isDeepLinking;
    PopupWindow popupWindow;
    String sellerContact, sellerEmail, sellerAddress, sellerName, postAdvertisementId, postSellerId;
    Float sellerRating;
    List<GroupResponse> groupList = new ArrayList<>();
    Intent intentPhoneCall;
    Context context;
    String quant_per_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_ad_detail_new);
        ButterKnife.bind(this);
        setIcons();
        isUser = UserPreferences.getInstance().getUserType().trim().equalsIgnoreCase("user");
        tvContact.setText(isUser ? getString(R.string.contactSeller) : getString(R.string.couponGeneratedBy));
        tvGenerateCode.setText(isUser ? getString(R.string.interest) : getString(R.string.purchasedBy));
        customDialog = new Dialog(this);
        layoutParent.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isPermissionsAllowed()) {
                isEnableStoragePermission = true;
                showData();
            }
        } else {
            isEnableStoragePermission = true;
            showData();

        }
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                imagePos = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        try {
            isDeepLinking = getIntent().getBooleanExtra(DEEPLINK_DATA, false);
        } catch (NullPointerException e) {
            isDeepLinking = false;
        }

//        layoutMinUser.setVisibility(isUser ? View.GONE : View.VISIBLE);

//        layoutViews.setVisibility(isUser ? View.GONE : View.VISIBLE);
//        tvTotalGroups.setVisibility(View.GONE);


        tvRateSeller.setVisibility(isUser?View.VISIBLE:View.GONE);
    }

    void showData() {
        try {
            SellerAdvertisementResponse item = (SellerAdvertisementResponse) getIntent().getParcelableExtra(DATA);
            postAdvertisementId = item.getAdvertisementId();
            selectedAds = item;
            setValues();
            if (!isUser) {
//                apiGetTotalGroups(item);
            }
//            else {
            apiGetSingleAdvertisement(false);
//            }
        } catch (NullPointerException e) {
            postAdvertisementId = getIntent().getStringExtra(DATA);
            apiGetSingleAdvertisement(true);

        }

    }

//    void apiGetTotalGroups(final SellerAdvertisementResponse item) {
//        if (Utils.isInterNetConnected(this)) {
//            RestClient.get().getTotalGroup(UserPreferences.getInstance().getToken(), item.getCategoryId1()).enqueue(new Callback<GroupResponse>() {
//                @Override
//                public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
//                    if (response.body() != null) {
//                        if (response.body().getStatus()) {
//                            groupList = response.body().getListData() != null ? response.body().getListData() : new ArrayList<GroupResponse>();
//                            setTotalGroups(item.getCategoryId1(), 1);
//                            setTotalGroups(item.getCategoryId2(), 2);
//                            setTotalGroups(item.getCategoryId3(), 3);
//                            setTotalGroups(item.getCategoryId4(), 4);
//                            setTotalGroups(item.getCategoryId5(), 5);
//                            setTotalGroups(item.getCategoryId6(), 6);
//                        } else {
//                            Toast.makeText(getApplicationContext(), "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    } else {
//                        Toast.makeText(getApplicationContext(), "" + getString(R.string.serverError), Toast.LENGTH_LONG).show();
//
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<GroupResponse> call, Throwable t) {
//                    Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
//
//                }
//            });
//        }
//    }

//    private void setTotalGroups(String id, int type) {
//
//        tvTotalGroups.setVisibility(View.VISIBLE);
//        if (id == null || id.equals("0")) {
//            return;
//        }
//        int totalGroups = 0;
//        if (type == 1) {
//            tvTotalGroups.setText(groupList.size() + " " + getString(R.string.matchingCriteria2));
//            return;
//        }
//        for (int i = 0; i < groupList.size(); i++) {
//            Integer selectedId = null;
//            switch (type) {
//                case 2:
//                    selectedId = groupList.get(i).getCategoryId2();
//                    break;
//                case 3:
//                    selectedId = groupList.get(i).getCategoryId3();
//                    break;
//                case 4:
//                    selectedId = groupList.get(i).getCategoryId4();
//                    break;
//                case 5:
//                    selectedId = groupList.get(i).getCategoryId5();
//                    break;
//                case 6:
//                    selectedId = groupList.get(i).getCategoryId6();
//                    break;
//                default:
//                    break;
//            }
//            if (selectedId != null && selectedId != 0 && selectedId == Integer.parseInt(id)) {
//                ++totalGroups;
//            }
//        }
//        tvTotalGroups.setText(totalGroups + " " + getString(R.string.matchingCriteria2));
//
//
//    }

    SellerAdvertisementResponse selectedAds;

    private void setValues() {

        postSellerId = selectedAds.getSellerId();
        sellerRating = selectedAds.getSelfRating();
        adapterImage = new SliderAdapter(SellerAddDetailActivity.this, selectedAds.getImagesDetails());
        mPager.setAdapter(adapterImage);
        mPager.setOffscreenPageLimit(5);
        indicator.setViewPager(mPager);

       /* tvStartDate.setText(item.getStartDate());
        tvEndDate.setText(item.getEndDate());
        tvGroupNum.setText(item.getGroupCount() != null ? item.getGroupCount() : "0");
        tvViewsNum.setText(item.getViewsCount() != null ? item.getViewsCount() : "0");
        tvCategory1.setText(item.getCategory1());
        tvCategory2.setText("" + item.getCategory2());
        layoutCategory2.setVisibility(item.getCategory2() != null ? View.VISIBLE : View.GONE);
        tvCategory3.setText("" + item.getCategory3());
        layoutCategory3.setVisibility(item.getCategory3() != null ? View.VISIBLE : View.GONE);
        tvCategory4.setText("" + item.getCategory4());
        layoutCategory4.setVisibility(item.getCategory4() != null ? View.VISIBLE : View.GONE);
        tvCategory5.setText("" + item.getCategory5());
        layoutCategory5.setVisibility(item.getCategory5() != null ? View.VISIBLE : View.GONE);
        tvCategory6.setText("" + item.getCategory6());
        layoutCategory6.setVisibility(item.getCategory6() != null ? View.VISIBLE : View.GONE);
        layoutCouponCodeCount.setVisibility(isUser ? View.VISIBLE : View.GONE);*/

        layoutParent.setVisibility(View.VISIBLE);


        tvDealName.setText(selectedAds.getAdvertisementName());
        tvActualPrice.setText(selectedAds.getActualPrice());
        tvOfferPrice.setText(selectedAds.getOfferPrice());
        tvLocation.setText(selectedAds.getLocation());
        tvpricefortwo.setText(selectedAds.getCostfortwo());




        tvDate.setText("Valid from: " + selectedAds.getStartDate() + " - " + selectedAds.getEndDate());

        tvDealDetails.setText(selectedAds.getAdvertisementDetails());
        tvMinUsers.setText("Min users. " + selectedAds.getMinUserCount());
        tvCodeCount.setText(selectedAds.getPendingCodeCount() + " " + getString(R.string.generatedCoupons));

        imgLike.setImageDrawable(ContextCompat.getDrawable(this, selectedAds.getIsliked() == 0 ? R.drawable.ic_dislike : R.drawable.ic_like));
        tvLikes.setText(selectedAds.getLikedcount());

        quant_per_user = selectedAds.getQuantityPerUser();

        ratingAvg.setRating(selectedAds.getRating() != null ? selectedAds.getRating() : 0);

        sellerContact = selectedAds.getSellerContact() != null ? "+" + selectedAds.getSellerContact() : "";
        sellerEmail = selectedAds.getEmail() != null ? selectedAds.getEmail() : "";
        sellerName = selectedAds.getSellerName();
        sellerAddress = selectedAds.getAddress()+", "+selectedAds.getLocation();

        if (isUser&&selectedAds.getCouponCode() != null) {
            tvGenerateCode.setText(getString(R.string.couponCode) + " : " + selectedAds.getCouponCode());
            tvGenerateCode.setAlpha(0.7f);
        }

    }

    @OnClick(R.id.imgLike)
    void apiSetLike() {


        if (!isUser) {
            return;
        }

        if (imgLike.getAlpha() == 0.7f) {
            return;
        }

        imgLike.setAlpha(0.7f);
        final boolean isLiked = selectedAds.getIsliked() == 1;
        Map<String, String> postFields = new HashMap<>();
        postFields.put("status", "" + (isLiked ? 0 : 1));
        postFields.put("advertisement_id", selectedAds.getAdvertisementId());
        new ApiCalls(this).apiSetLike(postFields, new CallBackApi() {
            @Override
            public void onSuccess(Response response) {
                PostResponse body = (PostResponse) response.body();
                selectedAds.setIsliked(isLiked ? 0 : 1);
                selectedAds.setLikedcount(body.getLikedCount());
                imgLike.setAlpha(1f);
                setValues();
            }

            @Override
            public void onFailure(String message) {
                imgLike.setAlpha(1f);

            }

            @Override
            public void onRetryYes() {
                apiSetLike();
            }

            @Override
            public void onRetryNo() {
            }
        });
    }


    private void setIcons() {
        mButtonLeftArrow.setText(R.string.icon_ionicon_var_chevron_left);
        mButtonLeftArrow.setTypeface(FontManager.getTypeFaceFromFontName(getApplicationContext(), FontManager.IONICONFONT));
        mButtonLeftArrow.setTextSize(20);
        mButtonLeftArrow.setTextColor(ContextCompat.getColor(this, R.color.white_color));

//        tvShareIcon.setText(R.string.icon_ionicon_var_android_share_alt);
//        tvShareIcon.setTypeface(FontManager.getTypeFaceFromFontName(SellerAddDetailActivity.this, FontManager.IONICONFONT));
//        tvShareIcon.setTextSize(25);
//        tvShareIcon.setTextColor(ContextCompat.getColor(SellerAddDetailActivity.this, R.color.orange_color));
    }

    @OnClick(R.id.layoutShare)
    void shareData() {
//        String shareBody = "Here is the share content body";
//        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//        sharingIntent.setType("text/plain");
//        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
//        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//        startActivity(Intent.createChooser(sharingIntent, "Share Details"));

        if (isEnableStoragePermission) {
            if (adapterImage != null) {

                String imgPath = null;
                try {
                    imgPath = selectedAds.getImagesDetails().get(imagePos).getImagePath();
                } catch (Exception e) {
                }

                if (imgPath == null) {
                    return;
                }
                Picasso.with(getApplicationContext())
                        .load(imgPath)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                                if (isEnableStoragePermission) {
                                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                                    String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
                                    Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                                    String data = "\n" + tvDealName.getText().toString() + "\n"+tvDealDetails.getText().toString()+"\n\n";
                                    data = data + "https://m.gogroup.com/details?advertisement_id=" + postAdvertisementId + "\n\n";
                                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, data);
                                    shareIntent.putExtra(android.content.Intent.EXTRA_STREAM, Uri.parse(path));
                                    shareIntent.setType("image/*");
                                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    startActivity(Intent.createChooser(shareIntent, "Share via"));
                                }
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                            }
                        });
            }
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.validationPermission), Toast.LENGTH_LONG)
                    .show();
        }

    }


    @OnClick(R.id.imgOption)
    void menu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.report_menu, popup.getMenu());

        if (UserPreferences.getInstance().getUserType().equalsIgnoreCase("user")) {
            popup.getMenu().findItem(R.id.edit).setVisible(false);
        } else {
            popup.getMenu().findItem(R.id.report).setVisible(false);

        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.report:
                        ReportDialog reportDialog = ReportDialog.instance(SellerAddDetailActivity.this);
                        reportDialog.show(getSupportFragmentManager(), "Report");

                        break;
                    case R.id.edit:
                        Intent i = new Intent(SellerAddDetailActivity.this, AddAdvertisement.class);
                        i.putExtra(AddAdvertisement.EDIT, true);
                        i.putExtra(AddAdvertisement.ADVERTISEMENT_ID, postAdvertisementId);
                        startActivityForResult(i, BaseActivity.REFRESH_ACTIVITY);

                        break;
                    default:
                        break;

                }
                return true;
            }
        });

        popup.show();
    }


    @Override
    public void onClickReportSubmit(String report) {
        apiReport(report);

    }

    private void apiReport(String comment) {
        if (Utils.isInterNetConnected(SellerAddDetailActivity.this)) {
            showProgressbar();

            RestClient.get().report(UserPreferences.getInstance().getToken(), comment, postAdvertisementId, "ads").
                    enqueue(new retrofit2.Callback<PostResponse>() {
                        @Override
                        public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
                                    customDialog.dismiss();
                                }

                                Utils.showShortToast(SellerAddDetailActivity.this, response.body().getMessage());

                            } else {
                                Utils.showServerError(SellerAddDetailActivity.this);
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
                            Utils.showShortToast(SellerAddDetailActivity.this, t.getMessage());
                        }
                    });
        }
    }

    private void apiGetSingleAdvertisement(final boolean flag) {
        if (Utils.isInterNetConnected(SellerAddDetailActivity.this)) {
            if (flag) {
                showProgressbar();
            }
            RestClient.get().getAdvertisement(UserPreferences.getInstance().getToken(), postAdvertisementId).
                    enqueue(new retrofit2.Callback<SellerAdvertisementResponse>() {
                        @Override
                        public void onResponse(Call<SellerAdvertisementResponse> call, Response<SellerAdvertisementResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
                                    if (response.body().getData() != null && response.body().getData().size() > 0) {
                                        selectedAds = response.body().getData().get(0);
                                        setValues();
                                    }


                                } else {
                                    Utils.showShortToast(SellerAddDetailActivity.this, response.body().getMessage());
                                }
                            } else {
                                Utils.showServerError(SellerAddDetailActivity.this);
                            }
                            hideProgressBar();

                        }

                        @Override
                        public void onFailure(final Call<SellerAdvertisementResponse> call, Throwable t) {
                            hideProgressBar();
                            Utils.showShortToast(SellerAddDetailActivity.this, t.getMessage());
                        }
                    });
        }
    }

    @OnClick((R.id.vendor_home_filter_left_arrow))
    void back() {
        if (isRefresh) {
            Intent intent = new Intent();
            setResult(BaseActivity.REFRESH_DASHBOARD, intent);
        }

        if (isDeepLinking) {
            Intent intent = new Intent(this, UserDashboardNew.class);
            startActivity(intent);
            ActivityCompat.finishAffinity(this);
        } else {
            finish();
        }
    }

    @OnClick(R.id.tvContact)
    void submit() {

        if (!isUser) {
            startActivity(new Intent(this, BuyerActivity.class).putExtra(BaseActivity.ID, postAdvertisementId)
                    .putExtra(BaseActivity.IS_PURCHASED, false));
            return;
        }

        if (Utils.isInterNetConnected(this)) {
            RestClient.get().sendEmail(UserPreferences.getInstance().getToken(), sellerEmail, postAdvertisementId)
                    .enqueue(new retrofit2.Callback<PostResponse>() {
                        @Override
                        public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {

                                } else {
                                    Utils.showShortToast(getApplicationContext(), response.body().getMessage());
                                }
                            } else {
                                Utils.showServerError(getApplicationContext());
                            }
                        }

                        @Override
                        public void onFailure(final Call<PostResponse> call, Throwable t) {
                            Utils.showShortToast(getApplicationContext(), t.getMessage());
                        }
                    });
        }

        popupDetails();

    }

    void popupDetails() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup_details, null);
        boolean focusable = true; // lets taps outside the popup also dismiss it
        popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, focusable);
//        popupWindow.showAtLocation(imgOption, Gravity.NO_GRAVITY, 0, 0);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        Utils.dimBehind(popupWindow);
        final TextView tvSellerContactPop = popupView.findViewById(R.id.tvSellerContactPop);
        final TextView tvSellerEmailIDPop = popupView.findViewById(R.id.tvSellerEmailIDPop);
        TextView tvSellerName = popupView.findViewById(R.id.tvSellerName);
        TextView tvAddress = popupView.findViewById(R.id.tvAddress);
        //tvSellerContact.setText(tvSellerContact.getText());
        tvSellerContactPop.setText(sellerContact);
        tvSellerEmailIDPop.setText(sellerEmail);
        tvAddress.setText(sellerAddress);
        tvSellerName.setText(sellerName);
        tvSellerContactPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uri = "tel:" + tvSellerContactPop.getText().toString().trim();
                intentPhoneCall = new Intent(Intent.ACTION_DIAL);
                intentPhoneCall.setData(Uri.parse(uri));
                if (ActivityCompat.checkSelfPermission(SellerAddDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SellerAddDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                    return;
                }
                startActivity(intentPhoneCall);
            }
        });

        tvSellerEmailIDPop.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("email", tvSellerEmailIDPop.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(SellerAddDetailActivity.this, getString(R.string.copied), Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    @OnClick(R.id.tvRateSeller)
    void actionRateSeller() {

        if(selectedAds==null||selectedAds.getIsRatingOption()==0)
        {
            return;
        }

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.dialog_rate_seller, null);
        boolean focusable = true; // lets taps outside the popup also dismiss it
        popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, focusable);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        Utils.dimBehind(popupWindow);
        RatingBar ratingBar = popupView.findViewById(R.id.ratingBar);
        ratingBar.setRating(sellerRating != null ? sellerRating : 0);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(final RatingBar ratingBar, final float rating, boolean fromUser) {
                sellerRating = rating;
                selectedAds.setSelfRating(rating);
                Map<String, String> postFields = new HashMap<>();
                postFields.put("seller_id", postSellerId);
                postFields.put("rating", "" + rating);
                new ApiCalls(SellerAddDetailActivity.this).apiInsertRating(postFields, new CallBackApi() {
                    @Override
                    public void onSuccess(Response response) {
                        PostResponse body = (PostResponse) response.body();
                        selectedAds.setRating(body.getRating());
                        ratingAvg.setRating(body.getRating() != null ? body.getRating() : 0);
                    }
                    @Override
                    public void onFailure(String message) {
                    }
                    @Override
                    public void onRetryYes() {
                    }
                    @Override
                    public void onRetryNo() {
                    }
                });

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
//                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    //getPhoto();
                    isEnableStoragePermission = true;
                    showData();

                } else {
                    isEnableStoragePermission = false;
                    Toast.makeText(getApplicationContext(), getString(R.string.validationPermission), Toast.LENGTH_LONG)
                            .show();
                }
            }
            break;
            case REQUEST_PHONE_CALL:
                Map<String, Integer> perms = new HashMap<String, Integer>();

                for (int i = 0; i < permissions.length; i++) {
                    perms.put(permissions[i], grantResults[i]);
                    if (perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        startActivity(intentPhoneCall);
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.validationPermissionPhoneCall), Toast.LENGTH_LONG).show();
                    }
                }
                break;


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == BaseActivity.REFRESH_ACTIVITY) {
            isRefresh = true;

            String info[] = data.getStringArrayExtra(BaseActivity.DATA);

            if (info != null) {

//                tvEndDate.setText(info[0]);
//                tvCategory1.setText(info[1]);
//                tvCategory2.setText(info[2]);
//                tvCategory3.setText(info[3]);
//                tvCategory4.setText(info[4]);
//                tvCategory5.setText(info[5]);
//                tvCategory6.setText(info[6]);
//
//                layoutCategory2.setVisibility(info[2].equals("") ? View.GONE : View.VISIBLE);
//                layoutCategory3.setVisibility(info[3].equals("") ? View.GONE : View.VISIBLE);
//                layoutCategory4.setVisibility(info[4].equals("") ? View.GONE : View.VISIBLE);
//                layoutCategory5.setVisibility(info[5].equals("") ? View.GONE : View.VISIBLE);
//                layoutCategory6.setVisibility(info[6].equals("") ? View.GONE : View.VISIBLE);

                tvLocation.setText(info[7]);
                tvMinUsers.setText( "Min users. " + info[8]);
//                tvTotalGroups.setText(info[9]);
                tvDealName.setText(info[10]);
                tvDealDetails.setText(info[11]);
                tvActualPrice.setText(info[12]);
                tvOfferPrice.setText(info[13]);
                tvpricefortwo.setText(info[14]);


            }
        } else {
            isRefresh = false;
        }
    }

    @OnClick(R.id.tvGenerateCode)
    void generateCouponCode() {

        if (!isUser) {
            startActivity(new Intent(this, BuyerActivity.class).putExtra(BaseActivity.ID, postAdvertisementId)
                    .putExtra(BaseActivity.IS_PURCHASED, true));
            return;
        }

        if (tvGenerateCode.getText().toString().contains(":")) {
            return;
        }


        FragmentManager imanager = (SellerAddDetailActivity.this).getSupportFragmentManager();
        QuantityDialog dialogFragment = QuantityDialog.getInstance(SellerAddDetailActivity.this);
        //QuantityDialog.setData(item);
        dialogFragment.show(imanager, "quantity");

    }

    public String sendItem()
    {
        return quant_per_user;
    }

    void GenerateCoupon(final String quantity)
    {
        Map<String, String> postFields = new HashMap<>();
        postFields.put("seller_id", postSellerId);
        postFields.put("advertisement_id", postAdvertisementId);
        postFields.put("quantity",quantity);
        showProgressbar();
        new ApiCalls(this).apiGenerateCouponCode(postFields, new CallBackApi() {
            @Override
            public void onSuccess(Response response) {
                PostResponse body = (PostResponse) response.body();
                Toast.makeText(SellerAddDetailActivity.this, "" + body.getMessage(), Toast.LENGTH_LONG).show();
                tvGenerateCode.setText(getString(R.string.couponCode) + " : " + body.getCouponCode());
                tvGenerateCode.setAlpha(0.7f);
                BaseActivity.isRefresh = true;
                Intent i = new Intent(SellerAddDetailActivity.this, MyPurchases.class);
                startActivity(i);
                hideProgressBar();
            }

            @Override
            public void onFailure(String message) {
                hideProgressBar();
            }

            @Override
            public void onRetryYes() {
                GenerateCoupon(quantity);
            }

            @Override
            public void onRetryNo() {
            }
        });
    }







    @BindView(R.id.vendor_home_filter_left_arrow)
    Button mButtonLeftArrow;
    //    @BindView(R.id.tvShareIcon)
//    TextView tvShareIcon;
    @BindView(R.id.pager)
    ViewPager mPager;
    //    @BindView(R.id.tvStartDate)
//    TextView tvStartDate;
//    @BindView(R.id.tvEndDate)
//    TextView tvEndDate;
    @BindView(R.id.tvDate)
    TextView tvDate;
    //    @BindView(R.id.tvGroupNum)
//    TextView tvGroupNum;
//    @BindView(R.id.tvViewsNum)
//    TextView tvViewsNum;
    @BindView(R.id.tvLocation)
    TextView tvLocation;
    //    @BindView(R.id.tvCategory1)
//    TextView tvCategory1;
//    @BindView(R.id.tvCategory2)
//    TextView tvCategory2;
//    @BindView(R.id.tvCategory3)
//    TextView tvCategory3;
//    @BindView(R.id.tvCategory4)
//    TextView tvCategory4;
//    @BindView(R.id.tvCategory5)
//    TextView tvCategory5;
    @BindView(R.id.tvDealDetails)
    ExpandableTextView tvDealDetails;
    @BindView(R.id.tvDealName)
    TextView tvDealName;
    //    @BindView(R.id.tvCategory6)
//    TextView tvCategory6;
//    @BindView(R.id.layoutCategory2)
//    LinearLayout layoutCategory2;
//    @BindView(R.id.layoutCategory3)
//    LinearLayout layoutCategory3;
//    @BindView(R.id.layoutCategory4)
//    LinearLayout layoutCategory4;
//    @BindView(R.id.layoutCategory5)
//    LinearLayout layoutCategory5;
//    @BindView(R.id.layoutCategory6)
//    LinearLayout layoutCategory6;
    @BindView(R.id.layoutParent)
    LinearLayout layoutParent;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    //    @BindView(R.id.layoutMinUser)
//    LinearLayout layoutMinUser;
//    @BindView(R.id.layoutDealDetails)
//    LinearLayout layoutDealDetails;
//    @BindView(R.id.layoutViews)
//    LinearLayout layoutViews;
    @BindView(R.id.tvMinUsers)
    TextView tvMinUsers;
    //    @BindView(R.id.tvTotalGroups)
//    TextView tvTotalGroups;
    @BindView(R.id.tvOfferPrice)
    TextView tvOfferPrice;
    @BindView(R.id.tvActualPrice)
    TextView tvActualPrice;
    @BindView(R.id.tvpricefortwo)
    TextView tvpricefortwo;
    @BindView(R.id.tvContact)
    TextView tvContact;
    @BindView(R.id.tvLikes)
    TextView tvLikes;
    @BindView(R.id.imgLike)
    ImageView imgLike;
    @BindView(R.id.tvGenerateCode)
    TextView tvGenerateCode;
    @BindView(R.id.tvRateSeller)
    TextView tvRateSeller;
    @BindView(R.id.tvCodeCount)
    TextView tvCodeCount;
    @BindView(R.id.ratingBar)
    RatingBar ratingAvg;

    @Override
    public void onQuantityConfirm(String quantity) {
        GenerateCoupon(quantity);
    }
//    @BindView(R.id.layoutCouponCodeCount)
//    LinearLayout layoutCouponCodeCount;


}