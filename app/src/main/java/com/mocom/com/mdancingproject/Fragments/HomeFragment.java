package com.mocom.com.mdancingproject.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mocom.com.mdancingproject.Activities.ClassActivity;
import com.mocom.com.mdancingproject.Adapter.CourseHomeAdapter;
import com.mocom.com.mdancingproject.Adapter.ImageBannerAdapter;
import com.mocom.com.mdancingproject.Adapter.StyleHomeAdapter;
import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.Dao.CourseHomeDao;
import com.mocom.com.mdancingproject.Dao.StyleHomeDao;
import com.mocom.com.mdancingproject.R;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.mocom.com.mdancingproject.config.config.DATA_URL;
import static com.mocom.com.mdancingproject.config.config.HOST_URL;

public class HomeFragment extends Fragment {

    private String styleUrl = DATA_URL + "get_style_all.php";
    private String courseUrl = DATA_URL + "get_course_style_all.php";
    private String BannerUrl = DATA_URL + "get_banner.php";
    ProgressDialog progressDialog;
    View layoutShowFirstOpen, layoutShowEmpty;
    String styleID = "";


    private static ViewPager viewPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private String[] urls;
    CirclePageIndicator indicator;



    private RecyclerView recyclerStyleView, recyclerCourseView;
    private RecyclerView.Adapter adapterStyle, adapterCourse;
    private List<StyleHomeDao> styleList;
    private List<CourseHomeDao> courseList;
    private ItemClickCallBack styleListener, courseListener;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        initFindViewByID(rootView);

        loadBanner();

        ViewCompat.setNestedScrollingEnabled(recyclerCourseView, false);

        styleList = new ArrayList<>();
        recyclerStyleView.setHasFixedSize(true);
        recyclerStyleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        courseList = new ArrayList<>();
        recyclerCourseView.setHasFixedSize(true);
        recyclerCourseView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        loadStyle();


        styleListener = (view, position) -> {
//            Toast.makeText(getContext(),styleList.get(position).getStyleID(),Toast.LENGTH_LONG).show();
            styleID = styleList.get(position).getStyleID();
            loadCourse();

        };

        courseListener = (view, position) ->{
            Intent intent = new Intent(getContext(), ClassActivity.class);
            intent.putExtra("courseID", courseList.get(position).getCourseID());
            intent.putExtra("courseName", courseList.get(position).getCourseName());
            startActivity(intent);
        };

    }

    private void loadBanner() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, BannerUrl, response -> {
            Log.d("Onresponse", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("msg").equals("success")) {
                    JSONArray array = jsonObject.getJSONArray("data");
                    urls = new String[array.length()];
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
//                        ImageBannerDao item = new ImageBannerDao(
//                                obj.getString("id"),
//                                obj.getString("title"),
//                                obj.getString("desc"),
//                                obj.getString("imgUrl")
//                        );
                        urls[i] = HOST_URL+obj.getString("imgUrl");
//                        urls[2] = HOST_URL+"imgBanner/0860476001551410367--Mask Group 34.png";
                    }
//                    urls = new String[] {"https://demonuts.com/Demonuts/SampleImages/W-03.JPG",
//                            "https://demonuts.com/Demonuts/SampleImages/W-08.JPG",
//                            "https://demonuts.com/Demonuts/SampleImages/W-10.JPG",
//                            "https://demonuts.com/Demonuts/SampleImages/W-13.JPG",
//                            "https://demonuts.com/Demonuts/SampleImages/W-17.JPG",
//                            "https://demonuts.com/Demonuts/SampleImages/W-21.JPG"};

                    viewPager.setAdapter(new ImageBannerAdapter(getContext(),urls));
                    indicator.setViewPager(viewPager);
                    final float density = getResources().getDisplayMetrics().density;
                    indicator.setRadius(5 * density);

                    NUM_PAGES = urls.length;
                    // Auto start of viewpager
                    final Handler handler = new Handler();
                    final Runnable Update = new Runnable() {
                        public void run() {
                            if (currentPage == NUM_PAGES) {
                                currentPage = 0;
                            }
                            viewPager.setCurrentItem(currentPage++, true);
                        }
                    };
                    Timer swipeTimer = new Timer();
                    swipeTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(Update);
                        }
                    }, 3000, 3000);

                    // Pager listener over indicator
                    indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                        @Override
                        public void onPageSelected(int position) {
                            currentPage = position;

                        }

                        @Override
                        public void onPageScrolled(int pos, float arg1, int arg2) {

                        }

                        @Override
                        public void onPageScrollStateChanged(int pos) {

                        }
                    });


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void initFindViewByID(View rootView) {
        recyclerStyleView = rootView.findViewById(R.id.recycler_style_home);
        recyclerCourseView = rootView.findViewById(R.id.recycler_course_home);
        layoutShowFirstOpen = rootView.findViewById(R.id.layout_show_first_open);
        layoutShowEmpty = rootView.findViewById(R.id.layout_show_empty);
        viewPager = rootView.findViewById(R.id.pager);
        indicator = rootView.findViewById(R.id.indicator);
    }

    private void loadStyle() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("ProgressDialog"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

        if (styleList != null || styleList.size() > 0) {
            styleList.clear();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, styleUrl, response -> {
            Log.d("Onresponse", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("msg").equals("success")) {
                    JSONArray array = jsonObject.getJSONArray("style");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        StyleHomeDao item = new StyleHomeDao(
                                obj.getString("courseStyleID"),
                                obj.getString("courseStyleName"),
                                obj.getString("courseStyleDesc"),
                                obj.getString("courseStyleImage")
                        );
                        styleList.add(item);
                    }
                    adapterStyle = new StyleHomeAdapter(styleListener, styleList, getContext());
                    recyclerStyleView.setAdapter(adapterStyle);

                    progressDialog.dismiss();
                    loadCourse();

                }else{
                    progressDialog.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void loadCourse() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("ProgressDialog"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        if (courseList != null || courseList.size() > 0) {
            courseList.clear();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest request = new StringRequest(Request.Method.POST, courseUrl, response -> {
            Log.d("onResponse", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("msg").equals("success")) {
                    JSONArray array = jsonObject.getJSONArray("course");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject objClass = array.getJSONObject(i);
                        CourseHomeDao item = new CourseHomeDao(
                                objClass.getString("imgUrl"),
                                objClass.getString("courseID"),
                                objClass.getString("courseName"),
                                objClass.getString("courseStyle"),
                                objClass.getString("courseLength"),
                                objClass.getString("courseDesc")
                        );

                        courseList.add(item);
                    }

                    if (courseList.size() == 0) {
                        layoutShowEmpty.setVisibility(View.VISIBLE);
                        recyclerCourseView.setVisibility(View.GONE);
                        layoutShowFirstOpen.setVisibility(View.GONE);
                    } else {
                        layoutShowEmpty.setVisibility(View.GONE);
                        layoutShowFirstOpen.setVisibility(View.GONE);
                        recyclerCourseView.setVisibility(View.VISIBLE);
                        adapterCourse = new CourseHomeAdapter(courseListener, courseList, getContext());
                        recyclerCourseView.setAdapter(adapterCourse);
                    }

                    progressDialog.dismiss();
                }else{
                    layoutShowEmpty.setVisibility(View.VISIBLE);
                    recyclerCourseView.setVisibility(View.GONE);
                    layoutShowFirstOpen.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("styleID", styleID);

                return params;
            }
        };
        requestQueue.add(request);
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
