package com.mocom.com.mdancingproject.Holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mocom.com.mdancingproject.Callback.RecyclerStyleClickCallBack;
import com.mocom.com.mdancingproject.R;

public class StyleHomeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private RecyclerStyleClickCallBack mListener;
    private ImageView imgUrl, imgInfo, imgSelect;
    private TextView style;
    Integer selectStyle = -1;

    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public ImageView getImgUrl() {
        return imgUrl;
    }

    public TextView getStyle() {
        return style;
    }

    public ImageView getImgInfo() {
        return imgInfo;
    }

    public StyleHomeHolder(@NonNull View itemView, RecyclerStyleClickCallBack listener) {
        super(itemView);
        mListener = listener;
        itemView.setOnClickListener(this);

        imgUrl = itemView.findViewById(R.id.img_style);
        style = itemView.findViewById(R.id.txt_style);
        imgInfo = itemView.findViewById(R.id.img_info);
        imgSelect = itemView.findViewById(R.id.img_select);

    }

    @Override
    public void onClick(View v) {
//        if(selectedItems.get(getAdapterPosition(), false)){
//            selectedItems.delete(getAdapterPosition());
//            v.setSelected(false);
//        }else{
//            selectedItems.put(getAdapterPosition(),true);
//            v.setSelected(true);
//        }
        mListener.onClick(v, getAdapterPosition(), imgSelect);
    }
}
