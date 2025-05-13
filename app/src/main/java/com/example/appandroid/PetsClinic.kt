package com.example.appandroid

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore

class PetsClinic : Fragment() {

    private lateinit var nombreEdit: EditText
    private lateinit var edadEdit: EditText
    private lateinit var vacunasEdit: EditText
    private lateinit var duenoEdit: EditText
    private lateinit var veterinarioEdit: EditText
    private lateinit var guardarBtn: Button
    private lateinit var volverBtn: Button
    private lateinit var eliminarBtn: Button

    private val db = FirebaseFirestore.getInstance()

    private lateinit var mascotaId: String

    // Nuevo método para crear una nueva instancia del fragmento con el id de la mascota
    companion object {
        fun newInstance(mascotaId: String): PetsClinic {
            val fragment = PetsClinic()
            val args = Bundle()
            args.putString("mascotaId", mascotaId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pets_clinic, container, false)

        // Obtener el ID de la mascota desde los argumentos
        mascotaId = arguments?.getString("mascotaId") ?: ""

        // Inicializamos los componentes del formulario
        nombreEdit = view.findViewById(R.id.editNombre)
        edadEdit = view.findViewById(R.id.editEdad)
        vacunasEdit = view.findViewById(R.id.editVacunas)
        duenoEdit = view.findViewById(R.id.editDueno)
        veterinarioEdit = view.findViewById(R.id.editVeterinario)
        guardarBtn = view.findViewById(R.id.btnGuardar)
        volverBtn = view.findViewById(R.id.btnVolver)
        eliminarBtn = view.findViewById(R.id.btnEliminar)

        // Cargar los datos de la mascota
        cargarMascota()

        // Configurar el botón de guardar
        guardarBtn.setOnClickListener {
            actualizarMascota()
        }

        eliminarBtn.setOnClickListener {
            eliminarMascota()
        }


        volverBtn.setOnClickListener {
            val intent = Intent(requireContext(), PetsTar::class.java) //Para volver a una actividad desde un fragmento se debe usar requireContext() en vez de this
            startActivity(intent)
        }

        return view
    }

    private fun cargarMascota() {
        // Obtener los datos de la mascota desde Firestore
        db.collection("mascotas").document(mascotaId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Mapear los datos a las vistas
                    val mascota = document.toObject(Pet::class.java)
                    mascota?.let {
                        nombreEdit.setText(it.nombreMascota)
                        edadEdit.setText(it.edadMascota.toString())
                        vacunasEdit.setText(it.vacunas)
                        duenoEdit.setText(it.nombreDueno)
                        veterinarioEdit.setText(it.nombreVeterinario)
                    }
                } else {
                    Toast.makeText(requireContext(), "Mascota no encontrada", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Error al cargar datos: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun actualizarMascota() {
        // Crear un mapa con los datos actualizados de la mascota
        val updatedMascota = hashMapOf(
            "nombreMascota" to nombreEdit.text.toString(),
            "edadMascota" to edadEdit.text.toString().toInt(),
            "vacunas" to vacunasEdit.text.toString(),
            "nombreDueno" to duenoEdit.text.toString(),
            "nombreVeterinario" to veterinarioEdit.text.toString()
        )

        // Actualizar los datos en Firestore
        db.collection("mascotas").document(mascotaId)
            .set(updatedMascota)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Mascota actualizada", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack() // Regresar al fragment anterior
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Error al actualizar: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun eliminarMascota() {

        db.collection("mascotas").document(mascotaId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Mascota eliminada", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack() // Regresar al fragment anterior
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Error al eliminar: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

}
