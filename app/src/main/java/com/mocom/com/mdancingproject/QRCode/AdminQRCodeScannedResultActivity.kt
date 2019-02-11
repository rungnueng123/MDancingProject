package com.mocom.com.mdancingproject.QRCode

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.mocom.com.mdancingproject.Dao.QRCodeStudentGenObject
import com.mocom.com.mdancingproject.Helper.EncryptionHelper
import com.mocom.com.mdancingproject.R
import com.mocom.com.mdancingproject.Singleton.VolleySingleton
import com.mocom.com.mdancingproject.config.config.DATA_URL
import kotlinx.android.synthetic.main.activity_admin_qrcode_scanned_result.*
import org.json.JSONException
import org.json.JSONObject

class AdminQRCodeScannedResultActivity : AppCompatActivity() {

    private val getQrDataUrl: String = DATA_URL + "query_for_get_data_qr.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_qrcode_scanned_result)
        if (intent.getSerializableExtra(SCANNED_STRING) == null)
            throw RuntimeException("No encrypted String found in intent")
        val decryptedString = EncryptionHelper.getInstance().getDecryptionString(intent.getStringExtra(SCANNED_STRING))
        val userObject = Gson().fromJson(decryptedString, QRCodeStudentGenObject::class.java)
        val secretKey = userObject.secret_key
        val baht = userObject.baht
        val coinAmt = userObject.coinAmt

        val stringRequest = object : StringRequest(Request.Method.POST, getQrDataUrl, Response.Listener<String> { response ->
            try {
                val obj = JSONObject(response)
                if (obj.getString("msg") == "success") {
                    txt_scan_name.text = obj.getString("user")
                    txt_scan_package.text = obj.getString("coinPackName")
                    txt_scan_coin.text = coinAmt
                    txt_scan_bath.text = baht
                }
//                Toast.makeText(applicationContext, obj.getString("msg"), Toast.LENGTH_LONG).show()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        },
                object : Response.ErrorListener {
                    override fun onErrorResponse(volleyError: VolleyError) {
                        Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show()
                    }
                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params.put("secretKey", secretKey)
                return params
            }
        }
//        VolleySingleton.instance?.addToRequestQueue(stringRequest)
        VolleySingleton.getInstance(applicationContext).addToRequestQueue(stringRequest)

    }

    companion object {
        private const val SCANNED_STRING: String = "scanned_string"
        fun getScannedActivity(callingClassContext: Context, encryptedString: String): Intent {
            return Intent(callingClassContext, AdminQRCodeScannedResultActivity::class.java)
                    .putExtra(SCANNED_STRING, encryptedString)
        }
    }
}
