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
import com.mocom.com.mdancingproject.Callback.RecyclerStyleClickCallBack;
import com.mocom.com.mdancingproject.Dao.StyleHomeDao;
import com.mocom.com.mdancingproject.DialogFragment.StyleDetailDialog;
import com.mocom.com.mdancingproject.Holder.StyleHomeHolder;
import com.mocom.com.mdancingproject.R;

import java.util.List;

import static com.mocom.com.mdancingproject.config.config.HOST_URL;

public class StyleHomeAdapter extends RecyclerView.Adapter<StyleHomeHolder> {

    private RecyclerStyleClickCallBack listener;
    private List<StyleHomeDao> styleHomeDaoList;
    Context context;

    public StyleHomeAdapter(RecyclerStyleClickCallBack listener, List<StyleHomeDao> styleHomeDaoList, Context context) {
        this.listener = listener;
        this.styleHomeDaoList = styleHomeDaoList;
        this.context = context;
    }

    @NonNull
    @Override
    public StyleHomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.style_home_item, parent, false);
        return new StyleHomeHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull StyleHomeHolder holder, int position) {
        StyleHomeDao styleHomeDao = styleHomeDaoList.get(position);

        holder.getStyle().setText(styleHomeDao.getStyleName());
        String imgUrl = HOST_URL + styleHomeDao.getStyleImage();
        Glide.with(context)
                .load(imgUrl)
                .into(holder.getImgUrl());

        holder.getImgInfo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInfoDialogFragment(styleHomeDao.getStyleName(), styleHomeDao.getStyleDesc());
            }
        });
        if (styleHomeDaoList.get(position).getStyleSelect().equals("0")) {
            holder.getRelativeLayout().setSelected(false);
        } else {
            holder.getRelativeLayout().setSelected(true);
        }
    }

    private void openInfoDialogFragment(String styleName, String styleDesc) {
        Bundle bundle = new Bundle();
        bundle.putString("styleName", styleName);
        bundle.putString("styleDesc", styleDesc);

        StyleDetailDialog dialog = new StyleDetailDialog();
        dialog.setArguments(bundle);
        dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "StyleDetailDialog");
    }

    @Override
    public int getItemCount() {
        if (styleHomeDaoList == null) {
            return 0;
        }
        if (styleHomeDaoList.size() == 0) {
            return 0;
        }
        return styleHomeDaoList.size();
    }
}
