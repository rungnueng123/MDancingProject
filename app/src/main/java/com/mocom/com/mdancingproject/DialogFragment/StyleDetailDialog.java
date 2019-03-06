package com.mocom.com.mdancingproject.DialogFragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mocom.com.mdancingproject.R;

public class StyleDetailDialog extends DialogFragment implements View.OnClickListener {

    String styleName, styleDesc;
    TextView txtName, txtDesc;
    Button btnClose;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_style_detail, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        initFindViewByID(rootView);
        styleName = this.getArguments().getString("styleName");
        styleDesc = this.getArguments().getString("styleDesc");

        txtName.setText(getResources().getString(R.string.style) + " : " + styleName);
        txtDesc.setText(getResources().getString(R.string.description) + " : " + styleDesc);
    }

    private void initFindViewByID(View rootView) {
        txtName = rootView.findViewById(R.id.txt_style_name);
        txtDesc = rootView.findViewById(R.id.txt_style_desc);
        btnClose = rootView.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnClose) {
            getDialog().dismiss();
        }
    }
}
