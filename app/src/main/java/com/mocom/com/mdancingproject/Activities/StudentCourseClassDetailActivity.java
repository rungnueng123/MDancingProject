package com.mocom.com.mdancingproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mocom.com.mdancingproject.R;

public class StudentCourseClassDetailActivity extends AppCompatActivity {

    String eventID;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_course_class_detail);

        Intent intent = getIntent();
        eventID = intent.getStringExtra("eventID");
//        Toast.makeText(this,eventID,Toast.LENGTH_LONG).show();

        initInstance();
    }

    private void initInstance() {
        initFindViewByID();
        initToolbar();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initFindViewByID() {
        toolbar = findViewById(R.id.toolbar_course_class_detail_single);
    }
}
