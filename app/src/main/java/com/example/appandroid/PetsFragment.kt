package com.example.appandroid

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class PetsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PetsAdapter
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pets, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewMascotas)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        cargarMascotas()

        return view
    }

    private fun cargarMascotas() {
        db.collection("mascotas") // Asegúrate de que este nombre de colección sea correcto
            .get()
            .addOnSuccessListener { result ->
                val mascotasList = mutableListOf<Pet>()
                for (document in result) {
                    // Convierte el documento a un objeto de la clase Pet
                    val mascota = document.toObject(Pet::class.java)
                    mascota.mascotaId = document.id  // Asigna el id del documento al campo mascotaId
                    mascotasList.add(mascota)
                }

                // Configura el adaptador para el RecyclerView
                adapter = PetsAdapter(mascotasList) { mascota ->
                    // Cuando se selecciona una mascota, abre el fragmento de edición
                    val fragment = PetsClinic.newInstance(mascota.mascotaId)  // Usa mascotaId aquí
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_mascotas, fragment) // Asegúrate de que este ID sea el correcto
                        .addToBackStack(null)
                        .commit()
                }

                // Establece el adaptador en el RecyclerView
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { e ->
                Log.e("MascotasFragment", "Error al cargar mascotas", e)
            }
    }
}
