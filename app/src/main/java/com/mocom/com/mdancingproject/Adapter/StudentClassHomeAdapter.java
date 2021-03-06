package com.mocom.com.mdancingproject.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.Dao.StudentClassHomeDao;
import com.mocom.com.mdancingproject.Holder.StudentClassHomeHolder;
import com.mocom.com.mdancingproject.R;

import java.util.List;

import static com.mocom.com.mdancingproject.config.config.HOST_URL;


public class StudentClassHomeAdapter extends RecyclerView.Adapter<StudentClassHomeHolder> {

    private ItemClickCallBack listener;
    private List<StudentClassHomeDao> studentClassList;
    Context context;

    public StudentClassHomeAdapter(ItemClickCallBack listener, List<StudentClassHomeDao> studentClassList, Context context) {
        this.listener = listener;
        this.studentClassList = studentClassList;
        this.context = context;
    }

    public void updateData(List<StudentClassHomeDao> dataset) {
        studentClassList.clear();
        studentClassList.addAll(dataset);
        notifyDataSetChanged();

    }

    public void setStudentClassList(List<StudentClassHomeDao> studentClassList) {
        this.studentClassList = studentClassList;
    }

    @NonNull
    @Override
    public StudentClassHomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_class_home, parent, false);
        return new StudentClassHomeHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentClassHomeHolder holder, int position) {
        StudentClassHomeDao studentClassHomeDao = studentClassList.get(position);
        holder.getClassName().setText("คลาส : "+studentClassHomeDao.getTitle());
        holder.getPlaylist().setText("เพลง : "+studentClassHomeDao.getPlaylistTitle());
        holder.getStyle().setText("สไตล์ : "+studentClassHomeDao.getCourseStyleName());
        holder.getTeacher().setText("ผู้สอน : "+studentClassHomeDao.getTeacher());
        holder.getTime().setText("เวลา : "+studentClassHomeDao.getEventStart()+" - "+studentClassHomeDao.getEventEnd());
        holder.getCourseID().setText(studentClassHomeDao.getCourseID());
        String imgUrl = HOST_URL + studentClassHomeDao.getImgUrl();
        Glide.with(context)
                .load(imgUrl)
                .into(holder.getImgUrl());
    }

    @Override
    public int getItemCount() {
        if (studentClassList == null) {
            return 0;
        }
        if (studentClassList.size() == 0) {
            return 0;
        }
        return studentClassList.size();
    }
}
