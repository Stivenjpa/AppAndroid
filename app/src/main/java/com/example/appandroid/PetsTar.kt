package com.example.appandroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction

/**
 * Actividad PetsTar: Actúa como contenedor principal para los fragmentos relacionados con las mascotas.
 * En su creación, carga inicialmente `PetsFragment` en un contenedor de fragmentos definido en su layout.
 * Esta actividad podría ser el punto de entrada para la sección de "Mascotas" de la aplicación.
 */
class PetsTar : AppCompatActivity() {

    /**
     * Método llamado cuando la actividad se crea por primera vez.
     * Establece el contenido de la vista a partir del archivo de layout `layout_petstar`.
     * Si es la primera vez que se crea la actividad (savedInstanceState es nulo),
     * inicia una transacción de fragmentos para añadir `PetsFragment` al contenedor.
     *
     * @param savedInstanceState Si la actividad se está reinicializando después de haber sido
     * previamente cerrada, este Bundle contiene los datos que suministró más recientemente en
     * onSaveInstanceState(Bundle). De lo contrario, es nulo.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establece el layout para esta actividad, que debe contener un FrameLayout o similar para los fragmentos.
        setContentView(R.layout.layout_petstar)

        // Verifica si el fragmento no está ya agregado (ej. en caso de recreación de la actividad).
        // savedInstanceState será nulo si la actividad se está creando por primera vez.
        if (savedInstanceState == null) {
            // Inicia una transacción para manejar fragmentos.
            val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            // Reemplaza el contenido del contenedor (identificado por R.id.fragment_mascotas)
            // con una nueva instancia de PetsFragment.
            // Es crucial que R.id.fragment_mascotas sea el ID del FrameLayout en layout_petstar.xml.
            fragmentTransaction.replace(R.id.fragment_mascotas, PetsFragment()) // Asume que fragment_mascotas es el contenedor.
            fragmentTransaction.commit() // Confirma la transacción para que los cambios surtan efecto.
        }
    }
}