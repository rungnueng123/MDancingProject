package com.mocom.com.mdancingproject.QRCode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.mocom.com.mdancingproject.Dao.QRCodeGenForBuyClassObject;
import com.mocom.com.mdancingproject.Helper.EncryptionHelper;
import com.mocom.com.mdancingproject.Helper.QRCodeHelper;
import com.mocom.com.mdancingproject.R;

public class QRCodeCoinForClassActivity extends AppCompatActivity {

    String userName, userID, eventID, eventName, coin, baht, serializeString, encryptedString;
    ImageView qrCodeImageView;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_coin_for_class);

        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        userID = intent.getStringExtra("userID");
        eventID = intent.getStringExtra("eventID");
        eventName = intent.getStringExtra("eventName");
        coin = intent.getStringExtra("coin");
        baht = intent.getStringExtra("baht");

        initFindViewByID();
        initInstance();
    }

    private void initFindViewByID() {
        qrCodeImageView = findViewById(R.id.qrCodeImageView);
    }

    private void initInstance() {
        genQrCode();
    }

    private void genQrCode() {
        QRCodeGenForBuyClassObject qrCodeGenForBuyClassObject = new QRCodeGenForBuyClassObject(userName, userID, eventID, eventName, coin, baht);
        Gson gson = new Gson();
        serializeString = gson.toJson(qrCodeGenForBuyClassObject);
        encryptedString = EncryptionHelper.getInstance().encryptionString(serializeString).encryptMsg();
        setImageBitmap(encryptedString);
    }

    private void setImageBitmap(String encryptedString) {
        bitmap = QRCodeHelper
                .newInstance(this)
                .setContent(encryptedString)
                .setErrorCorrectionLevel(ErrorCorrectionLevel.Q)
                .setMargin(2)
                .getQRCOde();
        qrCodeImageView.setImageBitmap(bitmap);
    }
}
