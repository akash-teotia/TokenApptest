package com.sonnetindianetworks.tokenapp

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.hbb20.CountryCodePicker
import kotlinx.android.synthetic.main.activity_generate_token.*
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class GenerateToken : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    lateinit var tokenNoGenerateToken: EditText

    lateinit var mobileGenerateToken: EditText
    var finalDate: String = ""
    lateinit var nameGenerateToken: EditText
    lateinit var dateGenerateToken: TextView
    lateinit var issuedByGenerateToken: EditText

    var day = 0
    var month = 0
    var year = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_token)

        tokenNoGenerateToken = findViewById(R.id.tokenNo_GenerateTokenActivity)
        mobileGenerateToken = findViewById(R.id.editText_mobile_GenerateTokenActivity)
        nameGenerateToken = findViewById(R.id.editText_name_GenerateTokenActivity)
        dateGenerateToken = findViewById(R.id.date_GenerateTokenActivity)
        issuedByGenerateToken = findViewById(R.id.editText_issuedBy_GenerateTokenActivity)

        button_sendToken_GenerateTokenActivity.setOnClickListener {
                      generateToken()
        }
        pickDate()

    }

    private fun generateToken() {

        val db = FirebaseFirestore.getInstance()
        val name = nameGenerateToken.text.toString().trim()

        val issuedBy = issuedByGenerateToken.text.toString()
        val date = finalDate
        val tokenNo = tokenNoGenerateToken.text.toString().toUpperCase(Locale.ROOT)
        val ccp: CountryCodePicker = findViewById(R.id.ccp)
        val dataMobile = "+" + ccp.fullNumberWithPlus + mobileGenerateToken.text.toString()
        Toast.makeText(this, dataMobile, Toast.LENGTH_SHORT).show()


        if (issuedBy.isEmpty() || date.isEmpty() || tokenNo.isEmpty() || name.isEmpty() || dataMobile.isEmpty()) {

            Toast.makeText(this, "Enter Valid Details", Toast.LENGTH_SHORT).show()
        } else {
            val token = TokenGenerate(tokenNo, issuedBy, dataMobile, name, date)

            db.collection("UserDetails").document(dataMobile).collection("IssuedToken")
                .document(issuedBy)
                .set(token, SetOptions.merge())
            Toast.makeText(this, "Token Successfully Issued + $date", Toast.LENGTH_SHORT).show()


        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_signOut -> {
                signOut()
            }

        }


        return super.onOptionsItemSelected(item)


    }

    private fun signOut() {
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        Toast.makeText(this, "Logout Successfully", Toast.LENGTH_SHORT).show()
    }


    private fun pickDate() {

        button_Date_GenerateToken.setOnClickListener {
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
        dateGenerateToken.text = datePick
        finalDate = datePick
    }


}

    data class TokenGenerate(
        val tokenNo: String? = null,
        val issuedBy: String? = null,
        val mobile: String? = null,
        val name: String? = null,
        val date: String? = null

    )
