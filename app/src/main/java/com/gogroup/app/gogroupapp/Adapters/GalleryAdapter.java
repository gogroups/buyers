package com.gogroup.app.gogroupapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gogroup.app.gogroupapp.R;

import java.util.ArrayList;

/**
 * Created by zabius on 8/1/17.
 */

public class GalleryAdapter extends BaseAdapter {
    private Context mContext;
    //private final int[] Imageid;
    ArrayList<Integer> Imageid = new ArrayList<>();


    public GalleryAdapter(Context c, ArrayList<Integer> Imageid ) {
        mContext = c;
        this.Imageid = Imageid;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Imageid.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.image_gallery, null);
            ImageView imageView = (ImageView)grid.findViewById(R.id.image_single);
            imageView.setImageResource(Imageid.get(position));
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}
