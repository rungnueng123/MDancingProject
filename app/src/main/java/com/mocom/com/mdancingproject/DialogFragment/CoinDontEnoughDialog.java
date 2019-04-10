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

public class CoinDontEnoughDialog extends DialogFragment implements View.OnClickListener {

    TextView txtClose,txtShop;
    public OnBackListener onBackListener;

    public interface OnBackListener {
        void sendOnBackListener(String close);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_coin_dont_enough, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        initFindViewByID(rootView);

    }

    private void initFindViewByID(View rootView) {
        txtShop = rootView.findViewById(R.id.txt_go_shop);
        txtShop.setOnClickListener(this);
        txtClose = rootView.findViewById(R.id.txt_close);
        txtClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == txtClose){
            onBackListener.sendOnBackListener(txtClose.getText().toString());
            getDialog().dismiss();
        }
        if(v == txtShop){
            onBackListener.sendOnBackListener(txtShop.getText().toString());
            getDialog().dismiss();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onBackListener = (CoinDontEnoughDialog.OnBackListener) getActivity();
        } catch (ClassCastException e) {
            Log.d("TAG", "onAttach: ClassCastException: " + e.getMessage());
        }

    }
}
