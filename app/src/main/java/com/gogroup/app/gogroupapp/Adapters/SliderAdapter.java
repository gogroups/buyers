package com.gogroup.app.gogroupapp.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gogroup.app.gogroupapp.Fragments.ImageDialog;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.ImageResponse;
import com.gogroup.app.gogroupapp.Seller.SellerAddDetailActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zabius on 8/3/17.
 */

public class SliderAdapter extends PagerAdapter {

    private List<ImageResponse> imageList = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;

    public SliderAdapter(final Context context, List<ImageResponse> images) {
        this.context = context;
        this.imageList = images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View myImageLayout = inflater.inflate(R.layout.single_image, view, false);
        final ImageView image = (ImageView) myImageLayout.findViewById(R.id.image);

        final ImageResponse item = imageList.get(position);
        if (item != null && item.getImagePath() != null) {

            Picasso.with(context).load(item.getImagePath()).into(image);

        }

        myImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager manager = ((SellerAddDetailActivity)context).getSupportFragmentManager();
                ImageDialog dialogFragment = ImageDialog.newInstance(item.getImagePath());
                dialogFragment.show(manager, "ImageDialog");
            }
        });
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}