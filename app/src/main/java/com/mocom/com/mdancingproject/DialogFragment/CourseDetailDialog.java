package com.mocom.com.mdancingproject.DialogFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mocom.com.mdancingproject.R;

public class CourseDetailDialog extends DialogFragment implements View.OnClickListener {

    private String courseName, courseStyle, courseLength, courseDesc;
    TextView txtCourse, txtStyle, txtLength, txtDesc, txtClose;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_course_detail, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        initFindViewByID(rootView);
        courseName = this.getArguments().getString("courseName");
        courseStyle = this.getArguments().getString("courseStyle");
        courseLength = this.getArguments().getString("courseLength");
        courseDesc = this.getArguments().getString("courseDesc");

        txtCourse.setText(getResources().getString(R.string.course) + " : " + courseName);
        txtStyle.setText(getResources().getString(R.string.style) + " : " + courseStyle);
        txtLength.setText(getResources().getString(R.string.length) + " : " + courseLength + " ชั่วโมง");
        txtDesc.setText(courseDesc);

    }

    private void initFindViewByID(View rootView) {
        txtCourse = rootView.findViewById(R.id.txt_course);
        txtStyle = rootView.findViewById(R.id.txt_style);
        txtLength = rootView.findViewById(R.id.txt_length);
        txtDesc = rootView.findViewById(R.id.txt_desc);
        txtClose = rootView.findViewById(R.id.txt_close);
        txtClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == txtClose) {
            getDialog().dismiss();
        }
    }
}
