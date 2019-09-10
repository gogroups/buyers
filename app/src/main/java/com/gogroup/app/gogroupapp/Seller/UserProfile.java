package com.gogroup.app.gogroupapp.Seller;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gogroup.app.gogroupapp.Adapters.GooglePlacesAutocompleteAdapter;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.FontManager;
import com.gogroup.app.gogroupapp.HelperClasses.RestClient;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.UserResponse;
import com.mikelau.croperino.Croperino;
import com.mikelau.croperino.CroperinoConfig;
import com.mikelau.croperino.CroperinoFileUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
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

public class UserProfile extends BaseActivity {
    Uri uriProfileImage;
    int refresh = -1;
    boolean isUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_profile);
        ButterKnife.bind(this);
        setIcons();


        isUser = UserPreferences.getInstance().getUserType().toLowerCase().trim().equals("user");
        layoutSellerDetails.setVisibility(isUser ? View.GONE : View.VISIBLE);

        etLocation.setAdapter(new GooglePlacesAutocompleteAdapter(UserProfile.this, R.layout.spinner_item));

        setInitialValues();

    }

    private void setInitialValues() {

        btnSave.setVisibility(View.GONE);
        etAge.setVisibility(isUser ? View.VISIBLE : View.GONE);
        tvAge.setVisibility(isUser ? View.VISIBLE : View.GONE);
//        layoutCustomAddress.setVisibility(isUser ? View.GONE : View.VISIBLE);
        Utils.disableEditTextFocus(etName);
        Utils.disableEditTextFocus(etLocation);
        Utils.disableEditTextFocus(etCode);
        Utils.disableEditTextFocus(etPhone);
        Utils.disableEditTextFocus(etAge);
        Utils.disableEditTextFocus(etZipCode);
        Utils.disableEditTextFocus(etSellerDetail);
        Utils.disableEditTextFocus(etAccountNum);
        Utils.disableEditTextFocus(etHolderName);
        Utils.disableEditTextFocus(etIfsc);
        Utils.disableEditTextFocus(etBankName);
        Utils.disableEditTextFocus(etPaytmNum);
        Utils.disableEditTextFocus(etCustomAddress);
        getProfileApi();
    }

    private void getProfileApi() {
        if (Utils.isInterNetConnected(UserProfile.this)) {
            showProgressbar();
            RestClient.get().getProfile(UserPreferences.getInstance().getToken()).
                    enqueue(new Callback<UserResponse>() {
                        @Override
                        public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
                                    UserResponse user = response.body().getData();
                                    etName.setText(user.getName());
                                    etAccountNum.setText(user.getAccountNum());
                                    etHolderName.setText(user.getAcHolderName());
                                    etIfsc.setText(user.getIfsc());
                                    etBankName.setText(user.getBankName());
                                    etPaytmNum.setText(user.getPaytmNum());
                                    etCustomAddress.setText(user.getAddress());

                                    String num = user.getContactNumber() != null ? user.getContactNumber() : "";
                                    switch (num.length()) {
                                        case 11:
                                            etCode.setText(num.substring(0, 1));
                                            etPhone.setText(num.substring(1, num.length()));
                                            break;
                                        case 12:
                                            etCode.setText(num.substring(0, 2));
                                            etPhone.setText(num.substring(2, num.length()));
                                            break;
                                        case 13:
                                            etCode.setText(num.substring(0, 3));
                                            etPhone.setText(num.substring(3, num.length()));
                                            break;
                                        default:
                                            etPhone.setText(num);
                                            break;

                                    }
                                    etAge.setText("" + user.getAge());
                                    etLocation.setText(user.getLocation());
                                    etZipCode.setText(user.getZipCode());
                                    tvEmail.setText(user.getEmail());
                                    tvTinNo.setText(user.getPanGst());
                                    tvCompanyName.setText(user.getCompanyName());
                                    etSellerDetail.setText(user.getUsp());

                                    if (user.getProfileImage() != null && !user.getProfileImage().equals("")) {
                                        Picasso.with(getApplicationContext()).load(user.getProfileImage()).into(imgProfile);
                                    }
                                    refresh = BaseActivity.REFRESH_PROFILE;
                                    UserPreferences.getInstance().updateProfile(user.getName(), user.getAge(), user.getContactNumber(), user.getAddress(), user.getLocation(), user.getProfileImage(), user.getZipCode());

                                } else {
                                    Utils.showShortToast(UserProfile.this, response.body().getMessage());
                                }

                            } else {
                                Utils.showServerError(UserProfile.this);
                            }
                            hideProgressBar();

                        }

                        @Override
                        public void onFailure(Call<UserResponse> call, Throwable t) {
                            hideProgressBar();
                            Utils.showShortToast(UserProfile.this, t.getMessage());
                        }
                    });

        }
    }

    private void setIcons() {
        mButtonLeftArrow.setText(R.string.icon_ionicon_var_chevron_left);
        mButtonLeftArrow.setTypeface(FontManager.getTypeFaceFromFontName(getApplicationContext(), FontManager.IONICONFONT));
        mButtonLeftArrow.setTextSize(20);
        mButtonLeftArrow.setTextColor(ContextCompat.getColor(this, R.color.white_color));
    }


    @OnClick(R.id.vendor_home_filter_left_arrow)
    void back() {
        Intent intent = new Intent();
        setResult(refresh, intent);
        finish();
    }

    @OnClick(R.id.imgEdit)
    void edit() {
        tvTitle.setText(getResources().getString(R.string.title_activity_seller_edit_profile));
        imgEdit.setVisibility(View.GONE);
        btnSave.setVisibility(View.VISIBLE);
        Utils.enableEditTextFocus(etName);
        Utils.enableEditTextFocus(etAge);
        Utils.enableEditTextFocus(etLocation);
        Utils.enableEditTextFocus(etPhone);
        Utils.enableEditTextFocus(etCode);
        Utils.enableEditTextFocus(etZipCode);
        Utils.enableEditTextFocus(etSellerDetail);
        Utils.enableEditTextFocus(etAccountNum);
        Utils.enableEditTextFocus(etHolderName);
        Utils.enableEditTextFocus(etIfsc);
        Utils.enableEditTextFocus(etBankName);
        Utils.enableEditTextFocus(etPaytmNum);
        Utils.enableEditTextFocus(etCustomAddress);
        tvSellerDetailGuidLine.setVisibility(View.VISIBLE);
        etName.requestFocus();
        etName.setSelection(etName.getText().length());
    }

    @OnClick(R.id.imgProfile)
    void getProfileImage() {
        if (btnSave.getVisibility() == View.VISIBLE) {

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
        CroperinoFileUtil.setupDirectory(UserProfile.this);
        if (CroperinoFileUtil.verifyStoragePermissions(UserProfile.this)) {
            prepareChooser();
        }
    }

    private void prepareChooser() {
        Croperino.prepareChooser(UserProfile.this, "Start Smiling! Snap a pic from your phone or use a saved one " + new String(Character.toChars(0x1F60A)), ContextCompat.getColor(UserProfile.this, android.R.color.white));
    }

    private void prepareCamera() {
        Croperino.prepareCamera(UserProfile.this);
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
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.validationCamera), Toast.LENGTH_LONG).show();
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
                    getPhoto();
                } else {
                    // Permission Denied
                    Toast.makeText(getApplicationContext(), getString(R.string.validationPermission), Toast.LENGTH_LONG)
                            .show();
                }
            }
            break;
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
                    CroperinoFileUtil.newGalleryFile(imageData, UserProfile.this);
                    uriProfileImage = Uri.fromFile(CroperinoFileUtil.getmFileTemp());
                    Picasso.with(getApplicationContext()).load(uriProfileImage).into(imgProfile);

                }
                break;
            default:
                break;
        }

    }

    @OnClick(R.id.btnSave)
    void save() {
        if (isValid()) {

            Map<String, RequestBody> postFields = new HashMap<String, RequestBody>();
            postFields.put("name", Utils.requestBody(etName.getText().toString().trim()));
            postFields.put("location", Utils.requestBody(etLocation.getText().toString().trim()));
            postFields.put("email", Utils.requestBody(tvEmail.getText().toString().trim()));
            postFields.put("zipCode", Utils.requestBody(etZipCode.getText().toString().trim()));
            postFields.put("contact_number", Utils.requestBody(etCode.getText().toString() + etPhone.getText().toString().trim()));
            postFields.put("account_number", Utils.requestBody(etAccountNum.getText().toString().trim()));
            postFields.put("ac_holder_name", Utils.requestBody(etHolderName.getText().toString().trim()));
            postFields.put("ifsc", Utils.requestBody(etIfsc.getText().toString().trim()));
            postFields.put("bank_name", Utils.requestBody(etBankName.getText().toString().trim()));
            postFields.put("paytm_no", Utils.requestBody(etPaytmNum.getText().toString().trim()));

            postFields.put("address", Utils.requestBody(etCustomAddress.getText().toString().trim()));
            if (isUser) {
                postFields.put("age", Utils.requestBody(etAge.getText().toString().trim()));
            } else {
                postFields.put("seller_usp", Utils.requestBody(etSellerDetail.getText().toString().trim()));

            }
            if (uriProfileImage == null && (UserPreferences.getInstance().getProfileImage() != null || UserPreferences.getInstance().getProfileImage() != "")) {
                postFields.put("image_path", Utils.requestBody(UserPreferences.getInstance().getProfileImage()));
            }

            List<MultipartBody.Part> multipartImg = new ArrayList<>();
            if (uriProfileImage != null) {
                File file = Utils.reduceImageSize(new File(uriProfileImage.getPath()));
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part multipart = MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestBody);
                multipartImg.add(multipart);
            }
            if (Utils.isInterNetConnected(UserProfile.this)) {
                showProgressbar();
                RestClient.get().editProfile(UserPreferences.getInstance().getToken(), multipartImg, postFields).
                        enqueue(new Callback<UserResponse>() {
                            @Override
                            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                                if (response.body() != null) {
                                    if (response.body().isStatus()) {
                                        refresh = BaseActivity.REFRESH_PROFILE;
                                        tvTitle.setText(getResources().getString(R.string.title_activity_seller_profile));
                                        imgEdit.setVisibility(View.VISIBLE);
                                        btnSave.setVisibility(View.GONE);
                                        tvSellerDetailGuidLine.setVisibility(View.GONE);
                                        Utils.disableEditTextFocus(etName);
                                        Utils.disableEditTextFocus(etAge);
                                        Utils.disableEditTextFocus(etLocation);
                                        Utils.disableEditTextFocus(etPhone);
                                        Utils.disableEditTextFocus(etCode);
                                        Utils.disableEditTextFocus(etZipCode);
                                        Utils.disableEditTextFocus(etSellerDetail);
                                        Utils.disableEditTextFocus(etCustomAddress);
                                        Utils.disableEditTextFocus(etAccountNum);
                                        Utils.disableEditTextFocus(etHolderName);
                                        Utils.disableEditTextFocus(etIfsc);
                                        Utils.disableEditTextFocus(etBankName);
                                        Utils.disableEditTextFocus(etPaytmNum);
                                        UserResponse user = response.body().getData();
                                        UserPreferences.getInstance().updateProfile(user.getName(), user.getAge(), user.getContactNumber(), user.getAddress(), user.getLocation(), user.getProfileImage(), user.getZipCode());

                                    }
                                    Utils.showShortToast(UserProfile.this, response.body().getMessage());

                                } else {
                                    Utils.showServerError(UserProfile.this);
                                }
                                hideProgressBar();

                            }

                            @Override
                            public void onFailure(Call<UserResponse> call, Throwable t) {
                                hideProgressBar();
                                Utils.showShortToast(UserProfile.this, t.getMessage());
                            }
                        });

            }
        }
    }


    @BindView(R.id.vendor_home_filter_left_arrow)
    Button mButtonLeftArrow;
    @BindView(R.id.imgEdit)
    ImageView imgEdit;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etAge)
    EditText etAge;
    @BindView(R.id.etZipCode)
    EditText etZipCode;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tvCompanyName)
    TextView tvCompanyName;
    @BindView(R.id.tvTinNo)
    TextView tvTinNo;
    @BindView(R.id.etSellerDetail)
    EditText etSellerDetail;
    @BindView(R.id.etCustomAddress)
    EditText etCustomAddress;
    @BindView(R.id.layoutSellerDetails)
    LinearLayout layoutSellerDetails;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etLocation)
    AutoCompleteTextView etLocation;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvAge)
    TextView tvAge;
    @BindView(R.id.sellerDetailGuidLine)
    TextView tvSellerDetailGuidLine;
    @BindView(R.id.imgProfile)
    CircleImageView imgProfile;

    @BindView(R.id.etAccountNum)
    EditText etAccountNum;
    @BindView(R.id.etHolderName)
    EditText etHolderName;
    @BindView(R.id.etIfsc)
    EditText etIfsc;
    @BindView(R.id.etBankName)
    EditText etBankName;
    @BindView(R.id.etPaytmNumber)
    EditText etPaytmNum;

    @BindView(R.id.layoutCustomAddress)
    LinearLayout layoutCustomAddress;

    private boolean isValid() {
        if (etName.getText().toString().trim().length() <= 0) {
            Utils.setEditTextError(etName, getResources().getString(R.string.validationName));
            return false;
        } else if (etAge.getText().toString().trim().length() <= 0 && isUser) {
            Utils.setEditTextError(etAge, getResources().getString(R.string.user_age_error));
            return false;
        } else if (etCode.getText().toString().trim().length() <= 0) {
            Utils.showShortToast(getApplicationContext(), getResources().getString(R.string.validationCode));
            return false;
        } else if (etPhone.getText().toString().trim().length() <= 0) {
            Utils.setEditTextError(etPhone, getResources().getString(R.string.validationContact));
            return false;
        } else if (etPhone.getText().toString().trim().length() < 10) {
            Utils.setEditTextError(etPhone, getResources().getString(R.string.validationValidContact));
            return false;
        } else if (etZipCode.getText().toString().trim().length() == 0) {
            Utils.setEditTextError(etZipCode, getString(R.string.validationZipCode));
            return false;
        } else if (etCustomAddress.getText().toString().trim().length() == 0) {
            Utils.setEditTextError(etCustomAddress, getString(R.string.validationDealDetails));
            return false;
        } else if (etLocation.getText().toString().trim().length() <= 0) {
            Utils.setEditTextError(etLocation, getResources().getString(R.string.validationLocation));
            return false;
        } else if (uriProfileImage == null && (UserPreferences.getInstance().getProfileImage() == null || UserPreferences.getInstance().getProfileImage().trim() == "")) {
            Utils.showShortToast(UserProfile.this, getResources().getString(R.string.validationProfilePhoto));
            return false;
        } else if (!isUser && etSellerDetail.getText().toString().trim().length() == 0) {
            Utils.setEditTextError(etSellerDetail, getString(R.string.validationSellerDetail));
            return false;
        }

        if (etAccountNum.getText().toString().trim().length() == 0) {
            if (!isUser) {
                if (etPaytmNum.getText().toString().trim().length() == 0) {
                    Utils.setEditTextError(etAccountNum, getString(R.string.validationAccountNum));
                    return false;
                } else {
                    clearAccountDetail();
                    return true;
                }
            } else if (etHolderName.getText().toString().trim().length() > 0 || etIfsc.getText().toString().trim().length() > 0
                    || etBankName.getText().toString().trim().length() > 0) {
                Utils.setEditTextError(etAccountNum, getString(R.string.validationAccountNum));
                return false;
            } else {
                clearAccountDetail();
                return true;
            }
        } else if (etHolderName.getText().toString().trim().length() == 0) {
            Utils.setEditTextError(etHolderName, getString(R.string.validationHolderName));
            return false;
        } else if (etIfsc.getText().toString().trim().length() == 0) {
            Utils.setEditTextError(etIfsc, getString(R.string.validationIfsc));
            return false;
        } else if (etBankName.getText().toString().trim().length() == 0) {
            Utils.setEditTextError(etBankName, getString(R.string.validationBankName));
            return false;
        }
        return true;
    }

    void clearAccountDetail() {
        etAccountNum.setText("");
        etBankName.setText("");
        etIfsc.setText("");
        etHolderName.setText("");
        etAccountNum.setError(null);
        etBankName.setError(null);
        etIfsc.setError(null);
        etHolderName.setError(null);

    }
}
