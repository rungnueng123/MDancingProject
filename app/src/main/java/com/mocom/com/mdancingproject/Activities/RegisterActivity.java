package com.mocom.com.mdancingproject.Activities;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
    private Button btnRegister;
    private TextView txtBirth;
    private Spinner spinnerSex;
    private DatePickerDialog.OnDateSetListener dateSetListener;

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
    }

    private void initInstance() {

        initSpinnerSex();

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "date: " + day + "-" + month + "-" + year);
                String date = day + "-" + month + "-" + year;
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
                if (position % 2 == 1) {
                    // Set the item background color
                    tv.setTextColor(Color.BLACK);
                } else {
                    // Set the alternate item background color
                    tv.setBackgroundColor(Color.parseColor("#d3cbcb"));
                    tv.setTextColor(Color.BLACK);
                }
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
            goRegister(username,email,password,phone,sex,birth);
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
        if (TextUtils.isEmpty(txtBirth.getText())) {
            txtBirth.setError("Birth date is required!");
        } else {
            txtBirth.setError(null);
        }
    }

    private void goRegister(String username, String email, String password, String phone, String sex, String birth) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, registerUrl, response -> {

        }, error -> {
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
























