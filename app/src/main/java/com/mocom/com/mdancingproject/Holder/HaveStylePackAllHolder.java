package com.mocom.com.mdancingproject.Holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.R;

public class HaveStylePackAllHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView txtPackStyle, txtStyle, txtHave;
    private ImageView img;
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

    public ImageView getImg() {
        return img;
    }

    public HaveStylePackAllHolder(@NonNull View itemView, ItemClickCallBack listener) {
        super(itemView);
        mListener = listener;
        itemView.setOnClickListener(this);

        img = itemView.findViewById(R.id.img_style);
        txtPackStyle = itemView.findViewById(R.id.txt_pack_style);
        txtStyle = itemView.findViewById(R.id.txt_style);
        txtHave = itemView.findViewById(R.id.txt_have);

    }

    @Override
    public void onClick(View v) {
        mListener.onClick(v, getAdapterPosition());
    }
}
