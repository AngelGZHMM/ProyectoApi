package com.angelgallegozayas.proyectoapicomida

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.angelgallegozayas.proyectoapicomida.ui.navegacion.Navegacion
import com.angelgallegozayas.proyectoapicomida.ui.theme.ProyectoApiComidaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoApiComidaTheme {
                Navegacion()

            }
        }
    }
}
