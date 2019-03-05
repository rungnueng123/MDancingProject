package com.mocom.com.mdancingproject.DialogFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mocom.com.mdancingproject.R;

import static com.mocom.com.mdancingproject.config.config.DATA_URL;

public class StudentCoinPackPaymentDialog extends DialogFragment implements View.OnClickListener {

    public interface OnSelectTypePayPackListener {
        void sendOnSelectTypePayPackCoinListener(String typePay, String coinPackID, String baht, String coinAmt);
    }

    public OnSelectTypePayPackListener onSelectTypePayPackListener;
    RadioGroup radioGroup;
    Button btnCancel, btnOk;
    String[] items;
    RadioButton radioButton;
    private String namePack, baht, coinAmt, coinPackID, userID, secret_key;
    private SharedPreferences sharedPreferences;
    String queryGenQrUrl = DATA_URL + "query_for_gen_qr.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_coin_pack_payment_student, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        initFindViewByID(rootView);
        namePack = this.getArguments().getString("namePack");
        baht = this.getArguments().getString("baht");
        coinPackID = this.getArguments().getString("coinPackID");
        coinAmt = this.getArguments().getString("coinAmt");
//        Toast.makeText(getContext(),namePack+' '+baht+' '+coinPackID+' '+coinAmt,Toast.LENGTH_LONG).show();
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
        if (v == btnCancel) {
            getDialog().dismiss();
        }
        if (v == btnOk) {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton selectRadioButton = v.getRootView().findViewById(selectedId);

            onSelectTypePayPackListener.sendOnSelectTypePayPackCoinListener(selectRadioButton.getText().toString(), coinPackID, baht, coinAmt);
            getDialog().dismiss();
//            int selectedId = radioGroup.getCheckedRadioButtonId();
//            RadioButton selectRadioButton = v.getRootView().findViewById(selectedId);
////            Toast.makeText(getContext(),selectRadioButton.getText().toString(),Toast.LENGTH_LONG).show();
//            if (selectRadioButton.getText().toString().equals(getResources().getString(R.string.qr_code))) {
//
//                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//                userID = sharedPreferences.getString(getString(R.string.UserID), "");
//
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, queryGenQrUrl, response -> {
////                    Log.d("Onresponse", response);
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        if (jsonObject.getString("msg").equals("success")) {
//                            JSONArray array = jsonObject.getJSONArray("data");
//                            for (int i = 0; i < array.length(); i++) {
//                                JSONObject obj = array.getJSONObject(i);
//                                secret_key = obj.getString("key");
//                            }
//                            Intent intent = new Intent(getContext(), StudentQRCodeActivity.class);
//                            intent.putExtra("secret_key",secret_key);
//                            intent.putExtra("baht",baht);
//                            intent.putExtra("coinAmt",coinAmt);
//                            startActivity(intent);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }, error -> {
//                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                }) {
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("coinPackID", coinPackID);
//                        params.put("userID", userID);
//
//                        return params;
//                    }
//                };
//                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//                requestQueue.add(stringRequest);
//
//
//
//            } else if (selectRadioButton.getText().toString().equals(getResources().getString(R.string.payment_gateway))) {
//                Intent intent = new Intent(getContext(), PaymentGatewayTestActivity.class);
//                intent.putExtra("coinPackID",coinPackID);
//                startActivity(intent);
//            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onSelectTypePayPackListener = (OnSelectTypePayPackListener) getActivity();
        } catch (ClassCastException e) {
            Log.d("TAG", "onAttach: ClassCastException: " + e.getMessage());
        }

    }
}
