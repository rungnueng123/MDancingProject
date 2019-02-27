package com.mocom.com.mdancingproject.Holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.R;

public class StudentStylePackageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView txtName,txtTime,txtCoin,txtStyle;
    private ImageView imgUrl;
    private Button btnBuy;
    private ItemClickCallBack mListener;

    public TextView getTxtName() {
        return txtName;
    }

    public TextView getTxtTime() {
        return txtTime;
    }

    public TextView getTxtCoin() {
        return txtCoin;
    }

    public TextView getTxtStyle() {
        return txtStyle;
    }

    public ImageView getImgUrl() {
        return imgUrl;
    }

    public Button getBtnBuy() {
        return btnBuy;
    }

    public StudentStylePackageHolder(@NonNull View itemView, ItemClickCallBack listener) {
        super(itemView);
        mListener = listener;
        itemView.setOnClickListener(this);

        txtName = itemView.findViewById(R.id.txt_style_pack_name);
        imgUrl = itemView.findViewById(R.id.img_style_pack);
        txtTime = itemView.findViewById(R.id.txt_style_pack_time);
        txtCoin = itemView.findViewById(R.id.txt_style_pack_coin);
        txtStyle = itemView.findViewById(R.id.txt_style_pack_style);
        btnBuy = itemView.findViewById(R.id.btn_coin_pack_buy);
    }

    @Override
    public void onClick(View v) {
        mListener.onClick(v, getAdapterPosition());
    }
}
