package com.mocom.com.mdancingproject.QRCode;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.mocom.com.mdancingproject.Dao.QRCodeGenObject;
import com.mocom.com.mdancingproject.Helper.EncryptionHelper;
import com.mocom.com.mdancingproject.Helper.QRCodeHelper;
import com.mocom.com.mdancingproject.R;

import java.util.Objects;

public class StudentQRCodeActivity extends AppCompatActivity implements View.OnClickListener {

    Button generateQrCodeButton;
    AppCompatEditText fullNameEditText, ageEditText;
    ImageView qrCodeImageView;


    String fullName, serializeString, encryptedString;
    Integer age;
//    QRCodeGenObject qrCodeGenObject;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_qrcode);
        initInstance();
    }

    private void initInstance() {
        initFindViewByID();
    }

    private void initFindViewByID() {
        fullNameEditText = findViewById(R.id.fullNameEditText);
        ageEditText = findViewById(R.id.ageEditText);
        generateQrCodeButton = findViewById(R.id.generateQrCodeButton);
        generateQrCodeButton.setOnClickListener(this);
        qrCodeImageView = findViewById(R.id.qrCodeImageView);

    }

    @Override
    public void onClick(View v) {
        if (v == generateQrCodeButton) {
//            val user = UserObject(fullName = fullNameEditText.getText().toString(), age = ageEditText.getText().toString());
            fullName = Objects.requireNonNull(fullNameEditText.getText()).toString();
            age = Integer.parseInt(Objects.requireNonNull(ageEditText.getText()).toString());

//            Toast.makeText(getApplication(),fullName+" "+age,Toast.LENGTH_LONG).show();
            QRCodeGenObject qrCodeGenObject = new QRCodeGenObject(fullName,age);
//            qrCodeGenObject.setFullName(fullName);
//            qrCodeGenObject.setAge(age);
            Gson gson = new Gson();
            serializeString  = gson.toJson(qrCodeGenObject);
            encryptedString = EncryptionHelper.getInstance().encryptionString(serializeString).encryptMsg();
            setImageBitmap(encryptedString);


        }
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
