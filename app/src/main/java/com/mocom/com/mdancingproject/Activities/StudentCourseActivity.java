package com.mocom.com.mdancingproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import static com.mocom.com.mdancingproject.config.config.HOST_URL;

public class StudentCourseActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "StudentCourseActivity";
    String getCourseUrl = DATA_URL + "json_get_course_detail_student.php";
    String courseID, youtubeUrl, gal1Url, gal2Url, gal3Url, gal4Url;
    TextView txtCourse, txtCoin, txtHour, txtStyle, txtDesc;
    ImageView gal1, gal2, gal3, gal4;
    Button btnWatchClass;
    private RecyclerView recyclerViewGallery;
    private RecyclerView.Adapter adapter;
    private List<StudentCourseGalleryDao> galleryList;
    private ItemClickCallBack listener;
    Toolbar toolbar;

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
        initToolbar();

        //Gallery
        galleryList = new ArrayList<>();

        recyclerViewGallery.setHasFixedSize(true);
        recyclerViewGallery.setLayoutManager(new GridLayoutManager(this, 2));


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


        listener = (view, position) -> {
            Toast.makeText(this, galleryList.get(position).getGallery(), Toast.LENGTH_LONG).show();
        };


    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());
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
                        txtCourse.setText(objClass.getString("Course"));
                        txtCoin.setText(objClass.getString("CoinAmt") + " Coins/Class");
                        txtHour.setText("จำนวนครั้งที่เรียน : " + objClass.getString("CourseLength"));
                        txtStyle.setText("Style : " + objClass.getString("courseStyleName"));
                        txtDesc.setText("Description : \n\n" + objClass.getString("Description"));
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
                        String imgUrl = HOST_URL + objGallery.getString("gallery");
                        if (i == 0) {
                            gal1Url = imgUrl;
                            Glide.with(getApplicationContext())
                                    .load(imgUrl)
                                    .into(gal1);
                        }
                        if (i == 1) {
                            gal2Url = imgUrl;
                            Glide.with(getApplicationContext())
                                    .load(imgUrl)
                                    .into(gal2);
                        }
                        if (i == 2) {
                            gal3Url = imgUrl;
                            Glide.with(getApplicationContext())
                                    .load(imgUrl)
                                    .into(gal3);
                        }
                        if (i == 3) {
                            gal4Url = imgUrl;
                            Glide.with(getApplicationContext())
                                    .load(imgUrl)
                                    .into(gal4);
                        }
                    }

                    if (galleryList.size() == 0) {
                    } else {
                        adapter = new StudentCourseGalleryAdapter(listener, galleryList, getApplicationContext());
                        recyclerViewGallery.setAdapter(adapter);
                    }
//                    Glide.with(context)
//                            .load(imgClass)
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
        recyclerViewGallery = findViewById(R.id.recycler_student_course_gallery);
        btnWatchClass = findViewById(R.id.btn_watch_class);
        btnWatchClass.setOnClickListener(this);
        toolbar = findViewById(R.id.toolbar_course_detail);
        gal1 = findViewById(R.id.img_gal_1);
        gal2 = findViewById(R.id.img_gal_2);
        gal3 = findViewById(R.id.img_gal_3);
        gal4 = findViewById(R.id.img_gal_4);
        gal1.setOnClickListener(this);
        gal2.setOnClickListener(this);
        gal3.setOnClickListener(this);
        gal4.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
//        Bundle bundle = new Bundle();
        Intent intentShowPic = new Intent(this, ShowPictureActivity.class);
        if (v == btnWatchClass) {
            Intent intent = new Intent(this, StudentCourseClassActivity.class);
            intent.putExtra("courseID", courseID);
            startActivity(intent);
        }
        if (v == gal1) {
            intentShowPic.putExtra("imageUrl", gal1Url);
            startActivity(intentShowPic);
//            bundle.putString("imageUrl", gal1Url);
//            Toast.makeText(getApplicationContext(),gal1Url,Toast.LENGTH_LONG).show();
        }
        if (v == gal2) {
            intentShowPic.putExtra("imageUrl", gal2Url);
            startActivity(intentShowPic);
//            bundle.putString("imageUrl", gal2Url);
//            Toast.makeText(getApplicationContext(),gal2Url,Toast.LENGTH_LONG).show();
        }
        if (v == gal3) {
            intentShowPic.putExtra("imageUrl", gal3Url);
            startActivity(intentShowPic);
//            bundle.putString("imageUrl", gal3Url);
//            Toast.makeText(getApplicationContext(),gal3Url,Toast.LENGTH_LONG).show();
        }
        if (v == gal4) {
            intentShowPic.putExtra("imageUrl", gal4Url);
            startActivity(intentShowPic);
//            bundle.putString("imageUrl", gal4Url);
//            Toast.makeText(getApplicationContext(),gal4Url,Toast.LENGTH_LONG).show();
        }

//        Fragment fragment = new ShowPictureFragment();
//        fragment.setArguments(bundle);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.for_show_picture, fragment).commit();
    }
}
