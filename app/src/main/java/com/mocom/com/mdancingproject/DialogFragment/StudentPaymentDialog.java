package com.mocom.com.mdancingproject.DialogFragment;

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

public class StudentPaymentDialog extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "StudentPaymentDialog";
    private TextView actionBuy, actionCancel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_payment_student, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        initFindViewByID(rootView);
    }

    private void initFindViewByID(View rootView) {
        actionBuy = rootView.findViewById(R.id.action_buy);
        actionCancel = rootView.findViewById(R.id.action_cancel);
        actionBuy.setOnClickListener(this);
        actionCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == actionBuy){
            Log.d(TAG, "onClick: capturing input");
        }
        if(v == actionCancel){
            Log.d(TAG, "onClick: closing dialog");
        }
    }
}
