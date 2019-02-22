package com.mocom.com.mdancingproject.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.Dao.CourseHomeDao;
import com.mocom.com.mdancingproject.Holder.CourseHomeHolder;
import com.mocom.com.mdancingproject.R;

import java.util.List;

import static com.mocom.com.mdancingproject.config.config.HOST_URL;

public class CourseHomeAdapter extends RecyclerView.Adapter<CourseHomeHolder> {

    private ItemClickCallBack listener;
    private List<CourseHomeDao> courseHomeDaoList;
    Context context;

    public CourseHomeAdapter(ItemClickCallBack listener, List<CourseHomeDao> courseHomeDaoList, Context context) {
        this.listener = listener;
        this.courseHomeDaoList = courseHomeDaoList;
        this.context = context;
    }

    @NonNull
    @Override
    public CourseHomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_home_item, parent, false);
        return new CourseHomeHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseHomeHolder holder, int position) {
        CourseHomeDao courseHomeDao = courseHomeDaoList.get(position);

        holder.getCourseName().setText(courseHomeDao.getCourseName());
        holder.getCourseStyle().setText(courseHomeDao.getCourseStyle());

        String imgUrl = HOST_URL + courseHomeDao.getImgUrl();
        Glide.with(context)
                .load(imgUrl)
                .into(holder.getImgUrl());

    }

    @Override
    public int getItemCount() {
        if (courseHomeDaoList == null) {
            return 0;
        }
        if (courseHomeDaoList.size() == 0) {
            return 0;
        }
        return courseHomeDaoList.size();
    }
}
