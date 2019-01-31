package com.mocom.com.mdancingproject.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mocom.com.mdancingproject.Activities.StudentCourseActivity;
import com.mocom.com.mdancingproject.Adapter.StudentClassHomeAdapter;
import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.Dao.StudentClassHomeDao;
import com.mocom.com.mdancingproject.R;
import com.shrikanthravi.collapsiblecalendarview.data.Day;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mocom.com.mdancingproject.config.config.DATA_URL;

public class StudentClassHomeFragment extends Fragment {

    private String jsonUrl = DATA_URL + "json_get_class_home_student.php";

    CollapsibleCalendar collapsibleCalendar;
    Integer year, month, date;
    TextView txtRecyclerEmpty;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<StudentClassHomeDao> classList;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ItemClickCallBack listener;

    public static StudentClassHomeFragment newInstance() {
        StudentClassHomeFragment fragment = new StudentClassHomeFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_student_class_home, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        initFindViewByID(rootView);
        initCalendarListener();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listener = (view, position) -> {
            Intent intent = new Intent(getActivity(), StudentCourseActivity.class);
            String courseID = classList.get(position).getCourseID();
            Log.d("CourseID", courseID);
            intent.putExtra("courseID", courseID);
            startActivity(intent);
        };

        swipeRefreshLayout = rootView.findViewById(R.id.pullToRefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            classList.clear();
            loadClassData();
            swipeRefreshLayout.setRefreshing(false);
        });

        classList = new ArrayList<>();

        //Test RecyclerView >>>
//        for(int i = 0; i<=10;i++){
//            StudentClassHomeDao studentClassHomeDao = new StudentClassHomeDao(
//                    "1",
//                    "09:35",
//                    "02:00",
//                    "fdsfsdffd",
//                    "abcde1234",
//                    "11111111111111",
//                    "http://danceschool.matchbox-station.com/imgBanner/images.png",
//                    "เต่างอย - จินตหรา พูนลา",
//                    "K-pop Junior"
//            );
//            classList.add(studentClassHomeDao);
//        }
//        adapter = new StudentClassHomeAdapter(listener,classList,getContext());
//        recyclerView.setAdapter(adapter);
        //Test RecyclerView <<<

        loadClassData();
    }

    private void loadClassData() {
        classList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, jsonUrl, response -> {
            Log.d("Onresponse", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("msg").equals("have class")) {
                    JSONArray array = jsonObject.getJSONArray("data");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        StudentClassHomeDao item = new StudentClassHomeDao(
                                obj.getString("eventID"),
                                obj.getString("eventStart"),
                                obj.getString("eventEnd"),
                                obj.getString("description"),
                                obj.getString("title"),
                                obj.getString("CourseID"),
                                obj.getString("Course"),
                                obj.getString("gallery1"),
                                obj.getString("playlistTitle"),
                                obj.getString("courseStyleName")
                        );
                        classList.add(item);
                    }

                    if (classList.size() == 0) {
                        txtRecyclerEmpty.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        txtRecyclerEmpty.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter = new StudentClassHomeAdapter(listener, classList, getContext());
                        recyclerView.setAdapter(adapter);
                    }

                } else {
                    txtRecyclerEmpty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, error -> {
//                    Log.d("onError", error.toString());
//                    Toast.makeText(getActivity(), "เกิดข้อผิดพลาดโปรดลองอีกครั้ง", Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("year", year.toString());
                params.put("month", month.toString());
                params.put("date", date.toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void initFindViewByID(View rootView) {
        recyclerView = rootView.findViewById(R.id.recycler_student_class_home);
        swipeRefreshLayout = rootView.findViewById(R.id.pullToRefresh);
        collapsibleCalendar = rootView.findViewById(R.id.calendarView);
        txtRecyclerEmpty = rootView.findViewById(R.id.txt_recycler_home_empty);

    }

    private void initCalendarListener() {
        Day day = collapsibleCalendar.getSelectedDay();
        year = day.getYear();
        month = (day.getMonth());
        date = day.getDay();
//        Toast.makeText(getActivity(),year + "/" + month + "/" + date,Toast.LENGTH_LONG).show();
//        Log.d("create",year + "/" + month + "/" + date);
        collapsibleCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {
            @Override
            public void onDaySelect() {
                Day day = collapsibleCalendar.getSelectedDay();
                year = day.getYear();
                month = day.getMonth() + 1;
                date = day.getDay();
//                Log.d("select",year + "/" + month + "/" + date);
                loadClassData();
//                Toast.makeText(getActivity(),year + "/" + month + "/" + date,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onItemClick(View view) {

            }

            @Override
            public void onDataUpdate() {

            }

            @Override
            public void onMonthChange() {

            }

            @Override
            public void onWeekChange(int i) {

            }
        });
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
