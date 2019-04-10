package com.mocom.com.mdancingproject.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.mocom.com.mdancingproject.Fragments.AdminHomeFragment;
import com.mocom.com.mdancingproject.QRCode.AdminQRCodeScannedActivity;
import com.mocom.com.mdancingproject.R;

public class AdminDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    FloatingActionButton fabQr;
    String UserID, User, Email, GroupID, Groups;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        sharedPreferences = getSharedPreferences("dancing",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Log.d("preference", sharedPreferences.getString(getString(R.string.Groups), ""));
        checkLogin();

        initFindViewByID();
        initInstance(savedInstanceState);

    }

    private void initFindViewByID() {
        fabQr = findViewById(R.id.fab_qr);
        fabQr.setOnClickListener(this);
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
                int id = item.getItemId();
                Fragment fragment = null;
                Class fragmentClass = null;
                if (id == R.id.nav_home) {
                    fragmentClass = AdminHomeFragment.class;
                } else if (id == R.id.nav_logout) {
                    editor.clear();
                    editor.commit();
                    LoginManager.getInstance().logOut();
                    Intent intentLogout = new Intent(getApplicationContext(), LoginActivity.class);
                    intentLogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
        String Groups = sharedPreferences.getString(getString(R.string.Groups), "");
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_dashboard, AdminHomeFragment.newInstance())
                    .commit();
        }
    }

    private void checkLogin() {
        String UserID = sharedPreferences.getString(getString(R.string.UserID), "");
        if (UserID.equals("")) {
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

    @Override
    public void onClick(View v) {
        if (v == fabQr) {
            Intent intent = new Intent(this, AdminQRCodeScannedActivity.class);
//            intent.putExtra("eventID","97");
            startActivity(intent);
        }
    }

    private void SavePreferences() {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(getString(R.string.UserID), UserID);
        editor.putString(getString(R.string.User), User);
        editor.putString(getString(R.string.Email), Email);
        editor.putString(getString(R.string.GroupID), GroupID);
        editor.putString(getString(R.string.Groups), Groups);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        SavePreferences();
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getResources().getString(R.string.twice_for_exit), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
