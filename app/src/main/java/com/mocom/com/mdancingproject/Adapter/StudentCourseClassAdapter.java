package com.mocom.com.mdancingproject.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.mocom.com.mdancingproject.Activities.ShowPictureActivity;
import com.mocom.com.mdancingproject.Dao.StudentCourseClassDao;
import com.mocom.com.mdancingproject.DialogFragment.StudentPaymentDialog;
import com.mocom.com.mdancingproject.Holder.StudentCourseClassHolder;
import com.mocom.com.mdancingproject.R;

import java.util.List;

import static com.mocom.com.mdancingproject.config.config.HOST_URL;

public class StudentCourseClassAdapter extends RecyclerView.Adapter<StudentCourseClassHolder> {

    private List<StudentCourseClassDao> studentCourseClassList;
    private Context context;

    public StudentCourseClassAdapter(List<StudentCourseClassDao> studentCourseClassList, Context context) {
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
        return new StudentCourseClassHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull StudentCourseClassHolder holder, int position) {
        StudentCourseClassDao studentCourseClassDao = studentCourseClassList.get(position);
        holder.getEventTitle().setText("คลาส : " + studentCourseClassDao.getEventTitle());
        holder.getPlaylist().setText("เพลง : " + studentCourseClassDao.getPlaylist());
        holder.getEventStyle().setText("สไตล์ : " + studentCourseClassDao.getEventStyle());
        holder.getEventTeacher().setText("ผู้สอน : " + studentCourseClassDao.getEventTeacher());
        holder.getEventDate().setText("วันที่เรียน : " + studentCourseClassDao.getEventDate());
        holder.getEventTime().setText("เวลา : " + studentCourseClassDao.getEventTime());
        holder.getEventEmpty().setText("ว่าง : " + studentCourseClassDao.getEventEmpty());
        holder.getEventBranch().setText("สาขา : "+studentCourseClassDao.getEventBranch());
        holder.getEventDesc().setText(studentCourseClassDao.getEventDesc());

        String imgUrl = HOST_URL + studentCourseClassDao.getImgUrl();
        Glide.with(context)
                .load(imgUrl)
                .into(holder.getImgUrl());

        //click button
        //TODO set hide payment
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        Date strDate = null;
//        try {
//            strDate = sdf.parse(studentCourseClassDao.getEventDate());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        if (new Date().after(strDate)) {
//            holder.getBtnPayment().setVisibility(View.GONE);
//        }else{
//            holder.getBtnPayment().setVisibility(View.VISIBLE);
//        }

        holder.getImgUrl().setOnClickListener(v ->{
            Intent intentShowPic = new Intent(context, ShowPictureActivity.class);
            intentShowPic.putExtra("imageUrl", imgUrl);
            context.startActivity(intentShowPic);
        });

        holder.getImgArrow().setOnClickListener(v -> {
            if(holder.getEventDesc().getVisibility() == View.GONE){
                holder.getImgArrow().setImageResource(R.drawable.ic_arrow_drop_down_open);
                holder.getEventDesc().setVisibility(View.VISIBLE);
            }else{
                holder.getImgArrow().setImageResource(R.drawable.ic_arrow_drop_down_close);
                holder.getEventDesc().setVisibility(View.GONE);
            }
        });

        holder.getBtnPayment().setOnClickListener(v -> {
            openDialogFragment(studentCourseClassDao.getCoin(),studentCourseClassDao.getEventID());
        });
    }

    private void openDialogFragment(String coin, String eventID) {
        Bundle bundle = new Bundle();
        bundle.putString("coin",coin);
        bundle.putString("eventID",eventID);
        StudentPaymentDialog dialog = new StudentPaymentDialog();
        dialog.setArguments(bundle);
        dialog.show(((AppCompatActivity)context).getSupportFragmentManager(),"StudentPaymentDialog");
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
