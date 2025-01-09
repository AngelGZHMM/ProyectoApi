package com.angelgallegozayas.proyectoapicomida.ui.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaInicio.PantallaInicioScreen
import com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaListaComidas.PantallaListaComidasScreen
import com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaListaComidas.PantallaListaComidasViewModel

@Composable
fun Navegacion() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "pantallaInicio") {
        composable("pantallaInicio") {
            PantallaInicioScreen(navegarAPantallaListaComidas = { navController.navigate("pantallaListaComidas") })
        }
        composable("pantallaListaComidas") {
            // Pasamos la función navegarAPantallaInicio para regresar
            PantallaListaComidasScreen(navegarAPantallaInicio = { navController.popBackStack() }, pantallaListaComidasViewModel = PantallaListaComidasViewModel())
        }
    }
}