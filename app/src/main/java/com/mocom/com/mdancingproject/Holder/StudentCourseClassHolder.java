package com.mocom.com.mdancingproject.Holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mocom.com.mdancingproject.R;

public class StudentCourseClassHolder extends RecyclerView.ViewHolder {

    private ImageView imgUrl, imgArrow;
    private TextView eventTitle, playlist, eventStyle, eventTeacher, eventDate, eventTime, eventEmpty, eventBranch, eventDesc;
    private Button btnPayment;

    public ImageView getImgUrl() {
        return imgUrl;
    }

    public ImageView getImgArrow() {
        return imgArrow;
    }

    public TextView getEventTitle() {
        return eventTitle;
    }

    public TextView getPlaylist() {
        return playlist;
    }

    public TextView getEventStyle() {
        return eventStyle;
    }

    public TextView getEventTeacher() {
        return eventTeacher;
    }

    public TextView getEventDate() {
        return eventDate;
    }

    public TextView getEventTime() {
        return eventTime;
    }

    public TextView getEventEmpty() {
        return eventEmpty;
    }

    public TextView getEventBranch() {
        return eventBranch;
    }

    public TextView getEventDesc() {
        return eventDesc;
    }

    public Button getBtnPayment() {
        return btnPayment;
    }

    public StudentCourseClassHolder(@NonNull View itemView) {
        super(itemView);

        imgUrl = itemView.findViewById(R.id.img_class);
        imgArrow = itemView.findViewById(R.id.img_arrow);
        eventTitle = itemView.findViewById(R.id.txt_title);
        playlist = itemView.findViewById(R.id.txt_playlist);
        eventStyle = itemView.findViewById(R.id.txt_style);
        eventTeacher = itemView.findViewById(R.id.txt_teacher);
        eventDate = itemView.findViewById(R.id.txt_date);
        eventTime = itemView.findViewById(R.id.txt_time);
        eventEmpty = itemView.findViewById(R.id.txt_empty);
        eventBranch = itemView.findViewById(R.id.txt_branch);
        eventDesc = itemView.findViewById(R.id.txt_show_desc);
        btnPayment = itemView.findViewById(R.id.btn_payment);
    }
}
