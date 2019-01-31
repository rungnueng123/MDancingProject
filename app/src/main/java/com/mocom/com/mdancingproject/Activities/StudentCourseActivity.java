package com.mocom.com.mdancingproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.mocom.com.mdancingproject.Adapter.StudentCourseGalleryAdapter;
import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.Dao.StudentCourseGalleryDao;
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

public class StudentCourseActivity extends AppCompatActivity {

    String courseID, youtubeUrl;
    private static final String TAG = "StudentCourseActivity";
    String getCourseUrl = DATA_URL + "json_get_course_detail_student.php";
    TextView txtCourse, txtCoin, txtHour, txtStyle, txtDesc;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<StudentCourseGalleryDao> galleryList;
    private ItemClickCallBack listener;

    YouTubePlayerFragment youtubePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_course);

        Intent intent = getIntent();
        courseID = intent.getStringExtra("courseID");
//        Toast.makeText(this, courseID, Toast.LENGTH_LONG).show();

        initInstance();
    }

    private void initInstance() {
        initFindViewByID();
        galleryList = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        loadCourseDetail();

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

    private void loadCourseDetail() {
        galleryList.clear();
        StringRequest request = new StringRequest(Request.Method.POST, getCourseUrl, response -> {
            Log.d("onResponse", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("msg").equals("success")) {
                    JSONArray classArray = jsonObject.getJSONArray("class");
                    for (int i = 0; i < classArray.length(); i++) {
                        JSONObject objClass = classArray.getJSONObject(i);
                        if (objClass.getString("Course").equals("null")) {
                            txtCourse.setText("Course: ");
                        } else {
                            txtCourse.setText("Course: " + objClass.getString("Course"));
                        }
                        if (objClass.getString("CoinAmt").equals("null")) {
                            txtCoin.setText("Coins: ");
                        } else {
                            txtCoin.setText("Coins: " + objClass.getString("CoinAmt"));
                        }
                        if (objClass.getString("CourseLength").equals("null")) {
                            txtHour.setText("Hours: ");
                        } else {
                            txtHour.setText("Hours: " + objClass.getString("CourseLength"));
                        }
                        if (objClass.getString("courseStyleName").equals("null")) {
                            txtStyle.setText("Style: ");
                        } else {
                            txtStyle.setText("Style: " + objClass.getString("courseStyleName"));
                        }
                        if (objClass.getString("Description").equals("null")) {
                            txtDesc.setText("Description: ");
                        } else {
                            txtDesc.setText("Description: " + objClass.getString("Description"));
                        }
                        if (objClass.getString("ClipLink").equals("null")) {
                            youtubeUrl = "";
                        } else {
                            youtubeUrl = objClass.getString("ClipLink");
                        }
                    }
                    JSONArray galleryArray = jsonObject.getJSONArray("gallery");
                    for (int i = 0; i < galleryArray.length(); i++) {
                        JSONObject objGallery = galleryArray.getJSONObject(i);
                        StudentCourseGalleryDao item = new StudentCourseGalleryDao(
                                objGallery.getString("gallery")
                        );
                        galleryList.add(item);
                    }

                    if (galleryList.size() == 0) {
                    } else {
                        adapter = new StudentCourseGalleryAdapter(listener, galleryList, getApplicationContext());
                        recyclerView.setAdapter(adapter);
                    }
//                    Glide.with(context)
//                            .load(imgUrl)
//                            .into(holder.getImgUrl());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
//                    Log.d("onError", error.toString());
//                    Toast.makeText(getActivity(), "เกิดข้อผิดพลาดโปรดลองอีกครั้ง", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
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

    private void initFindViewByID() {
        youtubePlayer = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_player);
        txtCourse = findViewById(R.id.txt_course_name);
        txtCoin = findViewById(R.id.txt_coin);
        txtHour = findViewById(R.id.txt_hour);
        txtStyle = findViewById(R.id.txt_style);
        txtDesc = findViewById(R.id.txt_description);
        recyclerView = findViewById(R.id.recycler_student_class_home);

    }
}
