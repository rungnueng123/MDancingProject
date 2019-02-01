package com.mocom.com.mdancingproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.mocom.com.mdancingproject.Adapter.StudentCourseClassAdapter;
import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.Dao.StudentCourseClassDao;
import com.mocom.com.mdancingproject.R;

import java.util.ArrayList;
import java.util.List;

import static com.mocom.com.mdancingproject.config.config.DATA_URL;

public class StudentCourseClassActivity extends AppCompatActivity {

    String getClassUrl = DATA_URL + "json_get_course_class_student.php";
    String courseID;
    Toolbar toolbar;
    private RecyclerView recyclerViewClass;
    private RecyclerView.Adapter adapter;
    private ItemClickCallBack listener;
    private List<StudentCourseClassDao> classList;

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

        //Test RecyclerView >>>
        for(int i = 0; i<=10;i++){
            StudentCourseClassDao studentClassHomeDao = new StudentCourseClassDao(
                    "1",
                    "imgBanner/images.png",
                    "fdsfsdffd",
                    "qweqrg",
                    "19/01/2019",
                    "07:00 - 09:00",
                    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
            );
            classList.add(studentClassHomeDao);
        }
        adapter = new StudentCourseClassAdapter(listener,classList,getApplicationContext());
        recyclerViewClass.setAdapter(adapter);
        //Test RecyclerView <<<
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
    }
}
