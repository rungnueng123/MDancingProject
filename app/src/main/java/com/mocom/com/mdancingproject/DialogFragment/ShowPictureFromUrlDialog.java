package com.mocom.com.mdancingproject.DialogFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mocom.com.mdancingproject.R;

public class ShowPictureFromUrlDialog extends DialogFragment {

    ImageView imgShow;
    private String imgUrl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_show_picture_from_url, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        initFindViewByID(rootView);
        imgUrl = this.getArguments().getString("imageUrl");

        Glide.with(getContext())
                .load(imgUrl)
                .into(imgShow);

    }

    private void initFindViewByID(View rootView) {
        imgShow = rootView.findViewById(R.id.img_show);
    }
}
