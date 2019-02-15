package com.mocom.com.mdancingproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mocom.com.mdancingproject.R;

public class ShowPictureActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView photoView;
    ImageView imgClose;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);

        Intent intent = getIntent();
        imageUrl = intent.getStringExtra("imageUrl");

        initInstance();
    }

    private void initInstance() {
        initFindViewByID();

        Glide.with(getApplicationContext())
                .load(imageUrl)
                .into(photoView);
    }

    private void initFindViewByID() {
        photoView = findViewById(R.id.photo_view);
        imgClose = findViewById(R.id.img_close);
        imgClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == imgClose){
            finish();
        }
    }
}
