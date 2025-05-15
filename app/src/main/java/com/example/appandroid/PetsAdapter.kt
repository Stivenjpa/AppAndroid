package com.example.appandroid;

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

/**
 * Adaptador para el RecyclerView que muestra la lista de mascotas.
 * Se encarga de vincular los datos de cada mascota con la vista de item (tarjeta de mascota).
 *
 * @property listaMascotas Lista de objetos `Pet` que se mostrarán.
 * @property onClick Lambda que se ejecuta cuando se hace clic en un ítem de la lista.
 */
class PetsAdapter(private val listaMascotas: List<Pet>, private val onClick: (Pet) -> Unit) :
    RecyclerView.Adapter<PetsAdapter.MascotaViewHolder>() {

    /**
     * ViewHolder para cada ítem de mascota en el RecyclerView.
     * Contiene referencias a los elementos de la UI dentro de `layout_tarjetamascota.xml`.
     * @param view La vista raíz del ítem de la mascota.
     */
    inner class MascotaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Referencias a los componentes de la UI en la tarjeta de la mascota.
        val imgMascota: ImageView = view.findViewById(R.id.imgMascota) // Imagen de la mascota.
        val txtNombre: TextView = view.findViewById(R.id.txtNombre)    // Nombre de la mascota.
        val txtEdad: TextView = view.findViewById(R.id.txtEdad)        // Edad de la mascota.
        val txtVacunas: TextView = view.findViewById(R.id.txtVacunas)  // Información de vacunas.
        val txtDueno: TextView = view.findViewById(R.id.txtDueno)      // Nombre del dueño.
        val txtVeterinario: TextView = view.findViewById(R.id.txtVeterinario) // Nombre del veterinario.
    }

    /**
     * Se llama cuando RecyclerView necesita un nuevo [MascotaViewHolder] del tipo dado para representar un ítem.
     * Infla el layout `layout_tarjetamascota.xml` para crear la vista del ítem.
     * @param parent El ViewGroup al que se añadirá esta nueva vista después de que se vincule a una posición del adaptador.
     * @param viewType El tipo de vista de la nueva Vista.
     * @return Un nuevo MascotaViewHolder que contiene la Vista para el nuevo ítem.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MascotaViewHolder {
        // Infla el layout XML que define cómo se verá cada tarjeta de mascota.
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_tarjetamascota, parent, false)
        return MascotaViewHolder(view) // Retorna una nueva instancia del ViewHolder.
    }

    /**
     * Se llama por RecyclerView para mostrar los datos en la posición especificada.
     * Este método actualiza el contenido del itemView para reflejar el ítem en la posición dada.
     * @param holder El ViewHolder que debe ser actualizado para representar el contenido del ítem en la posición dada en el conjunto de datos.
     * @param position La posición del ítem dentro del conjunto de datos del adaptador.
     */
    override fun onBindViewHolder(holder: MascotaViewHolder, position: Int) {
        val mascota = listaMascotas[position] // Obtiene la mascota actual de la lista.
        Log.d("PetsAdapter", "Tamaño de listaMascotas: ${listaMascotas.size}") // Log para depuración.

        // Asigna los datos de la mascota a los TextViews correspondientes en el ViewHolder.
        holder.txtNombre.text = "Nombre: ${mascota.nombreMascota}"
        holder.txtEdad.text = "Edad: ${mascota.edadMascota} años"
        holder.txtVacunas.text = "Vacunas: ${mascota.vacunas}"
        holder.txtDueno.text = "Dueño: ${mascota.nombreDueno}"
        holder.txtVeterinario.text = "Veterinario: ${mascota.nombreVeterinario}"

        // Carga la imagen de la mascota usando Glide.
        // Si la URL de la foto está vacía, carga una imagen por defecto.
        Glide.with(holder.itemView.context)
            .load(if (mascota.fotoUrl.isNotEmpty()) mascota.fotoUrl else R.drawable.mascota_default1) // Usa la URL o un recurso drawable.
            .placeholder(R.drawable.mascota_default1) // Imagen mostrada mientras se carga la real.
            .error(R.drawable.mascota_default1) // Imagen mostrada si hay error al cargar la URL.
            .into(holder.imgMascota) // ImageView destino.

        // Configura el listener de clic para el ítem completo de la vista.
        holder.itemView.setOnClickListener {
            onClick(mascota) // Ejecuta la lambda `onClick` pasando la mascota seleccionada.
        }
    }

    /**
     * Devuelve el número total de ítems en el conjunto de datos que tiene el adaptador.
     * @return El número total de mascotas en la lista.
     */
    override fun getItemCount(): Int = listaMascotas.size
}
