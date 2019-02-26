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
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;
import com.mocom.com.mdancingproject.DialogFragment.StudentCoinPackPaymentDialog;
import com.mocom.com.mdancingproject.Fragments.HomeFragment;
import com.mocom.com.mdancingproject.Fragments.StudentCoinFragment;
import com.mocom.com.mdancingproject.Fragments.StudentCourseFragment;
import com.mocom.com.mdancingproject.Fragments.StudentProfileFragment;
import com.mocom.com.mdancingproject.PaymentGateway.PaymentGatewayTestActivity;
import com.mocom.com.mdancingproject.QRCode.StudentQRCodeActivity;
import com.mocom.com.mdancingproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.mocom.com.mdancingproject.config.config.DATA_URL;

public class StudentDashboardActivity extends AppCompatActivity implements StudentCoinPackPaymentDialog.OnSelectTypePayPackListener {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private DrawerLayout drawerLayout;
    private String namePack, baht, coinAmt, secret_key;
    String UserID, User, Email, GroupID, Groups;
    NavigationView navigationView;
    String name, goBuyCoin, goProfile, sharedUserID;
    String queryGenQrUrl = DATA_URL + "query_for_gen_qr.php";

    @Override
    public void sendOnSelectTypePayPackCoinListener(String typePay, String coinPackID) {
        if (typePay.equals(getResources().getString(R.string.payment_gateway))) {
            Intent intent = new Intent(getApplicationContext(), PaymentGatewayTestActivity.class);
                intent.putExtra("coinPackID",coinPackID);
                startActivity(intent);
        } else if (typePay.equals(getResources().getString(R.string.qr_code))) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            sharedUserID = sharedPreferences.getString(getString(R.string.UserID), "");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, queryGenQrUrl, response -> {
//                    Log.d("Onresponse", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("msg").equals("success")) {
                        JSONArray array = jsonObject.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            secret_key = obj.getString("key");
                        }
                        Intent intent = new Intent(getApplicationContext(), StudentQRCodeActivity.class);
                        intent.putExtra("secret_key", secret_key);
                        intent.putExtra("baht", baht);
                        intent.putExtra("coinAmt", coinAmt);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("coinPackID", coinPackID);
                    params.put("userID", sharedUserID);

                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        sharedPreferences = getSharedPreferences("dancing",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Log.d("preference", sharedPreferences.getString(getString(R.string.User), ""));
        checkLogin();
        Intent intent = getIntent();
        goBuyCoin = intent.getStringExtra("goBuyCoin");
        goProfile = intent.getStringExtra("goProfile");
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
                if (id == R.id.nav_home) {
                    fragmentClass = HomeFragment.class;
                    actionbar.setTitle("Home");
                } else if (id == R.id.nav_course) {
                    fragmentClass = StudentCourseFragment.class;
                    actionbar.setTitle("Course");
                } else if (id == R.id.nav_coin) {
                    fragmentClass = StudentCoinFragment.class;
                    actionbar.setTitle("Coin");
                } else if (id == R.id.nav_profile) {
                    fragmentClass = StudentProfileFragment.class;
                    actionbar.setTitle("Profile");
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

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void checkFragmentShow(Bundle savedInstanceState) {
        String Groups = sharedPreferences.getString(getString(R.string.Groups), "");
        if (savedInstanceState == null) {
            if (!TextUtils.isEmpty(goBuyCoin)) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_dashboard, StudentCoinFragment.newInstance())
                        .commit();
            } else if (!TextUtils.isEmpty(goProfile)) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_dashboard, StudentProfileFragment.newInstance())
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_dashboard, HomeFragment.newInstance())
                        .commit();
            }
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

    private void LoadPreferences() {

    }

    private void checkLogin() {
        String UserID = sharedPreferences.getString(getString(R.string.UserID), "");
//        Toast.makeText(this,UserID,Toast.LENGTH_LONG).show();
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
    public void onBackPressed() {
        SavePreferences();
        super.onBackPressed();
    }
}
