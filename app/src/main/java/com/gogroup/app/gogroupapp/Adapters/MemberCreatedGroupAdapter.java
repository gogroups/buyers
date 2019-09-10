package com.gogroup.app.gogroupapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.Models.UserCreatedGroupModel;
import com.gogroup.app.gogroupapp.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zabius on 7/27/17.
 */

public class MemberCreatedGroupAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<UserCreatedGroupModel> data = new ArrayList<UserCreatedGroupModel>();

    public MemberCreatedGroupAdapter(Context c, ArrayList<UserCreatedGroupModel> mData ) {
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
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        UserCreatedGroupModel model = data.get(position);
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.member_created_groups_item,null);
            holder.groupNameTextView = convertView.findViewById(R.id.group_name);
            holder.totalMembersTextView = convertView.findViewById(R.id.group_members);
            holder.startDateTextView = convertView.findViewById(R.id.start_date);
            holder.endDateTextView = convertView.findViewById(R.id.end_date);
            holder.statusTextView = convertView.findViewById(R.id.status);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        holder.groupNameTextView.setText(model.getGroup_name());
        holder.totalMembersTextView.setTag(model.getGroup_members());
        holder.startDateTextView.setText(model.getStart_date());
        holder.endDateTextView.setTag(model.getEnd_date());
        holder.statusTextView.setTag(model.getStatus());
        //holder.groupImage.setImageDrawable();
        return  convertView;


    }
    public static class ViewHolder{
        TextView groupNameTextView,totalMembersTextView,startDateTextView,endDateTextView,statusTextView;
        CircleImageView groupImage;
    }
}
