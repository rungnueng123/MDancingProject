package com.mocom.com.mdancingproject.Fragments;

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
import com.mocom.com.mdancingproject.Adapter.StudentStylePackageAdapter;
import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.Dao.StudentStylePackageDao;
import com.mocom.com.mdancingproject.DialogFragment.ConfirmPayStyleDialog;
import com.mocom.com.mdancingproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.mocom.com.mdancingproject.config.config.DATA_URL;

public class StudentStylePackageFragment extends Fragment {

    private static final String TAG = "fragment";
    private String stylePackUrl = DATA_URL + "json_get_style_pack_student.php";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<StudentStylePackageDao> stylePackageList;
    private ItemClickCallBack listener;

    public static StudentStylePackageFragment newInstance() {
        StudentStylePackageFragment fragment = new StudentStylePackageFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_student_style_package, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        initFindViewByID(rootView);
//        Toast.makeText(getActivity(), getActivity().getClass().getSimpleName() , Toast.LENGTH_SHORT).show();
        stylePackageList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        listener = (view, position) -> {
            openDialogStylePayFragment(stylePackageList.get(position).getStylePackID(),stylePackageList.get(position).getNamePack(),stylePackageList.get(position).getCoin(),stylePackageList.get(position).getTimes(),stylePackageList.get(position).getStyle());
        };

        loadStylePack();

    }

    private void openDialogStylePayFragment(String stylePackID, String namePack, String coin, String times, String style) {
        Bundle bundle = new Bundle();
        bundle.putString("stylePackID", stylePackID);
        bundle.putString("namePack", namePack);
        bundle.putString("coin", coin);
        bundle.putString("times", times);
        bundle.putString("style", style);
        ConfirmPayStyleDialog dialog = new ConfirmPayStyleDialog();
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "TypePaymentClassDialog");
    }

    private void loadStylePack() {
        if (stylePackageList != null || stylePackageList.size() > 0) {
            stylePackageList.clear();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, stylePackUrl, response -> {
            Log.d("Onresponse", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("msg").equals("success")) {
                    JSONArray array = jsonObject.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        StudentStylePackageDao item = new StudentStylePackageDao(
                                obj.getString("stylePackID"),
                                obj.getString("namePack"),
                                obj.getString("styleID"),
                                obj.getString("style"),
                                obj.getString("coin"),
                                obj.getString("times"),
                                obj.getString("imgUrl")
                        );
                        stylePackageList.add(item);
                        adapter = new StudentStylePackageAdapter(listener,stylePackageList, getContext());
                        recyclerView.setAdapter(adapter);
                    }
                } else {

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
        recyclerView = rootView.findViewById(R.id.recycler_student_style_package);
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
