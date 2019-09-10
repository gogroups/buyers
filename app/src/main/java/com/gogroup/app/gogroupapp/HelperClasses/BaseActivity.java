package com.gogroup.app.gogroupapp.HelperClasses;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.gogroup.app.gogroupapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import butterknife.Optional;
import dmax.dialog.SpotsDialog;

/**
 * Created by zabius on 9/8/17.
 */

public class BaseActivity extends AppCompatActivity {



//    MobiComQuickConversationFragment 642 203
//    MobiComConversationFragment 1394


    public static final int REFRESH_DASHBOARD = 7;
    public static final int REFRESH_PROFILE = 8;
    public static final int REFRESH_ACTIVITY = 9;
    public static final int REFRESH_LIST = 10;
    public static final int REFRESH_FRAGMENT = 11;
    public static final int REQUEST_PHONE_CALL = 14;
    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    public static final String DEEPLINK_DATA = "deeplink";
    public static final String DATA = "data";
    public static final String IS_PURCHASED = "isPurchased";
    public static final String ID = "id";
    public static final String INFO = "info";
    public static final int VIEW_TYPE_LOADING = 123;
    public static final int VIEW_TYPE_ITEM = 321;
    public static boolean isRefresh;
    public SpotsDialog progressDialog;
    public AlertDialog alertDialog;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new SpotsDialog(BaseActivity.this);
        progressBar = new ProgressBar(getApplicationContext(), null, android.R.attr.progressBarStyleSmall);
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public void showProgressbar() {
        if (!progressDialog.isShowing())
//            progressDialog.setMessage(getString(R.string.loading));
//        progressDialog.setTitle(getString(R.string.pleaseWait));
            progressDialog.setCancelable(false);

        //  progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (progressDialog != null) {
            progressDialog.show();
        }

    }


    public void hideProgressBar() {
        try {

            if (progressDialog != null) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        }catch (Exception e){}
//        progressBar.setVisibility(View.GONE);
    }

    public void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("your internet is not working.")
                .setMessage("Can you retry?")
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                }).setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }
    public void showAlert(String title, String message) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(title != null ? title : getString(R.string.internetTitle))
                    .setMessage(message != null ? message : getString(R.string.internetMessage))
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    }).setCancelable(false);
            alertDialog = builder.create();
            alertDialog.show();
        } catch (Exception e) {
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        hideProgressBar();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isPermissionsAllowed() {
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
                    String message = "Allow GoGroup the following permissions\n\n" + permissionsNeeded.get(0);
                    for (int i = 1; i < permissionsNeeded.size(); i++)
                        message = message + permissionsNeeded.get(i);
                    showMessageOKCancel(message,
                            new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            });
                    return false;
                }
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return false;
        }

        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("Allow", okListener)
                .setNegativeButton("Deny", null)
                .create()
                .show();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
//                Map<String, Integer> perms = new HashMap<String, Integer>();
//                // Initial
////                perms.put(android.Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
//                perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
//                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
//                // Fill with results
//                for (int i = 0; i < permissions.length; i++)
//                    perms.put(permissions[i], grantResults[i]);
//                // Check for ACCESS_FINE_LOCATION
//                if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    // All Permissions Granted
//                    openGallery();
//                } else {
//                    // Permission Denied
//                    Toast.makeText(getApplicationContext(), getString(R.string.validationPermission), Toast.LENGTH_LONG)
//                            .show();
//                }
//            }
//            break;
//            default:super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }

    @Optional
    @OnClick(R.id.btnBack)
    void back()
    {
        finish();
    }

}
