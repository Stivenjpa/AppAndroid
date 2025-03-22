package com.example.appandroid

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

lateinit var btnsalir:ImageButton
class Bienvenida : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.layout_bienvenida)

        val emailUsuario = intent.getStringExtra("Correo") //Obtenemos el correo de la actividad "login"
        val emailTextView = findViewById<TextView>(R.id.textCorreo)
        emailTextView.text = "Usuario: $emailUsuario" //Mostramos el correo

        btnsalir = findViewById(R.id.BtnSalir)
        btnsalir.setOnClickListener{
            val intent = Intent(this,Login::class.java) //Salimos a la pantalla "login"
            startActivity(intent)
        }

        }

    }

