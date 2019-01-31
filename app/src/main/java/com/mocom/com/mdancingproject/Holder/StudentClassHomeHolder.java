package com.mocom.com.mdancingproject.Holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.R;

public class StudentClassHomeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView classID, classStart, classEnd, description, className, courseName, playlist, style, courseID;
    ImageView imgUrl;
    private ItemClickCallBack mListener;

    public TextView getClassID() {
        return classID;
    }

    public TextView getClassStart() {
        return classStart;
    }

    public TextView getClassEnd() {
        return classEnd;
    }

    public TextView getDescription() {
        return description;
    }

    public TextView getClassName() {
        return className;
    }

    public TextView getCourseName() {
        return courseName;
    }

    public TextView getPlaylist() {
        return playlist;
    }

    public TextView getStyle() {
        return style;
    }

    public TextView getCourseID() {
        return courseID;
    }

    public ImageView getImgUrl() {
        return imgUrl;
    }

    public StudentClassHomeHolder(@NonNull View itemView, ItemClickCallBack listener) {
        super(itemView);
        mListener = listener;

        classStart = itemView.findViewById(R.id.txt_start);
        classEnd = itemView.findViewById(R.id.txt_end);
        description = itemView.findViewById(R.id.txt_description);
        className = itemView.findViewById(R.id.txt_class_name);
        courseName = itemView.findViewById(R.id.txt_course);
        imgUrl = itemView.findViewById(R.id.img_class_home);
        playlist = itemView.findViewById(R.id.txt_playlist);
        style = itemView.findViewById(R.id.txt_style);
        courseID = itemView.findViewById(R.id.txt_courseID);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(itemView.getContext(), courseID.getText(), Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onClick(View v) {
        mListener.onClick(v, getAdapterPosition());
    }

}
