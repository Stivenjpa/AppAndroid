package com.example.appandroid;


data class Pet(
    var mascotaId: String = "",
    var nombreMascota: String = "",
    var edadMascota: Int = 0,
    var fotoUrl: String = "",
    var nombreDueno: String = "",
    var nombreVeterinario: String = "",
    var vacunas: String = ""
)