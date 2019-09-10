package com.gogroup.app.gogroupapp.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.Button;

import com.gogroup.app.gogroupapp.Adapters.ViewPagerAdapter;
import com.gogroup.app.gogroupapp.Fragments.MyCreatedGroupsFragment;
import com.gogroup.app.gogroupapp.Fragments.MyJoinedGroupsFragment;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.FontManager;
import com.gogroup.app.gogroupapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyGroups extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_group);
        ButterKnife.bind(this);
        setIcons();

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

    }


    private void setIcons() {
        btnBack.setText(R.string.icon_ionicon_var_chevron_left);
        btnBack.setTypeface(FontManager.getTypeFaceFromFontName(getApplicationContext(), FontManager.IONICONFONT));
        btnBack.setTextSize(20);
        btnBack.setTextColor(ContextCompat.getColor(this, R.color.white_color));
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MyCreatedGroupsFragment(), "CREATED INTEREST");
        adapter.addFragment(new MyJoinedGroupsFragment(), "JOINED INTEREST");
        viewPager.setAdapter(adapter);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }

    @OnClick(R.id.btnBack)
    void back() {
        finish();
    }


    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.btnBack)
    Button btnBack;

}
