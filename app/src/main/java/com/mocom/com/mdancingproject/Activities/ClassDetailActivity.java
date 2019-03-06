package com.mocom.com.mdancingproject.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.mocom.com.mdancingproject.DialogFragment.CoinTypePaymentClassDialog;
import com.mocom.com.mdancingproject.DialogFragment.FailBuyClassDialog;
import com.mocom.com.mdancingproject.DialogFragment.PaymentClassDialog;
import com.mocom.com.mdancingproject.DialogFragment.SelectTypeForByCoinDialog;
import com.mocom.com.mdancingproject.DialogFragment.StyleDontEnoughDialog;
import com.mocom.com.mdancingproject.DialogFragment.SuccessBuyClassDialog;
import com.mocom.com.mdancingproject.DialogFragment.TypePaymentClassDialog;
import com.mocom.com.mdancingproject.PaymentGateway.PaymentGatewayCoinForClassActivity;
import com.mocom.com.mdancingproject.QRCode.QRCodeCoinForClassActivity;
import com.mocom.com.mdancingproject.R;
import com.mocom.com.mdancingproject.config.config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.mocom.com.mdancingproject.config.config.DATA_URL;
import static com.mocom.com.mdancingproject.config.config.HOST_URL;

public class ClassDetailActivity extends AppCompatActivity implements View.OnClickListener, TypePaymentClassDialog.OnInputListener,
        SelectTypeForByCoinDialog.OnSelectTypeForByCoinListener, StyleDontEnoughDialog.OnBackStyleListener,
        SuccessBuyClassDialog.OnBackSuccessBuyClassListener, FailBuyClassDialog.OnBackFailBuyClassListener, CoinTypePaymentClassDialog.OnBackCoinTypePaymentClassListener {

    public static final int COIN_CAN_PAY_CODE = 1;
    public static final int PAY_PACKAGE = 2;
    public static final int PAY_COIN_BY_PGW = 3; // buy coin and buy class together

    View layoutProgress;

    String getClassUrl = DATA_URL + "get_single_class.php";
    String canBuyClassByCoinUrl = DATA_URL + "buy_class_by_coin.php";
    String genQrForBuyCoinUrl = DATA_URL + "query_qr_for_buy_coin.php";
    String checkCanBuyClassByStyle = DATA_URL + "check_can_buy_class_by_style.php";
    String buyClassByStyleUrl = DATA_URL + "buy_class_by_style.php";
    String checkCoinUrl = DATA_URL + "check_coin_for_payment.php";
    String qrBuyClassUrl = DATA_URL + "gen_qr_for_buy_class.php";
    String eventID, coinAmt, eventName, userID, canBuy, eventStyleID, sharedUserID, imgUrl, youtubeUrl, numEmpty, buyAlready;
    Toolbar toolbar;

    ImageView imgClass, imgArrow;
    TextView txtClassName, txtPlaylist, txtStyle, txtTeacher, txtDate, txtTime, txtEmpty, txtBranch, txtCoin, txtDesc;
    public TextView txtTypePay;
    Button btnPayment;
    private SharedPreferences sharedPreferences;
    YouTubePlayerFragment youtubePlayer;

    @Override
    public void sendOnBackCoinTypePaymentClassListener(String back, String coin, String eventID) {
        if (back.equals(getResources().getString(R.string.cancel))) {
            Toast.makeText(this, back, Toast.LENGTH_SHORT).show();

        }
        if (back.equals(getResources().getString(R.string.confirm))) {
            checkCoinCanPayment(coin, eventID);
        }
    }

    private void checkCoinCanPayment(String coin, String eventid) {
        layoutProgress.setVisibility(View.VISIBLE);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userID = sharedPreferences.getString(getString(R.string.UserID), "");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, checkCoinUrl, response -> {
            Log.d("response", response);
            try {
                JSONObject obj = new JSONObject(response);
                layoutProgress.setVisibility(View.GONE);
                if (obj.getString("message").equals("enough")) {
                    goBuyClass(userID, eventid);
                } else {
                    openDialogSelectTypeForByCoin();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            layoutProgress.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("coin", coin);
                params.put("userID", userID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void sendOnBackSuccessBuyClassListener(String back) {
        if (back.equals(getResources().getString(R.string.txt_payment_class_success))) {
            loadClassDetail();
            Toast.makeText(this, back, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void sendOnBackFailBuyClassListener(String back) {
        if (back.equals(getResources().getString(R.string.txt_payment_class_fail))) {
            Toast.makeText(this, back, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void sendOnBackStyleListener(String close) {
        if (close.equals(getResources().getString(R.string.shop))) {
            Intent intent = new Intent(getApplicationContext(), PaymentPackageActivity.class);
            startActivity(intent);
        }
        if (close.equals(getResources().getString(R.string.close))) {
            Toast.makeText(this, close, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void sendInput(String input) {
        txtTypePay.setText(input);
        if (txtTypePay.getText().toString().equals(getResources().getString(R.string.txt_type_payment_coin))) {
            Bundle bundle = new Bundle();
            bundle.putString("coin", coinAmt);
            bundle.putString("eventID", eventID);
            bundle.putString("eventName", eventName);
            CoinTypePaymentClassDialog dialog = new CoinTypePaymentClassDialog();
            dialog.setArguments(bundle);
            dialog.show(getSupportFragmentManager(), "CoinTypePaymentClassDialog");

//            Intent intent = new Intent(getApplicationContext(), CoinTypePaymentClassActivity.class);
//            intent.putExtra("coin", coinAmt);
//            intent.putExtra("eventID", eventID);
//            intent.putExtra("eventName", eventName);
//            startActivityForResult(intent, COIN_CAN_PAY_CODE);
        } else if (txtTypePay.getText().toString().equals(getResources().getString(R.string.txt_type_payment_style_pack))) {
            checkCanBuyClassByStylePack(eventStyleID);
        }
    }

    private void checkCanBuyClassByStylePack(String eventStyleID) {
        layoutProgress.setVisibility(View.VISIBLE);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedUserID = sharedPreferences.getString(getString(R.string.UserID), "");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, checkCanBuyClassByStyle, response -> {
            Log.d("checkCanBy", response);
            try {
                layoutProgress.setVisibility(View.GONE);
                JSONObject obj = new JSONObject(response);
                if (obj.getString("msg").equals("can")) {
                    goBuyClassByStyle(eventStyleID, sharedUserID);
                } else {
                    openDialogStyleDontEnough();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            layoutProgress.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("eventStyleID", eventStyleID);
                params.put("userID", sharedUserID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void openDialogStyleDontEnough() {
        StyleDontEnoughDialog dialog = new StyleDontEnoughDialog();
        dialog.show(getSupportFragmentManager(), "StyleDontEnoughDialog");
    }

    private void goBuyClassByStyle(String eventStyleID, String sharedUserID) {
        layoutProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, buyClassByStyleUrl, response -> {
            Log.d("buyclassbystyle", response);
            try {
                layoutProgress.setVisibility(View.GONE);
                JSONObject obj = new JSONObject(response);
                if (obj.getString("message").equals("success")) {
                    openDialogSuccessBuyClass();
                } else {
                    openDialogFailBuyClass();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            layoutProgress.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("eventStyleID", eventStyleID);
                params.put("userID", sharedUserID);
                params.put("eventID", eventID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void openDialogSuccessBuyClass() {
        SuccessBuyClassDialog dialog = new SuccessBuyClassDialog();
        dialog.show(getSupportFragmentManager(), "SuccessBuyClassDialog");
    }

    private void openDialogFailBuyClass() {
        FailBuyClassDialog dialog = new FailBuyClassDialog();
        dialog.show(getSupportFragmentManager(), "FailBuyClassDialog");
    }

    @Override
    public void sendSelectTypeForByCoin(String input) {
//        Toast.makeText(getApplicationContext(), input, Toast.LENGTH_SHORT).show();
        if (input.equals(getResources().getString(R.string.txt_type_payment_coin_by_coin))) {
            goBuyClassWithCoinIsEqualToCoinAmt();
        } else if (input.equals(getResources().getString(R.string.txt_type_payment_coin_by_pack))) {
            Intent intent = new Intent(getApplicationContext(), PaymentPackageActivity.class);
            startActivityForResult(intent, PAY_PACKAGE);
        } else if (input.equals(getResources().getString(R.string.txt_type_payment_coin_by_qr))) {
//            Toast.makeText(getApplicationContext(), input, Toast.LENGTH_SHORT).show();
            genQrForBuyCoin();
        }
    }

    private void goBuyClassWithCoinIsEqualToCoinAmt() {
        Intent intent = new Intent(this, PaymentGatewayCoinForClassActivity.class);
        intent.putExtra("userID", userID);
        intent.putExtra("eventID", eventID);
        startActivityForResult(intent, PAY_COIN_BY_PGW);
    }

    private void genQrForBuyCoin() {
        layoutProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, qrBuyClassUrl, response -> {
            Log.d("response", response);
            try {
                layoutProgress.setVisibility(View.GONE);
                JSONObject obj = new JSONObject(response);
                if (obj.getString("msg").equals("success")) {
                    Intent intent = new Intent(this, QRCodeCoinForClassActivity.class);
                    intent.putExtra("userName",obj.getString("userName"));
                    intent.putExtra("userID",obj.getString("userID"));
                    intent.putExtra("eventID",obj.getString("eventID"));
                    intent.putExtra("eventName",obj.getString("eventName"));
                    intent.putExtra("coin",obj.getString("coin"));
                    intent.putExtra("baht",obj.getString("baht"));
                    startActivity(intent);

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(obj.getString("msg"))
                            .setNegativeButton("ok", null);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            layoutProgress.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("eventID", eventID);
                params.put("userID", userID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail);

        Intent intent = getIntent();
        eventID = intent.getStringExtra("eventID");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userID = sharedPreferences.getString(getString(R.string.UserID), "");
//        Toast.makeText(this, eventID, Toast.LENGTH_SHORT).show();

        initFindViewByID();
        initToolbar();
        initInstance();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initInstance() {
        loadClassDetail();

    }

    private void loadClassDetail() {
        layoutProgress.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, getClassUrl, response -> {
            Log.d("onResponse", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("buyAlready").equals("yes")) {
                    btnPayment.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shapebtncantpayment));
                }
                buyAlready = jsonObject.getString("buyAlready");
                if (jsonObject.getString("msg").equals("success")) {
                    JSONArray classArray = jsonObject.getJSONArray("class");
                    for (int i = 0; i < classArray.length(); i++) {
                        JSONObject obj = classArray.getJSONObject(i);
                        if (obj.getString("eventEmpty").equals("0")) {
                            btnPayment.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shapebtncantpayment));
                            txtEmpty.setTextColor(Color.RED);
                        }else if(obj.getString("canBuy").equals("cant")){
                            btnPayment.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shapebtncantpayment));
                        }
                        txtClassName.setText("คลาส : " + obj.getString("eventTitle"));
                        eventName = obj.getString("eventTitle");
                        txtPlaylist.setText("เพลง : " + obj.getString("playlist"));
                        txtStyle.setText("สไตล์ : " + obj.getString("eventStyle"));
                        txtTeacher.setText("ผู้สอน : " + obj.getString("eventTeacher"));
                        txtDate.setText("วันที่เรียน : " + obj.getString("eventDate"));
                        txtTime.setText("เวลา : " + obj.getString("eventTime"));
                        txtEmpty.setText("ว่าง : " + obj.getString("eventEmpty"));
                        numEmpty = obj.getString("eventEmpty");
                        txtBranch.setText("สาขา : " + obj.getString("eventBranch"));
                        txtCoin.setText(obj.getString("coin") + " Coins/Time");
                        coinAmt = obj.getString("coin");
                        txtDesc.setText(obj.getString("description"));
                        imgUrl = HOST_URL + obj.getString("imgUrl");
                        Glide.with(getApplicationContext())
                                .load(imgUrl)
                                .into(imgClass);
                        canBuy = obj.getString("canBuy");

                        if (obj.getString("youtubeUrl").equals("null")) {
                            youtubeUrl = "";
                        } else {
                            youtubeUrl = obj.getString("youtubeUrl");
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

                        eventStyleID = obj.getString("eventStyleID");
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
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("eventID", eventID);
                params.put("userID", userID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void initFindViewByID() {
        layoutProgress = findViewById(R.id.layout_progressbar);
        toolbar = findViewById(R.id.toolbar_class_detail);
        imgClass = findViewById(R.id.img_class);
        imgClass.setOnClickListener(this);
        imgArrow = findViewById(R.id.img_arrow);
        imgArrow.setOnClickListener(this);
        txtClassName = findViewById(R.id.txt_class_name);
        txtPlaylist = findViewById(R.id.txt_playlist);
        txtStyle = findViewById(R.id.txt_style);
        txtTeacher = findViewById(R.id.txt_teacher);
        txtDate = findViewById(R.id.txt_date);
        txtTime = findViewById(R.id.txt_time);
        txtEmpty = findViewById(R.id.txt_empty);
        txtBranch = findViewById(R.id.txt_branch);
        txtCoin = findViewById(R.id.txt_coin);
        txtDesc = findViewById(R.id.txt_show_desc);
        btnPayment = findViewById(R.id.btn_payment);
        btnPayment.setOnClickListener(this);
        txtTypePay = findViewById(R.id.txt_type_pay);
        youtubePlayer = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_player_footage);

    }

    @Override
    public void onClick(View v) {
        if (v == imgClass) {
            Intent intentShowPic = new Intent(this, ShowPictureActivity.class);
            intentShowPic.putExtra("imageUrl", imgUrl);
            startActivity(intentShowPic);
        }
        if (v == imgArrow) {
            if (txtDesc.getVisibility() == View.GONE) {
                imgArrow.setImageResource(R.drawable.ic_arrow_drop_down_open);
                txtDesc.setVisibility(View.VISIBLE);
            } else {
                imgArrow.setImageResource(R.drawable.ic_arrow_drop_down_close);
                txtDesc.setVisibility(View.GONE);
            }
        }
        if (v == btnPayment) {
            if(buyAlready.equals("yes")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getResources().getString(R.string.txt_buy_class_already))
                        .setNegativeButton("ok", null);
                AlertDialog alert = builder.create();
                alert.show();
            }else if (numEmpty.equals("0")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getResources().getString(R.string.txt_full_class))
                        .setNegativeButton("ok", null);
                AlertDialog alert = builder.create();
                alert.show();
            }else if (canBuy.equals("can")) {
                openDialogTypePay(coinAmt, eventID, eventName);
//                openDialogFragment(coinAmt, eventID, eventName);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("คลาสนี้เลยเวลาการสอนแล้ว ไม่สามารถซื้อได้")
                        .setNegativeButton("ok", null);
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    private void openDialogTypePay(String coinAmt, String eventID, String eventName) {
        Bundle bundle = new Bundle();
        bundle.putString("coin", coinAmt);
        bundle.putString("eventID", eventID);
        bundle.putString("eventName", eventName);
        TypePaymentClassDialog dialog = new TypePaymentClassDialog();
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "TypePaymentClassDialog");
    }

    private void openDialogFragment(String coinAmt, String eventID, String eventName) {
        Bundle bundle = new Bundle();
        bundle.putString("coin", coinAmt);
        bundle.putString("eventID", eventID);
        bundle.putString("eventName", eventName);
        PaymentClassDialog dialog = new PaymentClassDialog();
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "PaymentClassDialog");
    }

    private void openDialogSelectTypeForByCoin() {
        SelectTypeForByCoinDialog dialog = new SelectTypeForByCoinDialog();
        dialog.show(getSupportFragmentManager(), "SelectTypeForByCoinDialog");
    }

    private void goBuyClass(String userID, String eventID) {
        layoutProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, canBuyClassByCoinUrl, response -> {
            Log.d("response", response);
            try {
                layoutProgress.setVisibility(View.GONE);
                JSONObject obj = new JSONObject(response);
                if (obj.getString("message").equals("Payment Success")) {
                    openDialogSuccessBuyClass();
//                    Intent intent = new Intent(getApplicationContext(), StudentDashboardActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.putExtra("goProfile", "goProfile");
//                    startActivity(intent);
                } else {
                    openDialogFailBuyClass();
//                    goBuyClass(userID, eventID);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            layoutProgress.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("eventID", eventID);
                params.put("userID", userID);
                params.put("coin", coinAmt);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
//        Intent intent = new Intent(getActivity(), StudentDashboardActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK  |  Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadClassDetail();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COIN_CAN_PAY_CODE) {
//            Toast.makeText(getApplicationContext(), data.getStringExtra("message"), Toast.LENGTH_SHORT).show();
//            if (data.getStringExtra("message").equals("Cancel")) {
//                Toast.makeText(getApplicationContext(), data.getStringExtra("message"), Toast.LENGTH_SHORT).show();
//            }
//            if (data.getStringExtra("message").equals("enough")) {
//                goBuyClass(userID, eventID);
//            }
//            if (data.getStringExtra("message").equals("Your coin don't enough! Please go to shop!")) {
//                openDialogSelectTypeForByCoin();
//            }
        }
        if (requestCode == PAY_PACKAGE) {

        }
        if (requestCode == PAY_COIN_BY_PGW) {
            if(!data.getStringExtra("message").equals("null")) {
                Toast.makeText(getApplicationContext(), data.getStringExtra("message"), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
