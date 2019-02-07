package com.mocom.com.mdancingproject.DialogFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mocom.com.mdancingproject.R;

import java.util.ArrayList;

public class StudentCoinPackPaymentDialog extends DialogFragment implements View.OnClickListener {

    RadioGroup radioGroup;
    Button btnCancel, btnOk;
    ArrayList<String> typePayList = new ArrayList<>();
    String[] items;
    RadioButton radioButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_coin_pack_payment_student, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        initFindViewByID(rootView);
//        createRadioButton(rootView);

    }

    private void createRadioButton(View rootView) {
        items = getResources().getStringArray(R.array.type_pay);
        for (int i = 0; i < items.length; i++) {
            String item = items[i];
            radioButton = new RadioButton(getContext());
            radioButton.setText(item);
            radioGroup.addView(radioButton);
        }
    }

    private void initFindViewByID(View rootView) {
        radioGroup = rootView.findViewById(R.id.radio_group);
        btnCancel = rootView.findViewById(R.id.btn_cancel);
        btnOk = rootView.findViewById(R.id.btn_ok);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnCancel){
            getDialog().dismiss();
        }
        if(v == btnOk){
            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton selectRadioButton = v.getRootView().findViewById(selectedId);
//            Toast.makeText(getContext(),selectRadioButton.getText().toString(),Toast.LENGTH_LONG).show();
            if(selectRadioButton.getText().toString().equals("PAYMENT GATEWAY")){
                Toast.makeText(getContext(),selectRadioButton.getText().toString(),Toast.LENGTH_LONG).show();
            }
        }
    }
}
