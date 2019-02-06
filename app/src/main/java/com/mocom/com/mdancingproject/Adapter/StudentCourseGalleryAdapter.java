package com.mocom.com.mdancingproject.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.Dao.StudentCourseGalleryDao;
import com.mocom.com.mdancingproject.Holder.StudentCourseGalleryHolder;
import com.mocom.com.mdancingproject.R;

import java.util.List;

import static com.mocom.com.mdancingproject.config.config.HOST_URL;

public class StudentCourseGalleryAdapter extends RecyclerView.Adapter<StudentCourseGalleryHolder> {

    private ItemClickCallBack listener;
    private List<StudentCourseGalleryDao> studentCourseGalleryList;
    Context context;

    public StudentCourseGalleryAdapter(ItemClickCallBack listener, List<StudentCourseGalleryDao> studentCourseGalleryList, Context context) {
        this.listener = listener;
        this.studentCourseGalleryList = studentCourseGalleryList;
        this.context = context;
    }

    public void updateData(List<StudentCourseGalleryDao> dataset) {
        studentCourseGalleryList.clear();
        studentCourseGalleryList.addAll(dataset);
        notifyDataSetChanged();

    }

    public void setStudentCourseGalleryList(List<StudentCourseGalleryDao> studentCourseGalleryList) {
        this.studentCourseGalleryList = studentCourseGalleryList;
    }

    @NonNull
    @Override
    public StudentCourseGalleryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_gallery_item, parent, false);
        return new StudentCourseGalleryHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentCourseGalleryHolder holder, int position) {
        StudentCourseGalleryDao studentCourseGalleryDao = studentCourseGalleryList.get(position);
        String imgUrl = HOST_URL + studentCourseGalleryDao.getGallery();
        Glide.with(context)
                .load(imgUrl)
                .into(holder.getGalleryUrl());

    }

    @Override
    public int getItemCount() {
        if (studentCourseGalleryList == null) {
            return 0;
        }
        if (studentCourseGalleryList.size() == 0) {
            return 0;
        }
        return studentCourseGalleryList.size();
    }
}
