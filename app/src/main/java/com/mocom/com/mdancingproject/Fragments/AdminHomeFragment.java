package com.mocom.com.mdancingproject.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mocom.com.mdancingproject.Activities.AdminClassDetailActivity;
import com.mocom.com.mdancingproject.Adapter.ClassAdapter;
import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.Dao.ClassDao;
import com.mocom.com.mdancingproject.Dao.StudentEventHomeDao;
import com.mocom.com.mdancingproject.R;
import com.shrikanthravi.collapsiblecalendarview.data.CalendarAdapter;
import com.shrikanthravi.collapsiblecalendarview.data.Day;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.mocom.com.mdancingproject.config.config.DATA_URL;

public class AdminHomeFragment extends Fragment {

    private String loadBranchUrl = DATA_URL + "get_all_branch.php";
    private String CalendarEventUrl = DATA_URL + "get_event_by_branch.php";
    private String CalendarClassUrl = DATA_URL + "get_class_by_branch_and_date.php";

    private ArrayList<String> branch = new ArrayList<String>();
    private Spinner spinnerBranch;

    CollapsibleCalendar collapsibleCalendar;
    Integer year, month, date, monthCheck;
    JSONArray arrayEvent;
    private List<StudentEventHomeDao> eventList = new ArrayList<>();

    View layoutProgress;
    String branchName;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ClassDao> classList;
    private ItemClickCallBack listener;
    TextView txtEmpty;
    String monthName;

    CalendarAdapter calendarAdapter;

    public static AdminHomeFragment newInstance() {
        AdminHomeFragment fragment = new AdminHomeFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_admin_home, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;

    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        initFindViewByID(rootView);

        loadAllBranch();

        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        classList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        listener = (view, position) -> {
            Intent intent = new Intent(getContext(), AdminClassDetailActivity.class);
            intent.putExtra("eventID", classList.get(position).getEventID());
            startActivity(intent);
        };

        Day day = collapsibleCalendar.getSelectedDay();
        year = day.getYear();
        month = (day.getMonth());
        date = day.getDay();
        monthCheck = month;
//        Log.d("111", year + "/" + month + "/" + date);
        loadClassData();
        collapsibleCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {
            @Override
            public void onDaySelect() {
                Day day = collapsibleCalendar.getSelectedDay();
                year = day.getYear();
                month = day.getMonth() + 1;
                date = day.getDay();
                classList.clear();
                loadClassData();
            }

            @Override
            public void onItemClick(View view) {
                monthCheck = month;

            }

            @Override
            public void onDataUpdate() {
                Log.d("eee", monthCheck + "/" + month);
                Day day = collapsibleCalendar.getSelectedDay();
                year = day.getYear();
                if (Objects.equals(monthCheck, month)) {
                    month = monthCheck;
                } else {
                    month = monthCheck;
                }
                date = day.getDay();
                Log.d("www", monthCheck + "/" + month);
                classList.clear();
                loadClassData();
            }

            @Override
            public void onMonthChange() {

            }

            @Override
            public void onWeekChange(int i) {
//                String week = String.valueOf(i);
//                Log.d("week",week);
            }
        });
    }

    private void initFindViewByID(View rootView) {
        spinnerBranch = rootView.findViewById(R.id.spinner_branch);
        collapsibleCalendar = rootView.findViewById(R.id.calendarView);
        recyclerView = rootView.findViewById(R.id.recycler_calendar_fragment);
        txtEmpty = rootView.findViewById(R.id.txt_recycler_calendar_fragment_empty);
        layoutProgress = rootView.findViewById(R.id.layout_progressbar);

    }

    private void loadAllBranch() {
        layoutProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, loadBranchUrl, response -> {
//            Log.d("Onresponse", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("msg").equals("success")) {
                    JSONArray array = jsonObject.getJSONArray("branch");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        branch.add(obj.getString("Branch"));
                    }

                    if (branch.size() > 0) {
                        setSpinner();
                    }
                    layoutProgress.setVisibility(View.GONE);
                } else {
                    layoutProgress.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            layoutProgress.setVisibility(View.GONE);
//            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void setSpinner() {
        ArrayAdapter<String> adapterBranch = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_text_layout, branch);
        adapterBranch.setDropDownViewResource(R.layout.branch_dropdown_item);
        spinnerBranch.setAdapter(adapterBranch);
//        Toast.makeText(this, "a", Toast.LENGTH_SHORT).show();
        spinnerBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branchName = branch.get(position);
                loadEventData();
//                initCalendarListener();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initCalendarListener() {
        Day day = collapsibleCalendar.getSelectedDay();
        year = day.getYear();
        month = (day.getMonth());
        date = day.getDay();
        monthCheck = month;
//        Log.d("111", year + "/" + month + "/" + date);
        classList.clear();
        loadClassData();
        collapsibleCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {
            @Override
            public void onDaySelect() {
                Day day = collapsibleCalendar.getSelectedDay();
                year = day.getYear();
                month = day.getMonth() + 1;
                date = day.getDay();
                classList.clear();
                loadClassData();
            }

            @Override
            public void onItemClick(View view) {
//                monthCheck = month;

            }

            @Override
            public void onDataUpdate() {
//                collapsibleCalendar = new CollapsibleCalendar(getApplicationContext());
//                Toast.makeText(getContext(), "sss", Toast.LENGTH_LONG).show();
//                Log.d("eee", monthCheck + "/" + month);
                Day day = collapsibleCalendar.getSelectedDay();
                year = day.getYear();
                if (Objects.equals(monthCheck, month)) {
                    month = day.getMonth();
                } else {
                    month = day.getMonth() + 1;
                }
                date = day.getDay();
//                Log.d("www", monthCheck + "/" + month);
                classList.clear();
                loadClassData();
            }

            @Override
            public void onMonthChange() {

            }

            @Override
            public void onWeekChange(int i) {
//                String week = String.valueOf(i);
//                Log.d("week",week);
            }
        });
    }

    private void loadClassData() {
//        Log.d("qqq", year + "/" + month + "/" + date);
        layoutProgress.setVisibility(View.VISIBLE);
        if (classList != null || classList.size() > 0) {
            classList.clear();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CalendarClassUrl, response -> {
//            Log.d("Onresponse", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                monthName = jsonObject.getString("monthName");
//                txtRecyclerDate.setText(date + " " + monthName + " " + year);
//                Toast.makeText(getContext(), jsonObject.getString("monthName"), Toast.LENGTH_LONG).show();
                if (jsonObject.getString("msg").equals("have class")) {
                    JSONArray array = jsonObject.getJSONArray("data");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        ClassDao item = new ClassDao(
                                obj.getString("eventID"),
                                obj.getString("eventStart"),
                                obj.getString("eventEnd"),
                                obj.getString("title"),
                                obj.getString("CourseID"),
                                obj.getString("gallery1"),
                                obj.getString("playlistTitle"),
                                obj.getString("courseStyleName"),
                                obj.getString("teacher")
                        );
//                        Toast.makeText(getContext(), jsonObject.getString("monthName"), Toast.LENGTH_LONG).show();
                        classList.add(item);
                    }
                    if (classList.size() == 0) {
                        txtEmpty.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        txtEmpty.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter = new ClassAdapter(listener, classList, getContext());
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                    }
                    layoutProgress.setVisibility(View.GONE);
                } else {
                    if (classList.size() == 0) {
                        txtEmpty.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                    layoutProgress.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            layoutProgress.setVisibility(View.GONE);
        }, error -> {
            layoutProgress.setVisibility(View.GONE);
            Log.d("error",error.getMessage());
//            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("year", year.toString());
                params.put("month", month.toString());
                params.put("date", date.toString());
                params.put("branchName", branchName);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void loadEventData() {
        layoutProgress.setVisibility(View.VISIBLE);
        if (eventList.size() > 0) {
            eventList.clear();
        }
//        classList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CalendarEventUrl, response -> {
//            Log.d("Onresponse", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("msg").equals("have event")) {
                    arrayEvent = jsonObject.getJSONArray("data");

                    for (int i = 0; i < arrayEvent.length(); i++) {
                        JSONObject obj = arrayEvent.getJSONObject(i);
                        StudentEventHomeDao item = new StudentEventHomeDao(
                                Integer.parseInt(obj.getString("year")),
                                Integer.parseInt(obj.getString("month")),
                                Integer.parseInt(obj.getString("day"))
                        );
                        eventList.add(item);
//                        if (eventList.size() > 0) {
//                            collapsibleCalendar = new CollapsibleCalendar(getApplicationContext());
//                            collapsibleCalendar.addEventTag(item.getYear(), item.getMonth() - 1, item.getDay());
//                        }
//                        collapsibleCalendar.addEventTag(item.getYear(), item.getMonth() - 1, item.getDay());
                    }

                    if (eventList.size() > 0) {
                        //TODO
//                        Log.d("aaa","aaa");
//                        collapsibleCalendar = new CollapsibleCalendar(getApplicationContext());
//                        calendarAdapter.getCalendar();
                        Calendar rightNow = Calendar.getInstance();
                        calendarAdapter = new CalendarAdapter(getContext(), rightNow);
                        calendarAdapter.refresh();
                        collapsibleCalendar.setAdapter(calendarAdapter);
                        for (int i = 0; i < eventList.size(); i++) {
                            collapsibleCalendar.addEventTag(eventList.get(i).getYear(), eventList.get(i).getMonth() - 1, eventList.get(i).getDay());
                        }
//                        initCalendarListener();
                    }
                    layoutProgress.setVisibility(View.GONE);

                } else {
                    Calendar rightNow = Calendar.getInstance();
                    calendarAdapter = new CalendarAdapter(getContext(), rightNow);
                    calendarAdapter.refresh();
                    collapsibleCalendar.setAdapter(calendarAdapter);
                    layoutProgress.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            layoutProgress.setVisibility(View.GONE);
//            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("year", year.toString());
                params.put("month", month.toString());
                params.put("date", date.toString());
                params.put("branchName", branchName);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void init(Bundle savedInstanceState) {
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
