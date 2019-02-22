package com.mocom.com.mdancingproject.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.mocom.com.mdancingproject.Adapter.ClassAdapter;
import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.Dao.ClassDao;
import com.mocom.com.mdancingproject.Dao.StudentEventHomeDao;
import com.mocom.com.mdancingproject.R;
import com.mocom.com.mdancingproject.config.config;
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

public class ClassActivity extends AppCompatActivity {

    private String getYoutubeUrl = DATA_URL + "get_youtube_by_course.php";
    private String loadBranchUrl = DATA_URL + "get_all_branch.php";
    private String jsonEventUrl = DATA_URL + "get_event_by_branch.php";
    private String jsonClassUrl = DATA_URL + "get_class_by_branch_and_date.php";

    String courseID, courseName, youtubeUrl, branchName;
    Integer year, month, date;
    Toolbar toolbar;
    private ArrayList<String> branch = new ArrayList<String>();
    private List<StudentEventHomeDao> eventHomeDaoList;
    private Spinner spinnerBranch;
    ProgressDialog progressDialog;

    YouTubePlayerFragment youtubePlayer;

    CollapsibleCalendar collapsibleCalendar;
    JSONArray arrayEvent;
    private List<StudentEventHomeDao> eventList = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ClassDao> classList;
    private ItemClickCallBack listener;
    TextView txtEmpty;
    String monthName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        Intent intent = getIntent();
        courseID = intent.getStringExtra("courseID");
        courseName = intent.getStringExtra("courseName");

        initInstance();
    }

    private void initInstance() {
        initFindViewByID();
        initToolbar();

        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        classList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        listener = (view, position) -> {
            Toast.makeText(getApplicationContext(), classList.get(position).getTitle(), Toast.LENGTH_LONG).show();
        };

        loadVideoYoutube();

        loadAllBranch();
//        collapsibleCalendar.addEventTag(2019, 02-1, 22);
    }

    private void loadVideoYoutube() {
        StringRequest request = new StringRequest(Request.Method.POST, getYoutubeUrl, response -> {
//            Log.d("onResponse", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("msg").equals("success")) {
                    if (jsonObject.getString("youtube").equals("null")) {
                        youtubeUrl = "";
                    } else {
                        youtubeUrl = jsonObject.getString("youtube");
                        youtubePlayer.initialize(config.getYoutubeKey(), new YouTubePlayer.OnInitializedListener() {
                            @Override
                            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                                youTubePlayer.cueVideo(youtubeUrl);
                            }

                            @Override
                            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                            }
                        });
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("courseID", courseID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    private void loadAllBranch() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("ProgressDialog"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, loadBranchUrl, response -> {
            Log.d("Onresponse", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("msg").equals("success")) {
                    JSONArray array = jsonObject.getJSONArray("branch");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        branch.add(obj.getString("Branch"));
                    }
                    progressDialog.dismiss();
                    if (branch.size() > 0) {
                        setSpinner();
                    }

                } else {
                    progressDialog.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show());

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void setSpinner() {
        ArrayAdapter<String> adapterBranch = new ArrayAdapter<String>(this,
                R.layout.spinner_text_layout, branch);
        adapterBranch.setDropDownViewResource(R.layout.branch_dropdown_item);
        spinnerBranch.setAdapter(adapterBranch);
//        Toast.makeText(this, "a", Toast.LENGTH_SHORT).show();
        spinnerBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branchName = branch.get(position);
//                Toast.makeText(getApplicationContext(), branchName, Toast.LENGTH_SHORT).show();
                initCalendarListener();
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
        loadEventData();
        loadClassData();
        collapsibleCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {
            @Override
            public void onDaySelect() {
                Day day = collapsibleCalendar.getSelectedDay();
                year = day.getYear();
                month = day.getMonth() + 1;
                date = day.getDay();
                loadClassData();
            }

            @Override
            public void onItemClick(View view) {

            }

            @Override
            public void onDataUpdate() {
//                collapsibleCalendar = new CollapsibleCalendar(getApplicationContext());
//                Toast.makeText(getApplicationContext(), "sss", Toast.LENGTH_LONG).show();
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

    private void loadEventData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("ProgressDialog"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        eventList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, jsonEventUrl, response -> {
            Log.d("Onresponse", response);
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
                        for (int i = 0; i < eventList.size(); i++) {
                            collapsibleCalendar.addEventTag(eventList.get(i).getYear(), eventList.get(i).getMonth() - 1, eventList.get(i).getDay());
                        }
                    }
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("year", year.toString());
                params.put("month", month.toString());
                params.put("date", date.toString());
                params.put("branchName", branchName);
                params.put("courseID", courseID);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void loadClassData() {
        if (classList != null || classList.size() > 0) {
            classList.clear();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, jsonClassUrl, response -> {
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
                        adapter = new ClassAdapter(listener, classList, getApplicationContext());
                        recyclerView.setAdapter(adapter);
                    }

                } else {
                    if (classList.size() == 0) {
                        txtEmpty.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        txtEmpty.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter = new ClassAdapter(listener, classList, getApplicationContext());
                        recyclerView.setAdapter(adapter);
                    }
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
                params.put("year", year.toString());
                params.put("month", month.toString());
                params.put("date", date.toString());
                params.put("branchName", branchName);
                params.put("courseID", courseID);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle("Course : " + courseName);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initFindViewByID() {
        youtubePlayer = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_player);
        toolbar = findViewById(R.id.toolbar_course_class_detail);
        spinnerBranch = findViewById(R.id.spinner_branch);
        collapsibleCalendar = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recycler_class_activity);
        txtEmpty = findViewById(R.id.txt_recycler_class_activity_empty);
    }
}
