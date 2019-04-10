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

public class CoinTypePaymentClassDialog extends DialogFragment implements View.OnClickListener {

    public OnBackCoinTypePaymentClassListener onBackCoinTypePaymentClassListener;
    TextView txtCancel, txtBuy, txtClass, txtCoin;
    String coin, eventID, eventName, userID, message;

    public interface OnBackCoinTypePaymentClassListener {
        void sendOnBackCoinTypePaymentClassListener(String back, String coin, String eventID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_coin_type_payment_class, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        initFindViewByID(rootView);
        coin = this.getArguments().getString("coin");
        eventID = this.getArguments().getString("eventID");
        eventName = this.getArguments().getString("eventName");

        txtClass.setText(eventName);
        txtCoin.setText(coin);

    }

    private void initFindViewByID(View rootView) {
        txtClass = rootView.findViewById(R.id.txt_class_name);
        txtCoin = rootView.findViewById(R.id.txt_coin_price);
        txtCancel = rootView.findViewById(R.id.action_cancel);
        txtBuy = rootView.findViewById(R.id.action_buy);
        txtCancel.setOnClickListener(this);
        txtBuy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == txtBuy) {
            onBackCoinTypePaymentClassListener.sendOnBackCoinTypePaymentClassListener(txtBuy.getText().toString(), coin, eventID);
            getDialog().dismiss();
        }
        if (v == txtCancel) {
            onBackCoinTypePaymentClassListener.sendOnBackCoinTypePaymentClassListener(txtCancel.getText().toString(), coin, eventID);
            getDialog().dismiss();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onBackCoinTypePaymentClassListener = (OnBackCoinTypePaymentClassListener) getActivity();
        } catch (ClassCastException e) {
            Log.d("TAG", "onAttach: ClassCastException: " + e.getMessage());
        }

    }
}
