package com.sonnetindianetworks.tokenapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var profession: EditText
    lateinit var name: EditText
    lateinit var mobile: EditText
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    lateinit var otp: EditText

    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()



        profession = findViewById(R.id.profession_activity_register)
        name = findViewById(R.id.name_activity_register)
        mobile = findViewById(R.id.mobile_text_activity_register)
        email = findViewById(R.id.email_activity_register)
        password = findViewById(R.id.password_activity_register)
        auth = FirebaseAuth.getInstance()


        already_have_account.setOnClickListener {
            Log.d("Login", " Tap already have account ")
            val intent = Intent(this, LoginActivity::class.java)

            startActivity(intent)
        }

        button_register.setOnClickListener {

            signUpUser()



        }


    }


    private fun signUpUser() {
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val email = email.text.toString()
        val password = password.text.toString()
        val profession = profession.text.toString()
        val name = name.text.toString().trim()
        val mobile = mobile.text.toString()

        if (email.isEmpty() || password.isEmpty() || profession.isEmpty() || name.isEmpty() || mobile.isEmpty()) {

            Toast.makeText(this, "Enter Valid Details", Toast.LENGTH_SHORT).show()
        } else {
            val user = User(name, profession, mobile.toInt(), email, password)

            db.collection("User").document(mobile).set(user, SetOptions.merge())
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        /*Toast.makeText(baseContext, "Authentication Successfully", Toast.LENGTH_SHORT)
                            .show()*/
                        intent = Intent(this, PhoneAuth::class.java)
                        intent.putExtra("key", mobile_text_activity_register.text.toString())


                        startActivity(intent)

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            baseContext, " Email Authentication failed ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    // ...
                }




        }


    }

}

data class User(
    val name: String? = null,
    val profession: String? = null,
    val mobile: Int? = null,
    val email: String? = null,
    val password: String? = null
)

