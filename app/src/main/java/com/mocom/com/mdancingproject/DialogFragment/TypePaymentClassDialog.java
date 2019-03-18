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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mocom.com.mdancingproject.R;

public class TypePaymentClassDialog extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "TypePaymentClassDialog";

    public interface OnInputListener {
        void sendInput(String input);
    }

    public OnInputListener onInputListener;
    private String coin, eventID, userID, eventName;
    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView txtCancel, txtOk;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_type_payment_class, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        initFindViewByID(rootView);

        coin = this.getArguments().getString("coin");
        eventID = this.getArguments().getString("eventID");
        eventName = this.getArguments().getString("eventName");
    }

    private void initFindViewByID(View rootView) {
        radioGroup = rootView.findViewById(R.id.radio_group);
        txtCancel = rootView.findViewById(R.id.txt_cancel);
        txtOk = rootView.findViewById(R.id.txt_ok);
        txtOk.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == txtCancel) {
            getDialog().dismiss();
        }
        if (v == txtOk) {
//            Toast.makeText(getContext(), "aaa", Toast.LENGTH_SHORT).show();
            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton selectRadioButton = v.getRootView().findViewById(selectedId);

//            ((ClassDetailActivity) getActivity()).txtTypePay.setText(selectRadioButton.getText().toString());

            onInputListener.sendInput(selectRadioButton.getText().toString());
            getDialog().dismiss();

//            if (selectRadioButton.getText().toString().equals(getResources().getString(R.string.txt_type_payment_coin))) {
//                Intent intent = new Intent(getActivity(), CoinTypePaymentClassActivity.class);
//                intent.putExtra("coin",coin);
//                intent.putExtra("eventID",eventID);
//                intent.putExtra("eventName",eventName);
//                startActivityForResult(intent, COIN_PAY_CODE);
//                getDialog().dismiss();
//            } else if (selectRadioButton.getText().toString().equals(getResources().getString(R.string.txt_type_payment_style_pack))) {
//
//            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onInputListener = (OnInputListener) getActivity();
        } catch (ClassCastException e) {
            Log.d("TAG", "onAttach: ClassCastException: " + e.getMessage());
        }

    }
}
