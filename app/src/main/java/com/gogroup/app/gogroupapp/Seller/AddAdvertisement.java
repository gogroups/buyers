package com.gogroup.app.gogroupapp.Seller;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gogroup.app.gogroupapp.Adapters.AdapterAddPhotos;
import com.gogroup.app.gogroupapp.Adapters.GooglePlacesAutocompleteAdapter;
import com.gogroup.app.gogroupapp.CallBackListeners.CallBackRecyclerItemClick;
import com.gogroup.app.gogroupapp.CustomViews.CustomSpinner;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.FontManager;
import com.gogroup.app.gogroupapp.HelperClasses.RestClient;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.CategoryResponse;
import com.gogroup.app.gogroupapp.Responses.GroupResponse;
import com.gogroup.app.gogroupapp.Responses.PostResponse;
import com.gogroup.app.gogroupapp.Responses.SellerAdvertisementResponse;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAdvertisement extends BaseActivity implements CallBackRecyclerItemClick, DatePickerDialog.OnDateSetListener {
    public static final String ADVERTISEMENT_ID = "advertisementId";
    Calendar calendarStarts, calendarEnds;
    DateFormat dateFormatPost = new SimpleDateFormat("yyyy-MM-dd");
    public Map<String, RequestBody> postFields = new HashMap<String, RequestBody>();
    public static final String EDIT = "edit";
    public static final String SELLER_ID = "sellerId";
    AdapterAddPhotos adapterAddPhotos;
    GridLayoutManager gridLayoutManager;
    ArrayList<Uri> uriImageList = new ArrayList<>();
    ArrayAdapter<String> spinnerAdapter;
    String[] arrCategory1, arrCategory2, arrCategory3, arrCategory4, arrCategory5, arrCategory6;
    String[] arrId1, arrId2, arrId3, arrId4, arrId5, arrId6;
    private static final int MAX_IMG_COUNT = 5;
    boolean isStart = false, isEdit, isLoadingDone;
    String postAdvertisementId;
    List<GroupResponse> groupList = new ArrayList<>();
    SellerAdvertisementResponse selectedAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_advertisement);
        ButterKnife.bind(this);

        try {
            isEdit = getIntent().getBooleanExtra(EDIT, false);
            postAdvertisementId = getIntent().getStringExtra(ADVERTISEMENT_ID);

        } catch (Exception e) {
        }

        setIcons();
        layoutSpinner2.setVisibility(View.GONE);
        layoutSpinner3.setVisibility(View.GONE);
        layoutSpinner4.setVisibility(View.GONE);
        layoutSpinner5.setVisibility(View.GONE);
        layoutSpinner6.setVisibility(View.GONE);
        tvTotalGroups.setVisibility(View.GONE);
        gridLayoutManager = new GridLayoutManager(AddAdvertisement.this, 2);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(AddAdvertisement.this, LinearLayoutManager.HORIZONTAL, false);
        recycler.setLayoutManager(gridLayoutManager);
//        recycler.setNestedScrollingEnabled(false);
        adapterAddPhotos = new AdapterAddPhotos();
        recycler.setAdapter(adapterAddPhotos);
        calendarStarts = Calendar.getInstance();
        calendarEnds = Calendar.getInstance();


        if (isEdit) {
            tvTitle.setText(getString(R.string.editAdvertisement));
            btnSubmit.setText(getString(R.string.update));
            apiGetSellerAdvertisement();
        } else {
            isLoadingDone=true;
            etLocation.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.spinner_item));
            getCategory1();

        }

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    getCategory2(arrId1[spinner1.getSelectedItemPosition()]);
                    apiGetTotalGroups(arrId1[spinner1.getSelectedItemPosition()]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    getCategory3(arrId2[spinner2.getSelectedItemPosition()]);
                    setTotalGroups(arrId2[spinner2.getSelectedItemPosition()], 2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    getCategory4(arrId3[spinner3.getSelectedItemPosition()]);
                    setTotalGroups(arrId3[spinner3.getSelectedItemPosition()], 3);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    getCategory5(arrId4[spinner4.getSelectedItemPosition()]);
                    setTotalGroups(arrId4[spinner4.getSelectedItemPosition()], 4);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    getCategory6(arrId5[spinner5.getSelectedItemPosition()]);
                    setTotalGroups(arrId5[spinner5.getSelectedItemPosition()], 5);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    setTotalGroups(arrId6[spinner6.getSelectedItemPosition()], 6);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    void apiGetTotalGroups(final String id) {
        if (Utils.isInterNetConnected(this)) {
            RestClient.get().getTotalGroup(UserPreferences.getInstance().getToken(), id).enqueue(new Callback<GroupResponse>() {
                @Override
                public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                    if (response.body() != null) {
                        if (response.body().getStatus()) {

                            groupList = response.body().getListData() != null ? response.body().getListData() : new ArrayList<GroupResponse>();

                            setTotalGroups(id, 1);
                        } else {
                            //Toast.makeText(AddAdvertisement.this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    } else {
                        Toast.makeText(AddAdvertisement.this, "" + getString(R.string.serverError), Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<GroupResponse> call, Throwable t) {
                    Toast.makeText(AddAdvertisement.this, "" + t.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        }
    }

    private void setTotalGroups(String id, int type) {
        tvTotalGroups.setVisibility(View.VISIBLE);
        int totalGroups = 0;
        if (type == 1) {
            tvTotalGroups.setText(groupList.size() + " " + getString(R.string.matchingCriteria2));
            return;
        }
        for (int i = 0; i < groupList.size(); i++) {
            Integer selectedId = null;
            switch (type) {
                case 2:
                    selectedId = groupList.get(i).getCategoryId2();
                    break;
                case 3:
                    selectedId = groupList.get(i).getCategoryId3();
                    break;
                case 4:
                    selectedId = groupList.get(i).getCategoryId4();
                    break;
                case 5:
                    selectedId = groupList.get(i).getCategoryId5();
                    break;
                case 6:
                    selectedId = groupList.get(i).getCategoryId6();
                    break;
                default:
                    break;
            }
            if (selectedId != null && selectedId != 0 && selectedId == Integer.parseInt(id)) {
                ++totalGroups;
            }
        }
        tvTotalGroups.setText(totalGroups + " " + getString(R.string.matchingCriteria2));


    }

    private void apiGetSellerAdvertisement() {

        if (Utils.isInterNetConnected(this)) {
            showProgressbar();
            RestClient.get().getSellerAdvertisement(UserPreferences.getInstance().getToken(), postAdvertisementId).
                    enqueue(new retrofit2.Callback<SellerAdvertisementResponse>() {
                        @Override
                        public void onResponse(Call<SellerAdvertisementResponse> call, Response<SellerAdvertisementResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {

                                    setValues(response.body().getAdvertisementData());
                                } else {
                                    Utils.showShortToast(AddAdvertisement.this, response.body().getMessage());
                                }
                            } else {
                                Utils.showServerError(AddAdvertisement.this);
                            }
//                            hideProgressBar();
                        }


                        @Override
                        public void onFailure(Call<SellerAdvertisementResponse> call, Throwable t) {
                            hideProgressBar();
                            Utils.showShortToast(AddAdvertisement.this, t.getMessage());
                        }
                    });
        }
    }

    private void setValues(SellerAdvertisementResponse data) {

        selectedAdd = data.getDetails();

        getCategory1();

        if (data.getImagesDetails() != null) {
            adapterAddPhotos.setData(AddAdvertisement.this, null, data.getImagesDetails(), this);
        }
        tvPicTitle.setText(getString(R.string.uploadedPic));
        etMinUsers.setText(selectedAdd.getMinUserCount());
        etDealName.setText(selectedAdd.getAdvertisementName());
        etDetails.setText(selectedAdd.getAdvertisementDetails());
        etLocation.setText(selectedAdd.getLocation());
        etActualPrice.setText(selectedAdd.getActualPrice());
        etpricefortwo.setText(selectedAdd.getCostfortwo());
        etOfferPrice.setText(selectedAdd.getOfferPrice());
        etLocation.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.spinner_item));

        etStarts.setText(selectedAdd.getStartDate());
        etStarts.setEnabled(false);
        etEnds.setText(selectedAdd.getEndDate());


    }

    private void getCategory1() {


        if (Utils.isInterNetConnected(AddAdvertisement.this)) {
            showProgressbar();
            RestClient.get().getCategory1(UserPreferences.getInstance().getToken()).
                    enqueue(new retrofit2.Callback<CategoryResponse>() {
                        @Override
                        public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
                                    arrCategory1 = new String[response.body().getData().size() + 1];
                                    arrId1 = new String[response.body().getData().size() + 1];
                                    arrCategory1[0] = getResources().getString(R.string.selectCategory);
                                    arrId1[0] = "";
                                    for (int i = 0; i < response.body().getData().size(); i++) {
                                        arrCategory1[i + 1] = response.body().getData().get(i).getCategoryTitle();
                                        arrId1[i + 1] = response.body().getData().get(i).getCategoryId();
                                    }
                                    getSpinner(arrCategory1);
                                    spinner1.setAdapter(spinnerAdapter);

                                    if (isEdit && selectedAdd != null) {
                                        apiGetTotalGroups(selectedAdd.getCategoryId1());
                                        setSelectedSpinner(spinner1, arrId1, selectedAdd.getCategoryId1().trim(), 1);
                                        selectedAdd.setCategoryId1(null);
                                    }

                                } else {
                                    Utils.showShortToast(AddAdvertisement.this, response.body().getMessage());
                                    hideProgressBar();
                                    isLoadingDone = true;
                                }
                            } else {
                                Utils.showServerError(AddAdvertisement.this);
                                hideProgressBar();
                            }
                            if (isLoadingDone) {
                                hideProgressBar();
                            }
                        }

                        @Override
                        public void onFailure(Call<CategoryResponse> call, Throwable t) {
                            hideProgressBar();
                            Utils.showShortToast(AddAdvertisement.this, t.getMessage());
                        }
                    });
        }
    }

    private void getCategory2(String id) {
        if (Utils.isInterNetConnected(AddAdvertisement.this)) {
            showProgressbar();
            RestClient.get().getCategory2(UserPreferences.getInstance().getToken(), id).
                    enqueue(new retrofit2.Callback<CategoryResponse>() {
                        @Override
                        public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
                                    layoutSpinner2.setVisibility(View.VISIBLE);
                                    if (response.body().getData().size() > 0) {
                                        arrCategory2 = new String[response.body().getData().size() + 1];
                                        arrId2 = new String[response.body().getData().size() + 1];
                                        arrCategory2[0] = getResources().getString(R.string.selectSubCategory1);
                                        arrId2[0] = "";
                                        for (int i = 0; i < response.body().getData().size(); i++) {
                                            arrCategory2[i + 1] = response.body().getData().get(i).getCategoryTitle();
                                            arrId2[i + 1] = response.body().getData().get(i).getCategoryId();
                                        }
                                        getSpinner(arrCategory2);
                                        spinner2.setAdapter(spinnerAdapter);
                                        if (isEdit && selectedAdd != null) {
                                            setSelectedSpinner(spinner2, arrId2, selectedAdd.getCategoryId2(), 2);
                                            selectedAdd.setCategoryId2(null);

                                        }
                                    }


                                } else {
                                    layoutSpinner2.setVisibility(View.GONE);
                                    hideProgressBar();
                                    isLoadingDone = true;
                                }
                                layoutSpinner3.setVisibility(View.GONE);
                                layoutSpinner4.setVisibility(View.GONE);
                                layoutSpinner5.setVisibility(View.GONE);
                                layoutSpinner6.setVisibility(View.GONE);
                                spinner3.setAdapter(null);
                                spinner4.setAdapter(null);
                                spinner5.setAdapter(null);
                                spinner6.setAdapter(null);
                            } else {
                                Utils.showServerError(AddAdvertisement.this);
                                hideProgressBar();
                            }
                            if (isLoadingDone) {
                                hideProgressBar();
                            }
                        }

                        @Override
                        public void onFailure(Call<CategoryResponse> call, Throwable t) {
                            hideProgressBar();
                            Utils.showShortToast(AddAdvertisement.this, t.getMessage());
                        }
                    });
        }
    }

    private void getCategory3(String id) {
        if (Utils.isInterNetConnected(AddAdvertisement.this)) {
            showProgressbar();
            RestClient.get().getCategory3(UserPreferences.getInstance().getToken(), id).
                    enqueue(new retrofit2.Callback<CategoryResponse>() {
                        @Override
                        public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
                                    layoutSpinner3.setVisibility(View.VISIBLE);
                                    if (response.body().getData().size() > 0) {
                                        arrCategory3 = new String[response.body().getData().size() + 1];
                                        arrId3 = new String[response.body().getData().size() + 1];
                                        arrCategory3[0] = getResources().getString(R.string.selectSubCategory2);
                                        arrId3[0] = "";
                                        for (int i = 0; i < response.body().getData().size(); i++) {
                                            arrCategory3[i + 1] = response.body().getData().get(i).getCategoryTitle();
                                            arrId3[i + 1] = response.body().getData().get(i).getCategoryId();
                                        }
                                        getSpinner(arrCategory3);
                                        spinner3.setAdapter(spinnerAdapter);
                                        if (isEdit && selectedAdd != null) {
                                            setSelectedSpinner(spinner3, arrId3, selectedAdd.getCategoryId3(), 3);
                                            selectedAdd.setCategoryId3(null);

                                        }
                                    }

                                } else {
                                    layoutSpinner3.setVisibility(View.GONE);
                                    hideProgressBar();
                                    isLoadingDone = true;
                                }
                                layoutSpinner4.setVisibility(View.GONE);
                                layoutSpinner5.setVisibility(View.GONE);
                                layoutSpinner6.setVisibility(View.GONE);
                                spinner4.setAdapter(null);
                                spinner5.setAdapter(null);
                                spinner6.setAdapter(null);
                            } else {
                                Utils.showServerError(AddAdvertisement.this);
                                hideProgressBar();
                            }
                            if (isLoadingDone) {
                                hideProgressBar();
                            }
                        }

                        @Override
                        public void onFailure(Call<CategoryResponse> call, Throwable t) {
                            hideProgressBar();
                            Utils.showShortToast(AddAdvertisement.this, t.getMessage());
                        }
                    });
        }
    }

    private void getCategory4(String id) {
        if (Utils.isInterNetConnected(AddAdvertisement.this)) {
            showProgressbar();
            RestClient.get().getCategory4(UserPreferences.getInstance().getToken(), id).
                    enqueue(new retrofit2.Callback<CategoryResponse>() {
                        @Override
                        public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
                                    layoutSpinner4.setVisibility(View.VISIBLE);
                                    if (response.body().getData().size() > 0) {
                                        arrCategory4 = new String[response.body().getData().size() + 1];
                                        arrId4 = new String[response.body().getData().size() + 1];
                                        arrCategory4[0] = getResources().getString(R.string.selectSubCategory3);
                                        arrId4[0] = "";
                                        for (int i = 0; i < response.body().getData().size(); i++) {
                                            arrCategory4[i + 1] = response.body().getData().get(i).getCategoryTitle();
                                            arrId4[i + 1] = response.body().getData().get(i).getCategoryId();
                                        }
                                        getSpinner(arrCategory4);
                                        spinner4.setAdapter(spinnerAdapter);
                                        if (isEdit && selectedAdd != null) {
                                            setSelectedSpinner(spinner4, arrId4, selectedAdd.getCategoryId4(), 4);
                                            selectedAdd.setCategoryId4(null);

                                        }
                                    }
                                } else {
                                    layoutSpinner4.setVisibility(View.GONE);
                                    hideProgressBar();
                                    isLoadingDone = true;
                                }
                                layoutSpinner5.setVisibility(View.GONE);
                                layoutSpinner6.setVisibility(View.GONE);
                                spinner5.setAdapter(null);
                                spinner6.setAdapter(null);
                            } else {
                                Utils.showServerError(AddAdvertisement.this);
                                hideProgressBar();
                            }
                            if (isLoadingDone) {
                                hideProgressBar();
                            }
                        }

                        @Override
                        public void onFailure(Call<CategoryResponse> call, Throwable t) {
                            hideProgressBar();
                            Utils.showShortToast(AddAdvertisement.this, t.getMessage());
                        }
                    });
        }
    }

    private void getCategory5(String id) {
        if (Utils.isInterNetConnected(AddAdvertisement.this)) {
            showProgressbar();
            RestClient.get().getCategory5(UserPreferences.getInstance().getToken(), id).
                    enqueue(new retrofit2.Callback<CategoryResponse>() {
                        @Override
                        public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
                                    layoutSpinner5.setVisibility(View.VISIBLE);
                                    if (response.body().getData().size() > 0) {
                                        arrCategory5 = new String[response.body().getData().size() + 1];
                                        arrId5 = new String[response.body().getData().size() + 1];
                                        arrCategory5[0] = getResources().getString(R.string.selectSubCategory4);
                                        arrId5[0] = "";
                                        for (int i = 0; i < response.body().getData().size(); i++) {
                                            arrCategory5[i + 1] = response.body().getData().get(i).getCategoryTitle();
                                            arrId5[i + 1] = response.body().getData().get(i).getCategoryId();
                                        }
                                        getSpinner(arrCategory5);
                                        spinner5.setAdapter(spinnerAdapter);
                                        if (isEdit && selectedAdd != null) {
                                            setSelectedSpinner(spinner5, arrId5, selectedAdd.getCategoryId5(), 5);
                                            selectedAdd.setCategoryId5(null);

                                        }
                                    }
                                } else {
                                    layoutSpinner5.setVisibility(View.GONE);
                                    hideProgressBar();
                                    isLoadingDone = true;
                                }
                                layoutSpinner6.setVisibility(View.GONE);
                                spinner6.setAdapter(null);
                            } else {
                                Utils.showServerError(AddAdvertisement.this);
                                hideProgressBar();
                            }
                            if (isLoadingDone) {

                                hideProgressBar();
                            }
                        }

                        @Override
                        public void onFailure(Call<CategoryResponse> call, Throwable t) {
                            hideProgressBar();
                            Utils.showShortToast(AddAdvertisement.this, t.getMessage());
                        }
                    });
        }
    }

    private void getCategory6(String id) {
        if (Utils.isInterNetConnected(AddAdvertisement.this)) {
            showProgressbar();
            RestClient.get().getCategory6(UserPreferences.getInstance().getToken(), id).
                    enqueue(new retrofit2.Callback<CategoryResponse>() {
                        @Override
                        public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
                                    layoutSpinner6.setVisibility(View.VISIBLE);
                                    if (response.body().getData().size() > 0) {
                                        arrCategory6 = new String[response.body().getData().size() + 1];
                                        arrId6 = new String[response.body().getData().size() + 1];
                                        arrCategory6[0] = getResources().getString(R.string.selectSubCategory5);
                                        arrId6[0] = "";
                                        for (int i = 0; i < response.body().getData().size(); i++) {
                                            arrCategory6[i + 1] = response.body().getData().get(i).getCategoryTitle();
                                            arrId6[i + 1] = response.body().getData().get(i).getCategoryId();
                                        }
                                        getSpinner(arrCategory6);
                                        spinner6.setAdapter(spinnerAdapter);
                                        if (isEdit && selectedAdd != null) {
                                            setSelectedSpinner(spinner6, arrId6, selectedAdd.getCategoryId6(), 6);
                                            selectedAdd.setCategoryId6(null);
                                        }
                                    } else {
                                        layoutSpinner6.setVisibility(View.GONE);
                                        hideProgressBar();
                                        isLoadingDone = true;
                                    }
                                }
                            } else {
                                Utils.showServerError(AddAdvertisement.this);
                                hideProgressBar();
                            }
                            if (isLoadingDone) {

                                hideProgressBar();
                            }
                        }

                        @Override
                        public void onFailure(Call<CategoryResponse> call, Throwable t) {
                            hideProgressBar();
                            Utils.showShortToast(AddAdvertisement.this, t.getMessage());
                        }
                    });
        }
    }

    void setSelectedSpinner(Spinner spinner, String[] arrId, String selectedId, int num) {

        if (selectedId != null && spinner.getAdapter() != null && arrId.length > 0) {
            for (int i = 0; i < arrId.length; i++) {
                if (arrId[i].equals(selectedId)) {
                    spinner.setSelection(i);
                    setTotalGroups(arrId[i], num);
                    return;
                }
            }
        }
        hideProgressBar();
    }


    private void setIcons() {
        btnBack.setText(R.string.icon_ionicon_var_chevron_left);
        btnBack.setTypeface(FontManager.getTypeFaceFromFontName(getApplicationContext(), FontManager.IONICONFONT));
        btnBack.setTextSize(20);
        btnBack.setTextColor(ContextCompat.getColor(this, R.color.white_color));
    }


    @OnClick(R.id.layoutPics)
    void callGallery() {
        if (!isEdit) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (isPermissionsAllowed()) {
                    openGallery(uriImageList);
                }

            } else {
                openGallery(uriImageList);
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
//                perms.put(android.Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    openGallery(uriImageList);
                } else {
                    // Permission Denied
                    Toast.makeText(getApplicationContext(), getString(R.string.validationPermission), Toast.LENGTH_LONG)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void openGallery(ArrayList<Uri> uriImageList) {
        FishBun.with(AddAdvertisement.this)
                .MultiPageMode()
                .setMaxCount(MAX_IMG_COUNT)
                .setPickerSpanCount(4)
                .setActionBarColor(ContextCompat.getColor(this, R.color.white_color), ContextCompat.getColor(this, R.color.white), true)
                .setActionBarTitleColor(ContextCompat.getColor(this, R.color.black_color))
                .setArrayPaths(uriImageList)
                .setAlbumSpanCount(2, 4)
                .setButtonInAlbumActivity(true)
                .setCamera(false)
                .setReachLimitAutomaticClose(false)
                .setHomeAsUpIndicatorDrawable(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black))
                .setOkButtonDrawable(ContextCompat.getDrawable(this, R.drawable.ic_check_black))
                .setAllViewTitle("All of your photos")
                .setActionBarTitle("Select photo ")
                .textOnImagesSelectionLimitReached("You can't select any more.")
                .textOnNothingSelected("Please select at least one photo")
                .startAlbum();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageData) {
        super.onActivityResult(requestCode, resultCode, imageData);
        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    uriImageList = imageData.getParcelableArrayListExtra(Define.INTENT_PATH);
                    adapterAddPhotos.setData(AddAdvertisement.this, uriImageList, null, this);
                    break;
                }

        }

    }

    @OnClick(R.id.btnBack)
    void back() {

        finish();
    }


    @Override
    public void onItemClick(View v, int position) {
        if (position == -1) {
            openGallery(uriImageList);
        } else {
            uriImageList.remove(position);
            adapterAddPhotos.setData(AddAdvertisement.this, uriImageList, null, this);
        }
    }

    public void getSpinner(String[] values) {
        spinnerAdapter = new ArrayAdapter<String>(AddAdvertisement.this, R.layout.spinner_item, values) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }
        };
    }

    @OnClick(R.id.etStarts)
    void setStartDate() {
        isStart = true;
        Date newDate = new Date();
        openCalendar(newDate);
    }

    @OnClick(R.id.etEnds)
    void setEndDate() {

        isStart = false;
        Date newDate = new Date();
        if (!etStarts.getText().toString().equals("")) {
            try {
                newDate = dateFormatPost.parse(etStarts.getText().toString().trim());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        openCalendar(newDate);
    }

    void openCalendar(Date date) {
        Calendar calendar = isStart ? calendarStarts : calendarEnds;
        DatePickerDialog dialog = new DatePickerDialog(AddAdvertisement.this, this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(date.getTime());
        // dialog.getDatePicker().setMinDate(newDate.getTime() - (newDate.getTime() % (24 * 60 * 60 * 1000)));
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        if (isStart) {
            calendarStarts.set(Calendar.YEAR, year);
            calendarStarts.set(Calendar.MONTH, month);
            calendarStarts.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            etStarts.setText(dateFormatPost.format(calendarStarts.getTime()));
            etStarts.setError(null);
            if (!etEnds.getText().toString().trim().equals("")) {
                Date sd, ed;
                try {
                    ed = dateFormatPost.parse(etEnds.getText().toString().trim());
                    sd = dateFormatPost.parse(etStarts.getText().toString().trim());
                    if (sd.after(ed)) {
                        etEnds.setText("");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        } else {
            calendarEnds.set(Calendar.YEAR, year);
            calendarEnds.set(Calendar.MONTH, month);
            calendarEnds.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            etEnds.setText(dateFormatPost.format(calendarEnds.getTime()));
            etEnds.setError(null);
        }


    }

    @OnClick(R.id.btnSubmit)
    void submit() {
        if (isEdit) {
            editAdd();
        } else {
            createAdd();
        }
    }

    void editAdd() {
        if (Utils.isInterNetConnected(AddAdvertisement.this)) {

            if (!isValid()) {
                return;
            }

            Map<String, String> postFields = new HashMap<String, String>();

            final String id1 = arrId1[spinner1.getSelectedItemPosition()],
                    id2 = spinner2.getAdapter() != null ?arrId2[spinner2.getSelectedItemPosition()]:"",
                    id3 = spinner3.getAdapter() != null ? arrId3[spinner3.getSelectedItemPosition()] : "",
                    id4 = spinner4.getAdapter() != null ? arrId4[spinner4.getSelectedItemPosition()] : "",
                    id5 = spinner5.getAdapter() != null ? arrId5[spinner5.getSelectedItemPosition()] : "",
                    id6 = spinner6.getAdapter() != null ? arrId6[spinner6.getSelectedItemPosition()] : "";
            final String cg1 = arrCategory1[spinner1.getSelectedItemPosition()],
                    cg2 = spinner2.getAdapter() != null ? spinner2.getSelectedItemPosition() > 0 ? arrCategory2[spinner2.getSelectedItemPosition()] : "" : "",
                    cg3 = spinner3.getAdapter() != null ? spinner3.getSelectedItemPosition() > 0 ? arrCategory3[spinner3.getSelectedItemPosition()] : "" : "",
                    cg4 = spinner4.getAdapter() != null ? spinner4.getSelectedItemPosition() > 0 ? arrCategory4[spinner4.getSelectedItemPosition()] : "" : "",
                    cg5 = spinner5.getAdapter() != null ? spinner5.getSelectedItemPosition() > 0 ? arrCategory5[spinner5.getSelectedItemPosition()] : "" : "",
                    cg6 = spinner6.getAdapter() != null ? spinner6.getSelectedItemPosition() > 0 ? arrCategory6[spinner6.getSelectedItemPosition()] : "" : "";


            postFields.clear();
            postFields.put("advertisement_id", postAdvertisementId);
            postFields.put("category_id", id1);
            postFields.put("subcategory_id", id2 != null ? id2 : "");
            postFields.put("subcategory_id2", id3 != null ? id3 : "");
            postFields.put("subcategory_id3", id4 != null ? id4 : "");
            postFields.put("subcategory_id4", id5 != null ? id5 : "");
            postFields.put("subcategory_id5", id6 != null ? id6 : "");
            postFields.put("location", etLocation.getText().toString().trim());
            postFields.put("advertisement_name", etDealName.getText().toString().trim());
            postFields.put("deal_details", etDetails.getText().toString());
            postFields.put("end_date", etEnds.getText().toString().trim());
            postFields.put("min_user_count", etMinUsers.getText().toString().trim());
            postFields.put("actual_price",etActualPrice.getText().toString().trim());
            postFields.put("offer_price", etOfferPrice.getText().toString().trim());
            postFields.put("costfor_two", etpricefortwo.getText().toString().trim());
            showProgressbar();
            RestClient.get().editAdvertisement(UserPreferences.getInstance().getToken(), postFields).
                    enqueue(new Callback<PostResponse>() {
                        @Override
                        public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
                                    Intent intent = new Intent();
                                    String[] data = {response.body().getData().getEndDate(), cg1, cg2, cg3, cg4, cg5, cg6, etLocation.getText().toString(),
                                            etMinUsers.getText().toString().replaceFirst("^0*", ""), tvTotalGroups.getText().toString(),
                                    etDealName.getText().toString(),etDetails.getText().toString(),etActualPrice.getText().toString(),etOfferPrice.getText().toString(),etpricefortwo.getText().toString()};
                                    intent.putExtra(BaseActivity.DATA, data);
                                    setResult(BaseActivity.REFRESH_ACTIVITY, intent);
                                    finish();
                                }
                                Utils.showShortToast(AddAdvertisement.this, response.body().getMessage());

                            } else {
                                Utils.showServerError(AddAdvertisement.this);
                            }
                            hideProgressBar();

                        }

                        @Override
                        public void onFailure(Call<PostResponse> call, Throwable t) {
                            hideProgressBar();
                            Utils.showShortToast(AddAdvertisement.this, t.getMessage());
                        }
                    });

        }
    }

    void createAdd() {
        if (isValid()) {

            String id2 = arrId2[spinner2.getSelectedItemPosition()],
                    id3 = spinner3.getAdapter() != null ? arrId3[spinner3.getSelectedItemPosition()] : "",
                    id4 = spinner4.getAdapter() != null ? arrId4[spinner4.getSelectedItemPosition()] : "",
                    id5 = spinner5.getAdapter() != null ? arrId5[spinner5.getSelectedItemPosition()] : "",
                    id6 = spinner6.getAdapter() != null ? arrId6[spinner6.getSelectedItemPosition()] : "";


            postFields.clear();
            postFields.put("category_id", Utils.requestBody(arrId1[spinner1.getSelectedItemPosition()]));
            postFields.put("subcategory_id", Utils.requestBody(id2 != null ? id2 : ""));
            postFields.put("subcategory_id2", Utils.requestBody(id3 != null ? id3 : ""));
            postFields.put("subcategory_id3", Utils.requestBody(id4 != null ? id4 : ""));
            postFields.put("subcategory_id4", Utils.requestBody(id5 != null ? id5 : ""));
            postFields.put("subcategory_id5", Utils.requestBody(id6 != null ? id6 : ""));
            postFields.put("location", Utils.requestBody(etLocation.getText().toString().trim()));
            postFields.put("advertisement_name", Utils.requestBody(etDealName.getText().toString().trim()));
            postFields.put("deal_details", Utils.requestBody(etDetails.getText().toString().trim()));
            postFields.put("start_date", Utils.requestBody(etStarts.getText().toString().trim()));
            postFields.put("end_date", Utils.requestBody(etEnds.getText().toString().trim()));
            postFields.put("min_user_count", Utils.requestBody(etMinUsers.getText().toString().trim()));
            postFields.put("actual_price", Utils.requestBody(etActualPrice.getText().toString().trim()));
            postFields.put("offer_price", Utils.requestBody(etOfferPrice.getText().toString().trim()));
            postFields.put("costfor_two", Utils.requestBody(etpricefortwo.getText().toString().trim()));
            List<MultipartBody.Part> multipartImg = new ArrayList<>();
            for (int i = 0; i < uriImageList.size(); i++) {
                File file = Utils.reduceImageSize(new File(Utils.getPath(uriImageList.get(i), AddAdvertisement.this)));
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part multipart = MultipartBody.Part.createFormData("uploaded_file[" + i + "]", file.getName(), requestBody);
                multipartImg.add(multipart);
            }
            if (Utils.isInterNetConnected(AddAdvertisement.this)) {
                showProgressbar();
                RestClient.get().createAdvertisement(UserPreferences.getInstance().getToken(), multipartImg, postFields).
                        enqueue(new Callback<PostResponse>() {
                            @Override
                            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                                if (response.body() != null) {
                                    if (response.body().isStatus()) {
                                        Intent intent = new Intent();
                                        setResult(BaseActivity.REFRESH_DASHBOARD, intent);
                                        finish();

                                    }
                                    Utils.showShortToast(AddAdvertisement.this, response.body().getMessage());

                                } else {
                                    Utils.showServerError(AddAdvertisement.this);
                                }
                                hideProgressBar();

                            }

                            @Override
                            public void onFailure(Call<PostResponse> call, Throwable t) {
                                hideProgressBar();
                                Utils.showShortToast(AddAdvertisement.this, t.getMessage());
                            }
                        });

            }
        }
    }

    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.tvPicTitle)
    TextView tvPicTitle;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.btnBack)
    Button btnBack;
    @BindView(R.id.spinner1)
    CustomSpinner spinner1;
    @BindView(R.id.spinner2)
    CustomSpinner spinner2;
    @BindView(R.id.spinner3)
    CustomSpinner spinner3;
    @BindView(R.id.spinner4)
    CustomSpinner spinner4;
    @BindView(R.id.spinner5)
    CustomSpinner spinner5;
    @BindView(R.id.spinner6)
    CustomSpinner spinner6;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.layoutSpinner2)
    LinearLayout layoutSpinner2;
    @BindView(R.id.layoutSpinner3)
    LinearLayout layoutSpinner3;
    @BindView(R.id.layoutSpinner4)
    LinearLayout layoutSpinner4;
    @BindView(R.id.layoutSpinner5)
    LinearLayout layoutSpinner5;
    @BindView(R.id.layoutSpinner6)
    LinearLayout layoutSpinner6;
    @BindView(R.id.etStarts)
    EditText etStarts;
    @BindView(R.id.etEnds)
    EditText etEnds;
    @BindView(R.id.etLocation)
    AutoCompleteTextView etLocation;
    @BindView(R.id.etDetails)
    EditText etDetails;
    @BindView(R.id.etDealName)
    EditText etDealName;
    @BindView(R.id.etMinUsers)
    EditText etMinUsers;
    @BindView(R.id.etActualPrice)
    EditText etActualPrice;
    @BindView(R.id.etOfferPrice)
    EditText etOfferPrice;
    @BindView(R.id.etpricefortwo)
    EditText etpricefortwo;
    @BindView(R.id.tvTotalGroups)
    TextView tvTotalGroups;

    private boolean
    isValid() {
        if (etDealName.getText().toString().trim().length() <= 0) {
            Utils.setEditTextError(etDealName, getResources().getString(R.string.validationDealName));
            return false;
        } else  if (spinner1.getAdapter() == null || spinner1.getSelectedItemPosition() == 0) {
            Utils.showShortToast(AddAdvertisement.this, getResources().getString(R.string.validationCategory));
            return false;
        } else if (etLocation.getText().toString().trim().length() <= 0) {
            Utils.setEditTextError(etLocation, getResources().getString(R.string.validationLocation));
            return false;
        }else  if (etActualPrice.getText().toString().trim().length() <= 0) {
            Utils.setEditTextError(etActualPrice, getResources().getString(R.string.validationActualPrice));
            return false;
        }else  if (etOfferPrice.getText().toString().trim().length() <= 0) {
            Utils.setEditTextError(etOfferPrice, getResources().getString(R.string.validationOfferPrice));
            return false;
        }else  if (etpricefortwo.getText().toString().trim().length() <= 0) {
            Utils.setEditTextError(etpricefortwo, getResources().getString(R.string.validationcostfortwo));
            return false;
        }else  if (etDetails.getText().toString().trim().length() <= 0) {
            Utils.setEditTextError(etDetails, getResources().getString(R.string.validationDealDetails));
            return false;

        } else if (etStarts.getText().toString().trim().length() <= 0) {
            Utils.showShortToast(AddAdvertisement.this, getResources().getString(R.string.validationStartDate));
            return false;
        } else if (etEnds.getText().toString().trim().length() <= 0) {
            Utils.showShortToast(AddAdvertisement.this, getResources().getString(R.string.validationEndDate));
            return false;
        } else if (etMinUsers.getText().toString().trim().length() <= 0) {
            Utils.setEditTextError(etMinUsers, getResources().getString(R.string.validationMinUsers));
            return false;
        } else if (!isEdit && uriImageList.size() == 0) {
            Utils.showShortToast(AddAdvertisement.this, getResources().getString(R.string.validationPhoto));
            return false;
        }
        return true;
    }


}
