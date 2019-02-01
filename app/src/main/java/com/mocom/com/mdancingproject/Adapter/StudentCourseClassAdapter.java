package com.mocom.com.mdancingproject.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.Dao.StudentCourseClassDao;
import com.mocom.com.mdancingproject.Holder.StudentCourseClassHolder;
import com.mocom.com.mdancingproject.R;

import java.util.List;

import static com.mocom.com.mdancingproject.config.config.HOST_URL;

public class StudentCourseClassAdapter extends RecyclerView.Adapter<StudentCourseClassHolder> {

    private ItemClickCallBack listener;
    private List<StudentCourseClassDao> studentCourseClassList;
    Context context;

    public StudentCourseClassAdapter(ItemClickCallBack listener, List<StudentCourseClassDao> studentCourseClassList, Context context) {
        this.listener = listener;
        this.studentCourseClassList = studentCourseClassList;
        this.context = context;
    }

    public void updateData(List<StudentCourseClassDao> dataset) {
        studentCourseClassList.clear();
        studentCourseClassList.addAll(dataset);
        notifyDataSetChanged();

    }

    public void setStudentCourseClassList(List<StudentCourseClassDao> studentCourseClassList) {
        this.studentCourseClassList = studentCourseClassList;
    }

    @NonNull
    @Override
    public StudentCourseClassHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_class_detail_item, parent, false);
        return new StudentCourseClassHolder(v, listener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull StudentCourseClassHolder holder, int position) {
        StudentCourseClassDao studentCourseClassDao = studentCourseClassList.get(position);
        holder.getEventTitle().setText("Class : "+studentCourseClassDao.getEventTitle());
        holder.getPlaylist().setText("PlayList : "+studentCourseClassDao.getPlaylist());
        holder.getEventDate().setText("Date : "+studentCourseClassDao.getEventDate());
        holder.getEventTime().setText("Time : "+studentCourseClassDao.getEventTime());
        holder.getEventDesc().setText("Description : "+studentCourseClassDao.getEventDesc());

        String imgUrl = HOST_URL + studentCourseClassDao.getImgUrl();
        Glide.with(context)
                .load(imgUrl)
                .into(holder.getImgUrl());

        //click button
        holder.getBtnDetail().setOnClickListener(v -> Toast.makeText(context,studentCourseClassDao.getEventID()+"Detail",Toast.LENGTH_LONG).show());
        holder.getBtnPayment().setOnClickListener(v -> Toast.makeText(context,studentCourseClassDao.getEventID()+"Payment",Toast.LENGTH_LONG).show());
    }

    @Override
    public int getItemCount() {
        if (studentCourseClassList == null) {
            return 0;
        }
        if (studentCourseClassList.size() == 0) {
            return 0;
        }
        return studentCourseClassList.size();
    }
}
