package com.gogroup.app.gogroupapp.User;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.HelperClasses.BaseActivity;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Seller.SellerAddDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactUs extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ButterKnife.bind(this);
        tvTitle.setText(getString(R.string.contactUs));
    }


    @OnClick(R.id.tvContact)
    void contact() {
        String uri = "tel:" + "+918800685443";
        Intent intentPhoneCall = new Intent(Intent.ACTION_DIAL);
        intentPhoneCall.setData(Uri.parse(uri));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
            return;
        }
        startActivity(intentPhoneCall);
    }

    @OnClick(R.id.tvEmail)
    void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "support@gogroup.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    @BindView(R.id.tvTitle)
    TextView tvTitle;
}
