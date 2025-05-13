package com.example.appandroid

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FormPets : AppCompatActivity() {

    // Definir los elementos del formulario
    private lateinit var nombreEdit: EditText
    private lateinit var edadEdit: EditText
    private lateinit var vacunasEdit: EditText
    private lateinit var duenoEdit: EditText
    private lateinit var veterinarioEdit: EditText
    private lateinit var guardarBtn: Button
    private lateinit var atrasBtn: Button

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_formpets)

        // Inicialización de los componentes de la interfaz
        nombreEdit = findViewById(R.id.editNombre)
        edadEdit = findViewById(R.id.editEdad)
        vacunasEdit = findViewById(R.id.editVacunas)
        duenoEdit = findViewById(R.id.editDueno)
        veterinarioEdit = findViewById(R.id.editVeterinario)
        guardarBtn = findViewById(R.id.btnGuardar)
        atrasBtn = findViewById(R.id.btnAtras)

        // Configurar el botón para guardar
        guardarBtn.setOnClickListener {
            guardarMascota()
        }

        atrasBtn.setOnClickListener{
            val intent = Intent(this, Bienvenida::class.java)
            startActivity(intent)
        }


    }

    private fun guardarMascota() {
        // Obtener los datos del formulario
        val nombre = nombreEdit.text.toString()
        val edad = edadEdit.text.toString().toIntOrNull() ?: 0
        val vacunas = vacunasEdit.text.toString()
        val dueno = duenoEdit.text.toString()
        val veterinario = veterinarioEdit.text.toString()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // Referencia a Firestore
        val db = FirebaseFirestore.getInstance()
        val nuevaMascotaRef = db.collection("mascotas").document()

        // Crear un mapa de datos para la mascota
        val mascota = hashMapOf(
            "vacunas" to vacunas,
            "edadMascota" to edad,
            "fotoUrl" to "",  // Vacío por ahora, luego agregarás la imagen
            "mascotaId" to nuevaMascotaRef.id,
            "nombreDueno" to dueno,
            "nombreMascota" to nombre,
            "nombreVeterinario" to veterinario,
            "userId" to userId
        )

        // Guardar los datos en Firestore
        nuevaMascotaRef.set(mascota)
            .addOnSuccessListener {
                // Mostrar mensaje de éxito
                Log.d("GuardarMascota", "Mascota guardada correctamente")
                Toast.makeText(this, "Mascota guardada", Toast.LENGTH_SHORT).show()
                finish()  // Cerrar la actividad después de guardar
            }
            .addOnFailureListener { exception ->
                // Mostrar mensaje de error
                Log.e("GuardarMascota", "Error al guardar: ${exception.message}")
                Toast.makeText(this, "Error al guardar: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
