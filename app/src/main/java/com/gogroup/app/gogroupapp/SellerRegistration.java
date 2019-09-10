package com.gogroup.app.gogroupapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gogroup.app.gogroupapp.Adapters.ViewPagerAdapter;
import com.gogroup.app.gogroupapp.CustomViews.CustomViewPager;
import com.gogroup.app.gogroupapp.CustomViews.SwipeDirection;
import com.gogroup.app.gogroupapp.Fragments.SellerRegistrationAccountFragment;
import com.gogroup.app.gogroupapp.Fragments.SellerRegistrationPrimaryFragment;
import com.gogroup.app.gogroupapp.Fragments.SellerRegistrationSecondaryFragment;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by zabius on 7/19/17.
 */

public class SellerRegistration extends BaseActivity {


    public static Map<String, RequestBody> postFields = new HashMap<String, RequestBody>();
    public MultipartBody.Part multipartImg = null;
    private TabLayout tabLayout;
    @BindView(R.id.viewpager)
    CustomViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registeration);
        ButterKnife.bind(this);
        viewPager.setAllowedSwipeDirection(SwipeDirection.left);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        LinearLayout tabStrip = ((LinearLayout)tabLayout.getChildAt(0));
        for(int i = 1; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return viewPager.getCurrentItem()!=2;
                }
            });
        }
    }
    private void setupViewPager(CustomViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SellerRegistrationPrimaryFragment(), "Primary Contact Person");
        adapter.addFragment(new SellerRegistrationSecondaryFragment(), "Secondary Contact Person");
       // adapter.addFragment(new SellerRegistrationAccountFragment(), "Account Details");
        viewPager.setAdapter(adapter);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 7) {
            finish();
        }
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
