package com.mocom.com.mdancingproject.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mocom.com.mdancingproject.Activities.HaveStylePackAllActivity;
import com.mocom.com.mdancingproject.Adapter.HaveStylePackAdapter;
import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.Dao.HaveStylePackDao;
import com.mocom.com.mdancingproject.DialogFragment.StylePackDetailDialog;
import com.mocom.com.mdancingproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mocom.com.mdancingproject.config.config.DATA_URL;

public class StudentProfileFragment extends Fragment implements View.OnClickListener {

    String profileUrl = DATA_URL + "profile.php";
    String stylePackUrl = DATA_URL + "style_pack.php";
    String userID;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    TextView txtProfile, txtCoin, txtUser, txtTel, txtBirth, txtShowStyleAll;
    BottomNavigationView bottomNavigationView;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private CardView cardView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<HaveStylePackDao> styleList;
    private ItemClickCallBack styleListener;

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
        initFindViewByID(rootView);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        sharedPreferences = getSharedPreferences("dancing",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        styleList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        queryProfile();
        queryStylePack();

        addTabs(viewPager);

        tabLayout.setupWithViewPager(viewPager);


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

        styleListener = (view, position) -> {
            openDialogDetail(styleList.get(position).getHaveTime(), styleList.get(position).getStylePack(), styleList.get(position).getImgUrl(), styleList.get(position).getStyleName());
        };


    }

    private void openDialogDetail(String time, String stylePack, String imgUrl, String styleName) {
        Bundle bundle = new Bundle();
        bundle.putString("time", time);
        bundle.putString("stylePack", stylePack);
        bundle.putString("imgUrl", imgUrl);
        bundle.putString("styleName", styleName);
        StylePackDetailDialog dialog = new StylePackDetailDialog();
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "StylePackDetailDialog");
    }

    private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new CourseApplicantProfileFragment(), "APPLICANT");
        adapter.addFrag(new HistoryProfileFragment(), "HISTORY");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void queryProfile() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        userID = sharedPreferences.getString(getString(R.string.UserID), "");
        StringRequest request = new StringRequest(Request.Method.POST, profileUrl, response -> {
//            Log.d("onResponse", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("msg").equals("success")) {
                    //profile
                    JSONArray array = jsonObject.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
//                        Log.d("Member name: ", obj.getString("UserID"));
                        if (obj.getString("User").equals("null")) {
                            txtUser.setText(getResources().getString(R.string.name) + ": ");
                        } else {
                            txtUser.setText(getResources().getString(R.string.name) + ": " + obj.getString("User"));
                        }
                        if (obj.getString("Phone").equals("null")) {
                            txtTel.setText(getResources().getString(R.string.tel) + ": ");
                        } else {
                            txtTel.setText(getResources().getString(R.string.tel) + ": " + obj.getString("Phone"));
                        }
                        if (obj.getString("Birth").equals("null")) {
                            txtBirth.setText(getResources().getString(R.string.birth) + ": ");
                        } else {
                            txtBirth.setText(getResources().getString(R.string.birth) + ": " + obj.getString("Birth"));
                        }
                        txtCoin.setText(getResources().getString(R.string.coin) + " : " + obj.getString("CoinAmt"));
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

    private void queryStylePack() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        userID = sharedPreferences.getString(getString(R.string.UserID), "");
        StringRequest request = new StringRequest(Request.Method.POST, stylePackUrl, response -> {
//            Log.d("onResponse", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("msg").equals("success")) {
                    //stylePack
                    JSONArray array1 = jsonObject.getJSONArray("styleData");
                    if (array1.length() < 1) {
                        cardView.setVisibility(View.GONE);
                    } else {
                        cardView.setVisibility(View.VISIBLE);
                        for (int i = 0; i < array1.length(); i++) {
                            JSONObject obj1 = array1.getJSONObject(i);
                            HaveStylePackDao item = new HaveStylePackDao(
                                    obj1.getString("stylePack"),
                                    obj1.getString("styleName"),
                                    obj1.getString("haveTime"),
                                    obj1.getString("imgUrl")
                            );
                            styleList.add(item);
                        }
                        adapter = new HaveStylePackAdapter(styleListener, styleList, getContext());
                        recyclerView.setAdapter(adapter);
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
        txtCoin = rootView.findViewById(R.id.txt_coin);
        txtCoin.setOnClickListener(this);
        txtUser = rootView.findViewById(R.id.txt_user);
        txtTel = rootView.findViewById(R.id.txt_tel);
        txtBirth = rootView.findViewById(R.id.txt_birth);

        viewPager = rootView.findViewById(R.id.view_pager);
        tabLayout = rootView.findViewById(R.id.tab_layout);

        cardView = rootView.findViewById(R.id.card_have_style_pack);
        recyclerView = rootView.findViewById(R.id.recycler_have_style_pack);
        txtShowStyleAll = rootView.findViewById(R.id.txt_all_pack_style);
        txtShowStyleAll.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        if (v == txtCoin) {
            Toast.makeText(getActivity(), "aaa", Toast.LENGTH_SHORT).show();
        }
        if (v == txtShowStyleAll) {
            Intent intent = new Intent(getContext(), HaveStylePackAllActivity.class);
            startActivity(intent);
        }
    }
}
