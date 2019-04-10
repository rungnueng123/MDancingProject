package com.mocom.com.mdancingproject.Fragments;

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
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mocom.com.mdancingproject.Activities.ClassDetailActivity;
import com.mocom.com.mdancingproject.Adapter.ClassAdapter;
import com.mocom.com.mdancingproject.Adapter.ImageBannerAdapter;
import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.Dao.ClassDao;
import com.mocom.com.mdancingproject.Dao.ImageBannerDao;
import com.mocom.com.mdancingproject.Dao.StudentEventHomeDao;
import com.mocom.com.mdancingproject.R;
import com.shrikanthravi.collapsiblecalendarview.data.CalendarAdapter;
import com.shrikanthravi.collapsiblecalendarview.data.Day;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static com.mocom.com.mdancingproject.config.config.DATA_URL;
import static com.mocom.com.mdancingproject.config.config.HOST_URL;

public class CalendarFragment extends Fragment {

    private String BannerUrl = DATA_URL + "get_banner.php";
    private String loadBranchUrl = DATA_URL + "get_all_branch.php";
    private String CalendarEventUrl = DATA_URL + "get_event_by_branch.php";
    private String CalendarClassUrl = DATA_URL + "get_class_by_branch_and_date.php";

    LinearLayout layoutBanner;
    int width, heigth;
    String branchName;

    private static ViewPager viewPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private String[] urls;
    CirclePageIndicator indicator;

    private ArrayList<String> branch = new ArrayList<String>();
    private Spinner spinnerBranch;

    View layoutProgress;

    CollapsibleCalendar collapsibleCalendar;
    CalendarAdapter calendarAdapter;
    Integer year, month, date, monthCheck;
    JSONArray arrayEvent;
    private List<StudentEventHomeDao> eventList = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ClassDao> classList;
    private List<ImageBannerDao> bannerList;
    private ItemClickCallBack listener;
    TextView txtEmpty;
    String monthName;

    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
        assert getFragmentManager() != null;
        getFragmentManager().beginTransaction()
                .add(R.id.container_style_course, SelectStyleForShowCourseFragment.newInstance())
                .commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        initFindViewByID(rootView);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        heigth = width / 2;
        layoutBanner.setLayoutParams(new LinearLayout.LayoutParams(width, heigth));
//        Log.d("width",width+"/"+heigth);

        bannerList = new ArrayList<>();

        loadBanner();
        loadAllBranch();

        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        classList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        listener = (view, position) -> {
            Intent intent = new Intent(getContext(), ClassDetailActivity.class);
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
        viewPager = rootView.findViewById(R.id.pager);
        indicator = rootView.findViewById(R.id.indicator);
        spinnerBranch = rootView.findViewById(R.id.spinner_branch);
        collapsibleCalendar = rootView.findViewById(R.id.calendarView);
        recyclerView = rootView.findViewById(R.id.recycler_calendar_fragment);
        txtEmpty = rootView.findViewById(R.id.txt_recycler_calendar_fragment_empty);
        layoutProgress = rootView.findViewById(R.id.layout_progressbar);
        layoutBanner = rootView.findViewById(R.id.layout_banner);

    }

    private void loadBanner() {
        layoutProgress.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BannerUrl, response -> {
            Log.d("Onresponse", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("msg").equals("success")) {
                    JSONArray array = jsonObject.getJSONArray("data");
                    urls = new String[array.length()];
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        ImageBannerDao item = new ImageBannerDao(
                                obj.getString("id"),
                                obj.getString("title"),
                                obj.getString("desc"),
                                obj.getString("imgUrl"),
                                obj.getString("posterUrl")
                        );
                        bannerList.add(item);
                        urls[i] = HOST_URL + obj.getString("imgUrl");
//                        urls[2] = HOST_URL+"imgBanner/0860476001551410367--Mask Group 34.png";
                    }
//                    urls = new String[] {"https://demonuts.com/Demonuts/SampleImages/W-03.JPG",
//                            "https://demonuts.com/Demonuts/SampleImages/W-08.JPG",
//                            "https://demonuts.com/Demonuts/SampleImages/W-10.JPG",
//                            "https://demonuts.com/Demonuts/SampleImages/W-13.JPG",
//                            "https://demonuts.com/Demonuts/SampleImages/W-17.JPG",
//                            "https://demonuts.com/Demonuts/SampleImages/W-21.JPG"};

                    viewPager.setAdapter(new ImageBannerAdapter(getContext(), urls, bannerList));
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
                    layoutProgress.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            layoutProgress.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void loadAllBranch() {
        layoutProgress.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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

                    if (branch.size() > 0) {
                        layoutProgress.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        setSpinner();
                    }
                    layoutProgress.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                } else {
                    layoutProgress.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show());

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
//                initCalendarListener();
                loadEventData();
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
                monthCheck = month;
            }

            @Override
            public void onDataUpdate() {
//                collapsibleCalendar = new CollapsibleCalendar(getApplicationContext());
//                Toast.makeText(getApplicationContext(), "sss", Toast.LENGTH_LONG).show();
                Day day = collapsibleCalendar.getSelectedDay();
                year = day.getYear();
                if (Objects.equals(monthCheck, month)) {
                    month = day.getMonth();
                } else {
                    month = day.getMonth() + 1;
                }
                date = day.getDay();
//                Log.d("qqq",year+"/"+month+"/"+date);
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
        layoutProgress.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
                        recyclerView.setAdapter(adapter);
                    }
                    layoutProgress.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                } else {
                    if (classList.size() == 0) {
                        txtEmpty.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        txtEmpty.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter = new ClassAdapter(listener, classList, getContext());
                        recyclerView.setAdapter(adapter);
                    }
                    layoutProgress.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            layoutProgress.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }, error -> {
            layoutProgress.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        eventList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CalendarEventUrl, response -> {
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
                        Calendar rightNow = Calendar.getInstance();
                        calendarAdapter = new CalendarAdapter(getContext(), rightNow);
                        calendarAdapter.refresh();
                        collapsibleCalendar.setAdapter(calendarAdapter);
                        for (int i = 0; i < eventList.size(); i++) {
                            collapsibleCalendar.addEventTag(eventList.get(i).getYear(), eventList.get(i).getMonth() - 1, eventList.get(i).getDay());
                        }
                    }
                    layoutProgress.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                } else {
                    Calendar rightNow = Calendar.getInstance();
                    calendarAdapter = new CalendarAdapter(getContext(), rightNow);
                    calendarAdapter.refresh();
                    collapsibleCalendar.setAdapter(calendarAdapter);
                    layoutProgress.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            layoutProgress.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
