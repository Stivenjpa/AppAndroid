package com.example.appandroid

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_splash)

        // Retraso de 2 segundos antes de pasar a la siguiente actividad
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashActivity, Login::class.java))
            finish()
        }, 2000)
    }
}