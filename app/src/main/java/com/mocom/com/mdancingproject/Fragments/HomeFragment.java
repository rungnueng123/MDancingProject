package com.mocom.com.mdancingproject.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.mocom.com.mdancingproject.Adapter.CourseHomeAdapter;
import com.mocom.com.mdancingproject.Adapter.StyleHomeAdapter;
import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.Dao.CourseHomeDao;
import com.mocom.com.mdancingproject.Dao.StyleHomeDao;
import com.mocom.com.mdancingproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mocom.com.mdancingproject.config.config.DATA_URL;

public class HomeFragment extends Fragment {

    private String styleUrl = DATA_URL + "get_style_all.php";
    private String courseUrl = DATA_URL + "get_course_style_all.php";
    ProgressDialog progressDialog;

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

        styleList = new ArrayList<>();
        recyclerStyleView.setHasFixedSize(true);
        recyclerStyleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        courseList = new ArrayList<>();
        recyclerCourseView.setHasFixedSize(true);
        recyclerCourseView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        loadStyle();


        styleListener = (view, position) -> {
//            Toast.makeText(getContext(),styleList.get(position).getStyleID(),Toast.LENGTH_LONG).show();

            loadCourse(styleList.get(position).getStyleID());

        };

    }

    private void initFindViewByID(View rootView) {
        recyclerStyleView = rootView.findViewById(R.id.recycler_style_home);
        recyclerCourseView = rootView.findViewById(R.id.recycler_course_home);
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

                }else{
                    progressDialog.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show());

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void loadCourse(String styleID) {
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
                                objClass.getString("courseDesc")
                        );

                        courseList.add(item);
                    }

                    adapterCourse = new CourseHomeAdapter(courseListener, courseList, getContext());
                    recyclerCourseView.setAdapter(adapterCourse);

                    progressDialog.dismiss();
                }else{
                    progressDialog.dismiss();
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
