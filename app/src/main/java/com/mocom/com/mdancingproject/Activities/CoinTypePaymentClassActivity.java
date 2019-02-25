package com.mocom.com.mdancingproject.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mocom.com.mdancingproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.mocom.com.mdancingproject.Activities.ClassDetailActivity.COIN_CAN_PAY_CODE;
import static com.mocom.com.mdancingproject.config.config.DATA_URL;

public class CoinTypePaymentClassActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CoinTypePaymentClassActivity";

    String checkCoinUrl = DATA_URL + "check_coin_for_payment.php";
    String coin, eventID, eventName, userID, message;
    TextView txtClass, txtCoin, actionBuy, actionCancel;
    private SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_type_payment_class);

        initFindViewById();

        Intent intent = getIntent();
        coin = intent.getStringExtra("coin");
        eventID = intent.getStringExtra("eventID");
        eventName = intent.getStringExtra("eventName");
//        Toast.makeText(getApplicationContext(), coin+eventName+eventID, Toast.LENGTH_SHORT).show();
        txtClass.setText(eventName);
        txtCoin.setText(coin);

        initInstance();
    }

    private void initInstance() {

    }

    private void initFindViewById() {
        txtClass = findViewById(R.id.txt_class_name);
        txtCoin = findViewById(R.id.txt_coin_price);
        actionBuy = findViewById(R.id.action_buy);
        actionCancel = findViewById(R.id.action_cancel);
        actionBuy.setOnClickListener(this);
        actionCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == actionCancel) {
            message = "Cancel";
            Intent intent = new Intent();
            intent.putExtra("message", message);
            setResult(1, intent);
            finish();//finishing activity
        }
        if (v == actionBuy) {
            checkCoinCanPayment();
        }
    }

    private void checkCoinCanPayment() {
//        progressDialog = new ProgressDialog(getApplicationContext());
//        progressDialog.setMessage("Loading..."); // Setting Message
//        progressDialog.setTitle("ProgressDialog"); // Setting Title
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
//        progressDialog.show(); // Display Progress Dialog
//        progressDialog.setCancelable(false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userID = sharedPreferences.getString(getString(R.string.UserID), "");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, checkCoinUrl, response -> {
            Log.d("response", response);
            try {
                JSONObject obj = new JSONObject(response);
//                if (obj.getString("message").equals("Your coin don't enough! Please go to shop!")) {
////                    Toast.makeText(this, "aaa", Toast.LENGTH_SHORT).show();
//                } else {
////                    goBuyClass(userID, eventID);
//                }

                Intent intent = new Intent();
                intent.putExtra("message",obj.getString("message"));
                setResult(COIN_CAN_PAY_CODE,intent);

                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//            progressDialog.dismiss();
            message = "Cancel";
            Intent intent = new Intent();
            intent.putExtra("message", message);//Put Message to pass over intent
            setResult(1, intent);//Set result OK
            finish();//finish activity
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("coin", coin);
                params.put("userID", userID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
