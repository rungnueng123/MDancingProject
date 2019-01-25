package com.mocom.com.mdancingproject.QRCode

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.gson.Gson
import com.mocom.com.mdancingproject.Dao.QRCodeGenObject
import com.mocom.com.mdancingproject.Helper.EncryptionHelper
import com.mocom.com.mdancingproject.R
import kotlinx.android.synthetic.main.activity_scanned.*

class ScannedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanned)
        if (intent.getSerializableExtra(SCANNED_STRING) == null)
            throw RuntimeException("No encrypted String found in intent")
        val decryptedString = EncryptionHelper.getInstance().getDecryptionString(intent.getStringExtra(SCANNED_STRING))
        val userObject = Gson().fromJson(decryptedString, QRCodeGenObject::class.java)
        scannedFullNameTextView.text = userObject.fullName
        scannedAgeTextView.text = userObject.age.toString()
    }

    companion object {
        private const val SCANNED_STRING: String = "scanned_string"
        fun getScannedActivity(callingClassContext: Context, encryptedString: String): Intent {
            return Intent(callingClassContext, ScannedActivity::class.java)
                    .putExtra(SCANNED_STRING, encryptedString)
        }
    }
}
