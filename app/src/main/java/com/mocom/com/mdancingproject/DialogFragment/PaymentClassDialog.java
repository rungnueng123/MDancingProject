package com.mocom.com.mdancingproject.DialogFragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mocom.com.mdancingproject.Activities.ClassDetailActivity;
import com.mocom.com.mdancingproject.Activities.StudentDashboardActivity;
import com.mocom.com.mdancingproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.mocom.com.mdancingproject.config.config.DATA_URL;

public class PaymentClassDialog extends DialogFragment implements View.OnClickListener {

    public ClassDetailActivity activity;

    private static final String TAG = "StudentPaymentDialog";
    String checkCoinUrl = DATA_URL + "check_coin_for_payment.php";
    String canBuyClassUrl = DATA_URL + "buy_class_by_coin.php";
    private TextView actionBuy, actionCancel, txtCoinPrice, txtClassName;
    private String coin, eventID, userID, eventName;
    private SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_payment_class, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        initFindViewByID(rootView);

        coin = this.getArguments().getString("coin");
        eventID = this.getArguments().getString("eventID");
        eventName = this.getArguments().getString("eventName");

        txtCoinPrice.setText(coin);
        txtClassName.setText(eventName);



    }

    private void initFindViewByID(View rootView) {
        actionBuy = rootView.findViewById(R.id.action_buy);
        actionCancel = rootView.findViewById(R.id.action_cancel);
        txtCoinPrice = rootView.findViewById(R.id.txt_coin_price);
        txtClassName = rootView.findViewById(R.id.txt_class_name);
        actionBuy.setOnClickListener(this);
        actionCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == actionBuy) {
//            Log.d(TAG, "onClick: capturing input");
            checkCoinCanPayment();
        }
        if (v == actionCancel) {
//            Log.d(TAG, "onClick: closing dialog");
            getDialog().dismiss();
        }
    }

    private void checkCoinCanPayment() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("ProgressDialog"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userID = sharedPreferences.getString(getString(R.string.UserID), "");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, checkCoinUrl, response -> {
//            Log.d("response", response);
            try {
                JSONObject obj = new JSONObject(response);
                if (obj.getString("message").equals("Your coin don't enough! Please go to shop!")) {
                    progressDialog.dismiss();
                    if (getActivity() != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(obj.getString("message"))
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(getActivity(), StudentDashboardActivity.class);
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra("goBuyCoin", "goBuyCoin");
                                        startActivity(intent);
                                    }
                                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getDialog().dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                } else {
                    goBuyClass(userID, eventID);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            getDialog().dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("coin", coin);
                params.put("userID", userID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void goBuyClass(String userID, String eventID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, canBuyClassUrl, response -> {
            Log.d("response", response);
            try {
                JSONObject obj = new JSONObject(response);
                if (obj.getString("message").equals("Payment Success")) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(getActivity(), StudentDashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("goProfile", "goProfile");
                    startActivity(intent);
                } else {
                    goBuyClass(userID, eventID);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            progressDialog.dismiss();
        }, error -> {
            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("eventID", eventID);
                params.put("userID", userID);
                params.put("coin", coin);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
//        Intent intent = new Intent(getActivity(), StudentDashboardActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK  |  Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
    }
}
