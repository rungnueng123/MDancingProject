package com.mocom.com.mdancingproject.Holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.R;

public class StudentCoinPackageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView txtName,txtCoin, txtPrice, txtCoinLarge;
    private ItemClickCallBack mListener;

    public TextView getTxtName() {
        return txtName;
    }

    public TextView getTxtCoin() {
        return txtCoin;
    }

    public TextView getTxtPrice() {
        return txtPrice;
    }

    public TextView getTxtCoinLarge() {
        return txtCoinLarge;
    }

    public StudentCoinPackageHolder(@NonNull View itemView, ItemClickCallBack listener) {
        super(itemView);
        mListener = listener;
        itemView.setOnClickListener(this);

        txtName = itemView.findViewById(R.id.txt_coin_pack_name);
        txtCoin = itemView.findViewById(R.id.txt_coin_pack_coin);
        txtPrice = itemView.findViewById(R.id.txt_coin_pack_price);
        txtCoinLarge = itemView.findViewById(R.id.txt_coin_large);
    }

    @Override
    public void onClick(View v) {
        mListener.onClick(v, getAdapterPosition());
    }
}
