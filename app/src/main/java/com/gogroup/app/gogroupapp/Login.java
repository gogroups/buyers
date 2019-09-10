package com.gogroup.app.gogroupapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.gogroup.app.gogroupapp.CallBackListeners.CallBackApi;
import com.gogroup.app.gogroupapp.HelperClasses.ApiCalls;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.FontManager;
import com.gogroup.app.gogroupapp.HelperClasses.RestClient;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.Models.TwitterModel;
import com.gogroup.app.gogroupapp.Responses.MesiboDetailResponse;
import com.gogroup.app.gogroupapp.Responses.PostResponse;
import com.gogroup.app.gogroupapp.Responses.UserResponse;
import com.gogroup.app.gogroupapp.Seller.SellerDashboard;
import com.gogroup.app.gogroupapp.User.DeepLinking;
import com.gogroup.app.gogroupapp.User.UserDashboard;
import com.gogroup.app.gogroupapp.User.UserDashboardNew;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.chatcamp.sdk.ChatCamp;
import io.chatcamp.sdk.ChatCampException;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Sandeep on 17-May-17.
 */

public class Login extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    CallbackManager callbackManager;
    private static final String TAG = Login.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;
    Dialog customDialog;
    boolean isDeepLinking, isShowPswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        setContentView(R.layout.login);
        ButterKnife.bind(Login.this);

        try {
            isDeepLinking = getIntent().getBooleanExtra(DEEPLINK_DATA, false);
        } catch (NullPointerException e) {
            isDeepLinking = false;
        }
        UserPreferences.getInstance().setIsDeepLink(isDeepLinking);

        Twitter.initialize(this);
        generateHashkey();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        twitterCallback();
        callbackManager = CallbackManager.Factory.create();
//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
//        btnFb.setReadPermissions(Arrays.asList("public_profile", "email","user_friends"));
        btnFb.setReadPermissions(Arrays.asList("public_profile", "email"));
        btnFb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d("facebookDetail", "" + loginResult + loginResult.getAccessToken() + loginResult.getAccessToken().getUserId() + loginResult.getRecentlyDeniedPermissions()
                        + loginResult.getRecentlyGrantedPermissions());

                FbRequestData(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(Login.this, "Canceled", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(Login.this, "" + exception.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
        loginTypesVisibility();
        imgEye.setVisibility(View.GONE);
        etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                imgEye.setVisibility(View.VISIBLE);
                etPassword.setSelection(etPassword.getText().length());
                if (etPassword.getText().toString().matches("")) {
                    imgEye.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        setIcons();
        customDialog = new Dialog(Login.this);

    }

    public void FbRequestData(AccessToken accessToken) {
        // GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken()
        GraphRequest request = GraphRequest.newMeRequest(accessToken
                , new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        JSONObject json = response.getJSONObject();
                        Log.d("facebook", "" + json + object);
                        Intent i = new Intent(Login.this, UserRegistration.class);
                        i.putExtra(UserRegistration.FACEBOOK_DATA, "" + object);
                        startActivity(i);
                        LoginManager.getInstance().logOut();
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email,gender,picture,name,link,birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }


    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }


    public void generateHashkey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.gogroup.app.gogroupapp", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("hash", Base64.encodeToString(md.digest(),
                        Base64.NO_WRAP));

            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Name not found", e.getMessage(), e);

        } catch (NoSuchAlgorithmException e) {
            Log.d("Error", e.getMessage(), e);
        }
    }


    private void handleGoogleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Intent i = new Intent(Login.this, UserRegistration.class);
            i.putExtra(UserRegistration.GOOGLE_DATA, acct);
            startActivity(i);
        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(Login.this, "Error", Toast.LENGTH_LONG).show();
        }
    }

    public void linkedinCallback() {
        LISessionManager.getInstance(getApplicationContext())
                .init(this, buildScope(), new AuthListener() {
                    @Override
                    public void onAuthSuccess() {
                        String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,email-address)";
                        //String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name)";
                        linkededinApiHelper(url);
                    }

                    @Override
                    public void onAuthError(LIAuthError error) {
                        Toast.makeText(getApplicationContext(), "failed" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }, true);


    }

    public void linkededinApiHelper(final String url) {
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(Login.this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {
                    Log.d("linked", "" + result);
                    Intent i = new Intent(Login.this, UserRegistration.class);
                    i.putExtra(UserRegistration.LINKED_DATA, "" + result.getResponseDataAsJson());
                    startActivity(i);
                    LISessionManager.getInstance(getApplicationContext()).clearSession();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiError(LIApiError error) {

            }
        });

    }

    public void twitterCallback() {
        btnTwitter.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();

                String token = authToken.token;
                String secret = authToken.secret;

                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
                AccountService accountService = twitterApiClient.getAccountService();
                Call<User> call = accountService.verifyCredentials(true, true, true);
                call.enqueue(new Callback<com.twitter.sdk.android.core.models.User>() {
                    @Override
                    public void success(Result<com.twitter.sdk.android.core.models.User> result) {
                        User user = result.data;
                        Intent i = new Intent(Login.this, UserRegistration.class);
                        TwitterModel twitter = new TwitterModel();
                        twitter.setValues(String.valueOf(user.id), user.email, user.name, user.screenName, user.profileImageUrl, user.location);
                        i.putExtra(UserRegistration.TWITTER_DATA, twitter);
                        startActivity(i);

                    }

                    @Override
                    public void failure(TwitterException exception) {
                    }
                });


            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                //  Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    private void setIcons() {
        mTextLogin.setText(R.string.icon_ionicon_var_ios_email_outline);
        mTextLogin.setTypeface(FontManager.getTypeFaceFromFontName(getApplicationContext(), FontManager.IONICONFONT));
        mTextLogin.setTextSize(20);
        mTextLogin.setTextColor(ContextCompat.getColor(this, R.color.black_color));


        mButtonPasswordIcon.setText(R.string.icon_ionicon_var_ios_unlocked_outline);
        mButtonPasswordIcon.setTypeface(FontManager.getTypeFaceFromFontName(getApplicationContext(), FontManager.IONICONFONT));
        mButtonPasswordIcon.setTextSize(20);
        mButtonPasswordIcon.setTextColor(ContextCompat.getColor(this, R.color.black_color));
    }

    public void loginTypesVisibility() {

        try {
            if (Utils.userType.toLowerCase().trim().equals("user")) {
                loginTypes.setVisibility(View.VISIBLE);
            } else {
                loginTypes.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            loginTypes.setVisibility(View.VISIBLE);

        }

    }


    private boolean isValid() {
        if (etUserName.getText().toString().length() == 0) {
            Utils.setEditTextError(etUserName, getResources().getString(R.string.login_email_error));
            return false;
        }
        if (!Utils.emailValidator(etUserName.getText().toString())) {
            Utils.setEditTextError(etUserName, getResources().getString(R.string.user_email_pattern_error));
            return false;
        } else if (etPassword.getText().toString().length() == 0) {
            Utils.setEditTextError(etPassword, getResources().getString(R.string.login_password_error));

            return false;
        }

        return true;
    }


    @OnClick(R.id.tvSignUp)
    void signUp() {
       // if (Utils.userType.equals("User")) {
            Intent i = new Intent(Login.this, UserRegistration.class);
            startActivity(i);
        //} else {
          //  Intent i = new Intent(Login.this, SellerRegistration.class);
           // startActivity(i);
        //}

    }

    @OnClick(R.id.imgTwitter)
    void twitterSignUp() {
        if (Utils.isInterNetConnected(Login.this))
            btnTwitter.performClick();

    }

    @OnClick(R.id.imgFb)
    void fbSignUp() {
        if (Utils.isInterNetConnected(Login.this))
            btnFb.performClick();

    }

    @OnClick(R.id.imgLinkedIn)
    void linkedInSignUp() {
        if (Utils.isInterNetConnected(Login.this))
            linkedinCallback();

    }

    @OnClick(R.id.imgGoogle)
    void googleSignUp() {
        if (Utils.isInterNetConnected(Login.this)) {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    @OnClick(R.id.img_eye)
    void showPassword() {
        if (isShowPswd) {
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            //etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imgEye.setImageResource(R.drawable.ic_eye);
            isShowPswd = false;

        } else {
            imgEye.setImageResource(R.drawable.crossed_eye);
            isShowPswd = true;
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

//        etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

        }

    }


    @BindView(R.id.btnTwitter)
    TwitterLoginButton btnTwitter;
    @BindView(R.id.btnGoogle)
    SignInButton btnGoogle;
    @BindView(R.id.btnFb)
    LoginButton btnFb;
    @BindView(R.id.etEmail)
    EditText etUserName;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.login_activity_login_icon)
    TextView mTextLogin;
    @BindView(R.id.login_activity_password_icon)
    TextView mButtonPasswordIcon;
    @BindView(R.id.user_login_types)
    LinearLayout loginTypes;
    @BindView(R.id.tvForgetPswd)
    TextView mTextViewForgotPassword;
    @BindView(R.id.img_eye)
    ImageView imgEye;


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @OnClick(R.id.login_login_button)
    void login() {
        if (isValid()) {
            UserPreferences.getInstance().setDeviceToken(FirebaseInstanceId.getInstance().getToken());
            if (Utils.isInterNetConnected(Login.this)) {
                showProgressbar();
                RestClient.get().login(etUserName.getText().toString().trim(), etPassword.getText().toString().trim(), "" + UserPreferences.getInstance().getDeviceToken(), Utils.userType).
                        enqueue(new retrofit2.Callback<UserResponse>() {
                            @Override
                            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                                if (response.body() != null) {
                                    if (response.body().isStatus()) {
                                        UserResponse userDetail = response.body().getData().getUserDetail();

                                        if (userDetail.getUserType().toLowerCase().trim().equals("user"))
                                        {

                                            applogicLogin(response.body().getData());
//                                            chatCamp(response.body().getData());

                                        } else {

                                            UserPreferences.getInstance().savePreference(response.body().getData());

                                            if (response.body().getData().getUserDetail().isVerifyOtp()) {

                                                if (userDetail.getIsActive() != null && userDetail.getIsActive() == 1) {
                                                    Intent i = new Intent(Login.this, SellerDashboard.class);
                                                    startActivity(i);
                                                    ActivityCompat.finishAffinity(Login.this);
                                                } else {
                                                    UserPreferences.getInstance().clearUserDetails();
                                                    Toast.makeText(Login.this, getString(R.string.inActiveStatus), Toast.LENGTH_LONG).show();
                                                }
                                                hideProgressBar();
                                            } else {
                                                updateOtp();
                                                hideProgressBar();

                                            }
                                        }
                                    } else {

                                        Utils.showShortToast(Login.this, response.body().getMessage());
                                        hideProgressBar();

                                    }

                                } else {
                                    Utils.showServerError(Login.this);
                                    hideProgressBar();

                                }

                            }

                            @Override
                            public void onFailure(Call<UserResponse> call, Throwable t) {
                                hideProgressBar();
                                Utils.showShortToast(Login.this, t.getMessage());
                            }
                        });

            }
        }

    }

    private void chatCamp(final UserResponse data) {

        ChatCamp.init(this, getString(R.string.chatCampAppId));
        ChatCamp.connect(data.getUserDetail().getUserId(), new ChatCamp.ConnectListener() {
            @Override
            public void onConnected(io.chatcamp.sdk.User user, ChatCampException e) {
                if (e != null) {

                    Utils.showShortToast(getApplicationContext(), getString(R.string.somethingWentWrong));
                } else {

                    ChatCamp.updateUserDisplayName(data.getUserDetail().getName(), new ChatCamp.UserUpdateListener() {
                        //                            ChatCamp.updateUserProfileUrl("https://iflychat.com", new ChatCamp.UserUpdateListener() {
                        @Override
                        public void onUpdated(io.chatcamp.sdk.User user, ChatCampException e) {

                            if(e!=null)
                            {
                                Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            UserPreferences.getInstance().savePreference(data);

                            if (data.getUserDetail().isVerifyOtp()) {
                                if (isDeepLinking) {
                                    Intent i = new Intent(Login.this, DeepLinking.class);
                                    startActivity(i);
                                    ActivityCompat.finishAffinity(Login.this);
                                    hideProgressBar();

                                } else {

                                    Intent i = new Intent(Login.this, UserDashboardNew.class);
                                    startActivity(i);
                                    ActivityCompat.finishAffinity(Login.this);
                                    hideProgressBar();
                                }
                            } else {
                                updateOtp();
                                hideProgressBar();
                            }
                            //Log.d("CHATCAMP APP", FirebaseInstanceId.getInstance().getToken());
                        }
                    });
                    if (FirebaseInstanceId.getInstance().getToken() != null) {
                        ChatCamp.updateUserPushToken(FirebaseInstanceId.getInstance().getToken(), new ChatCamp.UserPushTokenUpdateListener() {
                            @Override
                            public void onUpdated(io.chatcamp.sdk.User user, ChatCampException e) {
                                Log.d("CHATCAMP_APP", "PUSH TOKEN REGISTERED");

                            }
                        });
                    }
                }
            }
        });
    }

    private void applogicLogin(final UserResponse data) {

        final UserResponse userDetail = data.getUserDetail();
//        UserLoginTask.TaskListener listener = new UserLoginTask.TaskListener() {
//
//            @Override
//            public void onSuccess(RegistrationResponse registrationResponse, Context context) {

        UserPreferences.getInstance().savePreference(data);

        if (userDetail.isVerifyOtp()) {
            if (isDeepLinking) {
                Intent i = new Intent(Login.this, DeepLinking.class);
                startActivity(i);
                ActivityCompat.finishAffinity(Login.this);
                hideProgressBar();

            } else {

                Intent i = new Intent(Login.this, UserDashboardNew.class);
                startActivity(i);
                ActivityCompat.finishAffinity(Login.this);
                hideProgressBar();
            }
        } else {
            updateOtp();
            hideProgressBar();
        }
        //  ApplozicClient.getInstance(context).hideChatListOnNotification();
//            }
//
//            @Override
//            public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
//                //If any failure in registration the callback  will come here
//                hideProgressBar();
//                Toast.makeText(Login.this, "" + exception.getMessage(), Toast.LENGTH_LONG).show();
//
//            }


//        };

//        com.applozic.mobicomkit.api.account.user.User user = new com.applozic.mobicomkit.api.account.user.User();
//        user.setUserId(userDetail.getUserId()); //userId it can be any unique user identifier
//        user.setDisplayName(userDetail.getName()); //displayName is the name of the user which will be shown in chat messages
//        user.setEmail(userDetail.getEmail()); //optional
//        user.setAuthenticationTypeId(com.applozic.mobicomkit.api.account.user.User.AuthenticationType.APPLOZIC.getValue());  //User.AuthenticationType.APPLOZIC.getValue() for password verification from Applozic server and User.AuthenticationType.CLIENT.getValue() for access Token verification from your server set access token as password
//        user.setPassword(""); //optional, leave it blank for testing purpose, read this if you want to add additional security by verifying password from your server https://www.applozic.com/docs/configuration.html#access-token-url
//        user.setImageLink(userDetail.getProfileImage());//optional,pass your image link
//        new UserLoginTask(user, listener, this).execute((Void) null);


    }

    private void updateOtp() {
        if (Utils.isInterNetConnected(Login.this)) {
            showProgressbar();
            RestClient.get().updateOtp(UserPreferences.getInstance().getToken(), UserPreferences.getInstance().getEmail(), UserPreferences.getInstance().getUserId()).
                    enqueue(new retrofit2.Callback<UserResponse>() {
                        @Override
                        public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
                                    Intent i = new Intent(Login.this, OtpActivity.class);
                                    startActivity(i);
                                } else {

                                    Utils.showShortToast(Login.this, response.body().getMessage());
                                }

                            } else {
                                Utils.showServerError(Login.this);
                            }
                            hideProgressBar();

                        }

                        @Override
                        public void onFailure(Call<UserResponse> call, Throwable t) {
                            hideProgressBar();
                            Utils.showShortToast(Login.this, t.getMessage());
                        }
                    });

        }
    }

    @OnClick(R.id.tvForgetPswd)
    void forgotPswd() {
        customDialog.setContentView(R.layout.layout_forgot_password);
        final EditText email = (EditText) customDialog.findViewById(R.id.et_email);
        final ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = email.getText().toString();
                ClipData myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);
            }
        });
        TextView cancel = (TextView) customDialog.findViewById(R.id.tv_cancel);
        TextView ok = (TextView) customDialog.findViewById(R.id.tv_ok);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = customDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        customDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (!email.getText().toString().trim().isEmpty()) {
                    if (Utils.emailValidator(email.getText().toString())) {
                        sendPasswordToEmail(email.getText().toString());

                    } else {
                        Utils.setEditTextError(email, getResources().getString(R.string.user_email_pattern_error));

                    }
                } else {
                    Utils.setEditTextError(email, getResources().getString(R.string.login_email_error));

                }
            }

        });
        customDialog.show();
    }

    private void sendPasswordToEmail(String emailId) {
        if (Utils.isInterNetConnected(Login.this)) {
            showProgressbar();
            RestClient.get().forgotPassword(emailId).
                    enqueue(new retrofit2.Callback<PostResponse>() {
                        @Override
                        public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                            if (response.body() != null) {

                                if (response.body().isStatus()) {
                                    customDialog.dismiss();
                                }
                                Utils.showShortToast(Login.this, response.body().getMessage());

                            } else {
                                Utils.showServerError(Login.this);
                            }
                            hideProgressBar();

                        }

                        @Override
                        public void onFailure(Call<PostResponse> call, Throwable t) {
                            hideProgressBar();
                            Utils.showShortToast(Login.this, t.getMessage());
                        }
                    });

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //this method call is necessary to get our callback to get called.
        btnTwitter.onActivityResult(requestCode, resultCode, data);
        //callbackManager.onActivityResult(requestCode, resultCode, data);
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN && resultCode == Activity.RESULT_OK) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}