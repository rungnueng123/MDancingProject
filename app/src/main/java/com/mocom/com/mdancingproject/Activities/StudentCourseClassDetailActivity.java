package com.mocom.com.mdancingproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mocom.com.mdancingproject.R;

public class StudentCourseClassDetailActivity extends AppCompatActivity {

    String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_course_class_detail);

        Intent intent = getIntent();
        eventID = intent.getStringExtra("eventID");
//        Toast.makeText(this,eventID,Toast.LENGTH_LONG).show();
    }
}
