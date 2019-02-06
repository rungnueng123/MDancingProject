package com.mocom.com.mdancingproject.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mocom.com.mdancingproject.Adapter.StudentCoinPackageAdapter;
import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.Dao.StudentCoinPackageDao;
import com.mocom.com.mdancingproject.R;

import java.util.ArrayList;
import java.util.List;

public class StudentCoinPackageFragment extends Fragment {

    private static final String TAG = "fragment";
    private int position;
    private String title;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<StudentCoinPackageDao> coinPackageList;
    private ItemClickCallBack listener;

    public static StudentCoinPackageFragment newInstance(int position, String title) {
        StudentCoinPackageFragment fragment = new StudentCoinPackageFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("title", title);
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
        position = getArguments().getInt("position");
        title = getArguments().getString("someTitle");
        initFindViewByID(rootView);

        coinPackageList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        //Test RecyclerView >>>
        for(int i = 0; i<=10;i++){
            StudentCoinPackageDao studentClassHomeDao = new StudentCoinPackageDao(
                    "1",
                    "Gold Pack",
                    "2000",
                    "100",
                    "imgBanner/0001691001549012141--Asset 1@2x.png"
            );
            coinPackageList.add(studentClassHomeDao);
        }
        adapter = new StudentCoinPackageAdapter(coinPackageList,getContext());
        recyclerView.setAdapter(adapter);
        //Test RecyclerView <<<


    }

    private void initFindViewByID(View rootView) {
        recyclerView = rootView.findViewById(R.id.recycler_student_coin_package);
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
