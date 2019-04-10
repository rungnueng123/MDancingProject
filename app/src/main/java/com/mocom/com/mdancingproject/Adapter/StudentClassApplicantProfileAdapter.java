package com.mocom.com.mdancingproject.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.Dao.StudentClassApplicantProfileDao;
import com.mocom.com.mdancingproject.Holder.StudentClassApplicantProfileHolder;
import com.mocom.com.mdancingproject.R;

import java.util.List;

import static com.mocom.com.mdancingproject.config.config.HOST_URL;

public class StudentClassApplicantProfileAdapter extends RecyclerView.Adapter<StudentClassApplicantProfileHolder> {

    private ItemClickCallBack listener;
    private List<StudentClassApplicantProfileDao> studentClassApplicantProfileDaoList;
    Context context;

    public StudentClassApplicantProfileAdapter(ItemClickCallBack listener, List<StudentClassApplicantProfileDao> studentClassApplicantProfileDaoList, Context context) {
        this.listener = listener;
        this.studentClassApplicantProfileDaoList = studentClassApplicantProfileDaoList;
        this.context = context;
    }

    @NonNull
    @Override
    public StudentClassApplicantProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_class_applicant, parent, false);
        return new StudentClassApplicantProfileHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentClassApplicantProfileHolder holder, int position) {
        StudentClassApplicantProfileDao studentClassApplicantProfileDao = studentClassApplicantProfileDaoList.get(position);
        holder.getEventTitle().setText(studentClassApplicantProfileDao.getEventTitle());
        holder.getPlaylist().setText(studentClassApplicantProfileDao.getPlaylist());
        holder.getEventStyle().setText(studentClassApplicantProfileDao.getEventStyle());
        holder.getEventTeacher().setText(studentClassApplicantProfileDao.getTeacher());
        holder.getEventDate().setText(studentClassApplicantProfileDao.getEventDate());
        holder.getEventTime().setText(studentClassApplicantProfileDao.getEventTime());
        holder.getEventBranch().setText(studentClassApplicantProfileDao.getBranch());
        holder.getActive().setText(studentClassApplicantProfileDao.getActive());
        holder.getEventStatus().setText(studentClassApplicantProfileDao.getEventStatus());


        String imgUrl = HOST_URL + studentClassApplicantProfileDao.getImgUrl();
        Glide.with(context)
                .load(imgUrl)
                .into(holder.getImgUrl());
    }

    @Override
    public int getItemCount() {
        if (studentClassApplicantProfileDaoList == null) {
            return 0;
        }
        if (studentClassApplicantProfileDaoList.size() == 0) {
            return 0;
        }
        return studentClassApplicantProfileDaoList.size();
    }
}
