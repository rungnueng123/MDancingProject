package com.mocom.com.mdancingproject.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mocom.com.mdancingproject.Dao.NameCheckedDao;
import com.mocom.com.mdancingproject.Holder.NameCheckedHolder;
import com.mocom.com.mdancingproject.R;

import java.util.List;

public class NameCheckedAdapter extends RecyclerView.Adapter<NameCheckedHolder> {

    private List<NameCheckedDao> nameCheckedDaoList;
    Context context;

    public NameCheckedAdapter(List<NameCheckedDao> nameCheckedDaoList, Context context) {
        this.nameCheckedDaoList = nameCheckedDaoList;
        this.context = context;
    }

    @NonNull
    @Override
    public NameCheckedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_check, parent, false);
        return new NameCheckedHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NameCheckedHolder holder, int position) {
        NameCheckedDao nameCheckedDao = nameCheckedDaoList.get(position);
        holder.getTxtNo().setText(position+" : ");
        holder.getTxtStuName().setText(nameCheckedDao.getStuName());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
