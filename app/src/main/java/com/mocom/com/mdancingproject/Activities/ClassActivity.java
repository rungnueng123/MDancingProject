package com.mocom.com.mdancingproject.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.mocom.com.mdancingproject.R;
import com.mocom.com.mdancingproject.config.config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.mocom.com.mdancingproject.config.config.DATA_URL;

public class ClassActivity extends AppCompatActivity {

    private String getYoutubeUrl = DATA_URL + "get_youtube_by_course.php";
    private String loadBranchUrl = DATA_URL + "get_all_branch.php";

    String courseID, youtubeUrl;
    Toolbar toolbar;
    private ArrayList<String> branch = new ArrayList<String>();
    private Spinner spinnerBranch;
    ProgressDialog progressDialog;
    YouTubePlayerFragment youtubePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        Intent intent = getIntent();
        courseID = intent.getStringExtra("courseID");

        initInstance();
    }

    private void initInstance() {
        initFindViewByID();
        initToolbar();

        loadVideoYoutube();

        loadAllBranch();
    }

    private void loadVideoYoutube() {
        StringRequest request = new StringRequest(Request.Method.POST, getYoutubeUrl, response -> {
            Log.d("onResponse", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("msg").equals("success")) {
                    if(jsonObject.getString("youtube").equals("null")){
                        youtubeUrl = "";
                    }else{
                        youtubeUrl = jsonObject.getString("youtube");
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("courseID", courseID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    private void setSpinner() {
        ArrayAdapter<String> adapterBranch = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, branch);
        spinnerBranch.setAdapter(adapterBranch);

        spinnerBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadAllBranch() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("ProgressDialog"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, loadBranchUrl, response -> {
            Log.d("Onresponse", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("msg").equals("success")) {
                    JSONArray array = jsonObject.getJSONArray("branch");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        branch.add(obj.getString("Branch"));
                    }
                    progressDialog.dismiss();
                    if (branch.size() > 0) {
                        setSpinner();
                    }

                } else {
                    progressDialog.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show());

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initFindViewByID() {
        youtubePlayer = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_player);
        toolbar = findViewById(R.id.toolbar_course_class_detail);
        spinnerBranch = findViewById(R.id.spinner_branch);
    }
}
