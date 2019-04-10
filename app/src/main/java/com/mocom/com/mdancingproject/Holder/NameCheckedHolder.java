package com.mocom.com.mdancingproject.Holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mocom.com.mdancingproject.R;

public class NameCheckedHolder extends RecyclerView.ViewHolder {

    private TextView txtNo, txtStuName;

    public TextView getTxtNo() {
        return txtNo;
    }

    public TextView getTxtStuName() {
        return txtStuName;
    }

    public NameCheckedHolder(@NonNull View itemView) {
        super(itemView);

        txtNo = itemView.findViewById(R.id.txt_no);
        txtStuName = itemView.findViewById(R.id.txt_stu_name);
    }
}
