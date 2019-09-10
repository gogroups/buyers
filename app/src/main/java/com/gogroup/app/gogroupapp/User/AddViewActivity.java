package com.gogroup.app.gogroupapp.User;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.Adapters.AddViewListAdapter;
import com.gogroup.app.gogroupapp.Adapters.UserGroupChatListAdapter;
import com.gogroup.app.gogroupapp.HelperClasses.FontManager;
import com.gogroup.app.gogroupapp.Models.AddViewListModel;
import com.gogroup.app.gogroupapp.Models.UserChatModel;
import com.gogroup.app.gogroupapp.R;

import java.util.ArrayList;

public class AddViewActivity extends AppCompatActivity {

    Button mButtonLeftArrow;
    ListView viewedListVew;
    ArrayList<AddViewListModel> arrayList = new ArrayList<>();
    AddViewListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_view);
        initIds();
        setIcons();
        mButtonLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        for(int i=0;i<5;i++){
            AddViewListModel model = new AddViewListModel("Sandeep Sharma","Indore","Property for Sale");
            arrayList.add(model);
        }

        adapter = new AddViewListAdapter(AddViewActivity.this,arrayList);
        viewedListVew.setAdapter(adapter);

    }
    private void setIcons() {
        mButtonLeftArrow.setText(R.string.icon_ionicon_var_chevron_left);
        mButtonLeftArrow.setTypeface(FontManager.getTypeFaceFromFontName(getApplicationContext(), FontManager.IONICONFONT));
        mButtonLeftArrow.setTextSize(20);
        mButtonLeftArrow.setTextColor(ContextCompat.getColor(this, R.color.white_color));

    }

    private void initIds() {
        mButtonLeftArrow = (Button) findViewById(R.id.vendor_home_filter_left_arrow);
        viewedListVew = (ListView) findViewById(R.id.viewedListVew);

    }

}
