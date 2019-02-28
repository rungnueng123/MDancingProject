package com.mocom.com.mdancingproject.DialogFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mocom.com.mdancingproject.R;

public class FailBuyClassDialog extends DialogFragment implements View.OnClickListener {

    TextView txtFail;
    Button btnBack;
    public OnBackFailBuyClassListener onBackFailBuyClassListener;

    public interface OnBackFailBuyClassListener {
        void sendOnBackFailBuyClassListener(String back);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_fail_buy_class, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        initFindViewByID(rootView);
    }

    private void initFindViewByID(View rootView) {
        txtFail = rootView.findViewById(R.id.txt_fail);
        btnBack = rootView.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnBack){
            onBackFailBuyClassListener.sendOnBackFailBuyClassListener(txtFail.getText().toString());
            getDialog().dismiss();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onBackFailBuyClassListener = (OnBackFailBuyClassListener) getActivity();
        } catch (ClassCastException e) {
            Log.d("TAG", "onAttach: ClassCastException: " + e.getMessage());
        }

    }
}
