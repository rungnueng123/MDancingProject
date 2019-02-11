package com.mocom.com.mdancingproject.Holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mocom.com.mdancingproject.R;

public class StudentCoinPackageHolder extends RecyclerView.ViewHolder {

    private TextView txtName,txtCoin;
    private ImageView imgUrl;
    private Button btnBuy;

    public TextView getTxtName() {
        return txtName;
    }

    public TextView getTxtCoin() {
        return txtCoin;
    }

    public ImageView getImgUrl() {
        return imgUrl;
    }

    public Button getBtnBuy() {
        return btnBuy;
    }

    public StudentCoinPackageHolder(@NonNull View itemView) {
        super(itemView);

        txtName = itemView.findViewById(R.id.txt_coin_pack_name);
        txtCoin = itemView.findViewById(R.id.txt_coin_pack_coin);
        imgUrl = itemView.findViewById(R.id.img_coin_pack);
        btnBuy = itemView.findViewById(R.id.btn_coin_pack_buy);
    }
}
