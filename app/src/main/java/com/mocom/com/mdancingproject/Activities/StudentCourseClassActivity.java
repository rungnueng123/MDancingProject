package com.mocom.com.mdancingproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mocom.com.mdancingproject.Adapter.StudentCourseClassAdapter;
import com.mocom.com.mdancingproject.Dao.StudentCourseClassDao;
import com.mocom.com.mdancingproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mocom.com.mdancingproject.config.config.DATA_URL;

public class StudentCourseClassActivity extends AppCompatActivity implements View.OnClickListener {

    String getClassUrl = DATA_URL + "json_get_course_class_student.php";
    String courseID;
    Toolbar toolbar;
    private RecyclerView recyclerViewClass;
    private RecyclerView.Adapter adapter;
    private List<StudentCourseClassDao> classList;

    ImageView imgClass,imgArrow;
    TextView txtClassName,txtPlaylist,txtStyle,txtTeacher,txtDate,txtTime,txtEmpty,txtBranch,txtCoin,txtDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_course_class);

        Intent intent = getIntent();
        courseID = intent.getStringExtra("courseID");

        initInstance();

    }

    private void initInstance() {
        initFindViewByID();
        initToolbar();
        initRecyclerViewClass();
    }

    private void initRecyclerViewClass() {
        recyclerViewClass.setHasFixedSize(true);
        recyclerViewClass.setLayoutManager(new LinearLayoutManager(this));

        classList = new ArrayList<>();
        loadClassData();

        //Test RecyclerView >>>
//        for(int i = 0; i<=10;i++){
//            StudentCourseClassDao studentClassHomeDao = new StudentCourseClassDao(
//                    "1",
//                    "imgBanner/images.png",
//                    "fdsfsdffd",
//                    "qweqrg",
//                    "19/01/2019",
//                    "07:00 - 09:00",
//                    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//            );
//            classList.add(studentClassHomeDao);
//        }
//        adapter = new StudentCourseClassAdapter(listener,classList,getApplicationContext());
//        recyclerViewClass.setAdapter(adapter);
        //Test RecyclerView <<<
    }

    private void loadClassData() {
        classList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getClassUrl, response -> {
            Log.d("Onresponse", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("msg").equals("success")) {
                    JSONArray classArray = jsonObject.getJSONArray("class");
                    for (int i = 0; i < classArray.length(); i++) {
                        JSONObject objClass = classArray.getJSONObject(i);
                        StudentCourseClassDao item = new StudentCourseClassDao(
                                objClass.getString("eventID"),
                                objClass.getString("imgUrl"),
                                objClass.getString("eventTitle"),
                                objClass.getString("playlist"),
                                objClass.getString("eventStyle"),
                                objClass.getString("eventTeacher"),
                                objClass.getString("eventDate"),
                                objClass.getString("eventTime"),
                                objClass.getString("eventEmpty"),
                                objClass.getString("eventBranch"),
                                objClass.getString("description"),
                                objClass.getString("coin")
                        );

                        classList.add(item);
                    }

                    if (classList.size() == 0) {
                    } else {
                        adapter = new StudentCourseClassAdapter(classList, this);
                        recyclerViewClass.setAdapter(adapter);
                    }

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
                params.put("courseID", courseID);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
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
        toolbar = findViewById(R.id.toolbar_course_class_detail);
        recyclerViewClass = findViewById(R.id.recycler_student_course_class);
        imgClass = findViewById(R.id.img_class);
        imgArrow = findViewById(R.id.img_arrow);
        imgArrow.setOnClickListener(this);
        txtClassName = findViewById(R.id.txt_class_name);
        txtPlaylist = findViewById(R.id.txt_playlist);
        txtStyle = findViewById(R.id.txt_style);
        txtTeacher = findViewById(R.id.txt_teacher);
        txtDate = findViewById(R.id.txt_date);
        txtTime = findViewById(R.id.txt_time);
        txtEmpty = findViewById(R.id.txt_empty);
        txtBranch = findViewById(R.id.txt_branch);
        txtCoin = findViewById(R.id.txt_coin);
        txtDesc = findViewById(R.id.txt_show_desc);

    }

    @Override
    public void onClick(View v) {

    }
}
