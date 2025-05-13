package com.example.appandroid

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PetsAdapter(private val listaMascotas: List<Pet>, private val onClick: (Pet) -> Unit) :
    RecyclerView.Adapter<PetsAdapter.MascotaViewHolder>() {

    inner class MascotaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgMascota: ImageView = view.findViewById(R.id.imgMascota)
        val txtNombre: TextView = view.findViewById(R.id.txtNombre)
        val txtEdad: TextView = view.findViewById(R.id.txtEdad)
        val txtVacunas: TextView = view.findViewById(R.id.txtVacunas)
        val txtDueno: TextView = view.findViewById(R.id.txtDueno)
        val txtVeterinario: TextView = view.findViewById(R.id.txtVeterinario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MascotaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_tarjetamascota, parent, false)
        return MascotaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MascotaViewHolder, position: Int) {
        val mascota = listaMascotas[position]
        Log.d("PetsAdapter", "Tamaño de listaMascotas: ${listaMascotas.size}")
        holder.txtNombre.text = "Nombre: ${mascota.nombreMascota}"
        holder.txtEdad.text = "Edad: ${mascota.edadMascota} años"
        holder.txtVacunas.text = "Vacunas: ${mascota.vacunas}"
        holder.txtDueno.text = "Dueño: ${mascota.nombreDueno}"
        holder.txtVeterinario.text = "Veterinario: ${mascota.nombreVeterinario}"

        // Manejo de URL vacía en fotoUrl
        Glide.with(holder.itemView.context)
            .load(if (mascota.fotoUrl.isNotEmpty()) mascota.fotoUrl else R.drawable.mascota_default1)
            .placeholder(R.drawable.mascota_default1) // Imagen por defecto
            .into(holder.imgMascota)

        holder.itemView.setOnClickListener {
            onClick(mascota)
        }
    }

    override fun getItemCount(): Int = listaMascotas.size
}
