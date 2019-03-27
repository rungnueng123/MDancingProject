package com.mocom.com.mdancingproject.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mocom.com.mdancingproject.Adapter.HaveStylePackAllAdapter;
import com.mocom.com.mdancingproject.Callback.ItemClickCallBack;
import com.mocom.com.mdancingproject.Dao.HaveStylePackDao;
import com.mocom.com.mdancingproject.DialogFragment.StylePackDetailDialog;
import com.mocom.com.mdancingproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mocom.com.mdancingproject.config.config.DATA_URL;

public class HaveStylePackAllActivity extends AppCompatActivity {

    String stylePackUrl = DATA_URL + "style_pack.php";
    String userID;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<HaveStylePackDao> styleList;
    private ItemClickCallBack styleListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_have_style_pack_all);

        initInstance();
    }

    private void initInstance() {
        initFindViewById();
        initToolbar();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        sharedPreferences = getSharedPreferences("dancing",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        styleList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        queryStylePack();

        styleListener = (view, position) -> {
            openDialogDetail(styleList.get(position).getHaveTime(), styleList.get(position).getStylePack(), styleList.get(position).getImgUrl(), styleList.get(position).getStyleName());
        };

    }

    private void openDialogDetail(String time, String stylePack, String imgUrl, String styleName) {
        Bundle bundle = new Bundle();
        bundle.putString("time", time);
        bundle.putString("stylePack", stylePack);
        bundle.putString("imgUrl", imgUrl);
        bundle.putString("styleName", styleName);
        StylePackDetailDialog dialog = new StylePackDetailDialog();
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "StylePackDetailDialog");
    }

    private void queryStylePack() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        userID = sharedPreferences.getString(getString(R.string.UserID), "");
        StringRequest request = new StringRequest(Request.Method.POST, stylePackUrl, response -> {
//            Log.d("onResponse", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("msg").equals("success")) {
                    //stylePack
                    JSONArray array1 = jsonObject.getJSONArray("styleData");
                    for (int i = 0; i < array1.length(); i++) {
                        JSONObject obj1 = array1.getJSONObject(i);
                        HaveStylePackDao item = new HaveStylePackDao(
                                obj1.getString("stylePack"),
                                obj1.getString("styleName"),
                                obj1.getString("haveTime"),
                                obj1.getString("imgUrl")
                        );
                        styleList.add(item);
                    }
                    adapter = new HaveStylePackAllAdapter(styleListener, styleList, this);
                    recyclerView.setAdapter(adapter);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
//                    Log.d("onError", error.toString());
//                    Toast.makeText(getActivity(), "เกิดข้อผิดพลาดโปรดลองอีกครั้ง", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userID", userID);

                return params;
            }
        };
        requestQueue.add(request);
    }

    private void initFindViewById() {
        toolbar = findViewById(R.id.toolbar_have_style_pack_all);
        recyclerView = findViewById(R.id.recycler_have_style_pack);

    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());
    }
}
