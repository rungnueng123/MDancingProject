package com.mocom.com.mdancingproject.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.Dao.HaveStylePackDao;
import com.mocom.com.mdancingproject.Holder.HaveStylePackAllHolder;
import com.mocom.com.mdancingproject.R;

import java.util.List;

import static com.mocom.com.mdancingproject.config.config.HOST_URL;

public class HaveStylePackAllAdapter extends RecyclerView.Adapter<HaveStylePackAllHolder> {

    private ItemClickCallBack listener;
    private List<HaveStylePackDao> styleList;
    Context context;

    public HaveStylePackAllAdapter(ItemClickCallBack listener, List<HaveStylePackDao> styleList, Context context) {
        this.listener = listener;
        this.styleList = styleList;
        this.context = context;
    }

    @NonNull
    @Override
    public HaveStylePackAllHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.style_pack_have_all, parent, false);
        return new HaveStylePackAllHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull HaveStylePackAllHolder holder, int position) {
        HaveStylePackDao dao = styleList.get(position);
        holder.getTxtPackStyle().setText(dao.getStylePack());
        holder.getTxtStyle().setText(dao.getStyleName());
        holder.getTxtHave().setText("เหลือ " + dao.getHaveTime() + " ครั้ง");
        String imgUrl = HOST_URL + dao.getImgUrl();
        Glide.with(context).load(imgUrl).into(holder.getImg());
    }

    @Override
    public int getItemCount() {
        if (styleList == null) {
            return 0;
        }
        if (styleList.size() == 0) {
            return 0;
        }
        return styleList.size();
    }
}
