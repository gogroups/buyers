package com.gogroup.app.gogroupapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.gogroup.app.gogroupapp.Adapters.AdapterFragmentPager;
import com.gogroup.app.gogroupapp.Fragments.IntroFragment;
import com.gogroup.app.gogroupapp.HelperClasses.GoGroup;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.Responses.ListResponse;
import com.gogroup.app.gogroupapp.Seller.SellerDashboard;
import com.gogroup.app.gogroupapp.User.UserDashboardNew;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by zabius on 7/19/17.
 */

public class SplashActivity extends AppCompatActivity {

    ImageView mImageView;


    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        ButterKnife.bind(this);
        FirebaseApp.initializeApp(this);
        Fabric.with(this, new Crashlytics());

//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.gogroup.app.gogroupapp",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */

                if (UserPreferences.getInstance().isFirstTimeVisible()) {
                    imgSplash.setVisibility(View.GONE);
                    AdapterFragmentPager adapterFragmentPager = new AdapterFragmentPager(getSupportFragmentManager());
                    adapterFragmentPager.addFragments(new ArrayList<Fragment>(5));
                    viewPager.setAdapter(adapterFragmentPager);
                    viewPager.setOffscreenPageLimit(5);
                    indicator.setViewPager(viewPager);
                    return;
                }
                skipIntro();
            }
        }, SPLASH_DISPLAY_LENGTH);

        try {
            if (getIntent().getExtras() != null) {
                for (String key : getIntent().getExtras().keySet()) {
                    Object value = getIntent().getExtras().get(key);
                    GoGroup.getInstance().notificationMap.put(key, "" + value);
                }
            }
        } catch (Exception e) {
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 1) {
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    void skipIntro()
    {
        Utils.userType = "User";
        if (UserPreferences.getInstance().isLogin() && UserPreferences.getInstance().getIsVerifyOtp()) {
            Log.d("tokenUser", "" + UserPreferences.getInstance().getToken());
            if (UserPreferences.getInstance().getUserType().toLowerCase().equals("user")) {
                startActivity(UserDashboardNew.class);

            } else {
                startActivity(SellerDashboard.class);

            }
        } else {
            UserPreferences.getInstance().clearUserDetails();
            startActivity(Login.class);
        }
    }

    private void startActivity(Class activity) {

        Intent i = new Intent(SplashActivity.this, activity);
        startActivity(i);
        ActivityCompat.finishAffinity(SplashActivity.this);
    }


    @OnClick(R.id.btnNext)
    void onNext() {
        if (viewPager.getCurrentItem() == 1) {
            skipIntro();
            return;
        }
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }

    @OnClick(R.id.btnSkip)
    void onSkip() {
        skipIntro();
    }

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.btnNext)
    Button btnNext;
    @BindView(R.id.btnSkip)
    Button btnSkip;
    @BindView(R.id.imgSplash)
    ImageView imgSplash;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
}
