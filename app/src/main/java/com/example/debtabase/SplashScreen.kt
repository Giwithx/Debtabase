package com.example.debtabase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.debtabase.activities.MainActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler(Looper.getMainLooper()).postDelayed({
                                                    var intent = Intent(this@SplashScreen, MainActivity::class.java)
                                                startActivity(intent)
                                                finish()

        }, 3000)
    }
}