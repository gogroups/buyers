package com.gogroup.app.gogroupapp.Fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gogroup.app.gogroupapp.Adapters.GooglePlacesAutocompleteAdapter;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.FontManager;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.SellerRegistration;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerRegistrationPrimaryFragment extends Fragment {
    Uri uriUserImg;
    final int ACTION_REQUEST_GALLERY = 123;

    Context context;
    TextView registration_seller_name_icon, registration_seller_contact, registration_seller_email, registration_seller_tin,
            registration_seller_location, registration_seller_usp;
    ViewPager viewPager;

    public SellerRegistrationPrimaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_registration_primary_contact, container, false);
        ButterKnife.bind(this, view);
        initIds(view);
        setIcons();
        etLocation.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.spinner_item));

        return view;
    }

    private void setIcons() {
        registration_seller_name_icon.setText(R.string.icon_ionicon_var_ios_person_outline);
        registration_seller_name_icon.setTypeface(FontManager.getTypeFaceFromFontName(getActivity(), FontManager.IONICONFONT));
        registration_seller_name_icon.setTextSize(20);
        registration_seller_name_icon.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_color));

        tvLastName.setText(R.string.icon_ionicon_var_ios_person_outline);
        tvLastName.setTypeface(FontManager.getTypeFaceFromFontName(getActivity(), FontManager.IONICONFONT));
        tvLastName.setTextSize(20);
        tvLastName.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_color));

        registration_seller_email.setText(R.string.icon_ionicon_var_ios_email_outline);
        registration_seller_email.setTypeface(FontManager.getTypeFaceFromFontName(getActivity(), FontManager.IONICONFONT));
        registration_seller_email.setTextSize(20);
        registration_seller_email.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_color));

        registration_seller_tin.setText(R.string.icon_ionicon_var_android_list);
        registration_seller_tin.setTypeface(FontManager.getTypeFaceFromFontName(getActivity(), FontManager.IONICONFONT));
        registration_seller_tin.setTextSize(20);
        registration_seller_tin.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_color));

        registration_seller_contact.setText(R.string.icon_ionicon_var_android_call);
        registration_seller_contact.setTypeface(FontManager.getTypeFaceFromFontName(getActivity(), FontManager.IONICONFONT));
        registration_seller_contact.setTextSize(20);
        registration_seller_contact.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_color));

        registration_seller_location.setText(R.string.icon_ionicon_var_ios_location_outline);
        registration_seller_location.setTypeface(FontManager.getTypeFaceFromFontName(getActivity(), FontManager.IONICONFONT));
        registration_seller_location.setTextSize(20);
        registration_seller_location.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_color));

        tvAddress.setText(R.string.icon_ionicon_var_ios_location_outline);
        tvAddress.setTypeface(FontManager.getTypeFaceFromFontName(getActivity(), FontManager.IONICONFONT));
        tvAddress.setTextSize(20);
        tvAddress.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_color));


        registration_seller_usp.setText(R.string.fa_plus_square_o);
        registration_seller_usp.setTypeface(FontManager.getTypeFaceFromFontName(getActivity(), FontManager.IONICONFONT));
        registration_seller_usp.setTextSize(20);
        registration_seller_usp.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_color));

//        tvIconPswd.setText(R.string.fa_key);
//        tvIconPswd.setTypeface(FontManager.getTypeFaceFromFontName(getActivity(), FontManager.IONICONFONT));
//        tvIconPswd.setTextSize(20);
//        tvIconPswd.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_color));
//
//        tvIconConfirmPswd.setText(R.string.fa_lock);
//        tvIconConfirmPswd.setTypeface(FontManager.getTypeFaceFromFontName(getActivity(), FontManager.IONICONFONT));
//        tvIconConfirmPswd.setTextSize(20);
//        tvIconConfirmPswd.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_color));

    }

    private void initIds(View view) {
        registration_seller_name_icon = view.findViewById(R.id.registration_seller_name_icon);
        registration_seller_contact = view.findViewById(R.id.registration_seller_contact);
        registration_seller_email = view.findViewById(R.id.registration_seller_email);
        registration_seller_tin = view.findViewById(R.id.registration_seller_tin);
        registration_seller_location = view.findViewById(R.id.registration_seller_location);
        registration_seller_usp = view.findViewById(R.id.registration_seller_usp);
        viewPager = getActivity().findViewById(R.id.viewpager);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @OnClick(R.id.btnNext)
    public void next() {

//        if (true) {
//            viewPager.setCurrentItem(1);
//            return;
//        }

        if (isValid()) {
            SellerRegistration.postFields.clear();
            SellerRegistration.postFields.put("name", Utils.requestBody(etName.getText().toString().trim()));
            SellerRegistration.postFields.put("lastName", Utils.requestBody(""));
            SellerRegistration.postFields.put("contact_number", Utils.requestBody(etCode.getText().toString() + etContact.getText().toString().trim()));
            SellerRegistration.postFields.put("email", Utils.requestBody(etEmail.getText().toString().trim()));
            SellerRegistration.postFields.put("tin_number", Utils.requestBody(etTinNo.getText().toString().trim()));
            SellerRegistration.postFields.put("company_name", Utils.requestBody(etCompanyName.getText().toString().trim()));
            SellerRegistration.postFields.put("location", Utils.requestBody(etLocation.getText().toString().trim()));
            SellerRegistration.postFields.put("zipCode", Utils.requestBody(etZipCode.getText().toString().trim()));
            SellerRegistration.postFields.put("seller_usp", Utils.requestBody(etUsp.getText().toString().trim()));
            SellerRegistration.postFields.put("password", Utils.requestBody(etPswd.getText().toString().trim()));
            SellerRegistration.postFields.put("confirm_password", Utils.requestBody(etConfirmPswd.getText().toString().trim()));
            SellerRegistration.postFields.put("user_type", Utils.requestBody(Utils.userType));
            SellerRegistration.postFields.put("address", Utils.requestBody(etCustomAddress.getText().toString().trim()));

            if (uriUserImg != null) {
                File file = Utils.reduceImageSize(new File(uriUserImg.getPath()));
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                ((SellerRegistration) getActivity()).multipartImg = MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestBody);
            }
            viewPager.setCurrentItem(1);
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isPermissionsAllowed() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, android.Manifest.permission.CAMERA))
            permissionsNeeded.add("Camera\n");
        if (!addPermission(permissionsList, android.Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Read Storage\n");
        if (!addPermission(permissionsList, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write Storage\n");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                if (permissionsList.size() == 1 && permissionsList.contains(android.Manifest.permission.CAMERA)) {
                    return true;
                } else {
                    // Need Rationale
                    String message = "Allow GoGroup to the following permissions\n\n" + permissionsNeeded.get(0);
                    for (int i = 1; i < permissionsNeeded.size(); i++)
                        message = message + permissionsNeeded.get(i);
                    showMessageOKCancel(message,
                            new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            BaseActivity.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            });
                    return false;
                }
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    BaseActivity.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return false;
        }

        return true;

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("Allow", okListener)
                .setNegativeButton("Deny", null)
                .create()
                .show();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ActivityCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
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
        CroperinoFileUtil.setupDirectory(getActivity());
        if (CroperinoFileUtil.verifyStoragePermissions(getActivity())) {
            prepareChooser();
        }
    }

    private void prepareChooser() {
        Croperino.prepareChooser(getActivity(), "Start Smiling! Snap a pic from your phone or use a saved one " + new String(Character.toChars(0x1F60A)), ContextCompat.getColor(getActivity(), android.R.color.white));
    }

    private void prepareCamera() {
        Croperino.prepareCamera(getActivity());
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
                        Toast.makeText(getActivity(), getString(R.string.validationCamera), Toast.LENGTH_LONG).show();
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
            case BaseActivity.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
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
                    Toast.makeText(getActivity(), getString(R.string.validationPermission), Toast.LENGTH_LONG)
                            .show();
                }
            }
            break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CroperinoConfig.REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    uriUserImg = Uri.fromFile(CroperinoFileUtil.getmFileTemp());
                    Picasso.with(getActivity()).load(uriUserImg).into(imgUser);

                }
                break;
            case CroperinoConfig.REQUEST_PICK_FILE:
                if (resultCode == Activity.RESULT_OK) {
                    CroperinoFileUtil.newGalleryFile(data, getActivity());
                    uriUserImg = Uri.fromFile(CroperinoFileUtil.getmFileTemp());
                    Picasso.with(getActivity()).load(uriUserImg).into(imgUser);

                }
                break;
            default:
                break;

        }


    }


    @BindView(R.id.imgUser)
    CircleImageView imgUser;
    @BindView(R.id.tvIconConfirmPswd)
    TextView tvIconConfirmPswd;
    @BindView(R.id.tvIconPswd)
    TextView tvIconPswd;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.etCompanyName)
    EditText etCompanyName;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etContact)
    EditText etContact;
    @BindView(R.id.etPswd)
    EditText etPswd;
    @BindView(R.id.etConfirmPswd)
    EditText etConfirmPswd;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etLocation)
    AutoCompleteTextView etLocation;
    @BindView(R.id.etUsp)
    EditText etUsp;
    @BindView(R.id.etTinNo)
    EditText etTinNo;
    @BindView(R.id.etZipCode)
    EditText etZipCode;
    @BindView(R.id.tvLastName)
    TextView tvLastName;
    //    @BindView(R.id.etLastName)
//    EditText etLastName;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.etCustomAddress)
    EditText etCustomAddress;


    private boolean isValid() {
        if (etCompanyName.getText().toString().trim().length() == 0) {
            Utils.setEditTextError(etCompanyName, getString(R.string.validationCompanyName));
            return false;
        } else if (etName.getText().toString().trim().length() == 0) {
            Utils.setEditTextError(etName, getString(R.string.validationName));
            return false;
        } else if (etCode.getText().toString().trim().length() == 0) {
            Utils.showShortToast(getActivity(), getString(R.string.validationCode));
            return false;
        } else if (etContact.getText().toString().trim().length() == 0) {
            Utils.setEditTextError(etContact, getString(R.string.validationContact));
            return false;
        } else if (etContact.getText().toString().trim().length() < 10) {
            Utils.setEditTextError(etContact, getString(R.string.validationValidContact));
            return false;
        } else if (etEmail.getText().toString().trim().length() == 0) {
            Utils.setEditTextError(etEmail, getString(R.string.validationEmail));
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
            Utils.setEditTextError(etEmail, getString(R.string.validationValidEmail));
            return false;
        } else if (etTinNo.getText().toString().trim().length() == 0) {
            Utils.setEditTextError(etTinNo, getString(R.string.validationTinNo));
            return false;
        } else if (etCustomAddress.getText().toString().trim().length() == 0) {
            Utils.setEditTextError(etCustomAddress, getString(R.string.validationAddress));
            return false;
        } else if (etLocation.getText().toString().trim().length() == 0) {
            Utils.setEditTextError(etLocation, getString(R.string.validationLocation));
            return false;
        } else if (etPswd.getText().toString().trim().length() == 0) {
            Utils.setEditTextError(etPswd, getString(R.string.validationPassword));
            return false;
        } else if (etConfirmPswd.getText().toString().trim().length() == 0) {
            Utils.setEditTextError(etConfirmPswd, getString(R.string.validationConfirmPassword));
            return false;
        } else if (!etConfirmPswd.getText().toString().trim().equals(etPswd.getText().toString().trim())) {
            Utils.setEditTextError(etConfirmPswd, getString(R.string.validationMatchingPassword));
            return false;
        } else if (etZipCode.getText().toString().trim().length() == 0) {
            Utils.setEditTextError(etZipCode, getString(R.string.validationZipCode));
            return false;
        } else if (etUsp.getText().toString().trim().length() == 0) {
            Utils.setEditTextError(etUsp, getString(R.string.validationSellerDetail));
            return false;
        }
        return true;
    }

}
