package com.example.appandroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction

class PetsTar : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_petstar)

        // Verifica si el fragmento no está agregado
        if (savedInstanceState == null) {
            val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_mascotas, PetsFragment()) // Asume que fragment_container es el contenedor
            fragmentTransaction.commit()
        }
    }
}