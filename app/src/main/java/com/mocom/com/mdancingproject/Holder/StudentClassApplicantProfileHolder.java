package com.mocom.com.mdancingproject.Holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.R;

public class StudentClassApplicantProfileHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView eventTitle, playlist, eventStyle, eventTeacher, eventDate, eventTime, eventBranch, active, eventStatus;
    private ImageView imgUrl;
    private ItemClickCallBack mListener;

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

    public TextView getEventBranch() {
        return eventBranch;
    }

    public TextView getActive() {
        return active;
    }

    public TextView getEventStatus() {
        return eventStatus;
    }

    public ImageView getImgUrl() {
        return imgUrl;
    }

    public StudentClassApplicantProfileHolder(@NonNull View itemView, ItemClickCallBack listener) {
        super(itemView);
        mListener = listener;
        itemView.setOnClickListener(this);

        imgUrl = itemView.findViewById(R.id.img_class);
        eventTitle = itemView.findViewById(R.id.txt_title);
        playlist = itemView.findViewById(R.id.txt_playlist);
        eventStyle = itemView.findViewById(R.id.txt_style);
        eventTeacher = itemView.findViewById(R.id.txt_teacher);
        eventDate = itemView.findViewById(R.id.txt_date);
        eventTime = itemView.findViewById(R.id.txt_time);
        eventBranch = itemView.findViewById(R.id.txt_branch);
        active = itemView.findViewById(R.id.txt_active);
        eventStatus = itemView.findViewById(R.id.txt_active_show);
    }

    @Override
    public void onClick(View v) {
        mListener.onClick(v, getAdapterPosition());
    }
}
