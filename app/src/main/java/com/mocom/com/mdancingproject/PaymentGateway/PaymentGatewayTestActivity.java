package com.mocom.com.mdancingproject.PaymentGateway;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.SslError;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mocom.com.mdancingproject.Dao.StudentPaymentGatewayDao;
import com.mocom.com.mdancingproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.mocom.com.mdancingproject.config.config.DATA_URL;
import static com.mocom.com.mdancingproject.config.config.OMISE_API_VERSION;
import static com.mocom.com.mdancingproject.config.config.OMISE_PUBLIC_KEY;
import static com.mocom.com.mdancingproject.config.config.OMISE_SECRET_KEY;

public class PaymentGatewayTestActivity extends AppCompatActivity implements View.OnClickListener {

    String callWebViewUrl = DATA_URL + "get_url_omise.php";
    String coinPackID, userID;
    WebView webView;
    Button btnFinishPGW;
    StudentPaymentGatewayDao item;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway_test);

        Intent intent = getIntent();
        coinPackID = intent.getStringExtra("coinPackID");

        initInstance();
    }

    private void initInstance() {
        initFindViewByID();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userID = sharedPreferences.getString(getString(R.string.UserID), "");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        //webview.setWebViewClient(new MyBrowser());
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(new SSLTolerentWebViewClient());
        webView.loadUrl(callWebViewUrl + "?userID="+userID+"&coinPackID="+coinPackID+"&public_key_omise="+OMISE_PUBLIC_KEY+"&secret_key_omise="+OMISE_SECRET_KEY+"&api_version_omise="+OMISE_API_VERSION);
//        callWebViewShow();

    }

    private void initFindViewByID() {
        webView = findViewById(R.id.web_view);
        btnFinishPGW = findViewById(R.id.btn_pgw_finish);
        btnFinishPGW.setOnClickListener(this);

    }

    private void callWebViewShow() {

        webView = new WebView(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, callWebViewUrl, response -> {
            Log.d("onresponse", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("msg").equals("Success")) {
                    JSONArray array = jsonObject.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        item = new StudentPaymentGatewayDao(
                                obj.getString("paymentUrl"),
                                obj.getString("version"),
                                obj.getString("merchantId"),
                                obj.getString("currency"),
                                obj.getString("resultUrl1"),
                                obj.getString("hashValue"),
                                obj.getString("paymentDesc"),
                                obj.getString("orderId"),
                                obj.getDouble("amount")
                        );
//                        requestPayment();
                        webView.loadUrl(obj.getString("paymentUrl"));
//                        String postData = "version="+item.getVersion()
//                                +"&merchantId="+item.getMerchantId()
//                                +"&currency="+item.getCurrency()
//                                +"&resultUrl1="+item.getResultUrl1()
//                                +"&hashValue="+item.getHashValue()
//                                +"&paymentDesc="+item.getPaymentDesc()
//                                +"&orderId="+item.getOrderId()
//                                +"&amount="+item.getAmount();
////
//                        webView.postUrl(item.getPaymentUrl(),postData.getBytes());
                    }
                } else {
                    callWebViewShow();
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
                params.put("userID", userID);
                params.put("coinPackID", coinPackID);
                params.put("public_key_omise", OMISE_PUBLIC_KEY);
                params.put("secret_key_omise", OMISE_SECRET_KEY);
                params.put("api_version_omise", OMISE_API_VERSION);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    @Override
    public void onClick(View v) {
        if(v == btnFinishPGW){
            Intent intent = new Intent();
            intent.putExtra("message","goProfile");
            setResult(2,intent);
            finish();
        }
    }

    private class SSLTolerentWebViewClient extends WebViewClient {
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
            builder.setMessage(R.string.ok);
            builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                    finish();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
