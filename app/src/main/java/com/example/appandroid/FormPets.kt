package com.example.appandroid;

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Actividad FormPets: Permite al usuario registrar una nueva mascota.
 * Recopila información sobre la mascota y la guarda en Firebase Firestore.
 */
class FormPets : AppCompatActivity() {

    // Declaración de los componentes de la UI del formulario.
    private lateinit var nombreEdit: EditText      // Campo para el nombre de la mascota.
    private lateinit var edadEdit: EditText        // Campo para la edad de la mascota.
    private lateinit var vacunasEdit: EditText     // Campo para las vacunas de la mascota.
    private lateinit var duenoEdit: EditText       // Campo para el nombre del dueño.
    private lateinit var veterinarioEdit: EditText // Campo para el nombre del veterinario.
    private lateinit var guardarBtn: Button        // Botón para guardar los datos de la mascota.
    private lateinit var atrasBtn: ImageButton     // Botón para volver a la pantalla anterior (Bienvenida). (Cambiado a ImageButton si así lo tienes en el layout)

    /**
     * Método llamado cuando la actividad se crea por primera vez.
     * Infla el layout, inicializa los componentes de la UI y configura los listeners.
     */
    @SuppressLint("CutPasteId") // Anotación para suprimir advertencias de lint relacionadas con IDs, si es necesario.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establece el layout 'layout_formpets' como la interfaz de usuario para esta actividad.
        setContentView(R.layout.layout_formpets)

        // Inicialización de los componentes de la interfaz de usuario mediante sus IDs del XML.
        nombreEdit = findViewById(R.id.editNombre)
        edadEdit = findViewById(R.id.editEdad)
        vacunasEdit = findViewById(R.id.editVacunas)
        duenoEdit = findViewById(R.id.editDueno)
        veterinarioEdit = findViewById(R.id.editVeterinario)
        guardarBtn = findViewById(R.id.btnGuardar)
        atrasBtn = findViewById(R.id.btnAtras) // Asegúrate que el ID coincida y el tipo sea ImageButton si lo cambiaste en el XML

        // Configura el listener para el botón de guardar.
        guardarBtn.setOnClickListener {
            // Llama a la función para procesar y guardar los datos de la mascota.
            guardarMascota()
        }

        // Configura el listener para el botón de atrás.
        atrasBtn.setOnClickListener{
            // Crea un Intent para navegar de vuelta a la actividad Bienvenida.
            val intent = Intent(this, Bienvenida::class.java)
            // Inicia la actividad Bienvenida.
            startActivity(intent)
            // finish() // Opcional: Finaliza FormPets para que no quede en la pila de actividades al volver.
        }
    }

    /**
     * Recopila los datos del formulario, los valida (parcialmente) y los guarda en Firebase Firestore.
     */
    private fun guardarMascota() {
        // Obtiene el texto de cada EditText. Si están vacíos, se guarda una cadena vacía.
        val nombre = nombreEdit.text.toString().trim()
        // Convierte la edad a Int. Si no es un número válido o está vacío, se usa 0.
        val edad = edadEdit.text.toString().toIntOrNull() ?: 0
        val vacunas = vacunasEdit.text.toString().trim()
        val dueno = duenoEdit.text.toString().trim()
        val veterinario = veterinarioEdit.text.toString().trim()

        // Obtiene el ID del usuario actualmente autenticado en Firebase.
        // Si no hay usuario, no se procede con el guardado (early return).
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: run {
            Toast.makeText(this, "Error: Usuario no autenticado.", Toast.LENGTH_LONG).show()
            Log.w("GuardarMascota", "Intento de guardar mascota sin usuario autenticado.")
            return
        }

        // Validación simple para campos obligatorios (ejemplo: nombre de mascota).
        if (nombre.isEmpty()) {
            nombreEdit.error = "El nombre de la mascota es obligatorio"
            Toast.makeText(this, "Por favor, ingresa el nombre de la mascota.", Toast.LENGTH_SHORT).show()
            return
        }

        // Referencia a la instancia de Firebase Firestore.
        val db = FirebaseFirestore.getInstance()
        // Crea una referencia a un nuevo documento en la colección 'mascotas'. Firestore generará un ID único.
        val nuevaMascotaRef = db.collection("mascotas").document()

        // Crea un mapa (similar a un JSON) con los datos de la mascota.
        val mascota = hashMapOf(
            "vacunas" to vacunas,
            "edadMascota" to edad,
            "fotoUrl" to "",  // Campo para la URL de la foto, se deja vacío inicialmente.
            "mascotaId" to nuevaMascotaRef.id, // Guarda el ID del documento generado por Firestore.
            "nombreDueno" to dueno,
            "nombreMascota" to nombre,
            "nombreVeterinario" to veterinario,
            "userId" to userId // Asocia la mascota con el ID del usuario actual.
        )

        // Inicia la operación para guardar el mapa 'mascota' en el documento de Firestore.
        nuevaMascotaRef.set(mascota)
            .addOnSuccessListener { // Listener que se ejecuta si la operación de guardado es exitosa.
                Log.d("GuardarMascota", "Mascota guardada correctamente con ID: ${nuevaMascotaRef.id}")
                Toast.makeText(this, "Mascota guardada exitosamente", Toast.LENGTH_SHORT).show()
                finish()  // Cierra la actividad FormPets después de guardar, volviendo a la anterior.
            }
            .addOnFailureListener { exception -> // Listener que se ejecuta si la operación de guardado falla.
                Log.e("GuardarMascota", "Error al guardar la mascota: ${exception.message}", exception)
                Toast.makeText(this, "Error al guardar mascota: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }
}
