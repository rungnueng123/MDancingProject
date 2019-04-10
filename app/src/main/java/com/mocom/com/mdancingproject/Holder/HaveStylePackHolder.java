package com.mocom.com.mdancingproject.Holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.R;

public class HaveStylePackHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView txtPackStyle, txtStyle, txtHave;
    private ItemClickCallBack mListener;

    public TextView getTxtPackStyle() {
        return txtPackStyle;
    }

    public TextView getTxtStyle() {
        return txtStyle;
    }

    public TextView getTxtHave() {
        return txtHave;
    }

    public HaveStylePackHolder(@NonNull View itemView, ItemClickCallBack listener) {
        super(itemView);
        mListener = listener;
        itemView.setOnClickListener(this);

        txtHave = itemView.findViewById(R.id.txt_have);
        txtPackStyle = itemView.findViewById(R.id.txt_pack_style);
        txtStyle = itemView.findViewById(R.id.txt_style);
    }

    @Override
    public void onClick(View v) {
        mListener.onClick(v, getAdapterPosition());
    }
}
