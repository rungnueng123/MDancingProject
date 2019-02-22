package com.mocom.com.mdancingproject.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.Dao.ClassDao;
import com.mocom.com.mdancingproject.Holder.ClassHolder;
import com.mocom.com.mdancingproject.R;

import java.util.List;

import static com.mocom.com.mdancingproject.config.config.HOST_URL;

public class ClassAdapter extends RecyclerView.Adapter<ClassHolder> {

    private ItemClickCallBack listener;
    private List<ClassDao> classDaoList;
    Context context;

    public ClassAdapter(ItemClickCallBack listener, List<ClassDao> classDaoList, Context context) {
        this.listener = listener;
        this.classDaoList = classDaoList;
        this.context = context;
    }

    @NonNull
    @Override
    public ClassHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_home, parent, false);
        return new ClassHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassHolder holder, int position) {
        ClassDao classDao = classDaoList.get(position);
        holder.getClassName().setText("คลาส : "+classDao.getTitle());
        holder.getPlaylist().setText("เพลง : "+classDao.getPlaylistTitle());
        holder.getStyle().setText("สไตล์ : "+classDao.getCourseStyleName());
        holder.getTeacher().setText("ผู้สอน : "+classDao.getTeacher());
        holder.getTime().setText("เวลา : "+classDao.getEventStart()+" - "+classDao.getEventEnd());
        holder.getCourseID().setText(classDao.getCourseID());
        String imgUrl = HOST_URL + classDao.getImgUrl();
        Glide.with(context)
                .load(imgUrl)
                .into(holder.getImgUrl());
    }

    @Override
    public int getItemCount() {
        if (classDaoList == null) {
            return 0;
        }
        if (classDaoList.size() == 0) {
            return 0;
        }
        return classDaoList.size();
    }
}
