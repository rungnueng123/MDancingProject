package com.mocom.com.mdancingproject.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.Dao.StudentStylePackageDao;
import com.mocom.com.mdancingproject.Holder.StudentStylePackageHolder;
import com.mocom.com.mdancingproject.R;

import java.util.List;

import static com.mocom.com.mdancingproject.config.config.HOST_URL;

public class StudentStylePackageAdapter extends RecyclerView.Adapter<StudentStylePackageHolder> {

    private ItemClickCallBack listener;
    private List<StudentStylePackageDao> studentStylePackageList;
    private Context context;

    public StudentStylePackageAdapter(ItemClickCallBack listener, List<StudentStylePackageDao> studentStylePackageList, Context context) {
        this.listener = listener;
        this.studentStylePackageList = studentStylePackageList;
        this.context = context;
    }

    @NonNull
    @Override
    public StudentStylePackageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_style_package, parent, false);
        return new StudentStylePackageHolder(v,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentStylePackageHolder holder, int position) {
        StudentStylePackageDao studentStylePackageDao = studentStylePackageList.get(position);

        holder.getTxtCoin().setText(studentStylePackageDao.getCoin());
        holder.getTxtName().setText(studentStylePackageDao.getNamePack());
        holder.getTxtStyle().setText(studentStylePackageDao.getStyle());
        holder.getTxtTime().setText(studentStylePackageDao.getTimes());

        String imgUrl = HOST_URL + studentStylePackageDao.getImgUrl();
        Glide.with(context)
                .load(imgUrl)
                .into(holder.getImgUrl());
    }

    @Override
    public int getItemCount() {
        if (studentStylePackageList == null) {
            return 0;
        }
        if (studentStylePackageList.size() == 0) {
            return 0;
        }
        return studentStylePackageList.size();
    }
}
