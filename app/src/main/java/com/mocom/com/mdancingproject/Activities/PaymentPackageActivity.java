package com.mocom.com.mdancingproject.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mocom.com.mdancingproject.DialogFragment.CoinDontEnoughDialog;
import com.mocom.com.mdancingproject.DialogFragment.ConfirmPayStyleDialog;
import com.mocom.com.mdancingproject.DialogFragment.PaymentClassDialog;
import com.mocom.com.mdancingproject.DialogFragment.StudentCoinPackPaymentDialog;
import com.mocom.com.mdancingproject.Fragments.StudentCoinFragment;
import com.mocom.com.mdancingproject.PaymentGateway.PaymentGatewayTestActivity;
import com.mocom.com.mdancingproject.QRCode.StudentQRCodeActivity;
import com.mocom.com.mdancingproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.mocom.com.mdancingproject.config.config.DATA_URL;

public class PaymentPackageActivity extends AppCompatActivity implements StudentCoinPackPaymentDialog.OnSelectTypePayPackListener, CoinDontEnoughDialog.OnBackListener, PaymentClassDialog.OnCancelBuyListener, ConfirmPayStyleDialog.OnConfirmStyleListener {

    public static final int PAY_PACKAGE = 2;

    private SharedPreferences sharedPreferences;
    private String namePack, baht, coinAmt, secret_key;
    String queryGenQrUrl = DATA_URL + "query_for_gen_qr.php";
    Toolbar toolbar;
    String sharedUserID;



    @Override
    public void sendConfirmStyle(String confirm, String stylePackID, String coin) {
        if (confirm.equals(getResources().getString(R.string.cancel))){
            Toast.makeText(this, confirm, Toast.LENGTH_LONG).show();
        }
        if (confirm.equals(getResources().getString(R.string.confirm))){
            Toast.makeText(this, confirm, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void sendOnCancelBuyListener(String cancel) {
        if (cancel.equals(getResources().getString(R.string.cancel))){
            Toast.makeText(this, cancel, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void sendOnBackListener(String close) {
        if (close.equals(getResources().getString(R.string.close))){
            Toast.makeText(this, close, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void sendOnSelectTypePayPackCoinListener(String typePay, String coinPackID) {
        if(typePay.equals(getResources().getString(R.string.payment_gateway))){
            Intent intent = new Intent(getApplicationContext(), PaymentGatewayTestActivity.class);
            intent.putExtra("coinPackID", coinPackID);
            startActivityForResult(intent,PAY_PACKAGE);
        } else if(typePay.equals(getResources().getString(R.string.qr_code))){
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            sharedUserID = sharedPreferences.getString(getString(R.string.UserID), "");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, queryGenQrUrl, response -> {
//                    Log.d("Onresponse", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("msg").equals("success")) {
                        JSONArray array = jsonObject.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            secret_key = obj.getString("key");
                        }
                        Intent intent = new Intent(getApplicationContext(), StudentQRCodeActivity.class);
                        intent.putExtra("secret_key", secret_key);
                        intent.putExtra("baht", baht);
                        intent.putExtra("coinAmt", coinAmt);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("coinPackID", coinPackID);
                    params.put("userID", sharedUserID);

                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_package);

        initFindViewById();
        initInstance(savedInstanceState);
    }

    private void initFindViewById() {
        toolbar = findViewById(R.id.toolbar_payment_package);
    }

    private void initInstance(Bundle savedInstanceState) {
        initToolbar();

        if (savedInstanceState == null){
//            Bundle bundle = new Bundle();
//            bundle.putString("edttext", PAY_PACKAGE);
//            Fragment studentCoinFragment = new StudentCoinFragment();
//            studentCoinFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.payment_package_container, StudentCoinFragment.newInstance())
                    .commit();
        }
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PAY_PACKAGE){
//            Toast.makeText(this, "aaa", Toast.LENGTH_SHORT).show();
        }
    }
}
