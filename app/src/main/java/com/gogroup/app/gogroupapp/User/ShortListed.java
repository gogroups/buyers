package com.gogroup.app.gogroupapp.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.Button;

import com.gogroup.app.gogroupapp.Adapters.ViewPagerAdapter;
import com.gogroup.app.gogroupapp.Fragments.AdsFilterFragment;
import com.gogroup.app.gogroupapp.Fragments.AdsFragment;
import com.gogroup.app.gogroupapp.Fragments.GroupFilterFragment;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.FontManager;
import com.gogroup.app.gogroupapp.Models.FilterModel;
import com.gogroup.app.gogroupapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShortListed extends BaseActivity {
    public static final String DATA = "data";
    public String postCategoryId;
    public GroupsFragment userGroupsFragment;
    public AdsFragment userAdsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_listed);
        ButterKnife.bind(this);
        postCategoryId = getIntent().getStringExtra(DATA);

        userGroupsFragment = new GroupsFragment();
        userAdsFragment = new AdsFragment();
        setIcons();

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);


    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(userAdsFragment, getString(R.string.offersNDeals));
        adapter.addFragment(userGroupsFragment, getString(R.string.groups));
        viewPager.setAdapter(adapter);
    }


    @OnClick(R.id.btnBack)
    void back() {
        onBackPressed();
    }

    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.btnBack)
    Button btnBack;
    @BindView(R.id.viewpager)
    ViewPager viewPager;


    private void setIcons() {
        btnBack.setText(R.string.icon_ionicon_var_chevron_left);
        btnBack.setTypeface(FontManager.getTypeFaceFromFontName(getApplicationContext(), FontManager.IONICONFONT));
        btnBack.setTextSize(20);
        btnBack.setTextColor(ContextCompat.getColor(this, R.color.white_color));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case BaseActivity.REFRESH_DASHBOARD:
                Intent intent = new Intent();
                setResult(BaseActivity.REFRESH_DASHBOARD, intent);
                finish();
                break;
            default:
                break;

        }
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }


    @Override
    public void onBackPressed() {
        finish();
    }

}
