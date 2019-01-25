package com.mocom.com.mdancingproject.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.mocom.com.mdancingproject.Fragments.StudentCoinFragment;
import com.mocom.com.mdancingproject.Fragments.StudentCourseFragment;
import com.mocom.com.mdancingproject.Fragments.StudentHomeFragment;
import com.mocom.com.mdancingproject.Fragments.StudentProfileFragment;
import com.mocom.com.mdancingproject.R;

public class StudentDashboardActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        sharedPreferences = getSharedPreferences("dancing",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        checkLogin();

        initInstance(savedInstanceState);

    }

    private void initInstance(Bundle savedInstanceState) {
        checkFragmentShow(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                navigationView.getMenu().getItem(0).setChecked(true);
                int id = item.getItemId();
                Fragment fragment = null;
                Class fragmentClass = null;
               if(id == R.id.nav_home){
                   fragmentClass = StudentHomeFragment.class;
                   actionbar.setTitle("Home");
               }else if(id == R.id.nav_course){
                   fragmentClass = StudentCourseFragment.class;
                   actionbar.setTitle("Course");
               }else if(id == R.id.nav_coin){
                   fragmentClass = StudentCoinFragment.class;
                   actionbar.setTitle("Coin");
               }else if(id == R.id.nav_profile){
                   fragmentClass = StudentProfileFragment.class;
                   actionbar.setTitle("Profile");
               }else if(id == R.id.nav_logout){
                   editor.putString(getString(R.string.UserID), "");
                   editor.putString(getString(R.string.User), "");
                   editor.putString(getString(R.string.Email), "");
                   editor.putString(getString(R.string.GroupID), "");
                   editor.putString(getString(R.string.Groups), "");
                   editor.commit();
                   Intent intentLogout = new Intent(getApplicationContext(), LoginActivity.class);
                   startActivity(intentLogout);
                   finish();
                   return true;
               }
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_dashboard, fragment).commit();

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void checkFragmentShow(Bundle savedInstanceState) {
        String Groups = sharedPreferences.getString(getString(R.string.Groups),"");
        if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_dashboard, StudentHomeFragment.newInstance())
                        .commit();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
