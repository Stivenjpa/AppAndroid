package com.example.appandroid

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity

/**
 * Actividad SplashActivity: Pantalla de bienvenida que se muestra al iniciar la aplicación.
 * Muestra un layout (presumiblemente con un logo o animación) durante un corto período de tiempo (2 segundos)
 * y luego redirige automáticamente al usuario a la actividad `Login`.
 */
class SplashActivity : ComponentActivity() {
    /**
     * Método llamado cuando la actividad se crea por primera vez.
     * Configura el layout de la pantalla de splash y programa la transición a la actividad `Login`
     * después de un retraso de 2000 milisegundos (2 segundos).
     *
     * @param savedInstanceState Si la actividad se está reinicializando después de haber sido
     * previamente cerrada, este Bundle contiene los datos que suministró más recientemente en
     * onSaveInstanceState(Bundle). De lo contrario, es nulo.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establece el contenido de la vista a partir del archivo de layout `layout_splash`.
        setContentView(R.layout.layout_splash)

        // Utiliza un Handler para postergar la ejecución de una tarea.
        // Looper.getMainLooper() asegura que la tarea se ejecute en el hilo principal (UI thread).
        Handler(Looper.getMainLooper()).postDelayed({
            // Crea un Intent para iniciar la actividad Login.
            startActivity(Intent(this@SplashActivity, Login::class.java))
            // Finaliza SplashActivity para que el usuario no pueda volver a ella presionando el botón "atrás" desde Login.
            finish()
        }, 2000) // El retraso en milisegundos (2000 ms = 2 segundos).
    }
}