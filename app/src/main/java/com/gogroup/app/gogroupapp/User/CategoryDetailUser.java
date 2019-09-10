package com.gogroup.app.gogroupapp.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.Adapters.ViewPagerAdapter;
import com.gogroup.app.gogroupapp.Fragments.AdsFilterFragment;
import com.gogroup.app.gogroupapp.Fragments.AdsFragment;
import com.gogroup.app.gogroupapp.Fragments.GroupFilterFragment;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.FontManager;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;
import com.gogroup.app.gogroupapp.Models.FilterModel;
import com.gogroup.app.gogroupapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoryDetailUser extends BaseActivity
{
    public static final String DATA = "data", GROUP_TAG = "group", ADS_TAG = "ads", CATEGORY_NAME = "categoryName";

    public String postCategoryId, postSearchOffer = "", postSearchInterest = "";
    public FilterModel filterGroup = new FilterModel();
    public GroupsFragment userGroupsFragment;
    public FilterModel filterAds = new FilterModel();
    public AdsFragment userAdsFragment;
    GroupFilterFragment fragmentGroupFilter;
    AdsFilterFragment fragmentAdsFilter;
    int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_category_detail);
        ButterKnife.bind(this);
        postCategoryId = getIntent().getStringExtra(DATA);
        tvTitle.setText(getIntent().getStringExtra(CATEGORY_NAME));

        userAdsFragment = new AdsFragment();
        userGroupsFragment = new GroupsFragment();
        fragmentAdsFilter = new AdsFilterFragment();
        fragmentGroupFilter = new GroupFilterFragment();

        setIcons();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0); // this disable the animation
                Utils.hideKeyboard(CategoryDetailUser.this);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Utils.hideKeyboard(CategoryDetailUser.this);
                super.onDrawerOpened(drawerView);
                super.onDrawerSlide(drawerView, 0); // this disables the arrow @ completed state
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, findViewById(R.id.layoutFilter));
        addFilterFragment(fragmentAdsFilter, 0);

        final EditText etSearch = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        ImageView btnCloseSearch = (ImageView) searchView.findViewById(R.id.search_close_btn);
        etSearch.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        etSearch.setTextSize(15);
        searchView.setQueryHint(getString(R.string.searchOffers));
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTitle.setVisibility(View.GONE);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (currentPage == 0) {
                    postSearchOffer = query;
                    userAdsFragment.getSellerAdds(true);
                } else {
                    postSearchInterest = query;
                    userGroupsFragment.apiGetGroupList();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        btnCloseSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTitle.setVisibility(View.VISIBLE);

                etSearch.setText("");
                //Clear query
                searchView.setQuery("", false);
                //Collapse the action view
//                searchView.onActionViewCollapsed();
//                searchView.collapseActionView();
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }

                if (currentPage == 0 && postSearchOffer != null && !postSearchOffer.trim().equals("")) {
                    postSearchOffer = "";
                    userAdsFragment.getSellerAdds(true);
                } else if (postSearchInterest != null && !postSearchInterest.trim().equals("")) {
                    postSearchInterest = "";
                    userGroupsFragment.apiGetGroupList();
                }

            }
        });
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position)
            {
                currentPage = position;
                searchView.setQueryHint(position == 0 ? getString(R.string.searchOffers) : getString(R.string.searchInterest));
                etSearch.setText(position == 0 ? postSearchOffer : postSearchInterest);
                searchView.setQuery(position == 0 ? postSearchOffer : postSearchInterest, false);
                searchView.setIconified(searchView.getQuery().length() <= 0);
                if (position == 0) {
                    addFilterFragment(fragmentAdsFilter, position);
                } else {
                    addFilterFragment(fragmentGroupFilter, position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(userAdsFragment, getString(R.string.offersNDeals));
        adapter.addFragment(userGroupsFragment, "My Interests");
        viewPager.setAdapter(adapter);
    }

    @OnClick(R.id.add_group)
    void createGroup() {
        Intent i = new Intent(CategoryDetailUser.this, CreateGroup.class);
        i.putExtra(CreateGroup.CATEGORY_ID, postCategoryId);
        startActivityForResult(i, BaseActivity.REFRESH_FRAGMENT);
    }

    @OnClick(R.id.filter)
    void openFilter() {
        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);
        } else {
            drawer.openDrawer(Gravity.RIGHT);
        }
    }

    @OnClick(R.id.btnBack)
    void back() {
        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);
        } else {
            finish();
        }
    }

    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.btnBack)
    Button btnBack;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.drawer_layout)
    public DrawerLayout drawer;

    private void setIcons() {
        btnBack.setText(R.string.icon_ionicon_var_chevron_left);
        btnBack.setTypeface(FontManager.getTypeFaceFromFontName(getApplicationContext(), FontManager.IONICONFONT));
        btnBack.setTextSize(20);
        btnBack.setTextColor(ContextCompat.getColor(this, R.color.white_color));
    }


    private void addFilterFragment(Fragment fragment, int position) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case 0:
                if (fragmentManager.findFragmentByTag(GROUP_TAG) != null) {
                    //if the fragment exists, show it.
                    fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(GROUP_TAG)).commit();
                } else {
                    //if the fragment does not exist, add it to fragment manager.
                    fragmentManager.beginTransaction().add(R.id.layoutFilter, fragment, GROUP_TAG).commit();
                }
                if (fragmentManager.findFragmentByTag(ADS_TAG) != null) {
                    //if the other fragment is visible, hide it.
                    fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(ADS_TAG)).commit();
                }
                break;
            case 1:
                if (fragmentManager.findFragmentByTag(ADS_TAG) != null) {
                    //if the fragment exists, show it.
                    fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(ADS_TAG)).commit();
                } else {
                    //if the fragment does not exist, add it to fragment manager.
                    fragmentManager.beginTransaction().add(R.id.layoutFilter, fragment, ADS_TAG).commit();
                }
                if (fragmentManager.findFragmentByTag(GROUP_TAG) != null) {
                    fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(GROUP_TAG)).commit();

                }
                break;
        }
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
        back();
    }

}
