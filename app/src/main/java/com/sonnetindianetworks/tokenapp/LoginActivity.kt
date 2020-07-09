package com.sonnetindianetworks.tokenapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.TaskExecutors
import com.google.common.collect.MapMaker
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.hbb20.CountryCodePicker
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit
import java.util.prefs.Preferences


lateinit var auth: FirebaseAuth
lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
lateinit var mobile: EditText
lateinit var otp: EditText
lateinit var mobileNo: String
lateinit var sharedprefs: SharedPreferences
private lateinit var storedVerificationId: String
lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mobile = findViewById(R.id.mobile_LoginActivity)
        otp = findViewById(R.id.verify_otp_LoginActivity)
        auth = FirebaseAuth.getInstance()

        /* register_login.setOnClickListener {
            sendVerificationCodeToUser()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }*/

        verify_button_LoginActivity.setOnClickListener {




            if (otp.text.toString().trim().isNotEmpty()) {
                authenticate()
            } else {
                Toast.makeText(this, "Enter Valid Code", Toast.LENGTH_SHORT).show()

            }
            authenticate()
        }

        send_button_LoginActivity.setOnClickListener {

            if (mobile.text.isNotEmpty()) {

                sendVerificationCodeToUser()

            } else {
                Toast.makeText(this, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show()
            }

        }
    }


    private fun authenticate() {
        val otp = otp.text.toString()

if (::storedVerificationId.isInitialized ) {
    val credential = PhoneAuthProvider.getCredential(storedVerificationId, otp)
    signInWithPhoneAuthCredential(credential)

} else  {
    Toast.makeText(this, "Please Send OTP", Toast.LENGTH_SHORT).show()


}


    }

    private fun sendVerificationCodeToUser() {

        val ccp: CountryCodePicker = findViewById(R.id.ccp)


        verificationCallbacks()

        val phoneNo = "+" + ccp.fullNumber + mobile.text.toString()

        mobileNo = phoneNo


        Toast.makeText(this, phoneNo, Toast.LENGTH_LONG).show()

        PhoneAuthProvider.getInstance().verifyPhoneNumber(

            phoneNo, // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            TaskExecutors.MAIN_THREAD, // Activity (for callback binding)
            callbacks
        ) // OnVerificationStateChangedCallbacks


        sharedprefs = getSharedPreferences("MOBILE" , Context.MODE_PRIVATE)
        sharedprefs.edit().putString("MOBILE", phoneNo).apply()
    }

    private fun verificationCallbacks() {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                //Log.d(TAG, "onVerificationCompleted:$credential")

                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                //   Log.w(TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
//            Log.d(TAG, "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token

                // ...
            }
        }

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {


                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Phone", "signInWithCredential:success")

                    Toast.makeText(this, "Logged in Successfully", Toast.LENGTH_SHORT).show()
                    val mobile = mobile.text.toString()

                    val db = FirebaseFirestore.getInstance()
                    val uid = FirebaseAuth.getInstance().uid
                    val userdetail = UserDetails(mobileNo, uid)
                    db.collection("UserDetails").document(mobileNo)
                        .set(userdetail, SetOptions.merge())
                    val intent = Intent(this, Dashboard::class.java)
                    intent.putExtra("MOBILE", mobileNo)

                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    //  val user = task.result?.user
                    // ...
                } else {
                    // Sign in failed, display a message and update the UI
                    //Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(
                            this,
                            "verification code entered was invalid",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

    }


}

data class UserDetails(
    val mobile: String? = null,
    val uid: String? = null
)