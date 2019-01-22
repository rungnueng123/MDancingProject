package com.mocom.com.mdancingproject.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.mocom.com.mdancingproject.Fragments.AdminDashboardFragment;
import com.mocom.com.mdancingproject.Fragments.StudentDashboardFragment;
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

        initInstance(savedInstanceState);

    }

    private void initInstance(Bundle savedInstanceState) {
        checkFragmentShow(savedInstanceState);
    }

    private void checkFragmentShow(Bundle savedInstanceState) {
        String Groups = sharedPreferences.getString(getString(R.string.Groups),"");
        if (savedInstanceState == null) {
            //TODO
            if(Groups.equals("Boss")){
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_dashboard, AdminDashboardFragment.newInstance())
                        .commit();
            }else{
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_dashboard, StudentDashboardFragment.newInstance())
                        .commit();
            }
        }
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
