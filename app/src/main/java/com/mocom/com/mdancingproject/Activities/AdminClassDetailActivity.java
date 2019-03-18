package com.mocom.com.mdancingproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.mocom.com.mdancingproject.Adapter.NameCheckedAdapter;
import com.mocom.com.mdancingproject.Dao.NameCheckedDao;
import com.mocom.com.mdancingproject.QRCode.AdminQRCodeScannedActivity;
import com.mocom.com.mdancingproject.R;
import com.mocom.com.mdancingproject.config.config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mocom.com.mdancingproject.config.config.DATA_URL;

public class AdminClassDetailActivity extends AppCompatActivity implements View.OnClickListener {

    View layoutProgress;

    private String getClassUrl = DATA_URL + "get_admin_single_class.php";
    private String StudentCheckedUrl = DATA_URL + "get_student_check_by_event.php";
    String eventID, eventName, youtubeUrl;
    Toolbar toolbar;

    TextView txtClassName, txtPlaylist, txtStyle, txtTeacher, txtDate, txtTime, txtBranch, txtDesc, txtCount, txtCheckEmpty;
    public TextView txtTypePay;
    YouTubePlayerFragment youtubePlayer;
    FloatingActionButton fabQrCheck;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<NameCheckedDao> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_class_detail);

        Intent intent = getIntent();
        eventID = intent.getStringExtra("eventID");
//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        userID = sharedPreferences.getString(getString(R.string.UserID), "");
//        Toast.makeText(this, eventID, Toast.LENGTH_SHORT).show();

        initFindViewByID();
        initToolbar();
        initInstance();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initInstance() {
        loadClassDetail();

        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        studentList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

//        loadListStudentCheck();

    }

    private void loadListStudentCheck() {
        layoutProgress.setVisibility(View.VISIBLE);
        studentList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, StudentCheckedUrl, response -> {
            Log.d("Onresponse", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                txtCount.setText(getResources().getString(R.string.count_checked_student) + " " + jsonObject.getString("countChecked"));
                if (jsonObject.getString("msg").equals("success")) {
                    JSONArray array = jsonObject.getJSONArray("student");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        NameCheckedDao item = new NameCheckedDao(
                                obj.getString("User")
                        );
//                        Toast.makeText(getContext(), jsonObject.getString("monthName"), Toast.LENGTH_LONG).show();
                        studentList.add(item);
                    }
                    if (studentList.size() == 0) {
                        recyclerView.setVisibility(View.GONE);
                        txtCheckEmpty.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        txtCheckEmpty.setVisibility(View.GONE);
                        adapter = new NameCheckedAdapter(studentList, getApplicationContext());
                        recyclerView.setAdapter(adapter);
                    }
                } else if (jsonObject.getString("msg").equals("empty")) {
                    recyclerView.setVisibility(View.GONE);
                    txtCheckEmpty.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                }
                layoutProgress.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            layoutProgress.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("eventID", eventID);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void loadClassDetail() {
        layoutProgress.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, getClassUrl, response -> {
            Log.d("onResponse", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("msg").equals("success")) {
                    JSONArray classArray = jsonObject.getJSONArray("class");
                    for (int i = 0; i < classArray.length(); i++) {
                        JSONObject obj = classArray.getJSONObject(i);
                        txtClassName.setText("คลาส : " + obj.getString("eventTitle"));
                        eventName = obj.getString("eventTitle");
                        txtPlaylist.setText("เพลง : " + obj.getString("playlist"));
                        txtStyle.setText("สไตล์ : " + obj.getString("eventStyle"));
                        txtTeacher.setText("ผู้สอน : " + obj.getString("eventTeacher"));
                        txtDate.setText("วันที่เรียน : " + obj.getString("eventDate"));
                        txtTime.setText("เวลา : " + obj.getString("eventTime"));
                        txtBranch.setText("สาขา : " + obj.getString("eventBranch"));
                        txtDesc.setText(getResources().getString(R.string.description) + " : " + obj.getString("description"));

                        if (obj.getString("youtubeUrl").equals("null")) {
                            youtubeUrl = "";
                        } else {
                            youtubeUrl = obj.getString("youtubeUrl");
                            youtubePlayer.initialize(config.getYoutubeKey(), new YouTubePlayer.OnInitializedListener() {
                                @Override
                                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                                    youTubePlayer.cueVideo(youtubeUrl);
                                }

                                @Override
                                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                                }
                            });
                        }
                    }
                    layoutProgress.setVisibility(View.GONE);
                } else {
                    layoutProgress.setVisibility(View.GONE);
                    Toast.makeText(this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, error -> {
            layoutProgress.setVisibility(View.GONE);
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("eventID", eventID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void initFindViewByID() {
        layoutProgress = findViewById(R.id.layout_progressbar);
        toolbar = findViewById(R.id.toolbar_class_detail);
        txtClassName = findViewById(R.id.txt_class_name);
        txtPlaylist = findViewById(R.id.txt_playlist);
        txtStyle = findViewById(R.id.txt_style);
        txtTeacher = findViewById(R.id.txt_teacher);
        txtDate = findViewById(R.id.txt_date);
        txtTime = findViewById(R.id.txt_time);
        txtBranch = findViewById(R.id.txt_branch);
        txtDesc = findViewById(R.id.txt_desc);
        txtTypePay = findViewById(R.id.txt_type_pay);
        youtubePlayer = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_player_footage);
        fabQrCheck = findViewById(R.id.fab_qr_check);
        fabQrCheck.setOnClickListener(this);
        recyclerView = findViewById(R.id.recycler_list_stu_check);
        txtCount = findViewById(R.id.txt_count_check);
        txtCheckEmpty = findViewById(R.id.txt_list_check_name_empty);

    }

    @Override
    public void onClick(View v) {
        if (v == fabQrCheck) {
            Intent intent = new Intent(this, AdminQRCodeScannedActivity.class);
            intent.putExtra("eventID", eventID);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        loadClassDetail();
//        if (studentList.size() > 0) {
            loadListStudentCheck();
//        }
    }
}
