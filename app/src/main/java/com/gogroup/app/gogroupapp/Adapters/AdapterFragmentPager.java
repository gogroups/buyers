package com.gogroup.app.gogroupapp.Adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

import com.gogroup.app.gogroupapp.Fragments.IntroFragment;
import com.gogroup.app.gogroupapp.Responses.ListResponse;

import java.util.ArrayList;
import java.util.List;

public class AdapterFragmentPager extends FragmentPagerAdapter {

    List<Fragment> fragmentList = new ArrayList<>();

    public AdapterFragmentPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
//        switch (position) {
//            case 0:
//                return IntroFragment.newInstance(Color.parseColor("#03A9F4"), position); // blue
//            default:
//                return IntroFragment.newInstance(Color.parseColor("#4CAF50"), position); // green
//        }

        return fragmentList.get(position);
    }

    public void addFragments(List<Fragment> fragmentList) {
        this.fragmentList=fragmentList;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }
}