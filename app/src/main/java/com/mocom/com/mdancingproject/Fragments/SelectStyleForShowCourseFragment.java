package com.mocom.com.mdancingproject.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
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

public class SelectStyleForShowCourseFragment extends Fragment {

    private String styleUrl = DATA_URL + "get_style_all.php";
    private String courseUrl = DATA_URL + "get_course_style_all.php";
    View layoutShowFirstOpen, layoutShowEmpty, layoutProgress, layoutProgressStyle, layoutProgressCourse;
    String styleID = "";

    View selectView;
    Integer selectStyle = -1;

    private RecyclerView recyclerStyleView, recyclerCourseView;
    private RecyclerView.Adapter adapterStyle, adapterCourse;
    private List<StyleHomeDao> styleList;
    private List<CourseHomeDao> courseList;
    private ItemClickCallBack styleListener, courseListener;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public static SelectStyleForShowCourseFragment newInstance() {
        SelectStyleForShowCourseFragment fragment = new SelectStyleForShowCourseFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_select_style_for_show_course, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        initFindViewByID(rootView);

        ViewCompat.setNestedScrollingEnabled(recyclerCourseView, false);

        styleList = new ArrayList<>();
        recyclerStyleView.setHasFixedSize(true);
        recyclerStyleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        courseList = new ArrayList<>();
        recyclerCourseView.setHasFixedSize(true);
        recyclerCourseView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        loadStyle();


        styleListener = (view, position) -> {
//            Toast.makeText(getContext(), selectStyle.toString(), Toast.LENGTH_LONG).show();
            if (selectStyle != -1) {
                selectedItems.delete(selectStyle);
                selectView.setSelected(false);
            }

            selectStyle = position;
            selectView = view;
            selectedItems.put(position, true);
            view.setSelected(true);
            styleID = styleList.get(position).getStyleID();
            loadCourse();

        };

        courseListener = (view, position) -> {
            Intent intent = new Intent(getContext(), ClassActivity.class);
            intent.putExtra("courseID", courseList.get(position).getCourseID());
            intent.putExtra("courseName", courseList.get(position).getCourseName());
            startActivity(intent);
        };
    }

    private void initFindViewByID(View rootView) {
        recyclerStyleView = rootView.findViewById(R.id.recycler_style_home);
        recyclerCourseView = rootView.findViewById(R.id.recycler_course_home);
        layoutShowFirstOpen = rootView.findViewById(R.id.layout_show_first_open);
        layoutShowEmpty = rootView.findViewById(R.id.layout_show_empty);
        layoutProgress = rootView.findViewById(R.id.layout_progressbar);
        layoutProgressStyle = rootView.findViewById(R.id.layout_progressbar_style);
        layoutProgressCourse = rootView.findViewById(R.id.layout_progressbar_course);
    }

    private void loadStyle() {
//        layoutProgress.setVisibility(View.VISIBLE);
        recyclerStyleView.setVisibility(View.GONE);
//        layoutProgressStyle.setVisibility(View.VISIBLE);
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
                    recyclerStyleView.setVisibility(View.VISIBLE);
//                    layoutProgressStyle.setVisibility(View.GONE);
//                    layoutProgress.setVisibility(View.GONE);
                    loadCourse();

                } else {
//                    layoutProgress.setVisibility(View.GONE);
//                    layoutProgressStyle.setVisibility(View.GONE);
                    recyclerStyleView.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
//            layoutProgress.setVisibility(View.GONE);
//            layoutProgressStyle.setVisibility(View.GONE);
            recyclerStyleView.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void loadCourse() {
        recyclerCourseView.setVisibility(View.GONE);
//        layoutProgress.setVisibility(View.VISIBLE);
//        layoutProgressCourse.setVisibility(View.VISIBLE);
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
                    layoutProgress.setVisibility(View.GONE);
//                    layoutProgressCourse.setVisibility(View.GONE);
                } else {
                    layoutShowEmpty.setVisibility(View.VISIBLE);
                    recyclerCourseView.setVisibility(View.GONE);
                    layoutShowFirstOpen.setVisibility(View.GONE);
                    layoutProgress.setVisibility(View.GONE);
//                    layoutProgressCourse.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            layoutProgress.setVisibility(View.GONE);
//            layoutProgressCourse.setVisibility(View.GONE);
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
