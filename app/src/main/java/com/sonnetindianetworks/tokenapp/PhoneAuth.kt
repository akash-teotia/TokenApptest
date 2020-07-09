package com.sonnetindianetworks.tokenapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.TaskExecutors.MAIN_THREAD
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_phone_auth.*
import java.util.concurrent.TimeUnit

class PhoneAuth : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks


    lateinit var mobile: String
    lateinit var otp: EditText
    private lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_auth)
        otp = findViewById(R.id.verify_otp_activity)
        auth = FirebaseAuth.getInstance()


        val mobileNo = intent.getStringExtra("key")
        val creEmail = intent.getStringExtra("KeyEmail")
        val crePassword = intent.getStringExtra("keyPassword")

 mobile =  mobileNo


        if (mobile.trim().isNotEmpty()) {

              sendVerificationCodeToUser()

        } else {
            Toast.makeText(this, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show()
        }



        verify_button.setOnClickListener {
            if (otp.text.toString().trim().isNotEmpty()) {
                authenticate()
            } else {
               Toast.makeText(this, "Enter Valid Code", Toast.LENGTH_SHORT).show()

            }
        }
    }


    private fun authenticate() {
        val otp = otp.text.toString()


        val credential = PhoneAuthProvider.getCredential(storedVerificationId, otp)


        signInWithPhoneAuthCredential(credential)





    }

    private fun sendVerificationCodeToUser() {
        verificationCallbacks()
        val phoneNo = mobile


//        Toast.makeText(this, phoneNo, Toast.LENGTH_LONG).show()

        PhoneAuthProvider.getInstance().verifyPhoneNumber(

            "+61$phoneNo", // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            MAIN_THREAD, // Activity (for callback binding)
            callbacks
        ) // OnVerificationStateChangedCallbacks

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



                  startActivity(Intent(this, Dashboard::class.java))
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
