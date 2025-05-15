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

/**
 * Fragmento PetsFragment: Muestra una lista de mascotas obtenidas de Firebase Firestore.
 * Utiliza un RecyclerView y PetsAdapter para mostrar cada mascota en una tarjeta.
 * Permite al usuario seleccionar una mascota para ver/editar sus detalles en `PetsClinic` fragment.
 */
class PetsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView // RecyclerView para mostrar la lista de mascotas.
    private lateinit var adapter: PetsAdapter       // Adaptador para el RecyclerView.
    private val db = FirebaseFirestore.getInstance() // Instancia de FirebaseFirestore para acceder a la base de datos.

    /**
     * Se llama para que el fragmento instancie su vista de interfaz de usuario.
     * Infla el layout `fragment_pets.xml`, inicializa el RecyclerView y su LayoutManager,
     * y llama a `cargarMascotas()` para obtener y mostrar los datos.
     *
     * @param inflater El LayoutInflater que se puede usar para inflar cualquier vista en el fragmento.
     * @param container Si no es nulo, esta es la vista padre a la que se debe adjuntar la UI del fragmento.
     * @param savedInstanceState Si no es nulo, este fragmento se está reconstruyendo a partir de un estado guardado anteriormente.
     * @return Devuelve la Vista para la UI del fragmento, o nulo.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout XML para este fragmento.
        val view = inflater.inflate(R.layout.fragment_pets, container, false)
        // Inicializa el RecyclerView desde el layout.
        recyclerView = view.findViewById(R.id.recyclerViewMascotas)
        // Configura el LayoutManager para el RecyclerView (LinearLayoutManager por defecto para listas verticales).
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Llama a la función para cargar las mascotas desde Firestore.
        cargarMascotas()

        return view // Retorna la vista inflada.
    }

    /**
     * Carga la lista de mascotas desde la colección "mascotas" en Firebase Firestore.
     * En caso de éxito, crea y configura el `PetsAdapter` con los datos obtenidos
     * y lo asigna al RecyclerView. También maneja la navegación al fragmento `PetsClinic`
     * cuando se selecciona una mascota.
     */
    private fun cargarMascotas() {
        db.collection("mascotas") // Accede a la colección "mascotas". Asegúrate de que el nombre sea correcto.
            .get() // Realiza la operación de obtención de todos los documentos en la colección.
            .addOnSuccessListener { result -> // Listener para el caso de éxito.
                val mascotasList = mutableListOf<Pet>() // Crea una lista mutable para almacenar las mascotas.
                for (document in result) { // Itera sobre cada documento obtenido.
                    // Convierte el documento de Firestore a un objeto de la clase Pet.
                    val mascota = document.toObject(Pet::class.java)
                    // Asigna el ID del documento de Firestore al campo mascotaId del objeto Pet.
                    // Esto es crucial para poder referenciar el documento específico después (ej. al editar o eliminar).
                    mascota.mascotaId = document.id
                    mascotasList.add(mascota) // Añade la mascota a la lista.
                }
                Log.d("PetsFragment", "Mascotas cargadas: ${mascotasList.size}")

                // Configura el adaptador para el RecyclerView.
                adapter = PetsAdapter(mascotasList) { mascotaSeleccionada ->
                    // Define la acción a realizar cuando se hace clic en una mascota de la lista.
                    // Crea una instancia de PetsClinic, pasando el ID de la mascota seleccionada.
                    val fragment = PetsClinic.newInstance(mascotaSeleccionada.mascotaId)
                    Log.d("PetsFragment", "Abriendo PetsClinic para mascota ID: ${mascotaSeleccionada.mascotaId}")

                    // Inicia una transacción de fragmentos para reemplazar el contenido actual
                    // del contenedor (presumiblemente R.id.fragment_mascotas) con el fragmento PetsClinic.
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_mascotas, fragment) // Reemplaza el fragmento. Asegúrate que R.id.fragment_mascotas sea el ID correcto del contenedor.
                        .addToBackStack(null) // Añade la transacción a la pila de retroceso, permitiendo volver a PetsFragment.
                        .commit() // Confirma la transacción.
                }

                // Establece el adaptador en el RecyclerView para mostrar los datos.
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { e -> // Listener para el caso de fallo.
                // Registra un error si la carga de mascotas falla.
                Log.e("PetsFragment", "Error al cargar mascotas", e)
                // Considera mostrar un mensaje al usuario (ej. Toast o un TextView en el layout).
            }
    }
}
