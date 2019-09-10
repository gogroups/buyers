package com.gogroup.app.gogroupapp.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gogroup.app.gogroupapp.CallBackListeners.CallBackRecyclerItemClick;
import com.gogroup.app.gogroupapp.R;
import com.gogroup.app.gogroupapp.Responses.ImageResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmlabs-storage on 20/3/17.
 */

public class AdapterAddPhotos extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<Uri> imagePaths = new ArrayList<>();
    private CallBackRecyclerItemClick listener;
    List<ImageResponse> imageUrls;

    public AdapterAddPhotos() {
    }

    public void setData(Context context, ArrayList<Uri> imagePaths, List<ImageResponse> imageUrls, CallBackRecyclerItemClick listener) {
        this.context = context;
        this.imagePaths = imagePaths;
        this.imageUrls = imageUrls;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_card, parent, false);
                return new ImageViewHolder(view);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_add_photo, parent, false);
                return new AddImageViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof AdapterAddPhotos.AddImageViewHolder) {


            ((AddImageViewHolder) holder).addImageText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(view, -1);

                }
            });

        } else if (holder instanceof AdapterAddPhotos.ImageViewHolder) {


            if (imageUrls != null) {
                Picasso.with(((ImageViewHolder) holder).imageView.getContext()).load(imageUrls.get(position).getImagePath()).fit().centerCrop().into(((ImageViewHolder) holder).imageView);
                ((AdapterAddPhotos.ImageViewHolder) holder).deleteImage.setVisibility(View.GONE);
            } else {


                final Uri imagePath = imagePaths.get(position);
                Picasso.with(((ImageViewHolder) holder).imageView.getContext()).load(imagePath).fit().centerCrop().into(((ImageViewHolder) holder).imageView);
                ((AdapterAddPhotos.ImageViewHolder) holder).deleteImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onItemClick(view, position);
                    }
                });
            }
        }
    }


    @Override
    public int getItemCount() {
        if (imageUrls != null) {
            return imageUrls.size();
        } else {
            if (imagePaths.size() > 0 && imagePaths.size() < 5) {
                return imagePaths.size() + 1;
            } else {
                return imagePaths.size();
            }
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, deleteImage;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_item);
            deleteImage = (ImageView) itemView.findViewById(R.id.iv_delete);
        }
    }

    public class AddImageViewHolder extends RecyclerView.ViewHolder {
        private TextView addImageText;

        public AddImageViewHolder(View itemView) {
            super(itemView);
            addImageText = (TextView) itemView.findViewById(R.id.tv_addImg);


        }
    }

    @Override
    public int getItemViewType(int position) {
        if (imageUrls != null) {
            return 1;
        } else {
            if (position >= imagePaths.size()) {
                return 2;
            } else {
                return 1;
            }
        }

    }

}
