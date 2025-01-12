package com.angelgallegozayas.proyectoapicomida.ui.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaComidaSeleccionada.PantallaDetalleComidaScreen
import com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaInicio.PantallaInicioScreen
import com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaListaComidas.PantallaListaComidasScreen
import com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaListaComidas.PantallaListaComidasViewModel


@Composable
fun Navegacion() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = PantallaInicio
    ) {

        composable<PantallaInicio> {
            PantallaInicioScreen(
                navegarAPantallaListaComidas = {
                    navController.navigate(PantallaListaComidas)
                }
            )
        }
        composable<PantallaListaComidas> {
            val viewModel = PantallaListaComidasViewModel()
            PantallaListaComidasScreen(
                viewModel = viewModel,
                navigateToDetalle = { comidaId ->
                    navController.navigate(PantallaDetalleComida(comidaId))
                },
                navigateToInicio = {
                    navController.navigate(PantallaInicio) {
                        popUpTo(PantallaInicio) { inclusive = true }
                    }
                }
            )
        }

        composable<PantallaDetalleComida> { backStackEntry ->
            val comidaId = backStackEntry.toRoute<PantallaDetalleComida>().id

            PantallaDetalleComidaScreen(
                comidaId = comidaId
            ) {
                navController.navigate(PantallaListaComidas) {
                    popUpTo(PantallaListaComidas) { inclusive = true }
                }
            }
        }



    }
}


