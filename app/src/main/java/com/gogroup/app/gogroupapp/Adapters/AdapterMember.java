package com.gogroup.app.gogroupapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gogroup.app.gogroupapp.HelperClasses.UserPreferences;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.GroupListResponse;
import com.gogroup.app.gogroupapp.Responses.ListResponse;
import com.gogroup.app.gogroupapp.User.MemberProfile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterMember extends RecyclerView.Adapter<AdapterMember.MyViewHolder> {
    List<ListResponse> list = new ArrayList<ListResponse>();
    Context context;
    boolean isEnableDetail;
    String postGroupId;
    public AdapterMember(Context context, List<ListResponse> list, boolean isEnableDetail, String postGroupId) {
        this.context = context;
        this.list = list;
        this.isEnableDetail = isEnableDetail;
        this.postGroupId=postGroupId;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_circle_photo, null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final ListResponse item = list.get(position);

        if (item.getProfileImage() != null && item.getProfileImage().toString().trim() != "") {
            Picasso.with(context).load(item.getProfileImage()).into(holder.imgProfile);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEnableDetail&&!item.getUserId().trim().equals(UserPreferences.getInstance().getUserId().trim())) {
                    Intent i = new Intent(context, MemberProfile.class);
                    i.putExtra(MemberProfile.MEMBER_ID,item.getUserId());
                    i.putExtra(MemberProfile.GROUP_ID,postGroupId);
                    context.startActivity(i);
                }
            }
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgProfile)
        CircleImageView imgProfile;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}
