package com.gogroup.app.gogroupapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.HelperClasses.Utils;

/**
 * Created by zabius on 7/19/17.
 */

public class OptionLoginScreen extends BaseActivity {

    Button mBtnSellerLogin;
    Button mBtnUserLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_login_screen);
//        getSupportActionBar().hide();

        if(UserPreferences.getInstance().isFirstTimeVisible()) {
            isPermissionsAllowed();
            UserPreferences.getInstance().setUserValue();
        }

        mBtnSellerLogin = (Button) findViewById(R.id.option_login_screen_seller_login_button);
        mBtnUserLogin = (Button) findViewById(R.id.option_login_screen_user_login_button);

        mBtnSellerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OptionLoginScreen.this,Login.class);
                Utils.userType=getResources().getString(R.string.option_login_screen_seller);
                startActivity(i);
            }
        });

        mBtnUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OptionLoginScreen.this,Login.class);
                Utils.userType=getResources().getString(R.string.option_login_screen_user);
                startActivity(i);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

