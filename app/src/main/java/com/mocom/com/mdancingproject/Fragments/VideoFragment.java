package com.mocom.com.mdancingproject.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mocom.com.mdancingproject.Adapter.YoutubeSuggestAdapter;
import com.mocom.com.mdancingproject.Dao.YoutubeSuggestDao;
import com.mocom.com.mdancingproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.mocom.com.mdancingproject.config.config.DATA_URL;

public class VideoFragment extends Fragment {

    private String VideoSuggestUrl = DATA_URL + "get_youtube_suggest.php";
    View layoutProgress;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<YoutubeSuggestDao> youtubeList;

    public static VideoFragment newInstance() {
        VideoFragment fragment = new VideoFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_video, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        initFindViewByID(rootView);

        youtubeList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        loadVideo();

//        for (int i = 0; i <= 10; i++) {
//            YoutubeSuggestDao youtubeSuggestDao = new YoutubeSuggestDao(
//                    "aaa",
//                    "ioAZPphRuTs"
//            );
//            youtubeList.add(youtubeSuggestDao);
//        }
//        adapter = new YoutubeSuggestAdapter(youtubeList, getContext());
//        recyclerView.setAdapter(adapter);

    }

    private void loadVideo() {
        layoutProgress.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if (youtubeList != null || youtubeList.size() > 0) {
            youtubeList.clear();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, VideoSuggestUrl, response -> {
            Log.d("Onresponse", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("msg").equals("success")) {
                    JSONArray array = jsonObject.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        YoutubeSuggestDao item = new YoutubeSuggestDao(
                                obj.getString("title"),
                                obj.getString("youtubeUrl"),
                                obj.getString("author")
                        );
                        youtubeList.add(item);
                    }
                    adapter = new YoutubeSuggestAdapter(youtubeList, getContext());
                    recyclerView.setAdapter(adapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            layoutProgress.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }, error -> {
            layoutProgress.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void initFindViewByID(View rootView) {
        recyclerView = rootView.findViewById(R.id.recycler_clip);
        layoutProgress = rootView.findViewById(R.id.layout_progressbar);
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
