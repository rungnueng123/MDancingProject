package com.mocom.com.mdancingproject.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mocom.com.mdancingproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.mocom.com.mdancingproject.config.config.DATA_URL;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    String loginUrl = DATA_URL + "login.php";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private EditText edtUsername, edtPassword;
    private Button btnLogin, btnSignUp;
    private CheckBox chkRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initInstance();
    }

    private void checkLogin() {
        String UserID = sharedPreferences.getString(getString(R.string.UserID),"");
        if(!UserID.equals("")){
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void initInstance() {
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_pass);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        btnSignUp = findViewById(R.id.btn_signup);
        btnSignUp.setOnClickListener(this);
        chkRemember = findViewById(R.id.chk_remember);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        sharedPreferences = getSharedPreferences("dancing",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        checkLogin();

//        editor.putString("key", "mitch");
//        editor.commit();
//
//        String name = sharedPreferences.getString("key","default");
//        Log.d(TAG,"onCreate: name: "+name);

    }

    private void checkSharedPreferences() {
        String checkbox = sharedPreferences.getString(getString(R.string.checkbox), "False");
        String username = sharedPreferences.getString(getString(R.string.username), "");
        String password = sharedPreferences.getString(getString(R.string.password), "");

        edtUsername.setText(username);
        edtPassword.setText(password);
        if (checkbox.equals("True")) {
            chkRemember.setChecked(true);
        } else {
            chkRemember.setChecked(false);
        }

    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            checkInputNull();
            String username = edtUsername.getText().toString();
            String pass = edtPassword.getText().toString();
            goLogin(username, pass);
            if (chkRemember.isChecked()) {
                editor.putString(getString(R.string.checkbox), "True");
                editor.commit();

                editor.putString(getString(R.string.username), username);

                editor.putString(getString(R.string.password), pass);
            } else {
                editor.putString(getString(R.string.checkbox), "False");
                editor.commit();

                editor.putString(getString(R.string.username), "");

                editor.putString(getString(R.string.password), "");
            }
        }
        if(v == btnSignUp){
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void checkInputNull() {
        if (TextUtils.isEmpty(edtUsername.getText())) {
            edtUsername.setError("User name is required!");
        } else {
            edtUsername.setError(null);
        }
        if (TextUtils.isEmpty(edtPassword.getText())) {
            edtPassword.setError("Password is required!");
        } else {
            edtPassword.setError(null);
        }
    }

    private void goLogin(String username, String pass) {
        if (!username.isEmpty()) {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest request = new StringRequest(Request.Method.POST, loginUrl, response -> {
//                    Log.d("onResponse",response);
//                    edtCourse.setText("");
//                    Toast.makeText(getActivity(),"เพิ่มข้อมูลแล้วจ้า",Toast.LENGTH_SHORT).show();
                try {
                    //converting response to json object

                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("msg").equals("Login finish")) {
                        Toast.makeText(this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        JSONArray array = jsonObject.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            Log.d("Member name: ", obj.getString("UserID"));
                            String UserID = obj.getString("UserID");
                            String User = obj.getString("User");
                            String Email = obj.getString("Email");
                            String GroupID = obj.getString("GroupID");
                            String Groups = obj.getString("Groups");
//                            Toast.makeText(getApplicationContext(), UserID, Toast.LENGTH_SHORT).show();
                            editor.putString(getString(R.string.UserID), UserID);
                            editor.putString(getString(R.string.User), User);
                            editor.putString(getString(R.string.Email), Email);
                            editor.putString(getString(R.string.GroupID), GroupID);
                            editor.putString(getString(R.string.Groups), Groups);
                            editor.commit();
//                        courseList.add(item);
                        }
                        Intent intent = new Intent(this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    }else if(jsonObject.getString("msg").equals("verify email")){
//                        Toast.makeText(this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(jsonObject.getString("msg"))
                                .setNegativeButton("ok", null);
                        AlertDialog alert = builder.create();
                        alert.show();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(jsonObject.getString("msg"))
                                .setNegativeButton("ok", null);
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {
//                    Log.d("onError", error.toString());
//                    Toast.makeText(getActivity(), "เกิดข้อผิดพลาดโปรดลองอีกครั้ง", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", username);
                    params.put("password", pass);

                    return params;
                }
            };
            requestQueue.add(request);

        }
    }
}
