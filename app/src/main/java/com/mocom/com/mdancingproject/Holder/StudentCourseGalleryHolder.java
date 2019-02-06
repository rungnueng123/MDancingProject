package com.mocom.com.mdancingproject.Holder;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.R;

public class StudentCourseGalleryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ItemClickCallBack mListener;
    ImageView galleryUrl;

    public ImageView getGalleryUrl() {
        return galleryUrl;
    }

    public StudentCourseGalleryHolder(@NonNull View itemView, ItemClickCallBack listener) {
        super(itemView);
        mListener = listener;
        itemView.setOnClickListener(this);

        galleryUrl = itemView.findViewById(R.id.img_gallery_course);
    }

    @Override
    public void onClick(View v) {
        mListener.onClick(v, getAdapterPosition());
    }
}
