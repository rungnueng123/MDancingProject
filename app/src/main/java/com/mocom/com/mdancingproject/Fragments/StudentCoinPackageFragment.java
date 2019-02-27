package com.mocom.com.mdancingproject.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mocom.com.mdancingproject.Adapter.StudentCoinPackageAdapter;
import com.mocom.com.mdancingproject.Dao.StudentCoinPackageDao;
import com.mocom.com.mdancingproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.mocom.com.mdancingproject.config.config.DATA_URL;

public class StudentCoinPackageFragment extends Fragment {

    private static final String TAG = "fragment";
    private String coinPackUrl = DATA_URL + "json_get_coin_pack_student.php";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<StudentCoinPackageDao> coinPackageList;

    public static StudentCoinPackageFragment newInstance() {
        StudentCoinPackageFragment fragment = new StudentCoinPackageFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_student_coin_package, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        initFindViewByID(rootView);

        coinPackageList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        loadCoinPack();

        //Test RecyclerView >>>
//        for(int i = 0; i<=10;i++){
//            StudentCoinPackageDao studentClassHomeDao = new StudentCoinPackageDao(
//                    "1",
//                    "Gold Pack",
//                    "2000",
//                    "100",
//                    "imgBanner/0001691001549012141--Asset 1@2x.png"
//            );
//            coinPackageList.add(studentClassHomeDao);
//        }
//        adapter = new StudentCoinPackageAdapter(coinPackageList,getContext());
//        recyclerView.setAdapter(adapter);
        //Test RecyclerView <<<


    }

    private void loadCoinPack() {
        if (coinPackageList != null || coinPackageList.size() > 0) {
            coinPackageList.clear();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, coinPackUrl, response -> {
            Log.d("Onresponse", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("msg").equals("finish")) {
                    JSONArray array = jsonObject.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        StudentCoinPackageDao item = new StudentCoinPackageDao(
                                obj.getString("coinPackID"),
                                obj.getString("namePack"),
                                obj.getString("baht"),
                                obj.getString("coin"),
                                obj.getString("imgUrl")
                        );
                        coinPackageList.add(item);
                        adapter = new StudentCoinPackageAdapter(coinPackageList, getContext());
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(jsonObject.getString("message"))
                            .setNegativeButton("ok", null);
                    AlertDialog alert = builder.create();
                    alert.show();
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
        recyclerView = rootView.findViewById(R.id.recycler_student_coin_package);
    }

    @Override
    public void onResume() {
        super.onResume();
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
