package com.gogroup.app.gogroupapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.Models.AddViewListModel;
import com.gogroup.app.gogroupapp.Models.UserChatModel;
import com.gogroup.app.gogroupapp.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zabius on 7/28/17.
 */

public class AddViewListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<AddViewListModel> data = new ArrayList<AddViewListModel>();

    public AddViewListAdapter(Context c, ArrayList<AddViewListModel> mData ) {
        mContext = c;
        this.data = mData;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
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
        AddViewListAdapter.ViewHolder holder = null;
        AddViewListModel model = data.get(position);
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            holder = new AddViewListAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.user_add_view_list_item,null);
            holder.userNameTextView = convertView.findViewById(R.id.user_name);
            holder.userLocationTextView = convertView.findViewById(R.id.user_location);
            holder.categoryTextview = convertView.findViewById(R.id.category);

            convertView.setTag(holder);

        } else {
            holder = (AddViewListAdapter.ViewHolder) convertView.getTag();

        }
        holder.userNameTextView.setText(model.getUsername());
        holder.userLocationTextView.setText(model.getUserlocation());
        holder.categoryTextview.setText(model.getCategory());

        //holder.groupImage.setImageDrawable();
        return  convertView;


    }
    public static class ViewHolder{
        TextView userNameTextView,userLocationTextView,categoryTextview;
        CircleImageView groupImage;
    }
}

