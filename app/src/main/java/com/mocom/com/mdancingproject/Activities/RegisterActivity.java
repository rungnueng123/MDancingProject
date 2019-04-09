package com.mocom.com.mdancingproject.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mocom.com.mdancingproject.config.config.DATA_URL;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";
    String registerUrl = DATA_URL + "register.php";

    private EditText edtUsername, edtEmail, edtPass, edtCpass, edtTel;
    private Button btnRegister, btnBack;
    private TextView txtBirth;
    private Spinner spinnerSex;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    View layoutProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initFindViewByID();
        initInstance();
    }

    private void initFindViewByID() {

        spinnerSex = findViewById(R.id.spinnerSex);
        edtUsername = findViewById(R.id.edt_username);
        edtEmail = findViewById(R.id.edt_email);
        edtPass = findViewById(R.id.edt_pass);
        edtCpass = findViewById(R.id.edt_cpass);
        edtTel = findViewById(R.id.edt_tel);
        txtBirth = findViewById(R.id.txt_birth);
        txtBirth.setOnClickListener(this);
        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
        layoutProgress = findViewById(R.id.layout_progressbar);
    }

    private void initInstance() {

        initSpinnerSex();

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                Log.d(TAG, "date: " + day + "-" + month + "-" + year);
                String date = day + "-" + month + 1 + "-" + year;
                txtBirth.setText(date);
            }
        };

    }

    private void initSpinnerSex() {

        String[] sex = new String[]{
                "MALE",
                "FEMALE"
        };

        final List<String> sexList = new ArrayList<>(Arrays.asList(sex));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_sex_item, sexList) {
            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                    tv.setTextColor(Color.BLACK);
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_sex_item);
        spinnerSex.setAdapter(spinnerArrayAdapter);

        spinnerSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == txtBirth) {
            initDatePicker();
        }
        if (v == btnRegister) {
            checkInputNull();

            String username = edtUsername.getText().toString();
            String email = edtEmail.getText().toString();
            String password = edtPass.getText().toString();
            String phone = edtTel.getText().toString();
            String sex = spinnerSex.getSelectedItem().toString();
            String birth = txtBirth.getText().toString();
            goRegister(username, email, password, phone, sex, birth);
        }
        if(v == btnBack){
            finish();

        }
    }

    private void checkInputNull() {
        if (TextUtils.isEmpty(edtUsername.getText())) {
            edtUsername.setError("User name is required!");
        } else {
            edtUsername.setError(null);
        }
        if (TextUtils.isEmpty(edtEmail.getText())) {
            edtEmail.setError("Email is required!");
        } else {
            edtEmail.setError(null);
        }
        if (TextUtils.isEmpty(edtPass.getText())) {
            edtPass.setError("Password is required!");
        } else {
            edtPass.setError(null);
        }
        if (TextUtils.isEmpty(edtCpass.getText())) {
            edtCpass.setError("Confirm password is required!");
        } else {
            edtCpass.setError(null);
        }
        if (!edtPass.getText().toString().equals(edtCpass.getText().toString())) {
            edtPass.setError("Password is invalid");
            edtCpass.setError("Password is invalid");
            edtPass.setText("");
            edtCpass.setText("");
        } else {
            edtPass.setError(null);
        }
        if (TextUtils.isEmpty(edtTel.getText())) {
            edtTel.setError("Phone is required!");
        } else {
            edtTel.setError(null);
        }
//        if (TextUtils.isEmpty(txtBirth.getText())) {
//            txtBirth.setError("Birth date is required!");
//        } else {
//            txtBirth.setError(null);
//        }
    }

    private void goRegister(String username, String email, String password, String phone, String sex, String birth) {
        layoutProgress.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, registerUrl, response -> {
            Log.d("onResponse", response);
            try {
                JSONObject obj = new JSONObject(response);
                if (obj.getString("message").equals("You have been registered! Please verify your email!")) {
                    layoutProgress.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(obj.getString("message"))
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setNegativeButton("cancel", null);
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    layoutProgress.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(obj.getString("message"))
                            .setNegativeButton("ok", null);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            layoutProgress.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);
                params.put("phone", phone);
                params.put("sex", sex);
                params.put("birth", birth);

                return params;
            }
        };
        requestQueue.add(request);
    }

    private void initDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                RegisterActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                dateSetListener,
                year, month, day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }
}
























