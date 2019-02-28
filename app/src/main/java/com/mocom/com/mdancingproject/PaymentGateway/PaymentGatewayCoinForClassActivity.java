package com.mocom.com.mdancingproject.PaymentGateway;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.mocom.com.mdancingproject.R;

import static com.mocom.com.mdancingproject.config.config.DATA_URL;
import static com.mocom.com.mdancingproject.config.config.OMISE_API_VERSION;
import static com.mocom.com.mdancingproject.config.config.OMISE_PUBLIC_KEY;
import static com.mocom.com.mdancingproject.config.config.OMISE_SECRET_KEY;

public class PaymentGatewayCoinForClassActivity extends AppCompatActivity implements View.OnClickListener {

    String callWebViewUrl = DATA_URL + "get_url_omise.php";
    String userID, eventID;
    WebView webView;
    Button btnFinishPGW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway_coin_for_class);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        eventID = intent.getStringExtra("eventID");

        initInstance();
    }

    private void initInstance() {
        initFindViewByID();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        //webview.setWebViewClient(new MyBrowser());
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(new SSLTolerentWebViewClient());
        webView.loadUrl(callWebViewUrl + "?userID="+userID+"&eventID="+eventID+"&public_key_omise="+OMISE_PUBLIC_KEY+"&secret_key_omise="+OMISE_SECRET_KEY+"&api_version_omise="+OMISE_API_VERSION);

    }

    private void initFindViewByID() {
        webView = findViewById(R.id.web_view);
        btnFinishPGW = findViewById(R.id.btn_pgw_finish);
        btnFinishPGW.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnFinishPGW){
            Intent intent = new Intent();
            intent.putExtra("message","finish");
            setResult(3,intent);
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
