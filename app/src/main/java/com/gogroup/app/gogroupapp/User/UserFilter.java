package com.gogroup.app.gogroupapp.User;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.gogroup.app.gogroupapp.HelperClasses.FontManager;
import com.gogroup.app.gogroupapp.R;

public class UserFilter extends AppCompatActivity {
    Button mButtonLeftArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_group_filter);

        initIds();
        setIcons();
        mButtonLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setIcons() {
        mButtonLeftArrow.setText(R.string.icon_ionicon_var_chevron_left);
        mButtonLeftArrow.setTypeface(FontManager.getTypeFaceFromFontName(getApplicationContext(),FontManager.IONICONFONT));
        mButtonLeftArrow.setTextSize(20);
        mButtonLeftArrow.setTextColor(ContextCompat.getColor(this, R.color.white_color));
    }

    private void initIds() {
        mButtonLeftArrow = (Button) findViewById(R.id.btnBack);
        //iconStartDate = (TextView) findViewById(R.id.filter_activity_icon_start_date);

    }

}