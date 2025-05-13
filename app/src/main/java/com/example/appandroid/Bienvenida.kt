package com.example.appandroid

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Bienvenida : AppCompatActivity() {

    lateinit var btnsalir: ImageButton
    lateinit var btnRegistrar: Button
    lateinit var btnPetsFrag: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_bienvenida)

        val emailUsuario =
            intent.getStringExtra("Correo") //Obtenemos el correo de la actividad "login"
        val emailTextView = findViewById<TextView>(R.id.textCorreo)
        emailTextView.text = "Usuario: $emailUsuario" //Mostramos el correo

        btnsalir = findViewById(R.id.BtnSalir)
        btnRegistrar = findViewById(R.id.bntFormPets)
        btnPetsFrag = findViewById(R.id.bntFormPets2)

        btnRegistrar.setOnClickListener {
         val intent = Intent(this, FormPets::class.java) //Salimos a la pantalla "FormPets"
          startActivity(intent)
       }


        btnsalir.setOnClickListener {
            val intent = Intent(this, Login::class.java) //Salimos a la pantalla "login"
            startActivity(intent)
        }

        btnPetsFrag.setOnClickListener {
            val intent = Intent(this, PetsTar::class.java) //Salimos a la pantalla "login"
            startActivity(intent)
        }
    }
}
