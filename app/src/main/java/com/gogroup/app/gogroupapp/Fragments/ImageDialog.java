package com.gogroup.app.gogroupapp.Fragments;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.R;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;
import retrofit2.http.Url;

/**
 * Created by zabius-android on 12/6/2017.
 */

public class ImageDialog extends android.support.v4.app.DialogFragment {


    static String imageUrl;

    public static ImageDialog newInstance(String url) {
        ImageDialog dialog = new ImageDialog();

        imageUrl=url;

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_dialog, container, false);
        ButterKnife.bind(this, view);
        getDialog().setCancelable(true);

        if(imageUrl!=null&&!imageUrl.equals(""))
        {
            Picasso.with(getActivity()).load(imageUrl).into(imgAdd);
        }


        return view;
    }


//    @Override
//    public void onStart() {
//        super.onStart();
//        Dialog dialog = getDialog();
//        if (dialog != null) {
//            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        }
//    }




    @BindView(R.id.imgAdd)
    ImageView imgAdd;


}
