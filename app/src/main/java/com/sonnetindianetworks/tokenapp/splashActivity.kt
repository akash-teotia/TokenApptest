package com.sonnetindianetworks.tokenapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class splashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        if (supportActionBar != null)
            supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity

            startActivity(Intent(this,Dashboard::class.java))

            // close this activity
            finish()
        }, 1000)
    }
}
