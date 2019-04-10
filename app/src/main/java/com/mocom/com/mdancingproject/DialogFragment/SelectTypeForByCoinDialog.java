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

public class SelectTypeForByCoinDialog extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "SelectTypeForByCoinDialog";

    public interface OnSelectTypeForByCoinListener {
        void sendSelectTypeForByCoin(String input);
    }

    public OnSelectTypeForByCoinListener onSelectTypeForByCoinListener;
    RadioGroup radioGroup;
    TextView txtCancel, txtOk;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_select_type_for_by_coin, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        initFindViewByID(rootView);
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
            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton selectRadioButton = v.getRootView().findViewById(selectedId);

            onSelectTypeForByCoinListener.sendSelectTypeForByCoin(selectRadioButton.getText().toString());
            getDialog().dismiss();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onSelectTypeForByCoinListener = (SelectTypeForByCoinDialog.OnSelectTypeForByCoinListener) getActivity();
        } catch (ClassCastException e) {
            Log.d("TAG", "onAttach: ClassCastException: " + e.getMessage());
        }

    }

}
