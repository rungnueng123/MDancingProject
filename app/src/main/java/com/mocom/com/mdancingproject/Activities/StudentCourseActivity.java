package com.mocom.com.mdancingproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.mocom.com.mdancingproject.R;
import com.mocom.com.mdancingproject.config.config;

public class StudentCourseActivity extends AppCompatActivity {

    String courseID;
    private static final String TAG = "StudentCourseActivity";

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

        youtubePlayer.initialize(config.getYoutubeKey(), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.cueVideo("qOjlSEkUilc");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

    }

    private void initFindViewByID() {
        youtubePlayer = (YouTubePlayerFragment)
                getFragmentManager().findFragmentById(R.id.youtube_player);
    }
}
