package com.example.appandroid

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText //Convierte a string lo que ingresa el usuario
import android.widget.Toast //Muestra mensajes en popup
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


@SuppressLint("StaticFieldLeak")
private lateinit var loginbtn: Button
private lateinit var registrarbtn: Button

@SuppressLint("StaticFieldLeak")
private lateinit var emailEditText: EditText
private lateinit var passwordEditText: EditText

class Login : ComponentActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.layout_login)

        emailEditText = findViewById(R.id.TextEmail) //Se inicializan cuadros de texto
        passwordEditText = findViewById(R.id.TextPassword)
        loginbtn = findViewById(R.id.Btnlogin) //Se inicializa boton
        registrarbtn = findViewById(R.id.Btnregistrar)

        registrarbtn.setOnClickListener {
            val email =
                emailEditText.text.toString() //Convertimos datos que ingresa el usuario a text
            val password = passwordEditText.text.toString()

            if (password.length >= 6) { //Verificamos valides de contraseña
                registrarUsuario(email, password)

            } else {
                Toast.makeText(
                    this,
                    "La contraseña debe tener al menos 6 caracteres",
                    Toast.LENGTH_SHORT
                ).show()
            }


        }
        loginbtn.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) { //Validamos que el usuario ingrese los datos
                iniciarUsuario(email, password)
            } else {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    private fun registrarUsuario(
        email: String,
        password: String
    ) { //Registro del usuario en firebase
        val auth: FirebaseAuth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(
                        this,
                        "Usuario registrado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    limpiarDatos()

                } else {
                    val exception = task.exception
                    Toast.makeText(
                        this,
                        "El usuario ya está registrado",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }
    }

    private fun iniciarUsuario(email: String, password: String) {
        val auth: FirebaseAuth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val intent = Intent(this, Bienvenida::class.java)
                    intent.putExtra(
                        "Correo",
                        email
                    ) //Enviamos correo para ser usado en otras actividades
                    startActivity(intent)
                } else {
                    val exception = task.exception
                    Toast.makeText(
                        this,
                        "El usuario no esta registrado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }

    private fun limpiarDatos() {
        emailEditText.text.clear() //Limpiamos
        passwordEditText.text.clear()
    }
}