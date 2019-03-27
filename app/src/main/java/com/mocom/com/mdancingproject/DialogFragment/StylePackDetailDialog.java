package com.mocom.com.mdancingproject.DialogFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mocom.com.mdancingproject.R;

import org.jetbrains.annotations.NotNull;

import static com.mocom.com.mdancingproject.config.config.HOST_URL;

public class StylePackDetailDialog extends DialogFragment implements View.OnClickListener {

    private String time, stylePack, imgUrl, styleName;
    private TextView txtStylePack, txtStyleName, txtTime, txtClose;
    private ImageView img;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_style_pack_detail, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        initFindViewByID(rootView);

        time = this.getArguments().getString("time");
        stylePack = this.getArguments().getString("stylePack");
        imgUrl = this.getArguments().getString("imgUrl");
        styleName = this.getArguments().getString("styleName");

        String url = HOST_URL + imgUrl;
        Glide.with(getContext()).load(url).into(img);
        txtStylePack.setText(getResources().getString(R.string.pack) + " " + stylePack);
        txtStyleName.setText(getResources().getString(R.string.style) + " " + styleName);
        txtTime.setText("เหลือ " + time + " ครั้ง");
    }

    private void initFindViewByID(View rootView) {
        img = rootView.findViewById(R.id.img_style);
        txtStylePack = rootView.findViewById(R.id.txt_style);
        txtStyleName = rootView.findViewById(R.id.txt_style_name);
        txtTime = rootView.findViewById(R.id.txt_time);
        txtClose = rootView.findViewById(R.id.txt_close);
        txtClose.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        if (v == txtClose) {
            getDialog().dismiss();
        }
    }
}
