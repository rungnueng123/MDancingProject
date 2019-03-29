package com.mocom.com.mdancingproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mocom.com.mdancingproject.R;

import static com.mocom.com.mdancingproject.config.config.HOST_URL;

public class ShowPosterActivity extends AppCompatActivity implements View.OnClickListener {

    private String bannerUrl, posterUrl, desc;
    private TextView txtDetail;
    private ImageView imgBanner;
    LinearLayout layoutBanner;
    int width, heigth;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_poster);

        Intent intent = getIntent();
        bannerUrl = intent.getStringExtra("imgUrl");
        posterUrl = intent.getStringExtra("posterUrl");
        desc = intent.getStringExtra("desc");
        Log.d("url",bannerUrl);

        initInstance();
    }

    private void initInstance() {
        initFindViewById();
        initToolbar();

        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();


        txtDetail.setText(getResources().getString(R.string.description)+" : "+desc);
        if (!TextUtils.isEmpty(posterUrl)) {
            Glide.with(getApplicationContext()).load(HOST_URL + posterUrl).into(imgBanner);
            heigth = width;
        } else {
            Glide.with(getApplicationContext()).load(HOST_URL + bannerUrl).into(imgBanner);
            heigth = width / 2;
        }
        layoutBanner.setLayoutParams(new LinearLayout.LayoutParams(width, heigth));

//        if (!TextUtils.isEmpty(posterUrl)){
//            imgBanner.setVisibility(View.GONE);
//            imgPoster.setVisibility(View.VISIBLE);
//        }else{
//            imgBanner.setVisibility(View.VISIBLE);
//            imgPoster.setVisibility(View.GONE);
//        }
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initFindViewById() {
        toolbar = findViewById(R.id.toolbar);
        txtDetail = findViewById(R.id.txt_detail);
        imgBanner = findViewById(R.id.img_banner);
        imgBanner.setOnClickListener(this);
        layoutBanner = findViewById(R.id.layout_banner);
    }

    @Override
    public void onClick(View v) {
        if (v == imgBanner) {
            Intent intent = new Intent(getApplicationContext(), ShowPictureActivity.class);
            if (!TextUtils.isEmpty(posterUrl)) {
                intent.putExtra("imageUrl", HOST_URL+posterUrl);
            }else{
                intent.putExtra("imageUrl", HOST_URL+bannerUrl);
            }
            startActivity(intent);
        }
    }
}
