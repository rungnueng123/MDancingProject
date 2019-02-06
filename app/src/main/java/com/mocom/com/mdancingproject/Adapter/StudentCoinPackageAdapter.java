package com.mocom.com.mdancingproject.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mocom.com.mdancingproject.Dao.StudentCoinPackageDao;
import com.mocom.com.mdancingproject.Holder.StudentCoinPackageHolder;
import com.mocom.com.mdancingproject.R;

import java.util.List;

import static com.mocom.com.mdancingproject.config.config.HOST_URL;

public class StudentCoinPackageAdapter extends RecyclerView.Adapter<StudentCoinPackageHolder> {

    private List<StudentCoinPackageDao> studentCoinPackageList;
    private Context context;

    public StudentCoinPackageAdapter(List<StudentCoinPackageDao> studentCoinPackageList, Context context) {
        this.studentCoinPackageList = studentCoinPackageList;
        this.context = context;
    }

    @NonNull
    @Override
    public StudentCoinPackageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_coin_package, parent, false);
        return new StudentCoinPackageHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentCoinPackageHolder holder, int position) {
        StudentCoinPackageDao studentCoinPackageDao = studentCoinPackageList.get(position);
        holder.getTxtName().setText(studentCoinPackageDao.getNamePack());
        holder.getTxtCoin().setText(studentCoinPackageDao.getCoin());
        holder.getBtnBuy().setText(studentCoinPackageDao.getBath());

        String imgUrl = HOST_URL + studentCoinPackageDao.getImgUrl();
        Glide.with(context)
                .load(imgUrl)
                .into(holder.getImgUrl());

        holder.getBtnBuy().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, studentCoinPackageDao.getNamePack(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (studentCoinPackageList == null) {
            return 0;
        }
        if (studentCoinPackageList.size() == 0) {
            return 0;
        }
        return studentCoinPackageList.size();
    }

}
