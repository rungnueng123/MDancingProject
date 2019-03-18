package com.mocom.com.mdancingproject.Holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.R;

public class ClassHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView classID, className, playlist, style, teacher, time, courseID;
    private ImageView imgUrl;
    private ItemClickCallBack mListener;

    public TextView getClassID() {
        return classID;
    }

    public TextView getClassName() {
        return className;
    }

    public TextView getPlaylist() {
        return playlist;
    }

    public TextView getStyle() {
        return style;
    }

    public TextView getTeacher() {
        return teacher;
    }

    public TextView getTime() {
        return time;
    }

    public TextView getCourseID() {
        return courseID;
    }

    public ImageView getImgUrl() {
        return imgUrl;
    }

    public ClassHolder(@NonNull View itemView, ItemClickCallBack listener) {
        super(itemView);
        mListener = listener;
        itemView.setOnClickListener(this);

        className = itemView.findViewById(R.id.txt_class_name);
        imgUrl = itemView.findViewById(R.id.img_class_home);
        playlist = itemView.findViewById(R.id.txt_playlist);
        style = itemView.findViewById(R.id.txt_style);
        courseID = itemView.findViewById(R.id.txt_courseID);
        teacher = itemView.findViewById(R.id.txt_teacher);
        time = itemView.findViewById(R.id.txt_time);
    }

    @Override
    public void onClick(View v) {
        mListener.onClick(v, getAdapterPosition());
    }
}
