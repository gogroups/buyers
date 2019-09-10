package com.gogroup.app.gogroupapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.ImageResponse;
import com.gogroup.app.gogroupapp.Responses.ListResponse;
import com.gogroup.app.gogroupapp.Seller.SellerAddDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterShowPhoto extends RecyclerView.Adapter<AdapterShowPhoto.MyViewHolder> {
    List<ListResponse> list = new ArrayList<ListResponse>();
    Context context;
    boolean isClickableItem;

    public AdapterShowPhoto(Context context, List<ListResponse> list, boolean isClickableItem) {
        this.context = context;
        this.list = list;
        this.isClickableItem = isClickableItem;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_photo_show, null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final ListResponse item = list.get(position);

        if (item.getImage() != null && !item.getImage().toString().trim().equals("")) {
            Picasso.with(context).load(item.getImage()).into(holder.imgProfile);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isClickableItem) {
                    Intent intent = new Intent(context, SellerAddDetailActivity.class);
                    intent.putExtra(SellerAddDetailActivity.DATA, item.getAdvertisementId());
                    context.startActivity(intent);
                }
            }
        });

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgProfile)
        ImageView imgProfile;

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
