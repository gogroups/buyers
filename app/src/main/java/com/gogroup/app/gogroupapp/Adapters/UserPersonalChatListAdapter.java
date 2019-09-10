package com.gogroup.app.gogroupapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.Models.UserChatModel;
import com.gogroup.app.gogroupapp.Models.UserJoinedGroupModel;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.User.MemberChat;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zabius on 7/28/17.
 */

public class UserPersonalChatListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<UserChatModel> data = new ArrayList<UserChatModel>();

    public UserPersonalChatListAdapter(Context c, ArrayList<UserChatModel> mData ) {
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
        UserPersonalChatListAdapter.ViewHolder holder = null;
        UserChatModel model = data.get(position);
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            holder = new UserPersonalChatListAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.user_personal_chat_list_item,null);
            holder.userNameTextView = convertView.findViewById(R.id.user_name);
            holder.userMessageTextView = convertView.findViewById(R.id.message);
            holder.messageIcon = convertView.findViewById(R.id.message_icon);
            convertView.setTag(holder);

        } else {
            holder = (UserPersonalChatListAdapter.ViewHolder) convertView.getTag();

        }
        holder.userNameTextView.setText(model.getUser_name());
        holder.userMessageTextView.setText(model.getMessage());
        holder.messageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startPersonalChatIntent = new Intent(mContext, MemberChat.class);
                mContext.startActivity(startPersonalChatIntent);
            }
        });

        //holder.groupImage.setImageDrawable();
        return  convertView;


    }
    public static class ViewHolder{
        TextView userNameTextView,userMessageTextView;
        CircleImageView groupImage;
        ImageView messageIcon;
    }
}

