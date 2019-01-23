package com.mocom.com.mdancingproject.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mocom.com.mdancingproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.mocom.com.mdancingproject.config.config.DATA_URL;

public class StudentProfileFragment extends Fragment {

    String profileUrl = DATA_URL + "profile.php";
    String userID;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    TextView txtProfile,txtCoin,txtUser,txtTel,txtBirth;
    ImageView imgProfile;
    BottomNavigationView bottomNavigationView;

    public static StudentProfileFragment newInstance() {
        StudentProfileFragment fragment = new StudentProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_student_profile, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        sharedPreferences = getSharedPreferences("dancing",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        initFindViewByID(rootView);
        queryProfile();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.course_applicant:
                        selectedFragment = CourseApplicantProfileFragment.newInstance();
                        break;
                    case R.id.history:
                        selectedFragment = HistoryProfileFragment.newInstance();
                        break;
                }
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, selectedFragment);
                transaction.commit();
                return true;
            }
        });
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, CourseApplicantProfileFragment.newInstance());
        transaction.commit();


    }

    private void queryProfile() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        userID = sharedPreferences.getString(getString(R.string.UserID), "");
        StringRequest request = new StringRequest(Request.Method.POST, profileUrl, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.getString("msg").equals("success")) {
                    JSONArray array = jsonObject.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
//                        Log.d("Member name: ", obj.getString("UserID"));
                        txtUser.setText(obj.getString("User"));
                        txtTel.setText(obj.getString("Phone"));
                        txtBirth.setText(obj.getString("Birth"));
                        txtCoin.setText(obj.getString("CoinAmt"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
//                    Log.d("onError", error.toString());
//                    Toast.makeText(getActivity(), "เกิดข้อผิดพลาดโปรดลองอีกครั้ง", Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userID", userID);

                return params;
            }
        };
        requestQueue.add(request);
    }

    private void initFindViewByID(View rootView) {
        bottomNavigationView = rootView.findViewById(R.id.profile_navigation);
        txtProfile = rootView.findViewById(R.id.txt_profile);
        imgProfile = rootView.findViewById(R.id.img_profile);
        txtCoin = rootView.findViewById(R.id.txt_coin);
        txtUser = rootView.findViewById(R.id.txt_user);
        txtTel = rootView.findViewById(R.id.txt_tel);
        txtBirth = rootView.findViewById(R.id.txt_birth);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance (Fragment level's variables) State here
    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {
    }
}
