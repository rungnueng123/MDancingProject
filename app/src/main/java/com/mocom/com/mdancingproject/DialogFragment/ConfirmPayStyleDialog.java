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
import android.widget.TextView;

import com.mocom.com.mdancingproject.R;

public class ConfirmPayStyleDialog extends DialogFragment implements View.OnClickListener {

    public interface OnConfirmStyleListener {
        void sendConfirmStyle(String confirm, String stylePackID, String coin);
    }

    public OnConfirmStyleListener onConfirmStyleListener;
    TextView txtName, txtCoin, txtStyle, txtTime, txtOk, txtCancel;
    String namePack, coin, times, style, stylePackID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_confirm_pay_style, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        initFindViewByID(rootView);

        stylePackID = this.getArguments().getString("stylePackID");
        namePack = this.getArguments().getString("namePack");
        coin = this.getArguments().getString("coin");
        times = this.getArguments().getString("times");
        style = this.getArguments().getString("style");
        txtName.setText(getResources().getString(R.string.pack) + " : " + namePack);
        txtCoin.setText(getResources().getString(R.string.coin) + " : " + coin);
        txtStyle.setText(getResources().getString(R.string.style) + " : " + style);
        txtTime.setText(getResources().getString(R.string.txt_time_show) + " : " + times);
    }

    private void initFindViewByID(View rootView) {

        txtName = rootView.findViewById(R.id.txt_name_pack);
        txtStyle = rootView.findViewById(R.id.txt_style);
        txtTime = rootView.findViewById(R.id.txt_times);
        txtCoin = rootView.findViewById(R.id.txt_coin);
        txtCancel = rootView.findViewById(R.id.txt_cancel);
        txtOk = rootView.findViewById(R.id.txt_ok);
        txtOk.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == txtOk) {
            onConfirmStyleListener.sendConfirmStyle(txtOk.getText().toString(), stylePackID, coin);
            getDialog().dismiss();
        }
        if (v == txtCancel) {
            onConfirmStyleListener.sendConfirmStyle(txtCancel.getText().toString(), stylePackID, coin);
            getDialog().dismiss();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onConfirmStyleListener = (OnConfirmStyleListener) getActivity();
        } catch (ClassCastException e) {
            Log.d("TAG", "onAttach: ClassCastException: " + e.getMessage());
        }

    }
}
