package com.mocom.com.mdancingproject.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.Dao.StudentClassHistoryProfileDao;
import com.mocom.com.mdancingproject.Holder.StudentClassHistoryProfileHolder;
import com.mocom.com.mdancingproject.R;

import java.util.List;

import static com.mocom.com.mdancingproject.config.config.HOST_URL;

public class StudentClassHistoryProfileAdapter extends RecyclerView.Adapter<StudentClassHistoryProfileHolder> {

    private ItemClickCallBack listener;
    private List<StudentClassHistoryProfileDao> studentClassHistoryProfileDaoList;
    Context context;

    public StudentClassHistoryProfileAdapter(ItemClickCallBack listener, List<StudentClassHistoryProfileDao> studentClassHistoryProfileDaoList, Context context) {
        this.listener = listener;
        this.studentClassHistoryProfileDaoList = studentClassHistoryProfileDaoList;
        this.context = context;
    }

    @NonNull
    @Override
    public StudentClassHistoryProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_class_history, parent, false);
        return new StudentClassHistoryProfileHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentClassHistoryProfileHolder holder, int position) {
        StudentClassHistoryProfileDao studentClassHistoryProfileDao = studentClassHistoryProfileDaoList.get(position);
        holder.getEventTitle().setText(studentClassHistoryProfileDao.getEventTitle());
        holder.getPlaylist().setText(studentClassHistoryProfileDao.getPlaylist());
        holder.getEventStyle().setText(studentClassHistoryProfileDao.getEventStyle());
        holder.getEventTeacher().setText(studentClassHistoryProfileDao.getTeacher());
        holder.getEventDate().setText(studentClassHistoryProfileDao.getEventDate());
        holder.getEventTime().setText(studentClassHistoryProfileDao.getEventTime());
        holder.getEventBranch().setText(studentClassHistoryProfileDao.getBranch());


        String imgUrl = HOST_URL + studentClassHistoryProfileDao.getImgUrl();
        Glide.with(context)
                .load(imgUrl)
                .into(holder.getImgUrl());

    }

    @Override
    public int getItemCount() {
        if (studentClassHistoryProfileDaoList == null) {
            return 0;
        }
        if (studentClassHistoryProfileDaoList.size() == 0) {
            return 0;
        }
        return studentClassHistoryProfileDaoList.size();
    }
}
