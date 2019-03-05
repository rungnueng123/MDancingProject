package com.mocom.com.mdancingproject.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.mocom.com.mdancingproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.mocom.com.mdancingproject.config.config.DATA_URL;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private static final String EMAIL = "email";
    String loginUrl = DATA_URL + "login.php";
    String loginFBUrl = DATA_URL + "login_fb.php";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private LoginButton btnLoginFB;
    private CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    private TextView txtForgotPass, txtCreateAccount;
    private EditText edtUsername, edtPassword;
    private Button btnLogin, btnFBCustom;
    String UserID, User, Email, GroupID, Groups, firstName = "", lastName = "", email = "", id = "", birthday = "", gender = "";
    private URL profilePicture;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initFindViewByID();
        initInstance();
    }

    private void initFindViewByID() {
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_pass);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        btnLoginFB = findViewById(R.id.btn_login_fb);
        txtForgotPass = findViewById(R.id.txt_forgot);
        txtForgotPass.setOnClickListener(this);
        txtCreateAccount = findViewById(R.id.txt_create_account);
        txtCreateAccount.setOnClickListener(this);
        btnFBCustom = findViewById(R.id.btn_fb_custom);
        btnFBCustom.setOnClickListener(this);

    }

    private void checkLogin() {
        String UserID = sharedPreferences.getString(getString(R.string.UserID), "");
        String Groups = sharedPreferences.getString(getString(R.string.Groups), "");
        if (!UserID.equals("")) {
            if (Groups.equals("student")) {
                Intent intent = new Intent(this, StudentDashboardActivity.class);
                startActivity(intent);
                finish();
            } else if (!Groups.equals("")) {
                Intent intent = new Intent(this, AdminDashboardActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }

    private void initInstance() {

        //login facebook
        callbackManager = CallbackManager.Factory.create();

        FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.e(TAG, object.toString());
                        Log.e(TAG, response.toString());
                        try {
//                            Toast.makeText(getApplicationContext(),object.getString("first_name"),Toast.LENGTH_LONG).show();
                            id = object.getString("id");
                            if (object.has("first_name")) {
                                firstName = object.getString("first_name");
                            }
                            if (object.has("last_name")) {
                                lastName = object.getString("last_name");
                            }
                            if (object.has("email")) {
                                email = object.getString("email");
                            }
//                            if (object.has("gender")){
//                                gender = object.getString("gender");
//                            }
//                            if (object.has("birthday")) {
//                                birthday = object.getString("birthday");
//                            }

                            goMainScreenWithFB(id, firstName, lastName, email, birthday, gender);
//                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "email, first_name, last_name");
//                parameters.putString("fields", "id, first_name, last_name, email, gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("aaa", error.toString());
            }
        };
        btnLoginFB.setReadPermissions("email");
//        btnLoginFB.setReadPermissions("email", "user_birthday", "user_posts", "user_gender");
        btnLoginFB.registerCallback(callbackManager, callback);


        //sharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        checkLogin();

    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            checkInputNull();
            String username = edtUsername.getText().toString();
            String pass = edtPassword.getText().toString();
            goLogin(username, pass);
        }
        if (v == txtCreateAccount) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }
        if (v == txtForgotPass) {
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            startActivity(intent);
            finish();
        }
        if (v == btnFBCustom) {
            btnLoginFB.performClick();
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
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("ProgressDialog"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

        if (!username.isEmpty()) {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest request = new StringRequest(Request.Method.POST, loginUrl, response -> {
//                    Log.d("onResponse",response);
//                    edtCourse.setText("");
//                    Toast.makeText(getActivity(),"เพิ่มข้อมูลแล้วจ้า",Toast.LENGTH_SHORT).show();
                try {
                    //converting response to json object

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("msg").equals("Login finish")) {
//                        Toast.makeText(this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        JSONArray array = jsonObject.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            Log.d("Member name: ", obj.getString("UserID"));
                            UserID = obj.getString("UserID");
                            User = obj.getString("User");
                            Email = obj.getString("Email");
                            GroupID = obj.getString("GroupID");
                            Groups = obj.getString("Groups");
//                            Toast.makeText(getApplicationContext(), UserID, Toast.LENGTH_SHORT).show();
                            editor.putString(getString(R.string.UserID), UserID);
                            editor.putString(getString(R.string.User), User);
                            editor.putString(getString(R.string.Email), Email);
                            editor.putString(getString(R.string.GroupID), GroupID);
                            editor.putString(getString(R.string.Groups), Groups);
                            editor.commit();
//                        courseList.add(item);
                        }
                        progressDialog.dismiss();
                        //TODO
                        if (Groups.equals("student")) {
                            Intent intent = new Intent(this, StudentDashboardActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(this, AdminDashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else if (jsonObject.getString("msg").equals("verify email")) {
                        progressDialog.dismiss();
//                        Toast.makeText(this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(jsonObject.getString("msg"))
                                .setNegativeButton("ok", null);
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        progressDialog.dismiss();
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
                progressDialog.dismiss();
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

    private void goMainScreenWithFB(String id, String firstName, String lastName, String email, String birthday, String gender) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("ProgressDialog"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, loginFBUrl, response -> {
            Log.d("onResponse", response);
            try {
                //converting response to json object

                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("msg").equals("Login finish")) {
//                    Toast.makeText(this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    JSONArray array = jsonObject.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        Log.d("Member name: ", obj.getString("UserID"));
                        UserID = obj.getString("UserID");
                        User = obj.getString("User");
                        Email = obj.getString("Email");
                        GroupID = obj.getString("GroupID");
                        Groups = obj.getString("Groups");
//                            Toast.makeText(getApplicationContext(), UserID, Toast.LENGTH_SHORT).show();
                        editor.putString(getString(R.string.UserID), UserID);
                        editor.putString(getString(R.string.User), User);
                        editor.putString(getString(R.string.Email), Email);
                        editor.putString(getString(R.string.GroupID), GroupID);
                        editor.putString(getString(R.string.Groups), Groups);
                        editor.commit();
//                        courseList.add(item);
                    }
                    progressDialog.dismiss();
                    //TODO
                    if (!Groups.equals("student")) {
                        Intent intent = new Intent(this, AdminDashboardActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(this, StudentDashboardActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
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
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("firstName", firstName);
                params.put("lastName", lastName);
                params.put("email", email);
                params.put("birthday", birthday);
                params.put("gender", gender);

                return params;
            }
        };
        requestQueue.add(request);

    }

    @Override
    public void onResume() {
        checkLogin();
        super.onResume();
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
