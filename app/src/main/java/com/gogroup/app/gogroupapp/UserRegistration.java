package com.gogroup.app.gogroupapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gogroup.app.gogroupapp.Adapters.GooglePlacesAutocompleteAdapter;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.FontManager;
import com.gogroup.app.gogroupapp.HelperClasses.RestClient;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.Models.TwitterModel;
import com.gogroup.app.gogroupapp.OtpActivity;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.GoogleAddressResponse;
import com.gogroup.app.gogroupapp.Responses.RegisterResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.gson.Gson;
import com.mikelau.croperino.Croperino;
import com.mikelau.croperino.CroperinoConfig;
import com.mikelau.croperino.CroperinoFileUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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

public class UserRegistration extends BaseActivity
{

    public static final String GOOGLE_DATA = "google_data";
    public static final String TWITTER_DATA = "twitter_data";
    public static final String LINKED_DATA = "linked_in_data";
    public static final String FACEBOOK_DATA = "facebook_data";
    public Map<String, RequestBody> postFields = new HashMap<String, RequestBody>();
    public MultipartBody.Part multipartImg = null;
    TextView registeration_icon_username_textview, registeration_icon_email_textview, registeration_icon_phone,registeration_icon_location;
    GoogleSignInAccount googleData;
    TwitterModel twitterData;
    JSONObject linkedData;
    JSONObject facebookData;
    Uri uriProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        ButterKnife.bind(this);
       // layoutAccountDetail.setVisibility(View.GONE);
        setIds();
        setIcons();

        try {
            googleData = (GoogleSignInAccount) getIntent().getParcelableExtra(GOOGLE_DATA);
            setGoogleValues();

        } catch (NullPointerException e) {
        }

        try {
            twitterData = (TwitterModel) getIntent().getParcelableExtra(TWITTER_DATA);
            setTwitterValues();

        } catch (NullPointerException e) {
        }

        try {
            linkedData = new JSONObject(getIntent().getStringExtra(LINKED_DATA));
            setLinkedInValues();

        } catch (NullPointerException e) {
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            facebookData = new JSONObject(getIntent().getStringExtra(FACEBOOK_DATA));
            setFacebookValues();

        } catch (NullPointerException e) {
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //  actvLocation.setThreshold(1);

        etLocation.setAdapter(new GooglePlacesAutocompleteAdapter(UserRegistration.this, R.layout.spinner_item));

    }

    public List<String> checkGoogleAddress(CharSequence charSequence) {

        final List<String> addresses = new ArrayList<>();
        RestClient.get().getGoogleAddress(String.valueOf(charSequence)).enqueue(new Callback<GoogleAddressResponse>() {
            @Override
            public void onResponse(Call<GoogleAddressResponse> call, Response<GoogleAddressResponse> response) {
                if (response.body() != null) {
                    for (int k = 0; k < response.body().getResults().size(); k++) {
                        String address = null;

                        for (int i = 0; i < response.body().getResults().get(0).getAddressComponents().size(); i++) {
                            if (response.body().getResults().get(0).getAddressComponents().get(i).getTypes().get(0).toLowerCase().trim().equals("locality")) {
                                address = response.body().getResults().get(0).getAddressComponents().get(i).getLongName();
                            }
                            if (response.body().getResults().get(0).getAddressComponents().get(i).getTypes().get(0).toLowerCase().trim().equals("administrative_area_level_1")) {
                                address += response.body().getResults().get(0).getAddressComponents().get(i).getShortName();

                            }
                            if (response.body().getResults().get(0).getAddressComponents().get(i).getTypes().get(0).toLowerCase().trim().equals("country")) {
                                address += response.body().getResults().get(0).getAddressComponents().get(i).getShortName();

                            }
                        }
                        if (address != null) {
                            addresses.add(address);
                        }
                    }

//                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(UserRegistration.this,android.R.layout.select_dialog_item,addresses);
//                    etLocation.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<GoogleAddressResponse> call, Throwable t) {
            }
        });
        return addresses;
    }


    private void setFacebookValues() {


        try {
            etName.setText("" + facebookData.get("first_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            etLastName.setText("" + facebookData.get("last_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String id = facebookData.getString("id");
            final String url = "https://graph.facebook.com/" + id + "/picture?type=large";
            Log.d("nonce", "" + url);
            Picasso.with(getApplicationContext()).load(url).into(imgUser);
            Picasso.with(getApplicationContext())
                    .load(url)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
                            uriProfileImage = Uri.parse(path);

                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            etEmail.setText("" + facebookData.get("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setLinkedInValues() {
        try {
            etName.setText("" + linkedData.get("firstName"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            etLastName.setText("" + linkedData.get("lastName"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            etEmail.setText("" + linkedData.get("emailAddress"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (linkedData.get("pictureUrl") != null) {
                Picasso.with(getApplicationContext())
                        .load(linkedData.getInt("pictureUrl"))
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
                                uriProfileImage = Uri.parse(path);
                                Picasso.with(getApplicationContext()).load(uriProfileImage).into(imgUser);

                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setTwitterValues() {
        etName.setText(twitterData.getName());
        etEmail.setText(twitterData.getEmail());
        if (twitterData.getPhotoUrl() != null) {
            Picasso.with(getApplicationContext())
                    .load(twitterData.getPhotoUrl())
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
                            uriProfileImage = Uri.parse(path);
                            Picasso.with(getApplicationContext()).load(uriProfileImage).into(imgUser);

                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        }
    }

    private void setGoogleValues() {

        etName.setText(googleData.getDisplayName());
        etEmail.setText(googleData.getEmail());
        if (googleData.getPhotoUrl() != null) {
            Picasso.with(getApplicationContext())
                    .load(googleData.getPhotoUrl())
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
                            uriProfileImage = Uri.parse(path);
                            Picasso.with(getApplicationContext()).load(uriProfileImage).into(imgUser);

                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        }
    }

    @OnClick(R.id.imgUser)
    void userImg() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isPermissionsAllowed()) {
                getPhoto();
            }

        } else {
            getPhoto();
        }


    }

    private void getPhoto() {
        new CroperinoConfig("IMG_" + System.currentTimeMillis() + ".jpg", "/GoGroup/Pictures", "/sdcard/GoGroup/Pictures");
        CroperinoFileUtil.setupDirectory(UserRegistration.this);
        if (CroperinoFileUtil.verifyStoragePermissions(UserRegistration.this)) {
            prepareChooser();
        }
    }

    private void prepareChooser() {
        Croperino.prepareChooser(UserRegistration.this, "Start Smiling! Snap a pic from your phone or use a saved one " + new String(Character.toChars(0x1F60A)), ContextCompat.getColor(UserRegistration.this, android.R.color.white));
    }

    private void prepareCamera() {
        Croperino.prepareCamera(UserRegistration.this);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 7) {
            finish();
        }
        switch (requestCode) {
            case CroperinoConfig.REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    uriProfileImage = Uri.fromFile(CroperinoFileUtil.getmFileTemp());
                    Picasso.with(getApplicationContext()).load(uriProfileImage).into(imgUser);

                }
                break;
            case CroperinoConfig.REQUEST_PICK_FILE:
                if (resultCode == Activity.RESULT_OK) {
                    CroperinoFileUtil.newGalleryFile(data, UserRegistration.this);
                    uriProfileImage = Uri.fromFile(CroperinoFileUtil.getmFileTemp());
                    Picasso.with(getApplicationContext()).load(uriProfileImage).into(imgUser);

                }
                break;
            default:
                break;

        }


    }


    @OnClick(R.id.btnSignUp)
    void signUp()
    {
        if (isValid())
        {
            postFields.clear();
            postFields.put("name", Utils.requestBody(etName.getText().toString().trim()));
            postFields.put("lastName", Utils.requestBody(etLastName.getText().toString().trim()));
            postFields.put("contact_number", Utils.requestBody(etCode.getText().toString() + etPhone.getText().toString().trim()));
            postFields.put("email", Utils.requestBody(etEmail.getText().toString().trim()));
            postFields.put("tin_number", Utils.requestBody(""));
           // postFields.put("company_name", Utils.requestBody(""));
            //postFields.put("zipCode", Utils.requestBody(etZipCode.getText().toString().trim()));
            postFields.put("location", Utils.requestBody(etLocation.getText().toString().trim()));
            //postFields.put("seller_usp", Utils.requestBody(""));
            //postFields.put("age", Utils.requestBody(etAge.getText().toString().trim()));
            postFields.put("password", Utils.requestBody(etPswd.getText().toString().trim()));
            postFields.put("confirm_password", Utils.requestBody(etConfirmPswd.getText().toString().trim()));
            postFields.put("user_type", Utils.requestBody(Utils.userType));
            //postFields.put("secondary_name", Utils.requestBody(""));
            //postFields.put("secondary_contact", Utils.requestBody(""));
            //postFields.put("secondary_email", Utils.requestBody(""));
            //postFields.put("account_number", Utils.requestBody(etAccountNum.getText().toString().trim()));
            //postFields.put("ac_holder_name", Utils.requestBody(etHolderName.getText().toString().trim()));
            //postFields.put("ifsc", Utils.requestBody(etIfsc.getText().toString().trim()));
            //postFields.put("bank_name", Utils.requestBody(etBankName.getText().toString().trim()));
            //postFields.put("paytm_no", Utils.requestBody(etPaytmNum.getText().toString().trim()));
            //postFields.put("address", Utils.requestBody(etCustomAddress.getText().toString().trim()));

            if (uriProfileImage != null) {
                File file = Utils.reduceImageSize(new File(uriProfileImage.getPath()));
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                multipartImg = MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestBody);
            }

            if (Utils.isInterNetConnected(UserRegistration.this))
            {
                showProgressbar();
                RestClient.get().registerSeller(multipartImg, postFields).
                        enqueue(new Callback<RegisterResponse>() {
                            @Override
                            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response)
                            {
                                if (response.body() != null)
                                {

                                    if (response.body().isSuccess())
                                    {
                                        Intent i = new Intent(UserRegistration.this, OtpActivity.class);
                                        i.putExtra(OtpActivity.DATA, response.body().getData());
                                        startActivityForResult(i, 7);
                                        Utils.showShortToast(UserRegistration.this, "" + response.body().getMessage());

                                    }
                                    else
                                        {

                                        Gson gson = new Gson();
                                        gson.toJson(response.body().getMessage());
                                        try {
                                            JSONObject a = new JSONObject(gson.toJson(response.body().getMessage()));
                                            try {
                                                Utils.showShortToast(UserRegistration.this, a.getString("contact_number"));

                                            } catch (JSONException e) {

                                            }
                                            try {
                                                Utils.showShortToast(UserRegistration.this, a.getString("email"));

                                            } catch (JSONException e) {

                                            }
                                            try {
                                                Utils.showShortToast(UserRegistration.this, a.getString("name"));

                                            } catch (JSONException e) {

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                } else {

                                    Utils.showServerError(UserRegistration.this);
                                }
                                hideProgressBar();

                            }

                            @Override
                            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                                hideProgressBar();
                                Utils.showShortToast(UserRegistration.this, t.getMessage());
                            }
                        });

            }
        }

    }


    public boolean isValid()
    {


        if (TextUtils.isEmpty(etName.getText().toString().trim())) {
            Utils.setEditTextError(etName, getString(R.string.user_name_error));
            return false;
        } //else if (TextUtils.isEmpty(etAge.getText().toString().trim())) {
            ///Utils.setEditTextError(etAge, getString(R.string.user_age_error));
            //return false;
        //}
        else if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
            Utils.setEditTextError(etEmail, getString(R.string.user_email_error));
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches()) {
            Utils.setEditTextError(etEmail, getString(R.string.user_email_pattern_error));
            return false;
        }// else if (etCustomAddress.getText().toString().trim().length() == 0) {
           // Utils.setEditTextError(etCustomAddress, getString(R.string.validationAddress));
            //return false;
        //}
        else if (TextUtils.isEmpty(etLocation.getText().toString().trim())) {
            Utils.setEditTextError(etLocation, getString(R.string.validationLocation));
            return false;
        }
        else if (etPswd.getText().toString().trim().length() == 0) {
            Utils.setEditTextError(etPswd, getString(R.string.validationPassword));
            return false;
        } else if (etConfirmPswd.getText().toString().trim().length() == 0) {
            Utils.setEditTextError(etConfirmPswd, getString(R.string.validationConfirmPassword));
            return false;
        } else if (!etConfirmPswd.getText().toString().trim().equals(etPswd.getText().toString().trim())) {
            Utils.setEditTextError(etConfirmPswd, getString(R.string.validationMatchingPassword));
            return false;
        } else if (TextUtils.isEmpty(etCode.getText().toString().trim())) {
            Utils.showShortToast(getApplicationContext(), getString(R.string.validationCode));
            return false;
        } else if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
            Utils.setEditTextError(etPhone, getString(R.string.validationContact));
            return false;
        } else if (etPhone.getText().toString().trim().length() < 10) {
            Utils.setEditTextError(etPhone, getString(R.string.validationValidContact));
            return false;
        }// else if (etZipCode.getText().toString().trim().length() == 0) {
           // Utils.setEditTextError(etZipCode, getString(R.string.validationZipCode));
            //return false;
        //}
      /*  if (layoutAccountDetail.getVisibility() == View.VISIBLE) {
            if (etAccountNum.getText().toString().trim().length() == 0) {
                if (etPaytmNum.getText().toString().trim().length() == 0) {
                    Utils.setEditTextError(etAccountNum, getString(R.string.validationAccountNum));
                    return false;
                } else {
                    clearAccountDetail(false);
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
        }
        if (layoutAccountDetail.getVisibility() == View.VISIBLE) {
            if (etAccountNum.getText().toString().trim().length() == 0) {
                if (etPaytmNum.getText().toString().trim().length() == 0) {
                    Utils.setEditTextError(etAccountNum, getString(R.string.validationAccountNum));
                    return false;
                } else {
                    clearAccountDetail(false);
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
        }*/
        return true;
    }

    private void setIcons() {
        registeration_icon_username_textview.setText(R.string.icon_ionicon_var_ios_person_outline);
        registeration_icon_username_textview.setTypeface(FontManager.getTypeFaceFromFontName(UserRegistration.this, FontManager.IONICONFONT));
        registeration_icon_username_textview.setTextSize(20);
        registeration_icon_username_textview.setTextColor(ContextCompat.getColor(UserRegistration.this, R.color.black_color));

        tvLastName.setText(R.string.icon_ionicon_var_ios_person_outline);
        tvLastName.setTypeface(FontManager.getTypeFaceFromFontName(UserRegistration.this, FontManager.IONICONFONT));
        tvLastName.setTextSize(20);
        tvLastName.setTextColor(ContextCompat.getColor(UserRegistration.this, R.color.black_color));

        registeration_icon_email_textview.setText(R.string.icon_ionicon_var_ios_email_outline);
        registeration_icon_email_textview.setTypeface(FontManager.getTypeFaceFromFontName(UserRegistration.this, FontManager.IONICONFONT));
        registeration_icon_email_textview.setTextSize(20);
        registeration_icon_email_textview.setTextColor(ContextCompat.getColor(UserRegistration.this, R.color.black_color));

        registeration_icon_phone.setText(R.string.icon_ionicon_var_android_call);
        registeration_icon_phone.setTypeface(FontManager.getTypeFaceFromFontName(UserRegistration.this, FontManager.IONICONFONT));
        registeration_icon_phone.setTextSize(20);
        registeration_icon_phone.setTextColor(ContextCompat.getColor(UserRegistration.this, R.color.black_color));

        registeration_icon_location.setText(R.string.icon_ionicon_var_ios_location_outline);
        registeration_icon_location.setTypeface(FontManager.getTypeFaceFromFontName(UserRegistration.this, FontManager.IONICONFONT));
        registeration_icon_location.setTextSize(20);
        registeration_icon_location.setTextColor(ContextCompat.getColor(UserRegistration.this, R.color.black_color));

    /*    tvAddress.setText(R.string.icon_ionicon_var_ios_location_outline);
        tvAddress.setTypeface(FontManager.getTypeFaceFromFontName(UserRegistration.this, FontManager.IONICONFONT));
        tvAddress.setTextSize(20);
        tvAddress.setTextColor(ContextCompat.getColor(UserRegistration.this, R.color.black_color));

        registeration_icon_age.setText(R.string.icon_ionicon_var_android_calendar);
        registeration_icon_age.setTypeface(FontManager.getTypeFaceFromFontName(UserRegistration.this, FontManager.IONICONFONT));
        registeration_icon_age.setTextSize(20);
        registeration_icon_age.setTextColor(ContextCompat.getColor(UserRegistration.this, R.color.black_color));*/
    }

    private void setIds() {
        registeration_icon_username_textview = (TextView) findViewById(R.id.registeration_icon_username_textview);
        //registeration_icon_age = (TextView) findViewById(R.id.registeration_icon_age);
        registeration_icon_email_textview = (TextView) findViewById(R.id.registeration_icon_email_textview);
        registeration_icon_phone = (TextView) findViewById(R.id.registeration_icon_phone);
        registeration_icon_location = (TextView) findViewById(R.id.registeration_icon_location);

    }

 /*   @OnClick(R.id.cbAccountDetail)
    void actionCB(CheckBox cb) {
        clearAccountDetail(true);
        layoutAccountDetail.setVisibility(cb.isChecked() ? View.VISIBLE : View.GONE);


    }

    void clearAccountDetail(boolean isClearPaytm) {
        etAccountNum.setText("");
        etBankName.setText("");
        etIfsc.setText("");
        etHolderName.setText("");
        etAccountNum.setError(null);
        etBankName.setError(null);
        etIfsc.setError(null);
        etHolderName.setError(null);

        if(isClearPaytm) {
            etPaytmNum.setText("");
            etPaytmNum.setError(null);

        }

    }*/

    @BindView(R.id.imgUser)
    CircleImageView imgUser;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etLocation)
    AutoCompleteTextView etLocation;
    @BindView(R.id.etPhone)
    EditText etPhone;
    //@BindView(R.id.etAge)
    //EditText etAge;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPswd)
    EditText etPswd;
    @BindView(R.id.etConfirmPswd)
    EditText etConfirmPswd;
    //@BindView(R.id.etZipCode)
   // EditText etZipCode;
    @BindView(R.id.tvLastName)
    TextView tvLastName;
    @BindView(R.id.etLastName)
    EditText etLastName;
    @BindView(R.id.etCode)
    EditText etCode;
   /* @BindView(R.id.etAccountNum)
    EditText etAccountNum;
    @BindView(R.id.etHolderName)
    EditText etHolderName;
    @BindView(R.id.etIfsc)
    EditText etIfsc;
    @BindView(R.id.etBankName)
    EditText etBankName;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.etPaytmNumber)
    EditText etPaytmNum;
    @BindView(R.id.etCustomAddress)
    EditText etCustomAddress;
    @BindView(R.id.layoutAccountDetail)
    LinearLayout layoutAccountDetail;
*/
}
