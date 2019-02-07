package com.mocom.com.mdancingproject.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.mocom.com.mdancingproject.Dao.StudentCoinPackageDao;
import com.mocom.com.mdancingproject.DialogFragment.StudentCoinPackPaymentDialog;
import com.mocom.com.mdancingproject.Holder.StudentCoinPackageHolder;
import com.mocom.com.mdancingproject.R;

import java.util.List;

import static com.mocom.com.mdancingproject.config.config.HOST_URL;

public class StudentCoinPackageAdapter extends RecyclerView.Adapter<StudentCoinPackageHolder> {

    private List<StudentCoinPackageDao> studentCoinPackageList;
    private Context context;
//    ArrayList<String> typePayList = new ArrayList<>();
//    String[] items;
//    boolean[] checkedItem;

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
        holder.getBtnBuy().setText(studentCoinPackageDao.getBaht());

        String imgUrl = HOST_URL + studentCoinPackageDao.getImgUrl();
        Glide.with(context)
                .load(imgUrl)
                .into(holder.getImgUrl());

        holder.getBtnBuy().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                items = context.getResources().getStringArray(R.array.type_pay);
//                checkedItem = new boolean[items.length];
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("Payment").setMultiChoiceItems(R.array.type_pay, null, new DialogInterface.OnMultiChoiceClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//                        if (isChecked) {
//                            for (int i = 0; i<checkedItem.length;i++){
//                                typePayList.remove(items[i]);
//                            }
//                            typePayList.add(items[which]);
//                        } else {
//                            typePayList.remove(items[which]);
//                        }
//                    }
//                }).setPositiveButton("SELECT", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String selection = "";
//                        for (String ms : typePayList) {
//                            selection = selection + "\n" + ms;
//                        }
//                        Toast.makeText(context, selection, Toast.LENGTH_LONG).show();
//                    }
//                });
//                AlertDialog dialog = builder.create();
//                dialog.show();
                openDialogFragment(studentCoinPackageDao.getCoinPackID(),studentCoinPackageDao.getCoin(),studentCoinPackageDao.getBaht(),studentCoinPackageDao.getNamePack());
            }
        });
    }

    private void openDialogFragment(String coinPackID, String coin, String baht, String namePack) {
        Bundle bundle = new Bundle();
        bundle.putString("namePack", namePack);
        bundle.putString("baht", baht);
        bundle.putString("coin", coin);
        bundle.putString("coinPackID", coinPackID);
        StudentCoinPackPaymentDialog dialog = new StudentCoinPackPaymentDialog();
        dialog.setArguments(bundle);
        dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "StudentCoinPackPaymentDialog");
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
