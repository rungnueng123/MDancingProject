package com.mocom.com.mdancingproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mocom.com.mdancingproject.R;

public class ShowPictureActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView photoView;
    ImageView imgClose;
    TextView txtDesc;
    private String imageUrl, desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);

        Intent intent = getIntent();
        imageUrl = intent.getStringExtra("imageUrl");
        desc = intent.getStringExtra("txtDesc");

        initInstance();
    }

    private void initInstance() {
        initFindViewByID();

        Glide.with(getApplicationContext())
                .load(imageUrl)
                .into(photoView);

        if (!TextUtils.isEmpty(desc)) {
            txtDesc.setVisibility(View.VISIBLE);
            txtDesc.setText(getResources().getString(R.string.description) + " : " + desc);
        } else {
            txtDesc.setVisibility(View.GONE);
        }

    }

    private void initFindViewByID() {
        photoView = findViewById(R.id.photo_view);
        txtDesc = findViewById(R.id.txt_desc);
        imgClose = findViewById(R.id.img_close);
        imgClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == imgClose) {
            finish();
        }
    }
}
