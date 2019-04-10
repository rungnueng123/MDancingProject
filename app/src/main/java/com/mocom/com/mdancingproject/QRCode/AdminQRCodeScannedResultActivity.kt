package com.mocom.com.mdancingproject.QRCode

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.mocom.com.mdancingproject.Dao.QRCodeGenForBuyClassObject
import com.mocom.com.mdancingproject.Dao.QRCodeGenForCheckedObject
import com.mocom.com.mdancingproject.Dao.QRCodeStudentGenObject
import com.mocom.com.mdancingproject.DialogFragment.FailBuyClassDialog
import com.mocom.com.mdancingproject.DialogFragment.SuccessBuyClassDialog
import com.mocom.com.mdancingproject.Helper.EncryptionHelper
import com.mocom.com.mdancingproject.R
import com.mocom.com.mdancingproject.Singleton.VolleySingleton
import com.mocom.com.mdancingproject.config.config.DATA_URL
import kotlinx.android.synthetic.main.activity_admin_qrcode_scanned_result.*
import org.json.JSONException
import org.json.JSONObject

class AdminQRCodeScannedResultActivity : AppCompatActivity(), SuccessBuyClassDialog.OnBackSuccessBuyClassListener, FailBuyClassDialog.OnBackFailBuyClassListener {

    private val TAG: String = "ScannedResultActivity"
    private val getQrDataUrl: String = DATA_URL + "query_for_get_data_qr.php"
    private val buyCoinAndClassUrl: String = DATA_URL + "buy_class_and_coin_with_qr.php"
    private val buyCoinPackUrl: String = DATA_URL + "buy_coin_pack_with_qr.php"
    private val checkStudentClassUrl: String = DATA_URL + "check_student_have_class.php"
    private val userWrongClassUrl: String = DATA_URL + "user_wrong_class.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_qrcode_scanned_result)
        if (intent.getSerializableExtra(SCANNED_STRING) == null)
            throw RuntimeException("No encrypted String found in intent")
//        if (intent.getSerializableExtra(EVENT_ID_FOR_CHECK) == null)
//            throw RuntimeException("No encrypted String found in intent")
        val eventIDForCheck = intent.getStringExtra(EVENT_ID_FOR_CHECK)
//        Toast.makeText(applicationContext,eventIDForCheck,Toast.LENGTH_LONG).show()
        val decryptedString = EncryptionHelper.getInstance().getDecryptionString(intent.getStringExtra(SCANNED_STRING))
        val payCoinObject = Gson().fromJson(decryptedString, QRCodeStudentGenObject::class.java)
        val payCoinAndClassObject = Gson().fromJson(decryptedString, QRCodeGenForBuyClassObject::class.java)
        val checkedStudentObject = Gson().fromJson(decryptedString, QRCodeGenForCheckedObject::class.java)
        if (payCoinObject.secret_key != null) {
            //pay coin only
            val secretKey = payCoinObject.secret_key
            val baht = payCoinObject.baht
            val coinAmt = payCoinObject.coinAmt

            val stringRequest = object : StringRequest(Request.Method.POST, getQrDataUrl, Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response)
                    if (obj.getString("msg") == "success") {
                        layout_buy_coin_only.visibility = View.VISIBLE
                        layout_buy_coin_and_class.visibility = View.GONE
                        layout_btn_ok_and_cancel.visibility = View.VISIBLE
                        layout_show_checked_result.visibility = View.GONE
                        txt_scan_userid.text = obj.getString("userID")
                        txt_scan_coinpackid.text = obj.getString("coinPackID")
                        txt_scan_name.text = obj.getString("user")
                        txt_scan_package.text = obj.getString("coinPackName")
                        txt_scan_coin.text = coinAmt
                        txt_scan_baht.text = baht
//                        Toast.makeText(applicationContext,txt_scan_userid.text,Toast.LENGTH_LONG).show()
                    }
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
            VolleySingleton.getInstance(applicationContext).addToRequestQueue(stringRequest)
        } else if (payCoinAndClassObject.eventID != null && payCoinAndClassObject.baht != null) {
            layout_buy_coin_and_class.visibility = View.VISIBLE
            layout_buy_coin_only.visibility = View.GONE
            layout_btn_ok_and_cancel.visibility = View.VISIBLE
            layout_show_checked_result.visibility = View.GONE
            val userName = payCoinAndClassObject.userName
            val userID = payCoinAndClassObject.userID
            val eventID = payCoinAndClassObject.eventID
            val eventName = payCoinAndClassObject.eventName
            val coin = payCoinAndClassObject.coin
            val baht = payCoinAndClassObject.baht
            val coinHave = payCoinAndClassObject.coinHave
            txt_name_for_class.text = userName
            txt_event_for_class.text = eventName
            txt_coin_for_class.text = (coin.toInt() - coinHave.toInt()).toString()
            txt_baht_for_class.text = baht

        } else if (eventIDForCheck != null && !eventIDForCheck.isEmpty()) {
            if (checkedStudentObject.eventID != null && checkedStudentObject.userID != null && checkedStudentObject.checked != null) {
                layout_show_checked_result.visibility = View.VISIBLE
                layout_buy_coin_and_class.visibility = View.GONE
                layout_buy_coin_only.visibility = View.GONE
                layout_btn_ok_and_cancel.visibility = View.GONE
                layout_progressbar.visibility = View.VISIBLE

                if (checkedStudentObject.eventID == eventIDForCheck) {
                    val stringRequest = object : StringRequest(Request.Method.POST, checkStudentClassUrl, Response.Listener<String> { response ->
                        //                        Log.d(TAG, response)
                        layout_progressbar.visibility = View.GONE
                        try {
                            val obj = JSONObject(response)
                            when {
                                obj.getString("msg") == "cant regis" -> {
                                    img_status_check.setImageResource(R.drawable.ic_fail)
                                    txt_name_class_check.text = obj.getString("className")
                                    txt_name_student.text = obj.getString("userName")
                                    txt_status_student.setText(R.string.cant_regis)
                                }
                                obj.getString("msg") == "check already" -> {
                                    img_status_check.setImageResource(R.drawable.ic_fail)
                                    txt_name_class_check.text = obj.getString("className")
                                    txt_name_student.text = obj.getString("userName")
                                    txt_status_student.setText(R.string.checked_enroll)
                                }
                                obj.getString("msg") == "fail" -> {
                                    img_status_check.setImageResource(R.drawable.ic_fail)
                                    txt_name_student.setText(R.string.connect_fail)
                                }
                                obj.getString("msg") == "success" -> {
                                    img_status_check.setImageResource(R.drawable.ic_success)
                                    txt_name_class_check.text = obj.getString("className")
                                    txt_name_student.text = obj.getString("userName")
                                    txt_status_student.setText(R.string.checked_already)
                                }
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    },
                            object : Response.ErrorListener {
                                override fun onErrorResponse(volleyError: VolleyError) {
                                    layout_progressbar.visibility = View.GONE
                                    img_status_check.setImageResource(R.drawable.ic_fail)
                                    txt_name_student.setText(R.string.connect_fail)
                                }
                            }) {
                        @Throws(AuthFailureError::class)
                        override fun getParams(): MutableMap<String, String> {
                            val params = HashMap<String, String>()
                            params.put("userID", checkedStudentObject.userID)
                            params.put("eventID", eventIDForCheck)
                            return params
                        }
                    }
                    VolleySingleton.getInstance(applicationContext).addToRequestQueue(stringRequest)
                } else if (checkedStudentObject.eventID != eventIDForCheck) {
                    //เข้าผิดคลาส
                    val stringRequest = object : StringRequest(Request.Method.POST, userWrongClassUrl, Response.Listener<String> { response ->
                        Log.d(TAG, response)
                        layout_progressbar.visibility = View.GONE
                        try {
                            val obj = JSONObject(response)
                            when {
                                obj.getString("msg") == "fail" -> {
                                    img_status_check.setImageResource(R.drawable.ic_fail)
                                    txt_name_student.setText(R.string.connect_fail)
                                }
                                obj.getString("msg") == "success" -> {
                                    img_status_check.setImageResource(R.drawable.ic_fail)
                                    txt_name_student.text = obj.getString("userName")
                                    txt_name_class_check.text = obj.getString("className")
                                    txt_status_student.setText(R.string.checked_wrong_class)
                                }
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    },
                            object : Response.ErrorListener {
                                override fun onErrorResponse(volleyError: VolleyError) {
                                    layout_progressbar.visibility = View.GONE
                                    img_status_check.setImageResource(R.drawable.ic_fail)
                                    txt_name_student.setText(R.string.connect_fail)
                                }
                            }) {
                        @Throws(AuthFailureError::class)
                        override fun getParams(): MutableMap<String, String> {
                            val params = HashMap<String, String>()
                            params.put("userID", checkedStudentObject.userID)
                            params.put("eventID", eventIDForCheck)
                            return params
                        }
                    }
                    VolleySingleton.getInstance(applicationContext).addToRequestQueue(stringRequest)
                }

            }
        } else {
            layout_show_checked_result.visibility = View.VISIBLE
            layout_buy_coin_and_class.visibility = View.GONE
            layout_buy_coin_only.visibility = View.GONE
            layout_btn_ok_and_cancel.visibility = View.GONE
            img_status_check.setImageResource(R.drawable.ic_fail)
            txt_name_student.setText(R.string.use_function_in_class)
        }

        btn_checked_back.setOnClickListener {
            finish()
        }

        btn_back.setOnClickListener {
            finish()
        }

        btn_ok.setOnClickListener {
            if (!txt_scan_name.text.toString().isEmpty()) {
                layout_progressbar.visibility = View.VISIBLE
                val stringRequest = object : StringRequest(Request.Method.POST, buyCoinPackUrl, Response.Listener<String> { response ->
                    Log.d(TAG, response)
                    layout_progressbar.visibility = View.GONE
                    try {
                        val obj = JSONObject(response)
                        if (obj.getString("message") == "success") {
                            openDialogSuccess()
                        } else {
                            openDialogFail()
                        }
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
                        params.put("userID", txt_scan_userid.text.toString())
                        params.put("coinPackID", txt_scan_coinpackid.text.toString())
                        params.put("coin", txt_scan_coin.text.toString())
                        params.put("baht", txt_scan_baht.text.toString())
                        return params
                    }
                }
                VolleySingleton.getInstance(applicationContext).addToRequestQueue(stringRequest)
//                Toast.makeText(applicationContext, "a", Toast.LENGTH_LONG).show()
//                Toast.makeText(applicationContext, txt_scan_name.text, Toast.LENGTH_LONG).show()
            } else if (!txt_event_for_class.text.toString().isEmpty()) {
                layout_progressbar.visibility = View.VISIBLE
                val stringRequest = object : StringRequest(Request.Method.POST, buyCoinAndClassUrl, Response.Listener<String> { response ->
                    Log.d(TAG, response)
                    layout_progressbar.visibility = View.GONE
                    try {
                        val obj = JSONObject(response)
                        if (obj.getString("message") == "Payment Success") {
                            openDialogSuccess()
                        } else {
                            openDialogFail()
                        }
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
                        params.put("userID", payCoinAndClassObject.userID)
                        params.put("eventID", payCoinAndClassObject.eventID)
                        params.put("coin", payCoinAndClassObject.coin)
                        params.put("baht", payCoinAndClassObject.baht)
                        params.put("coinHave", payCoinAndClassObject.coinHave)
                        return params
                    }
                }
                VolleySingleton.getInstance(applicationContext).addToRequestQueue(stringRequest)
//                Toast.makeText(applicationContext, "b", Toast.LENGTH_LONG).show()
//                Toast.makeText(applicationContext, txt_event_for_class.text, Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun openDialogFail() {
        var failBuyClassDialog: FailBuyClassDialog = FailBuyClassDialog()
        failBuyClassDialog.show(supportFragmentManager, "failBuyClassDialog")
    }

    private fun openDialogSuccess() {
        var successBuyClassDialog: SuccessBuyClassDialog = SuccessBuyClassDialog()
        successBuyClassDialog.show(supportFragmentManager, "successBuyClassDialog")
    }

    override fun sendOnBackSuccessBuyClassListener(back: String?) {
        finish()
    }

    override fun sendOnBackFailBuyClassListener(back: String?) {
        finish()
    }

    companion object {
        private const val SCANNED_STRING: String = "scanned_string"
        private const val EVENT_ID_FOR_CHECK: String = "eventID"
        fun getScannedActivity(callingClassContext: Context, encryptedString: String, eventID: String): Intent {
            return Intent(callingClassContext, AdminQRCodeScannedResultActivity::class.java)
                    .putExtra(SCANNED_STRING, encryptedString)
                    .putExtra(EVENT_ID_FOR_CHECK, eventID)
        }
    }
}
