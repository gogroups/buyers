package com.gogroup.app.gogroupapp.User;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.gogroup.app.gogroupapp.Adapters.ViewPagerAdapter;
import com.gogroup.app.gogroupapp.Fragments.MyMembersFragment;
import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.FontManager;
import com.gogroup.app.gogroupapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TabChatScreen extends BaseActivity  {

    MyMembersFragment membersFragment, groupFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_tab_chat_screen);
        ButterKnife.bind(this);
        setIcons();
        membersFragment = MyMembersFragment.newInstance(0);
        groupFragment = MyMembersFragment.newInstance(1);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);



        final EditText etSearch = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        ImageView btnCloseSearch = (ImageView) searchView.findViewById(R.id.search_close_btn);
        etSearch.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  tvTitle.setVisibility(View.GONE);
            }
        });


//        searchView.setQuery("asd",true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                membersFragment.filterMemberList(newText);
                groupFragment.filterGroupList(newText);

                return false;
            }
        });


        btnCloseSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etSearch.setText("");
                searchView.setQuery("", false);
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
//                if (postSearchText != null && postSearchText.trim() != "") {
//                    postSearchText = null;
//                    userGroupsFragment.apiGetGroupList();
//                }
            }
        });

    }

    private void setIcons() {
        btnBack.setText(R.string.icon_ionicon_var_chevron_left);
        btnBack.setTypeface(FontManager.getTypeFaceFromFontName(getApplicationContext(),FontManager.IONICONFONT));
        btnBack.setTextSize(20);
        btnBack.setTextColor(ContextCompat.getColor(this, R.color.white_color));
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(membersFragment, "Members");
//        adapter.addFragment(new UserGroupChatListFragment(), "Groups");
        adapter.addFragment(groupFragment, "Groups");
        viewPager.setAdapter(adapter);
    }



    @OnClick(R.id.btnBack)
    void back() {
        finish();
    }


    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.btnBack) Button btnBack;
    @BindView(R.id.searchView) SearchView searchView;

}
