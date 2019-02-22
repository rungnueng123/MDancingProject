package com.mocom.com.mdancingproject.Holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.R;

public class CourseHomeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ItemClickCallBack mListener;
    ImageView imgUrl;
    TextView courseName, courseStyle, courseDesc;

    public ImageView getImgUrl() {
        return imgUrl;
    }

    public TextView getCourseName() {
        return courseName;
    }

    public TextView getCourseStyle() {
        return courseStyle;
    }

    public TextView getCourseDesc() {
        return courseDesc;
    }

    public CourseHomeHolder(@NonNull View itemView, ItemClickCallBack listener) {
        super(itemView);
        mListener = listener;
        itemView.setOnClickListener(this);

        imgUrl = itemView.findViewById(R.id.img_course_home);
        courseName = itemView.findViewById(R.id.txt_course_name);
        courseStyle = itemView.findViewById(R.id.txt_course_style);
    }

    @Override
    public void onClick(View v) {
        mListener.onClick(v, getAdapterPosition());
    }
}
