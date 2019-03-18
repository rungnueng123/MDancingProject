package com.mocom.com.mdancingproject.DialogFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mocom.com.mdancingproject.R;

public class CheckedAlreadyDialog extends DialogFragment implements View.OnClickListener {

    TextView txtClose;

    public static CheckedAlreadyDialog newInstance() {
        CheckedAlreadyDialog fragment = new CheckedAlreadyDialog();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_checked_already, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        initFindViewByID(rootView);

    }

    private void initFindViewByID(View rootView) {
        txtClose = rootView.findViewById(R.id.txt_close);
        txtClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == txtClose){
            getDialog().dismiss();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
