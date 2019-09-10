package com.gogroup.app.gogroupapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.Models.ChatModel;
import com.gogroup.app.gogroupapp.Models.UserCreatedGroupModel;
import com.gogroup.app.gogroupapp.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zabius on 8/1/17.
 */

public class GroupchatAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ChatModel> data = new ArrayList<ChatModel>();

    public GroupchatAdapter(Context c, ArrayList<ChatModel> mData ) {
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
        GroupchatAdapter.ViewHolder holder = null;
        ChatModel model = data.get(position);
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            holder = new GroupchatAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.group_chat_list_item,null);
            holder.username = convertView.findViewById(R.id.username);
            holder.message = convertView.findViewById(R.id.message_text);
            holder.time = convertView.findViewById(R.id.message_time);
            holder.bubble_layout = convertView.findViewById(R.id.bubble_layout);
            holder.image = convertView.findViewById(R.id.member_image);
            convertView.setTag(holder);

        } else {
            holder = (GroupchatAdapter.ViewHolder) convertView.getTag();

        }
        if(model.getIsMine() == 1){
            holder.username.setVisibility(View.GONE);
            holder.image.setVisibility(View.GONE);
            holder.bubble_layout.setBackgroundResource(R.drawable.bubble_right);
            LinearLayout bubble_parent = (LinearLayout) holder.bubble_layout.getParent();
            bubble_parent.setGravity(Gravity.RIGHT);
        }
        else{
            holder.bubble_layout.setBackgroundResource(R.drawable.bubble_left);
            LinearLayout bubble_parent = (LinearLayout) holder.bubble_layout.getParent();
            bubble_parent.setGravity(Gravity.LEFT);
        }

        holder.message.setTag(model.getMessage());
        holder.time.setTag(model.getTime());
        //holder.groupImage.setImageDrawable();
        return  convertView;


    }
    public static class ViewHolder{
        TextView username,message,time;
        LinearLayout bubble_layout;
        CircleImageView image;
    }
}
