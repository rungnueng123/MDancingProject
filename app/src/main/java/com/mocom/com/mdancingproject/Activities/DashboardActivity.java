package com.mocom.com.mdancingproject.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.mocom.com.mdancingproject.R;

public class DashboardActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        sharedPreferences = getSharedPreferences("dancing",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        checkLogin();

        initInstance();

    }

    private void initInstance() {
    }

    private void checkLogin() {
        String UserID = sharedPreferences.getString(getString(R.string.UserID),"");
        if(UserID.equals("")){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onResume() {
        checkLogin();
        super.onResume();
    }
}
