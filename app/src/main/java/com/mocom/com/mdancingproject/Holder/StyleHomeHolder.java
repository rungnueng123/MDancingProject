package com.mocom.com.mdancingproject.Holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.R;

public class StyleHomeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ItemClickCallBack mListener;
    ImageView imgUrl, imgInfo;
    TextView style;
//    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public ImageView getImgUrl() {
        return imgUrl;
    }

    public TextView getStyle() {
        return style;
    }

    public ImageView getImgInfo() {
        return imgInfo;
    }

    public StyleHomeHolder(@NonNull View itemView, ItemClickCallBack listener) {
        super(itemView);
        mListener = listener;
        itemView.setOnClickListener(this);

        imgUrl = itemView.findViewById(R.id.img_style);
        style = itemView.findViewById(R.id.txt_style);
        imgInfo = itemView.findViewById(R.id.img_info);
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
        mListener.onClick(v, getAdapterPosition());
    }
}
