package com.sonnetindianetworks.tokenapp

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.hbb20.CountryCodePicker
import kotlinx.android.synthetic.main.activity_generate_token.*
import kotlinx.android.synthetic.main.activity_request_token.*
import java.util.*

class RequestToken : AppCompatActivity(),DatePickerDialog.OnDateSetListener {
    lateinit var orgName: EditText
    lateinit var orgMsg: EditText
    lateinit var mobileOrg: EditText
    lateinit var mobileYour: EditText
    lateinit var nameYour: EditText
    lateinit var dateOfRequesting: TextView
    var day = 0
    var month = 0
    var year = 0
    var finalDate: String = ""
    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_token)

        orgName = findViewById(R.id.orgName_RequestToken)
        orgMsg = findViewById(R.id.MsgOrg_RequestToken)
        mobileOrg = findViewById(R.id.OrgMobile_RequestToken)
        nameYour = findViewById(R.id.orgName_RequestToken)
        mobileYour = findViewById(R.id.YourMobile_requestToken)
        dateOfRequesting = findViewById(R.id.editText_date_RequestToken)

        button_RequestToken.setOnClickListener {
            sendRequest()
        }
    }


    private fun sendRequest() {
        val db = FirebaseFirestore.getInstance()
        val nameOrg = orgName.text.toString().trim()
        val mobileOrg = mobileOrg.text.toString().trim()
        val msgOrg = orgMsg.text.toString().trim()
        val nameYour = nameYour.text.toString().trim()
        val mobileYour = mobileYour.text.toString().trim()
        val ccpOrg: CountryCodePicker = findViewById(R.id.ccp_org)
val  ccpYour: CountryCodePicker = findViewById(R.id.ccp_your)
val orgMobile = "+" + ccpOrg.fullNumber + mobileOrg
        val yourMobile = "+" + ccpYour.fullNumber + mobileYour
        if (nameOrg.isEmpty() || mobileOrg.isEmpty() || msgOrg.isEmpty() || nameYour.isEmpty() || mobileYour.isEmpty()) {

            Toast.makeText(this, "Enter Valid Details ", Toast.LENGTH_SHORT).show()

        }
        else{
           // val reqToken = TokenRequest(nameOrg , )
        }


    }


    private fun pickDate() {

        button_Date_RequestToken.setOnClickListener {
            DatePickerDialog(this, this, year, month, day).show()

            val cal: Calendar = Calendar.getInstance()
            day = cal.get(Calendar.DAY_OF_MONTH)
            month = cal.get(Calendar.MONTH)
            year = cal.get(Calendar.YEAR)


        }

    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month + 1
        savedYear = year
        pickDate()
        val datePick = "$savedDay-$savedMonth-$savedYear"
        dateOfRequesting.text = datePick
        finalDate = datePick
    }

}

data class TokenRequest(
    val orgName: String? = null,
    val OrgMsg: String? = null,
    val mobileOrg: String? = null,
    val mobileYour: String? = null,
    val nameYour: String? = null,
    val dateOfRequesting: String? = null

)
