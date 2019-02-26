package com.mocom.com.mdancingproject.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.mocom.com.mdancingproject.DialogFragment.StudentCoinPackPaymentDialog;
import com.mocom.com.mdancingproject.Fragments.StudentCoinFragment;
import com.mocom.com.mdancingproject.R;

public class PaymentPackageActivity extends AppCompatActivity implements StudentCoinPackPaymentDialog.OnSelectTypePayPackListener {

    public static final int PAY_PACKAGE = 2;

    Toolbar toolbar;
    String payPackage;



    @Override
    public void sendOnSelectTypePayPackCoinListener(String typePay, String coinPackID) {
        if(typePay.equals(getResources().getString(R.string.payment_gateway))){
            Toast.makeText(getApplicationContext(), typePay, Toast.LENGTH_SHORT).show();
        } else if(typePay.equals(getResources().getString(R.string.qr_code))){
            Toast.makeText(getApplicationContext(), typePay, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_package);

        initFindViewById();
        initInstance(savedInstanceState);
    }

    private void initFindViewById() {
        toolbar = findViewById(R.id.toolbar_payment_package);
    }

    private void initInstance(Bundle savedInstanceState) {
        initToolbar();

        if (savedInstanceState == null){
//            Bundle bundle = new Bundle();
//            bundle.putString("edttext", PAY_PACKAGE);
//            Fragment studentCoinFragment = new StudentCoinFragment();
//            studentCoinFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.payment_package_container, StudentCoinFragment.newInstance())
                    .commit();
        }
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());
    }
}
