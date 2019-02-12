package com.mocom.com.mdancingproject.PaymentGateway;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

public class PaymentGatewayTestActivity extends AppCompatActivity {

    String callWebViewUrl = DATA_URL + "get_url_2c2p.php";
    String coinPackID;
    WebView webView;
    StudentPaymentGatewayDao item;

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
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
//        //webview.setWebViewClient(new MyBrowser());
//        webView.setWebChromeClient(new WebChromeClient());
//        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setUseWideViewPort(true);
//        webView.getSettings().setBuiltInZoomControls(true);
//        webView.setWebViewClient(new SSLTolerentWebViewClient());
//        webView.loadUrl("https://danceschool.matchbox-station.com/MDancingPHP/get_url_2c2p.php?coinPackID=1");
//        callWebViewShow();

    }

    private void initFindViewByID() {
        webView = findViewById(R.id.web_view);
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
                params.put("coinPackID", coinPackID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

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
