package com.gogroup.app.gogroupapp.User;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gogroup.app.gogroupapp.Adapters.GooglePlacesAutocompleteAdapter;
import com.gogroup.app.gogroupapp.CustomViews.CustomSpinner;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.FontManager;
import com.gogroup.app.gogroupapp.HelperClasses.RestClient;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.CategoryResponse;
import com.gogroup.app.gogroupapp.Responses.GroupDetailResponse;
import com.gogroup.app.gogroupapp.Responses.GroupListResponse;
import com.gogroup.app.gogroupapp.Responses.PostResponse;
import com.mikelau.croperino.Croperino;
import com.mikelau.croperino.CroperinoConfig;
import com.mikelau.croperino.CroperinoFileUtil;
import com.squareup.picasso.Picasso;

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
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateGroup extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    public static final String EDIT = "edit";
    public static final String GROUP_ID = "groupId";
    public static final String CATEGORY_ID = "groupId";
    String postGroupId, previousCategoryId;
    boolean isEdit;
    Calendar calendarStarts, calendarEnds;
    DateFormat dateFormatPost = new SimpleDateFormat("yyyy-MM-dd");
    Uri uriProfileImage;
    ArrayAdapter<String> spinnerAdapter;
    String[] arrCategory1, arrCategory2, arrCategory3, arrCategory4, arrCategory5, arrCategory6;
    String[] arrId1 = new String[0], arrId2 = new String[0], arrId3 = new String[0], arrId4 = new String[0],
            arrId5 = new String[0], arrId6 = new String[0];
    boolean isStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        ButterKnife.bind(this);
        setIcons();
        try {
            isEdit = getIntent().getBooleanExtra(EDIT, false);
            postGroupId = getIntent().getStringExtra(GROUP_ID);

        } catch (Exception e) {
        }
        if (isEdit) {
            tvTitle.setText(getString(R.string.editInterest));
            btnCreate.setText(getString(R.string.update));
            apiGetGroupDetails();

        } else {
            previousCategoryId = getIntent().getStringExtra(CATEGORY_ID);
            etLocation.setAdapter(new GooglePlacesAutocompleteAdapter(CreateGroup.this, R.layout.spinner_item));
            getCategory1();

        }


        layoutSpinner2.setVisibility(View.GONE);
        layoutSpinner3.setVisibility(View.GONE);
        layoutSpinner4.setVisibility(View.GONE);
        layoutSpinner5.setVisibility(View.GONE);
        layoutSpinner6.setVisibility(View.GONE);
        calendarStarts = Calendar.getInstance();
        calendarEnds = Calendar.getInstance();

        List<String> unitValues = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            unitValues.add(i + "0");
            unitValues.add(i + "00");
            unitValues.add(i + "000");
            unitValues.add(i + "0000");
            unitValues.add(i + "00000");
            unitValues.add(i + "000000");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, unitValues);
        etCostRange.setThreshold(1);
        etCostRange.setAdapter(adapter);
        etCostRange.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (String.valueOf(charSequence).length() == 5) {
//                    String value = etCostRange.getText().toString();
//                    etCostRange.setText(value.substring(0,2)+","+value.substring(2,value.length()));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    getCategory2(arrId1[spinner1.getSelectedItemPosition()]);
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
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setIcons() {
        btnBack.setText(R.string.icon_ionicon_var_chevron_left);
        btnBack.setTypeface(FontManager.getTypeFaceFromFontName(getApplicationContext(), FontManager.IONICONFONT));
        btnBack.setTextSize(20);
        btnBack.setTextColor(ContextCompat.getColor(this, R.color.white_color));
    }

    private void apiGetGroupDetails() {

        if (Utils.isInterNetConnected(CreateGroup.this)) {
            showProgressbar();
            RestClient.get().getGroupDetailForEdit(UserPreferences.getInstance().getToken(), postGroupId).
                    enqueue(new retrofit2.Callback<GroupDetailResponse>() {
                        @Override
                        public void onResponse(Call<GroupDetailResponse> call, Response<GroupDetailResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {

                                    setValues(response.body().getGroupDetails());
                                } else {
                                    Utils.showShortToast(CreateGroup.this, response.body().getMessage());
                                }
                            } else {
                                Utils.showServerError(CreateGroup.this);
                            }
                            hideProgressBar();
                        }


                        @Override
                        public void onFailure(Call<GroupDetailResponse> call, Throwable t) {
                            hideProgressBar();
                            Utils.showShortToast(CreateGroup.this, t.getMessage());
                        }
                    });
        }
    }

    private void setValues(GroupListResponse item) {

        if (item.getCategory1() != null) {
            getSpinner(new String[]{item.getCategory1()});
            spinner1.setAdapter(spinnerAdapter);
        }
        if (item.getCategory2() != null) {
            layoutSpinner2.setVisibility(View.VISIBLE);
            getSpinner(new String[]{item.getCategory2()});
            spinner2.setAdapter(spinnerAdapter);
        }
        if (item.getCategory3() != null) {
            layoutSpinner3.setVisibility(View.VISIBLE);
            getSpinner(new String[]{item.getCategory3()});
            spinner3.setAdapter(spinnerAdapter);
        }
        if (item.getCategory4() != null) {
            layoutSpinner4.setVisibility(View.VISIBLE);
            getSpinner(new String[]{item.getCategory4()});
            spinner4.setAdapter(spinnerAdapter);
        }
        if (item.getCategory5() != null) {
            layoutSpinner5.setVisibility(View.VISIBLE);
            getSpinner(new String[]{item.getCategory5()});
            spinner5.setAdapter(spinnerAdapter);
        }
        if (item.getCategory6() != null) {
            layoutSpinner6.setVisibility(View.VISIBLE);
            getSpinner(new String[]{item.getCategory6()});
            spinner6.setAdapter(spinnerAdapter);
        }


//        for (int i = 0; i < arrId1.length; i++) {
//            if (arrId1[i].equals(item.getCategoryId())) {
//                spinner1.setSelection(i);
//                break;
//            }
//        }
//
//        layoutSpinner2.setVisibility(View.GONE);
//        for (int i = 0; i < arrId2.length; i++) {
//            if (arrId2[i].equals(item.getCategoryId2())) {
//                spinner2.setSelection(i);
//                layoutSpinner2.setVisibility(View.VISIBLE);
//                break;
//            }
//        }
//        layoutSpinner3.setVisibility(View.GONE);
//        for (int i = 0; i < arrId3.length; i++) {
//            if (arrId3[i].equals(item.getCategoryId3())) {
//                spinner3.setSelection(i);
//                layoutSpinner3.setVisibility(View.VISIBLE);
//                break;
//            }
//        }
//        layoutSpinner4.setVisibility(View.GONE);
//        for (int i = 0; i < arrId4.length; i++) {
//            if (arrId4[i].equals(item.getCategoryId4())) {
//                spinner4.setSelection(i);
//                layoutSpinner4.setVisibility(View.VISIBLE);
//                break;
//            }
//        }
//        layoutSpinner5.setVisibility(View.GONE);
//        for (int i = 0; i < arrId5.length; i++) {
//            if (arrId5[i].equals(item.getCategoryId5())) {
//                spinner5.setSelection(i);
//                layoutSpinner5.setVisibility(View.VISIBLE);
//                break;
//            }
//        }
//        layoutSpinner6.setVisibility(View.GONE);
//        for (int i = 0; i < arrId6.length; i++) {
//            if (arrId6[i].equals(item.getCategoryId6())) {
//                spinner6.setSelection(i);
//                layoutSpinner6.setVisibility(View.VISIBLE);
//                break;
//            }
//        }
        if (item.getGroupImage() != null && item.getGroupImage() != "") {
            Picasso.with(getApplicationContext()).load(item.getGroupImage()).into(imgProfile);
        }
        etGroupName.setText(item.getGroupName());
        etCostRange.setText(item.getCostRange());
        etLocation.setText(item.getLocation());
//        etNumOfMember.setText(item.getMembersCount());
        etDesc.setText(item.getDescription());
//        etStarts.setText(item.getStartDate());
//        etStarts.setEnabled(false);
        etEnds.setText(item.getEndDate());
        etGroupName.setEnabled(false);
        etCostRange.setEnabled(false);
//        etNumOfMember.setEnabled(false);
        etDesc.setEnabled(false);
        etLocation.setEnabled(false);
        spinner1.setEnabled(false);
        spinner2.setEnabled(false);
        spinner3.setEnabled(false);
        spinner4.setEnabled(false);
        spinner5.setEnabled(false);
        spinner6.setEnabled(false);
        spinner1.setAlpha(0.5f);
        spinner2.setAlpha(0.5f);
        spinner3.setAlpha(0.5f);
        spinner4.setAlpha(0.5f);
        spinner5.setAlpha(0.5f);
        spinner6.setAlpha(0.5f);

    }


    private void getCategory1() {

        if (Utils.isInterNetConnected(CreateGroup.this)) {
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
                                    for (int i = 0; i < arrId1.length; i++) {
                                        if (previousCategoryId == null) {
                                            break;
                                        }
                                        if (previousCategoryId.equals(arrId1[i])) {
                                            spinner1.setSelection(i);
                                            spinner1.setClickable(false);
                                            spinner1.setEnabled(false);
                                            spinner1.setFocusable(false);
                                            spinner1.setFocusableInTouchMode(false);
                                            break;
                                        }
                                    }

                                } else {
                                    Utils.showShortToast(CreateGroup.this, response.body().getMessage());
                                }
                            } else {
                                Utils.showServerError(CreateGroup.this);
                            }
                            hideProgressBar();
                        }


                        @Override
                        public void onFailure(Call<CategoryResponse> call, Throwable t) {
                            hideProgressBar();
                            Utils.showShortToast(CreateGroup.this, t.getMessage());
                        }
                    });
        }
    }

    private void getCategory2(String id) {

        if (Utils.isInterNetConnected(CreateGroup.this)) {
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


                                    }
                                } else {
                                    layoutSpinner2.setVisibility(View.GONE);
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
                                Utils.showServerError(CreateGroup.this);
                            }
                            hideProgressBar();
                        }

                        @Override
                        public void onFailure(Call<CategoryResponse> call, Throwable t) {
                            hideProgressBar();
                            Utils.showShortToast(CreateGroup.this, t.getMessage());
                        }
                    });
        }
    }

    private void getCategory3(String id) {

        if (Utils.isInterNetConnected(CreateGroup.this)) {
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
                                    }

                                } else {
                                    layoutSpinner3.setVisibility(View.GONE);
                                }
                                layoutSpinner4.setVisibility(View.GONE);
                                layoutSpinner5.setVisibility(View.GONE);
                                layoutSpinner6.setVisibility(View.GONE);
                                spinner4.setAdapter(null);
                                spinner5.setAdapter(null);
                                spinner6.setAdapter(null);
                            } else {
                                Utils.showServerError(CreateGroup.this);
                            }
                            hideProgressBar();
                        }

                        @Override
                        public void onFailure(Call<CategoryResponse> call, Throwable t) {
                            hideProgressBar();
                            Utils.showShortToast(CreateGroup.this, t.getMessage());
                        }
                    });
        }
    }

    private void getCategory4(String id) {
        if (Utils.isInterNetConnected(CreateGroup.this)) {
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
                                    }
                                } else {
                                    layoutSpinner4.setVisibility(View.GONE);
                                }
                                layoutSpinner5.setVisibility(View.GONE);
                                layoutSpinner6.setVisibility(View.GONE);
                                spinner5.setAdapter(null);
                                spinner6.setAdapter(null);
                            } else {
                                Utils.showServerError(CreateGroup.this);
                            }
                            hideProgressBar();
                        }

                        @Override
                        public void onFailure(Call<CategoryResponse> call, Throwable t) {
                            hideProgressBar();
                            Utils.showShortToast(CreateGroup.this, t.getMessage());
                        }
                    });
        }
    }

    private void getCategory5(String id) {
        if (Utils.isInterNetConnected(CreateGroup.this)) {
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
                                    }
                                } else {
                                    layoutSpinner5.setVisibility(View.GONE);
                                }
                                layoutSpinner6.setVisibility(View.GONE);
                                spinner6.setAdapter(null);
                            } else {
                                Utils.showServerError(CreateGroup.this);
                            }
                            hideProgressBar();
                        }

                        @Override
                        public void onFailure(Call<CategoryResponse> call, Throwable t) {
                            hideProgressBar();
                            Utils.showShortToast(CreateGroup.this, t.getMessage());
                        }
                    });
        }
    }

    private void getCategory6(String id) {

        if (Utils.isInterNetConnected(CreateGroup.this)) {
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
                                    } else {
                                        layoutSpinner6.setVisibility(View.GONE);

                                    }
                                }
                            } else {
                                Utils.showServerError(CreateGroup.this);
                            }
                            hideProgressBar();
                        }

                        @Override
                        public void onFailure(Call<CategoryResponse> call, Throwable t) {
                            hideProgressBar();
                            Utils.showShortToast(CreateGroup.this, t.getMessage());
                        }
                    });
        }
    }


    @OnClick(R.id.imgProfile)
    void getProfileImage() {
        if (!isEdit) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (isPermissionsAllowed()) {
                    getPhoto();
                }
            } else {
                getPhoto();
            }
        }
    }

    private void getPhoto() {
        new CroperinoConfig("IMG_" + System.currentTimeMillis() + ".jpg", "/GoGroup/Pictures", "/sdcard/GoGroup/Pictures");
        CroperinoFileUtil.setupDirectory(CreateGroup.this);
        if (CroperinoFileUtil.verifyStoragePermissions(CreateGroup.this)) {
            prepareChooser();
        }
    }

    private void prepareChooser() {
        Croperino.prepareChooser(CreateGroup.this, "Start Smiling! Snap a pic from your phone or use a saved one " + new String(Character.toChars(0x1F60A)), ContextCompat.getColor(CreateGroup.this, android.R.color.white));
    }

    private void prepareCamera() {
        Croperino.prepareCamera(CreateGroup.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CroperinoFileUtil.REQUEST_CAMERA) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];
                if (permission.equals(android.Manifest.permission.CAMERA)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        prepareCamera();
                    }
                }
            }
        } else if (requestCode == CroperinoFileUtil.REQUEST_EXTERNAL_STORAGE) {
            boolean wasReadGranted = false;
            boolean wasWriteGranted = false;
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];
                if (permission.equals(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        wasReadGranted = true;
                    }
                }
                if (permission.equals(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        wasWriteGranted = true;
                    }
                }
            }
            if (wasReadGranted && wasWriteGranted) {
                prepareChooser();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageData) {
        super.onActivityResult(requestCode, resultCode, imageData);
        switch (requestCode) {
            case CroperinoConfig.REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    uriProfileImage = Uri.fromFile(CroperinoFileUtil.getmFileTemp());
                    Picasso.with(getApplicationContext()).load(uriProfileImage).into(imgProfile);

                }
                break;
            case CroperinoConfig.REQUEST_PICK_FILE:
                if (resultCode == Activity.RESULT_OK) {
                    CroperinoFileUtil.newGalleryFile(imageData, CreateGroup.this);
                    uriProfileImage = Uri.fromFile(CroperinoFileUtil.getmFileTemp());
                    Picasso.with(getApplicationContext()).load(uriProfileImage).into(imgProfile);

                }
                break;
            default:
                break;
        }

    }

//    @OnClick(R.id.etStarts)
//    void setStartDate() {
//        if (!isEdit) {
//            isStart = true;
//            Date newDate = new Date();
//            openCalendar(newDate);
//        }
//    }

    @OnClick(R.id.etEnds)
    void setEndDate() {
//
        isStart = false;
        Date newDate = new Date();
//        if (!etStarts.getText().toString().equals("")) {
//            try {
//                newDate = dateFormatPost.parse(etStarts.getText().toString().trim());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
        openCalendar(newDate);
    }

    void openCalendar(Date date) {
        Calendar calendar = isStart ? calendarStarts : calendarEnds;
        DatePickerDialog dialog = new DatePickerDialog(CreateGroup.this, this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(date.getTime());
        // dialog.getDatePicker().setMinDate(newDate.getTime() - (newDate.getTime() % (24 * 60 * 60 * 1000)));

        if (!isEdit && !isStart) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, 90);
            dialog.getDatePicker().setMaxDate(new Date(c.getTimeInMillis()).getTime());
        }
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        if (isStart) {
            calendarStarts.set(Calendar.YEAR, year);
            calendarStarts.set(Calendar.MONTH, month);
            calendarStarts.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//            etStarts.setText(dateFormatPost.format(calendarStarts.getTime()));
//            etStarts.setError(null);
            if (!etEnds.getText().toString().trim().equals("")) {
                Date sd, ed;
                try {
                    ed = dateFormatPost.parse(etEnds.getText().toString().trim());
//                    sd = dateFormatPost.parse(etStarts.getText().toString().trim());
//                    if (sd.after(ed)) {
//                        etEnds.setText("");
//                    }
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

    @OnClick(R.id.btnCreate)
    void onClickCreate() {
        if (isEdit) {
            editGroup();
        } else {
            createGroup();
        }
    }

    void editGroup() {
        //  if (isValid()) {
        Map<String, String> postFields = new HashMap<String, String>();
        postFields.put("group_id", postGroupId);
        // postFields.put("cost_range", (etCostRange.getText().toString().trim()).replaceAll(",", ""));
        //postFields.put("members_count", etNumOfMember.getText().toString().trim());
        postFields.put("end_date", etEnds.getText().toString().trim());

        if (Utils.isInterNetConnected(CreateGroup.this)) {
            showProgressbar();
            RestClient.get().editGroup(UserPreferences.getInstance().getToken(), postFields).
                    enqueue(new Callback<PostResponse>() {
                        @Override
                        public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {

                                    Intent intent = new Intent();
                                    setResult(BaseActivity.REFRESH_ACTIVITY, intent);
                                    finish();
                                    Utils.showShortToast(CreateGroup.this, response.body().getMessage());

                                } else {
                                    hideProgressBar();
                                    Utils.showShortToast(CreateGroup.this, response.body().getMessage());
                                }
                            } else {
                                Utils.showServerError(CreateGroup.this);
                                hideProgressBar();
                            }
                        }

                        @Override
                        public void onFailure(Call<PostResponse> call, Throwable t) {
                            hideProgressBar();
                            Utils.showShortToast(CreateGroup.this, t.getMessage());
                        }
                    });

        }
        // }
    }


    void createGroup() {

        if (isValid()) {

            String id2 = spinner2.getAdapter() != null ? arrId2[spinner2.getSelectedItemPosition()] : "",
                    id3 = spinner3.getAdapter() != null ? arrId3[spinner3.getSelectedItemPosition()] : "",
                    id4 = spinner4.getAdapter() != null ? arrId4[spinner4.getSelectedItemPosition()] : "",
                    id5 = spinner5.getAdapter() != null ? arrId5[spinner5.getSelectedItemPosition()] : "",
                    id6 = spinner6.getAdapter() != null ? arrId6[spinner6.getSelectedItemPosition()] : "";


            Map<String, RequestBody> postFields = new HashMap<String, RequestBody>();

            postFields.put("category_id", Utils.requestBody(arrId1[spinner1.getSelectedItemPosition()]));
            postFields.put("subcategory_id", Utils.requestBody(id2 != null ? id2 : ""));
            postFields.put("subcategory_id2", Utils.requestBody(id3 != null ? id3 : ""));
            postFields.put("subcategory_id3", Utils.requestBody(id4 != null ? id4 : ""));
            postFields.put("subcategory_id4", Utils.requestBody(id5 != null ? id5 : ""));
            postFields.put("subcategory_id5", Utils.requestBody(id6 != null ? id6 : ""));
            postFields.put("group_name", Utils.requestBody(etGroupName.getText().toString().trim()));
            postFields.put("location", Utils.requestBody(etLocation.getText().toString().trim()));
            postFields.put("description", Utils.requestBody(etDesc.getText().toString().trim()));
            postFields.put("createdby_userid", Utils.requestBody(UserPreferences.getInstance().getUserId()));
            postFields.put("cost_range", Utils.requestBody((etCostRange.getText().toString().trim()).replaceAll(",", "")));
            postFields.put("members_count", Utils.requestBody("0"));
            postFields.put("start_date", Utils.requestBody(dateFormatPost.format(new Date())));
            postFields.put("end_date", Utils.requestBody(etEnds.getText().toString().trim()));

            MultipartBody.Part multipart = null;
            if (uriProfileImage != null) {
                File file = Utils.reduceImageSize(new File(uriProfileImage.getPath()));
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                multipart = MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestBody);
            }
            if (Utils.isInterNetConnected(CreateGroup.this)) {
                showProgressbar();
                RestClient.get().createGroup(UserPreferences.getInstance().getToken(), multipart, postFields).
                        enqueue(new Callback<PostResponse>() {
                            @Override
                            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                                if (response.body() != null) {
                                    if (response.body().isStatus()) {


                                        //appLozicCreateGroup(response.body().getData(), response.body().getMessage());
                                        Intent intent = new Intent();
                                        setResult(BaseActivity.REFRESH_FRAGMENT, intent);
                                        Utils.showDialog(CreateGroup.this, getString(R.string.createdSuccessful), response.body().getMessage());

                                    } else {
                                        hideProgressBar();
                                        Utils.showLongToastMessageOnActivity(CreateGroup.this, response.body().getMessage());

                                    }

                                } else {
                                    Utils.showServerError(CreateGroup.this);
                                    hideProgressBar();

                                }

                            }

                            @Override
                            public void onFailure(Call<PostResponse> call, Throwable t) {
                                hideProgressBar();
                                Utils.showShortToast(CreateGroup.this, t.getMessage());
                            }
                        });

            }
        }
    }


    @BindView(R.id.btnCreate)
    Button btnCreate;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
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
    //    @BindView(R.id.etStarts)
//    EditText etStarts;
    @BindView(R.id.etEnds)
    EditText etEnds;
    @BindView(R.id.etDesc)
    EditText etDesc;
    @BindView(R.id.etGroupName)
    EditText etGroupName;
    //    @BindView(R.id.etNumOfMember)
//    EditText etNumOfMember;
    @BindView(R.id.etCostRange)
    AutoCompleteTextView etCostRange;
    @BindView(R.id.etLocation)
    AutoCompleteTextView etLocation;

    @BindView(R.id.btnBack)
    Button btnBack;
    @BindView(R.id.imgProfile)
    CircleImageView imgProfile;

    @OnClick(R.id.btnBack)
    void back() {
        finish();
    }

    public void getSpinner(String[] values) {
        spinnerAdapter = new ArrayAdapter<String>(CreateGroup.this, R.layout.spinner_item, values) {
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

    private boolean isValid() {
        if (etGroupName.getText().toString().trim().length() <= 0) {
            Utils.setEditTextError(etGroupName, getResources().getString(R.string.validationName));
            return false;
        } else if (spinner1.getAdapter() == null || spinner1.getSelectedItemPosition() == 0) {
            Utils.showShortToast(CreateGroup.this, getResources().getString(R.string.validationCategory));
            return false;
        } else if (spinner2.getAdapter() != null && arrId2.length > 1 && spinner2.getSelectedItemPosition() == 0) {
            Utils.showShortToast(CreateGroup.this, getResources().getString(R.string.validationSubCategory1));
            return false;
        } else if (etCostRange.getText().toString().trim().length() <= 0) {
            Utils.setEditTextError(etCostRange, getResources().getString(R.string.validationCostRange));
            return false;
        } else if (etLocation.getText().toString().trim().length() <= 0) {
            Utils.setEditTextError(etLocation, getResources().getString(R.string.validationLocation));
            return false;
        }
//        else if (etNumOfMember.getText().toString().trim().length() <= 0) {
//            Utils.setEditTextError(etNumOfMember, getResources().getString(R.string.validationNumOfMembers));
//            return false;
//        }
//        else if (etStarts.getText().toString().trim().length() <= 0) {
//            Utils.showShortToast(CreateGroup.this, getResources().getString(R.string.validationStartDate));
//            return false;
//        }
        else if (etEnds.getText().toString().trim().length() <= 0) {
            Utils.showShortToast(CreateGroup.this, getResources().getString(R.string.validationEndDate));
            return false;
        }
//        else if (uriProfileImage == null && !isEdit) {
//            Utils.showShortToast(CreateGroup.this, getResources().getString(R.string.validationPhoto));
//            return false;
//        }
        else if (etDesc.getText().toString().trim().length() <= 0) {
            Utils.setEditTextError(etDesc, getResources().getString(R.string.validationDesc));
            return false;
        }
        return true;
    }


}