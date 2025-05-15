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

/**
 * Actividad Login: Gestiona el inicio de sesión y registro de usuarios.
 * Utiliza Firebase Authentication para manejar las credenciales y el estado de verificación del correo.
 */
@SuppressLint("StaticFieldLeak") // Suprime advertencias sobre posibles fugas de memoria con vistas estáticas, común en ejemplos pero revisar en producción.
private lateinit var loginbtn: Button // Botón para iniciar sesión.
private lateinit var registrarbtn: Button // Botón para registrar un nuevo usuario.

@SuppressLint("StaticFieldLeak") // Suprime advertencias sobre posibles fugas de memoria con vistas estáticas.
private lateinit var emailEditText: EditText // Campo de texto para ingresar el email del usuario.
private lateinit var passwordEditText: EditText // Campo de texto para ingresar la contraseña del usuario.

class Login : ComponentActivity() {

    /**
     * Método llamado cuando la actividad se crea por primera vez.
     * Configura la vista, inicializa los componentes de la UI y define los listeners para los botones.
     * @param savedInstanceState Estado previamente guardado de la actividad, si existe.
     */
    @SuppressLint("MissingInflatedId") // Suprime advertencias si algún ID no se encuentra, útil durante el desarrollo.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Habilita la visualización de borde a borde para una UI más inmersiva.
        setContentView(R.layout.layout_login) // Establece el archivo de layout para esta actividad.

        // Inicialización de los componentes de la UI usando sus IDs definidos en el archivo XML.
        emailEditText = findViewById(R.id.TextEmail) //Se inicializan cuadros de texto
        passwordEditText = findViewById(R.id.TextPassword)
        loginbtn = findViewById(R.id.Btnlogin) //Se inicializa boton
        registrarbtn = findViewById(R.id.Btnregistrar)

        // Configura el listener para el botón de registrar.
        registrarbtn.setOnClickListener {
            // Obtiene el email y la contraseña ingresados por el usuario.
            val email =
                emailEditText.text.toString() //Convertimos datos que ingresa el usuario a text
            val password = passwordEditText.text.toString()

            // Valida que la contraseña tenga al menos 6 caracteres.
            if (password.length >= 6) { //Verificamos valides de contraseña
                registrarUsuario(email, password) // Llama a la función para registrar al usuario.

            } else {
                // Muestra un mensaje de error si la contraseña es demasiado corta.
                Toast.makeText(
                    this,
                    "La contraseña debe tener al menos 6 caracteres",
                    Toast.LENGTH_SHORT
                ).show()
            }


        }
        // Configura el listener para el botón de inicio de sesión.
        loginbtn.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            // Valida que ambos campos (email y contraseña) no estén vacíos.
            if (email.isNotEmpty() && password.isNotEmpty()) { //Validamos que el usuario ingrese los datos
                iniciarUsuario(email, password) // Llama a la función para iniciar sesión.
            } else {
                // Muestra un mensaje si algún campo está vacío.
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    /**
     * Registra un nuevo usuario en Firebase Authentication utilizando el email y la contraseña proporcionados.
     * Envía un correo de verificación al usuario tras un registro exitoso.
     * @param email Email del nuevo usuario.
     * @param password Contraseña para el nuevo usuario.
     */
    private fun registrarUsuario(
        email: String,
        password: String
    ) { //Registro del usuario en firebase
        val auth: FirebaseAuth = Firebase.auth // Obtiene la instancia de FirebaseAuth.
        // Intenta crear un nuevo usuario con el email y contraseña.
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task -> // Listener para manejar el resultado de la operación.
                if (task.isSuccessful) { // Si el registro es exitoso.
                    val user = auth.currentUser // Obtiene el usuario recién creado.
                    user?.sendEmailVerification() //Enviamos el correo de verificacion
                    // Muestra un mensaje de éxito y solicita la verificación del correo.
                    Toast.makeText(
                        this,
                        "Usuario registrado correctamente,verifique su direccion de correo",
                        Toast.LENGTH_SHORT
                    ).show()
                    limpiarDatos() // Limpia los campos de texto.

                } else { // Si el registro falla.
                    // val exception = task.exception // Se puede usar para depurar el error específico.
                    // Muestra un mensaje genérico de error (podría ser más específico).
                    Toast.makeText(
                        this,
                        "El usuario ya está registrado", // O "Error al registrar el usuario."
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }
    }

    /**
     * Inicia sesión de un usuario existente en Firebase Authentication.
     * Verifica si el correo del usuario ha sido validado antes de permitir el acceso.
     * Si el inicio de sesión es exitoso y el correo está verificado, navega a la actividad `Bienvenida`.
     * @param email Email del usuario que intenta iniciar sesión.
     * @param password Contraseña del usuario.
     */
    private fun iniciarUsuario(email: String, password: String) {
        val auth: FirebaseAuth = Firebase.auth // Obtiene la instancia de FirebaseAuth.
        // Intenta iniciar sesión con el email y contraseña proporcionados.
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task -> // Listener para manejar el resultado.
                if (task.isSuccessful) { // Si el inicio de sesión es exitoso.
                    val user = auth.currentUser
                    // Verifica que el usuario exista y que su email haya sido verificado.
                    if (user != null && user.isEmailVerified) { //Verificamos el correo
                        // Crea un Intent para navegar a la actividad Bienvenida.
                        val intent = Intent(this, Bienvenida::class.java)
                        intent.putExtra(
                            "Correo",
                            email
                        ) //Enviamos correo para ser usado en otras actividades
                        startActivity(intent) // Inicia la actividad Bienvenida.
                        // finish() // Opcional: finalizar LoginActivity para que no se pueda volver con el botón atrás.

                    } else { // Si el correo no ha sido verificado.
                        Toast.makeText(
                            this,
                            "El correo no ha sido verificado",
                            Toast.LENGTH_SHORT
                        ).show()

                    }


                } else { // Si el inicio de sesión falla (ej. credenciales incorrectas).
                    // val exception = task.exception // Útil para depuración.
                    Toast.makeText(
                        this,
                        "El usuario no esta registrado", // O "Credenciales incorrectas."
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }

    /**
     * Limpia el contenido de los campos de texto de email y contraseña.
     */
    private fun limpiarDatos() {
        emailEditText.text.clear() //Limpiamos
        passwordEditText.text.clear()
    }
}