package com.mocom.com.mdancingproject.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.mocom.com.mdancingproject.Activities.StudentCourseClassDetailActivity;
import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.Dao.StudentCourseClassDao;
import com.mocom.com.mdancingproject.DialogFragment.StudentPaymentDialog;
import com.mocom.com.mdancingproject.Holder.StudentCourseClassHolder;
import com.mocom.com.mdancingproject.R;

import java.util.List;

import static com.mocom.com.mdancingproject.config.config.HOST_URL;

public class StudentCourseClassAdapter extends RecyclerView.Adapter<StudentCourseClassHolder> {

    private ItemClickCallBack listener;
    private List<StudentCourseClassDao> studentCourseClassList;
    private Context context;

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
        holder.getEventTitle().setText("Class : " + studentCourseClassDao.getEventTitle());
        holder.getPlaylist().setText("PlayList : " + studentCourseClassDao.getPlaylist());
        holder.getEventDate().setText("Date : " + studentCourseClassDao.getEventDate());
        holder.getEventTime().setText("Time : " + studentCourseClassDao.getEventTime());
        holder.getEventDesc().setText("Description : " + studentCourseClassDao.getEventDesc());

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

        //set click listener
        holder.getBtnDetail().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StudentCourseClassDetailActivity.class);
                intent.putExtra("eventID", studentCourseClassDao.getEventID());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.getBtnPayment().setOnClickListener(v -> {
//            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
//            alertDialog.setTitle("Are you sure to buy this class");
//            alertDialog.setMessage("Coins: " + studentCourseClassDao.getCoin());
//            alertDialog.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.cancel();
//                }
//            });
//            alertDialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                    Toast.makeText(getApplicationContext(), "aaa", Toast.LENGTH_SHORT).show();
//
//                }
//            });
//
//            AlertDialog dialog = alertDialog.create();
//            dialog.show();

            StudentPaymentDialog dialog = new StudentPaymentDialog();
            dialog.show(((AppCompatActivity)context).getSupportFragmentManager(),"StudentPaymentDialog");
        });
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
