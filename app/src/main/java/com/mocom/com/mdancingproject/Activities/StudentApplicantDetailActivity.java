package com.mocom.com.mdancingproject.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.mocom.com.mdancingproject.DialogFragment.CancelAlreadyDialog;
import com.mocom.com.mdancingproject.DialogFragment.CheckedAlreadyDialog;
import com.mocom.com.mdancingproject.QRCode.QRCodeCheckedStudentActivity;
import com.mocom.com.mdancingproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.mocom.com.mdancingproject.config.config.DATA_URL;
import static com.mocom.com.mdancingproject.config.config.HOST_URL;

public class StudentApplicantDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private String dataUrl = DATA_URL + "get_class_for_applicant.php";
    View layoutProgress;

    String eventID, active, userID;
    Toolbar toolbar;
    ImageView imgClass;
    TextView txtTitle, txtPlaylist, txtStyle, txtTeacher, txtDate, txtTime, txtRoom, txtBranch, txtStatus, txtDesc, txtActive;
    Button btnQrCheck;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_applicant_detail);

        Intent intent = getIntent();
        eventID = intent.getStringExtra("eventID");
        active = intent.getStringExtra("active");

        initInstance();
    }

    private void initInstance() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userID = sharedPreferences.getString(getString(R.string.UserID), "");
        initFindViewByID();
        initToolbar();
        loadClassDetail();
    }

    private void loadClassDetail() {
        layoutProgress.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, dataUrl, response -> {
            Log.d("onResponse", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("msg").equals("success")) {
                    JSONArray array = jsonObject.getJSONArray("class");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        if (!obj.getString("eventTitle").equals("null")) {
                            txtTitle.setText(obj.getString("eventTitle"));
                        }
                        if (!obj.getString("playlist").equals("null")) {
                            txtPlaylist.setText(obj.getString("playlist"));
                        }
                        if (!obj.getString("eventStyle").equals("null")) {
                            txtStyle.setText(obj.getString("eventStyle"));
                        }
                        if (!obj.getString("eventTeacher").equals("null")) {
                            txtTeacher.setText(obj.getString("eventTeacher"));
                        }
                        if (!obj.getString("eventDate").equals("null")) {
                            txtDate.setText(obj.getString("eventDate"));
                        }
                        if (!obj.getString("eventTime").equals("null")) {
                            txtTime.setText(obj.getString("eventTime"));
                        }
                        if (!obj.getString("eventRoom").equals("null")) {
                            txtRoom.setText(obj.getString("eventRoom"));
                        }
                        if (!obj.getString("eventBranch").equals("null")) {
                            txtBranch.setText(obj.getString("eventBranch"));
                        }
                        if (!obj.getString("eventStatus").equals("null")) {
                            txtStatus.setText(obj.getString("eventStatus"));
                        }
                        if (!obj.getString("description").equals("null")) {
                            txtDesc.setText(obj.getString("description"));
                        }
                        if (!obj.getString("imgUrl").equals("null")) {
                            String imgUrl = HOST_URL + obj.getString("imgUrl");
                            Glide.with(getApplicationContext())
                                    .load(imgUrl)
                                    .into(imgClass);
                        }
                        if (!obj.getString("Active").equals("null")) {
                            txtActive.setText(obj.getString("Active"));
                        }
                    }
                }
                layoutProgress.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
                params.put("eventID", eventID);
                params.put("userID", userID);
                params.put("active", active);

                return params;
            }
        };
        requestQueue.add(request);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initFindViewByID() {
        toolbar = findViewById(R.id.toolbar_class_detail);
        imgClass = findViewById(R.id.img_class);
        txtTitle = findViewById(R.id.txt_title);
        txtPlaylist = findViewById(R.id.txt_playlist);
        txtStyle = findViewById(R.id.txt_style);
        txtTeacher = findViewById(R.id.txt_teacher);
        txtDate = findViewById(R.id.txt_date);
        txtTime = findViewById(R.id.txt_time);
        txtRoom = findViewById(R.id.txt_room);
        txtBranch = findViewById(R.id.txt_branch);
        txtStatus = findViewById(R.id.txt_status);
        txtDesc = findViewById(R.id.txt_desc);
        txtActive = findViewById(R.id.txt_active);
        btnQrCheck = findViewById(R.id.btn_qr_check);
        btnQrCheck.setOnClickListener(this);
        layoutProgress = findViewById(R.id.layout_progressbar);
    }

    @Override
    public void onClick(View v) {
        if (v == btnQrCheck) {
            if (txtActive.getText().toString().equals("1")) {
                Intent intent = new Intent(this, QRCodeCheckedStudentActivity.class);
                intent.putExtra("eventID", eventID);
                intent.putExtra("userID", userID);
                startActivity(intent);
            } else if(txtActive.getText().toString().equals("0")) {
                openDialogChecked();
            }else{
                openDialogCancel();
            }
        }
    }

    private void openDialogCancel() {
        CancelAlreadyDialog dialog = new CancelAlreadyDialog();
        dialog.show(getSupportFragmentManager(), "CancelAlreadyDialog");
    }

    private void openDialogChecked() {
        CheckedAlreadyDialog dialog = new CheckedAlreadyDialog();
        dialog.show(getSupportFragmentManager(), "CheckedAlreadyDialog");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadClassDetail();
    }
}
