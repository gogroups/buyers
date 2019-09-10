package com.gogroup.app.gogroupapp.HelperClasses;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.GoogleAddressResponse;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gaurav on 5/21/2017.
 */

public class Utils {

    static DatePickerDialog mDatePicker;
    static int mDate, mMonth, mYear, mHour, mMinute, mSeconds;
    static String mStrDate;
    static KProgressHUD mProgressHud;
    public static String userType;

    public static int MAX_IMG_WIDTH = 1024;
    public static int MAX_IMG_HEIGHT = 768;
    public static int imgSize = (int) Math.ceil(Math.sqrt(MAX_IMG_WIDTH * MAX_IMG_HEIGHT));

    static SimpleDateFormat defaultTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static DateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
    public static DateFormat dateFormatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static DateFormat dateFormat6 = new SimpleDateFormat("MMMM dd, yyyy"); //August 21, 2014
    public static DateFormat dateFormat3 = new SimpleDateFormat("EEE, MMM dd"); //Wed, Aug 21
    public static DateFormat dateFormat4 = new SimpleDateFormat("EEE, MMMM dd"); //Wed, August 21
    public static DateFormat dateFormat5 = new SimpleDateFormat("EEEE, MMMM dd"); //Wednesday, August 21
    public static DateFormat dateFormatDefault = new SimpleDateFormat("MMM dd, yyyy"); //Aug 21, 2014
    public static SimpleDateFormat dateFormatZone = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzzz");
    public static SimpleDateFormat timeFormatDefault = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    public static SimpleDateFormat timeFormat12 = new SimpleDateFormat("hh:mm aa");
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a", Locale.getDefault());
    public static SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());

    static CallBackFromAlertDialog mCallBackFromAlerDialog;


    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void showShortToast(Context activity, String message) {
        Toast.makeText(activity, message != null ? message : "", Toast.LENGTH_LONG).show();
    }

    public static void showServerError(Context activity) {
        Toast.makeText(activity, activity.getString(R.string.serverError), Toast.LENGTH_LONG).show();
    }

    public static void setEditTextError(EditText editText, String message) {
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.setFocusable(true);
        editText.setCursorVisible(true);
        editText.setError(message);

    }

    public static void enableEditTextFocus(EditText editText) {
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.setFocusable(true);
        editText.setCursorVisible(true);
    }

    public static void disableEditTextFocus(EditText editText) {
        editText.setFocusableInTouchMode(false);
        editText.setFocusable(false);
        editText.setCursorVisible(false);
    }

    public static RequestBody requestBody(String name) {
        return RequestBody.create(MediaType.parse("text/plain"), name);
    }

    public static boolean isInterNetConnected(Context context) {
        boolean isTrue = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            if (networkInfo.isAvailable() && networkInfo.isConnected()) {
                isTrue = true;
            }
        }

        if (!isTrue) {
            Utils.showShortToast(context, context.getString(R.string.Validation_Internet_Connection));
        }

        return isTrue;
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;

        if (html == null) {
            return null;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static void picasso(Context activity, String path, ImageView imgFile) {
        if (path == null || path.equals("")) {
            return;
        }

        Picasso.with(activity)
                .load(path)
                .transform(new BitmapTransform(MAX_IMG_WIDTH, MAX_IMG_HEIGHT))
                .resize(imgSize, imgSize)
                .centerInside().placeholder(R.drawable.place_holder)
                .into(imgFile);
    }

    public static boolean isValidEmail(String str) {
        Pattern patterns = Patterns.EMAIL_ADDRESS;
        boolean match = patterns.matcher(str).matches();
        return match;
    }

    public static boolean isValidPhone(String str) {
        Pattern patterns = Patterns.PHONE;
        boolean match = patterns.matcher(str).matches();
        return match;
    }

    public static String getDate(Context c) {

        mStrDate = new String();
        Calendar cal = Calendar.getInstance();
        mDate = cal.get(Calendar.DATE);
        mMonth = cal.get(Calendar.MONTH);
        mYear = cal.get(Calendar.YEAR);

        mDatePicker = new DatePickerDialog(c, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String str = String.valueOf(dayOfMonth) + "-" + String.valueOf((monthOfYear + 1)) + "-" + String.valueOf(year);
                mStrDate = str;
            }
        }, mYear, mMonth, mDate);

        mDatePicker.show();

        return mStrDate;
    }

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // Ignore exceptions if any
            Log.e("KeyBoardUtil", e.toString(), e);
        }
    }


    public static void showLongToastMessageOnActivity(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }


    /* By Atinder */
    public static void saveIntToSharedPreference(Context context, int value, String key) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.shared), context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }


    public static int getIntFromSharedPreferenceForKey(String key, Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.shared), context.MODE_PRIVATE);
        int value = sharedPreferences.getInt(key, 0);
        return value;
    }

    /* End By Atinder */


    public static void saveStringToSharedPreference(Context context, String value, String key) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.shared), context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getStringFromSharedPreferenceForKey(String key, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.shared), context.MODE_PRIVATE);
        String value = sharedPreferences.getString(key, "");
        return value;
    }

    public static void setProgressDialogWithTitle(String title, ProgressDialog p) {
        p.setTitle(title);
        p.setCancelable(false);
        p.setIndeterminate(false);
        p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    public static Bitmap getCircularBitmapWithWhiteBorder(Bitmap bitmap,
                                                          int borderWidth, int color) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }

        final int width = bitmap.getWidth() + borderWidth;
        final int height = bitmap.getHeight() + borderWidth;

        Bitmap canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        Canvas canvas = new Canvas(canvasBitmap);
        float radius = width > height ? ((float) height) / 2 : ((float) width) / 2;
        canvas.drawCircle(width / 2, height / 2, radius, paint);
        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(borderWidth);
        canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2, paint);
        return canvasBitmap;
    }

    public static void versionCheckForDrawable(ImageView imageView, int drawableId, Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
            imageView.setImageDrawable(context.getDrawable(drawableId));
        else
            imageView.setImageDrawable(context.getResources().getDrawable(drawableId));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void versionSetDrawableForView(View imageView, int drawableId, Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
            imageView.setBackground(context.getDrawable(drawableId));
        else
            imageView.setBackground(context.getResources().getDrawable(drawableId));
    }


    public static void setDrawableForImageView(ImageView imageView, int drawableId, Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
            imageView.setImageDrawable(context.getDrawable(drawableId));
        else
            imageView.setImageDrawable(context.getResources().getDrawable(drawableId));
    }

//    public static void setDrawableForNetworkImageView(NetworkImageView imageView, int drawableId, Context context){
//        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
//            imageView.setImageDrawable(context.getDrawable(drawableId));
//        else
//            imageView.setImageDrawable(context.getResources().getDrawable(drawableId));
//    }

    public static boolean stringNullCheck(String string) {
        if (string != null) {
            if (string.length() > 0) {
                return true;
            }
        }

        return false;
    }

    public static String validStringCheck(String str) {
        if (str != null && str.length() > 0) {
            return str;
        } else {
            return "N/A";
        }

    }


    public static void setUnderLineToButton(Button btn, String str) {
        //textview.setPaintFlags(textview.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        String udata = "Underlined Text";
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        btn.setText(content);

    }

    public static void setUnderLineToTextView(TextView textView, String str) {
        String udata = "Underlined Text";
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        textView.setText(content);

    }


    /**
     * Showing the progresshud
     *
     * @param context  - Activity Context
     * @param strTitle - Title on the progresshud
     */
    public static void showProgressHudOn(Context context, String strTitle) {

        mProgressHud = null;
        mProgressHud = KProgressHUD.create(context, KProgressHUD.Style.SPIN_INDETERMINATE);
        if (mProgressHud != null) {
            if (strTitle != null) {
                mProgressHud.setLabel(strTitle);
            }

            mProgressHud.setCancellable(false);
            mProgressHud.setDimAmount(0.5f);
            mProgressHud.setAnimationSpeed(1);
            mProgressHud.show();
        } else {

            Toast.makeText(context, "ProgressHud is not initialized", Toast.LENGTH_LONG).show();
        }

    }

    public static void hideProgressHud() {
        if (mProgressHud != null) {
            mProgressHud.dismiss();
            mProgressHud = null;
        } else {
            Log.d("Hud message", "Hud is not initialized");
        }
    }


    public static void hideKeyboard(Activity activity, View viewToHide) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(viewToHide.getWindowToken(), 0);
    }

    public static void showAlertDialog(Context context, String strTitle, String strMessage, final CallBackFromAlertDialog callBackFromAlertDialog) {

        AlertDialog.Builder checkinAll = new AlertDialog.Builder(context);
        checkinAll.setTitle(strTitle);
        checkinAll.setMessage(strMessage);
        checkinAll.setCancelable(true);


        checkinAll.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (callBackFromAlertDialog != null) {
                    callBackFromAlertDialog.sendCallBackOfButtonClicked(true);
                }

            }
        });
        checkinAll.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (callBackFromAlertDialog != null) {
                    callBackFromAlertDialog.sendCallBackOfButtonClicked(false);
                }

            }
        });

        checkinAll.show();


    }

    public static void showDialog(final Activity activity, String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(activity,
                R.style.Theme_AppCompat_DayNight_Dialog));

        alertDialogBuilder.setTitle(title!=null?title:"");
        alertDialogBuilder.setMessage(message!=null?message:"");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                activity.finish();

            }
        });
        alertDialogBuilder.show();
    }

    public interface CallBackFromAlertDialog {
        public void sendCallBackOfButtonClicked(boolean check);

    }


    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }


    }


    public static File reduceImageSize(File file) {
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            o.inPurgeable = true;
            o.inScaled = true;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 150;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);

            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getPath(Uri uri, Context context) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    public static List<String> checkGoogleAddress(CharSequence charSequence) {

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


                }
            }

            @Override
            public void onFailure(Call<GoogleAddressResponse> call, Throwable t) {
            }
        });
        return addresses;
    }

    public static void dimBehind(PopupWindow popupWindow) {
        View container = popupWindow.getContentView().getRootView();
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.35f;
        wm.updateViewLayout(container, p);
    }



    public static String convertDateFormat(String date, DateFormat targetFormat) {
        try {
            Date originalDate = dateFormat.parse(date);
//            String result = date.equals(dateFormat.format(new Date())) ? getString(R.string.today) :targetFormat.format(originalDate);
            String result = targetFormat.format(originalDate);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
