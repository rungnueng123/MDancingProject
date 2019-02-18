package com.mocom.com.mdancingproject.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.mocom.com.mdancingproject.Adapter.StudentClassHistoryProfileAdapter;
import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.Dao.StudentClassHistoryProfileDao;
import com.mocom.com.mdancingproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mocom.com.mdancingproject.config.config.DATA_URL;

public class HistoryProfileFragment extends Fragment {

    private String jsonUrl = DATA_URL + "json_get_class_history_student.php";

    View layoutShowEmpty;
    TextView txtRecyclerDate;
    String userID;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<StudentClassHistoryProfileDao> classList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPreferences sharedPreferences;

    private ItemClickCallBack listener;

    public static HistoryProfileFragment newInstance() {
        HistoryProfileFragment fragment = new HistoryProfileFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_history_profile, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        initFindViewByID(rootView);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        classList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        swipeRefreshLayout.setOnRefreshListener(() -> {
            classList.clear();
            loadClassHistoryData();
            swipeRefreshLayout.setRefreshing(false);
        });

        loadClassHistoryData();


    }

    private void loadClassHistoryData() {
        if (classList != null || classList.size() > 0) {
            classList.clear();
        }
        userID = sharedPreferences.getString(getString(R.string.UserID), "");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, jsonUrl, response -> {
            Log.d("Onresponse", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("msg").equals("success")) {
                    JSONArray array = jsonObject.getJSONArray("class");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject objClass = array.getJSONObject(i);
                        StudentClassHistoryProfileDao item = new StudentClassHistoryProfileDao(
                                objClass.getString("eventID"),
                                objClass.getString("eventTitle"),
                                objClass.getString("description"),
                                objClass.getString("playlist"),
                                objClass.getString("eventStyle"),
                                objClass.getString("eventTeacher"),
                                objClass.getString("eventDate"),
                                objClass.getString("eventTime"),
                                objClass.getString("eventBranch"),
                                objClass.getString("imgUrl")
                        );

                        classList.add(item);
                    }

                    if (classList.size() == 0) {
                    } else {
                        adapter = new StudentClassHistoryProfileAdapter(listener, classList, getContext());
                        recyclerView.setAdapter(adapter);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userID", userID);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void initFindViewByID(View rootView) {
        recyclerView = rootView.findViewById(R.id.recycler_student_class_history);
        swipeRefreshLayout = rootView.findViewById(R.id.pullToRefresh);
        layoutShowEmpty = rootView.findViewById(R.id.layout_show_empty);
        txtRecyclerDate = rootView.findViewById(R.id.txt_recycler_home_empty);
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
