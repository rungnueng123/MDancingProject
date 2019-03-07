package com.mocom.com.mdancingproject.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import static com.mocom.com.mdancingproject.config.config.DATA_URL;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ForgotPasswordActivity";
    String requestUrl = DATA_URL + "forgot_pass.php";

    private EditText edtEmail;
    private Button btnRequest, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initFindViewID();
    }

    private void initFindViewID() {
        edtEmail = findViewById(R.id.edt_email);
        btnRequest = findViewById(R.id.btn_send_request);
        btnRequest.setOnClickListener(this);
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == btnRequest){
            checkInputNull();
            String email = edtEmail.getText().toString();
            goSendRequest(email);
        }
        if(v == btnBack){
            Intent intent = new Intent(this,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void goSendRequest(String email) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST,requestUrl,response -> {
            Log.d(TAG,response);
            try {
                JSONObject obj = new JSONObject(response);
                if (obj.getString("message").equals("You have been requested! Please check your email!")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(obj.getString("message"))
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setNegativeButton("cancel", null);
                    AlertDialog alert = builder.create();
                    alert.show();
                } else if (obj.getString("message").equals("Something wrong happened! Please try again!")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(obj.getString("message"))
                            .setPositiveButton("send again", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    goSendRequest(edtEmail.getText().toString());
                                }
                            })
                            .setNegativeButton("cancel", null);
                    AlertDialog alert = builder.create();
                    alert.show();
                }else if (obj.getString("message").equals("Your Email Address is not registered")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(obj.getString("message"))
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    edtEmail.setText("");
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        },error -> {
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);

                return params;
            }
        };
        requestQueue.add(request);

    }

    private void checkInputNull() {
        if (TextUtils.isEmpty(edtEmail.getText())) {
            edtEmail.setError("Email is required!");
        } else {
            edtEmail.setError(null);
        }
    }
}

































