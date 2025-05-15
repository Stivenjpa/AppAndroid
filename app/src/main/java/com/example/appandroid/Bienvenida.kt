package com.example.appandroid

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * Actividad Bienvenida: Pantalla principal después del inicio de sesión.
 * Muestra un mensaje de bienvenida personalizado con el correo del usuario y ofrece opciones de navegación.
 */
class Bienvenida : AppCompatActivity() {

    // Declaración tardía de los componentes de la UI para evitar que sean nulos antes de inflar el layout.
    lateinit var btnsalir: ImageButton      // Botón para cerrar sesión y volver al Login.
    lateinit var btnRegistrar: Button     // Botón para navegar al formulario de registro de mascotas.
    lateinit var btnPetsFrag: Button      // Botón para navegar a la vista de tarjetas de mascotas.

    /**
     * Método que se llama cuando la actividad es creada por primera vez.
     * Aquí se infla el layout, se inicializan los componentes de la UI y se configuran los listeners.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establece el archivo de layout XML 'layout_bienvenida' como la interfaz de usuario para esta actividad.
        setContentView(R.layout.layout_bienvenida)

        // Obtiene el correo del usuario pasado desde la actividad anterior (Login) a través del Intent.
        val emailUsuario = intent.getStringExtra("Correo")
        // Referencia al TextView donde se mostrará el correo.
        val emailTextView = findViewById<TextView>(R.id.textCorreo)
        // Actualiza el texto del TextView para mostrar "Usuario: [correo del usuario]".
        emailTextView.text = "Usuario: $emailUsuario"

        // Inicialización de los botones mediante su ID definido en el archivo XML de layout.
        btnsalir = findViewById(R.id.BtnSalir)
        btnRegistrar = findViewById(R.id.bntFormPets)
        btnPetsFrag = findViewById(R.id.bntFormPets2)

        // Configura el listener para el botón de registrar mascota.
        btnRegistrar.setOnClickListener {
            // Crea un Intent para iniciar la actividad FormPets.
            val intent = Intent(this, FormPets::class.java)
            // Inicia la actividad FormPets.
            startActivity(intent)
        }

        // Configura el listener para el botón de salir.
        btnsalir.setOnClickListener {
            // Crea un Intent para iniciar la actividad Login.
            val intent = Intent(this, Login::class.java)
            // Inicia la actividad Login, permitiendo al usuario cerrar sesión.
            startActivity(intent)
        }

        // Configura el listener para el botón de ver tarjetas de mascotas.
        btnPetsFrag.setOnClickListener {
            // Crea un Intent para iniciar la actividad PetsTar (presumiblemente donde se muestran las tarjetas).
            val intent = Intent(this, PetsTar::class.java)
            // Inicia la actividad PetsTar.
            startActivity(intent)
        }
    }
}
