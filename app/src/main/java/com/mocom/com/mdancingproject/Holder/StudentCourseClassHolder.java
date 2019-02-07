package com.mocom.com.mdancingproject.Holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mocom.com.mdancingproject.R;

public class StudentCourseClassHolder extends RecyclerView.ViewHolder {

    private ImageView imgUrl;
    private TextView eventID, eventTitle, playlist, eventDate, eventTime, eventDesc;
    private Button btnPayment, btnDetail;


    public ImageView getImgUrl() {
        return imgUrl;
    }

    public TextView getEventID() {
        return eventID;
    }

    public TextView getEventTitle() {
        return eventTitle;
    }

    public TextView getPlaylist() {
        return playlist;
    }

    public TextView getEventDate() {
        return eventDate;
    }

    public TextView getEventTime() {
        return eventTime;
    }

    public TextView getEventDesc() {
        return eventDesc;
    }

    public Button getBtnPayment() {
        return btnPayment;
    }

    public Button getBtnDetail() {
        return btnDetail;
    }

    public StudentCourseClassHolder(@NonNull View itemView) {
        super(itemView);

        imgUrl = itemView.findViewById(R.id.img_class);
        eventTitle = itemView.findViewById(R.id.txt_title);
        playlist = itemView.findViewById(R.id.txt_playlist);
        eventDate = itemView.findViewById(R.id.txt_date);
        eventTime = itemView.findViewById(R.id.txt_time);
        eventDesc = itemView.findViewById(R.id.txt_desc);
        btnPayment = itemView.findViewById(R.id.btn_payment);
        btnDetail = itemView.findViewById(R.id.btn_detail);
    }
}
