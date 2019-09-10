package com.gogroup.app.gogroupapp.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.HelperClasses.FontManager;
import com.gogroup.app.gogroupapp.R;

import java.util.ArrayList;

/**
 * Created by Sandeep on 20-May-17.
 */

public class AdapterCustomUserHomeNavigationViewListView extends BaseAdapter {


    Context mContext;
    ArrayList<String> mMenus;

    public AdapterCustomUserHomeNavigationViewListView(ArrayList<String> mArraysMenus, Context context)
    {
        mMenus = mArraysMenus;
        mContext = context;

    }

    @Override
    public int getCount() {
        return mMenus.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.custom_user_home_navigation_view, null, false);
        }
        TextView mTextViewNavigationIconsMenus = (TextView) convertView.findViewById(R.id.navigation_user_home_icon_items);
        TextView mTextViewNavigationMenus = (TextView) convertView.findViewById(R.id.user_home_activity_navigation_menus);
        if (position == 0)
        {
            mTextViewNavigationIconsMenus.setText(R.string.icon_ionicon_var_heart);
            mTextViewNavigationIconsMenus.setTypeface(FontManager.getTypeFaceFromFontName(mContext,FontManager.IONICONFONT));
            mTextViewNavigationIconsMenus.setTextSize(25);
            mTextViewNavigationIconsMenus.setTextColor(ContextCompat.getColor(mContext, R.color.orange_color));
        }
        else if (position == 1)
        {
            mTextViewNavigationIconsMenus.setText(R.string.icon_ionicon_var_ios_people_outline);
            mTextViewNavigationIconsMenus.setTypeface(FontManager.getTypeFaceFromFontName(mContext,FontManager.IONICONFONT));
            mTextViewNavigationIconsMenus.setTextSize(25);
            mTextViewNavigationIconsMenus.setTextColor(ContextCompat.getColor(mContext, R.color.orange_color));
        }
        else if (position == 2)
        {
            mTextViewNavigationIconsMenus.setText(R.string.icon_ionicon_var_chatbox_working);
            mTextViewNavigationIconsMenus.setTypeface(FontManager.getTypeFaceFromFontName(mContext,FontManager.IONICONFONT));
            mTextViewNavigationIconsMenus.setTextSize(25);
            mTextViewNavigationIconsMenus.setTextColor(ContextCompat.getColor(mContext, R.color.orange_color));
        }
        else if (position == 3)
        {
            mTextViewNavigationIconsMenus.setText(R.string.icon_ionicon_var_chatbox_working);
            mTextViewNavigationIconsMenus.setTypeface(FontManager.getTypeFaceFromFontName(mContext,FontManager.IONICONFONT));
            mTextViewNavigationIconsMenus.setTextSize(25);
            mTextViewNavigationIconsMenus.setTextColor(ContextCompat.getColor(mContext, R.color.orange_color));
        }
        if (position == 4)
        {
            mTextViewNavigationIconsMenus.setText(R.string.icon_ionicon_var_android_settings);
            mTextViewNavigationIconsMenus.setTypeface(FontManager.getTypeFaceFromFontName(mContext,FontManager.IONICONFONT));
            mTextViewNavigationIconsMenus.setTextSize(25);
            mTextViewNavigationIconsMenus.setTextColor(ContextCompat.getColor(mContext, R.color.orange_color));
        }
        else if (position == 5)
        {
            mTextViewNavigationIconsMenus.setText(R.string.icon_ionicon_var_android_contacts);
            mTextViewNavigationIconsMenus.setTypeface(FontManager.getTypeFaceFromFontName(mContext,FontManager.IONICONFONT));
            mTextViewNavigationIconsMenus.setTextSize(25);
            mTextViewNavigationIconsMenus.setTextColor(ContextCompat.getColor(mContext, R.color.orange_color));
        }
        else if (position == 6)
        {
            mTextViewNavigationIconsMenus.setText(R.string.icon_ionicon_var_android_share_alt);
            mTextViewNavigationIconsMenus.setTypeface(FontManager.getTypeFaceFromFontName(mContext,FontManager.IONICONFONT));
            mTextViewNavigationIconsMenus.setTextSize(25);
            mTextViewNavigationIconsMenus.setTextColor(ContextCompat.getColor(mContext, R.color.orange_color));
        }

        else if (position == 7)
        {
            mTextViewNavigationIconsMenus.setText(R.string.icon_ionicon_var_android_share_alt);
            mTextViewNavigationIconsMenus.setTypeface(FontManager.getTypeFaceFromFontName(mContext,FontManager.IONICONFONT));
            mTextViewNavigationIconsMenus.setTextSize(25);
            mTextViewNavigationIconsMenus.setTextColor(ContextCompat.getColor(mContext, R.color.orange_color));
        }

        String strMenusText = mMenus.get(position);
        mTextViewNavigationMenus.setText(strMenusText);


        return convertView;
    }
}
