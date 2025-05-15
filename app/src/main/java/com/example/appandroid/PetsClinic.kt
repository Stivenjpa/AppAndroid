package com.example.appandroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Fragmento PetsClinic: Permite ver, editar y eliminar los detalles de una mascota específica.
 * Obtiene el ID de la mascota de los argumentos, carga sus datos desde Firestore,
 * y permite al usuario actualizar o eliminar la mascota.
 */
class PetsClinic : Fragment() {

    // Declaración de los componentes de la UI del formulario.
    private lateinit var nombreEdit: EditText      // Campo para el nombre de la mascota.
    private lateinit var edadEdit: EditText        // Campo para la edad de la mascota.
    private lateinit var vacunasEdit: EditText     // Campo para las vacunas de la mascota.
    private lateinit var duenoEdit: EditText       // Campo para el nombre del dueño.
    private lateinit var veterinarioEdit: EditText // Campo para el nombre del veterinario.
    private lateinit var guardarBtn: Button        // Botón para guardar/actualizar los datos.
    private lateinit var volverBtn: ImageButton    // Botón para volver a la pantalla anterior (ahora ImageButton).
    private lateinit var eliminarBtn: Button       // Botón para eliminar la mascota.

    // Instancia de FirebaseFirestore para interactuar con la base de datos.
    private val db = FirebaseFirestore.getInstance()

    // ID de la mascota que se está visualizando/editando. Se inicializa en onCreateView.
    private lateinit var mascotaId: String

    /**
     * Companion object para proporcionar un método factoría (`newInstance`)
     * que crea una instancia de `PetsClinic` y le pasa el `mascotaId` como argumento.
     * Esto es útil para asegurar que los argumentos necesarios se pasan correctamente al fragmento.
     */
    companion object {
        /**
         * Crea una nueva instancia de PetsClinic, configurando el ID de la mascota como argumento.
         * @param mascotaId El ID de la mascota a mostrar/editar.
         * @return Una nueva instancia de PetsClinic.
         */
        fun newInstance(mascotaId: String): PetsClinic {
            val fragment = PetsClinic()
            val args = Bundle() // Bundle para pasar datos al fragmento.
            args.putString("mascotaId", mascotaId) // Añade el ID de la mascota al Bundle.
            fragment.arguments = args // Asigna los argumentos al fragmento.
            return fragment
        }
    }

    /**
     * Se llama para que el fragmento instancie su vista de interfaz de usuario.
     * Infla el layout, inicializa los componentes de la UI, obtiene el ID de la mascota
     * de los argumentos y configura los listeners para los botones.
     * @param inflater El LayoutInflater que se puede usar para inflar cualquier vista en el fragmento.
     * @param container Si no es nulo, esta es la vista padre a la que se debe adjuntar la UI del fragmento.
     * @param savedInstanceState Si no es nulo, este fragmento se está reconstruyendo a partir de un estado guardado anteriormente.
     * @return Devuelve la Vista para la UI del fragmento, o nulo.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout XML 'fragment_pets_clinic' para este fragmento.
        val view = inflater.inflate(R.layout.fragment_pets_clinic, container, false)

        // Obtiene el ID de la mascota desde los argumentos pasados a través de newInstance.
        // Si no se encuentra, se usa una cadena vacía (aunque esto debería manejarse con más robustez).
        mascotaId = arguments?.getString("mascotaId") ?: ""

        // Inicialización de los componentes de la UI definidos en el XML.
        nombreEdit = view.findViewById(R.id.editNombre)
        edadEdit = view.findViewById(R.id.editEdad)
        vacunasEdit = view.findViewById(R.id.editVacunas)
        duenoEdit = view.findViewById(R.id.editDueno)
        veterinarioEdit = view.findViewById(R.id.editVeterinario)
        guardarBtn = view.findViewById(R.id.btnGuardar)
        volverBtn = view.findViewById(R.id.btnVolver) // Asegúrate que el ID coincida y el tipo sea ImageButton.
        eliminarBtn = view.findViewById(R.id.btnEliminar)

        // Carga los datos de la mascota en los EditTexts si mascotaId no está vacío.
        if (mascotaId.isNotEmpty()) {
            cargarMascota()
        } else {
            // Manejo de caso donde mascotaId es vacío (ej. error o nueva mascota, aunque este fragmento parece ser para editar existentes).
            Toast.makeText(requireContext(), "ID de mascota no proporcionado.", Toast.LENGTH_LONG).show()
            Log.w("PetsClinic", "mascotaId está vacío en onCreateView.")
            // Podría ser útil deshabilitar botones o mostrar un mensaje al usuario.
        }

        // Configura el listener para el botón de guardar/actualizar.
        guardarBtn.setOnClickListener {
            if (mascotaId.isNotEmpty()) {
                actualizarMascota() // Llama a la función para actualizar los datos.
            } else {
                Toast.makeText(requireContext(), "No se puede actualizar sin ID de mascota.", Toast.LENGTH_SHORT).show()
            }
        }

        // Configura el listener para el botón de eliminar.
        eliminarBtn.setOnClickListener {
            if (mascotaId.isNotEmpty()) {
                eliminarMascota() // Llama a la función para eliminar la mascota.
            } else {
                Toast.makeText(requireContext(), "No se puede eliminar sin ID de mascota.", Toast.LENGTH_SHORT).show()
            }
        }

        // Configura el listener para el botón de volver.
        volverBtn.setOnClickListener {
            // Navega hacia atrás en la pila de fragmentos o a una actividad específica.
            // Si este fragmento se añadió con addToBackStack, popBackStack es lo usual.
            parentFragmentManager.popBackStack() 
            // Si se quiere ir a una actividad específica como PetsTar (aunque PetsTar parece ser un contenedor de fragmentos):
            // val intent = Intent(requireContext(), PetsTar::class.java)
            // startActivity(intent)
        }

        return view // Retorna la vista inflada del fragmento.
    }

    /**
     * Carga los datos de la mascota desde Firebase Firestore utilizando el `mascotaId`
     * y los muestra en los campos EditText correspondientes.
     */
    private fun cargarMascota() {
        // Accede al documento de la mascota en la colección 'mascotas' usando su ID.
        db.collection("mascotas").document(mascotaId)
            .get() // Realiza la operación de obtención.
            .addOnSuccessListener { document -> // Listener para el caso de éxito.
                if (document.exists()) { // Verifica si el documento existe.
                    // Convierte los datos del documento al objeto Pet.
                    val mascota = document.toObject(Pet::class.java)
                    mascota?.let { // Si la conversión es exitosa y el objeto no es nulo.
                        // Asigna los datos de la mascota a los EditTexts.
                        nombreEdit.setText(it.nombreMascota)
                        edadEdit.setText(it.edadMascota.toString())
                        vacunasEdit.setText(it.vacunas)
                        duenoEdit.setText(it.nombreDueno)
                        veterinarioEdit.setText(it.nombreVeterinario)
                    }
                } else {
                    // Muestra un mensaje si la mascota no se encuentra.
                    Toast.makeText(requireContext(), "Mascota no encontrada", Toast.LENGTH_SHORT).show()
                    Log.w("PetsClinic", "No se encontró la mascota con ID: $mascotaId")
                }
            }
            .addOnFailureListener { exception -> // Listener para el caso de fallo.
                Toast.makeText(requireContext(), "Error al cargar datos: ${exception.message}", Toast.LENGTH_SHORT).show()
                Log.e("PetsClinic", "Error al cargar mascota con ID: $mascotaId", exception)
            }
    }

    /**
     * Actualiza los datos de la mascota en Firebase Firestore con la información
     * ingresada en los campos EditText.
     */
    private fun actualizarMascota() {
        // Obtiene los valores de los EditText. Es importante validar y/o limpiar estos datos.
        val nombre = nombreEdit.text.toString().trim()
        val edad = edadEdit.text.toString().toIntOrNull() // Convertir a Int, manejando posible error de formato.
        val vacunas = vacunasEdit.text.toString().trim()
        val dueno = duenoEdit.text.toString().trim()
        val veterinario = veterinarioEdit.text.toString().trim()

        // Validación simple (ejemplo: nombre y edad no pueden ser vacíos/nulos).
        if (nombre.isEmpty()) {
            nombreEdit.error = "El nombre no puede estar vacío."
            return
        }
        if (edad == null) {
            edadEdit.error = "La edad debe ser un número válido."
            return
        }

        // Crea un mapa con los datos actualizados de la mascota.
        val updatedMascota = hashMapOf(
            "nombreMascota" to nombre,
            "edadMascota" to edad, // Guarda la edad como Int.
            "vacunas" to vacunas,
            "nombreDueno" to dueno,
            "nombreVeterinario" to veterinario
            // No se actualiza fotoUrl ni mascotaId aquí, ya que mascotaId es la clave del documento.
            // userId tampoco se actualiza generalmente en la edición por el usuario final de la mascota.
        )

        // Actualiza el documento en Firestore.
        // Usar `update` en lugar de `set` si solo se quieren modificar campos específicos y evitar sobrescribir el documento completo
        // si, por ejemplo, el mapa `updatedMascota` no incluyera todos los campos existentes.
        db.collection("mascotas").document(mascotaId)
            .update(updatedMascota as Map<String, Any>) // Se hace un cast a Map<String, Any> para el método update.
            .addOnSuccessListener { // Listener para el caso de éxito.
                Toast.makeText(requireContext(), "Mascota actualizada", Toast.LENGTH_SHORT).show()
                Log.d("PetsClinic", "Mascota actualizada con ID: $mascotaId")
                parentFragmentManager.popBackStack() // Regresar al fragmento anterior en la pila.
            }
            .addOnFailureListener { exception -> // Listener para el caso de fallo.
                Toast.makeText(requireContext(), "Error al actualizar: ${exception.message}", Toast.LENGTH_SHORT).show()
                Log.e("PetsClinic", "Error al actualizar mascota con ID: $mascotaId", exception)
            }
    }

    /**
     * Elimina la mascota actual de Firebase Firestore.
     */
    private fun eliminarMascota() {
        // Elimina el documento de la mascota de la colección 'mascotas'.
        db.collection("mascotas").document(mascotaId)
            .delete()
            .addOnSuccessListener { // Listener para el caso de éxito.
                Toast.makeText(requireContext(), "Mascota eliminada", Toast.LENGTH_SHORT).show()
                Log.d("PetsClinic", "Mascota eliminada con ID: $mascotaId")
                parentFragmentManager.popBackStack() // Regresar al fragmento anterior.
            }
            .addOnFailureListener { exception -> // Listener para el caso de fallo.
                Toast.makeText(requireContext(), "Error al eliminar: ${exception.message}", Toast.LENGTH_SHORT).show()
                Log.e("PetsClinic", "Error al eliminar mascota con ID: $mascotaId", exception)
            }
    }

}
