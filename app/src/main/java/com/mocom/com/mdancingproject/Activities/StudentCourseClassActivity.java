package com.mocom.com.mdancingproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mocom.com.mdancingproject.R;

public class StudentCourseClassActivity extends AppCompatActivity {

    String courseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_course_class);

        Intent intent = getIntent();
        courseID = intent.getStringExtra("courseID");
        Toast.makeText(getApplicationContext(), courseID, Toast.LENGTH_LONG).show();
    }
}
