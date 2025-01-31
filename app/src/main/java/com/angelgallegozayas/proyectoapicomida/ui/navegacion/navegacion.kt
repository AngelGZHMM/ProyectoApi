package com.angelgallegozayas.proyectoapicomida.ui.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.angelgallegozayas.proyectoapicomida.data.AuthManager
import com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaComidaSeleccionada.PantallaDetalleComidaScreen
import com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaInicio.ConectadoScreen
import com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaInicio.ContraseñaOlvidadaScreen
import com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaInicio.PantallaInicioScreen
import com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaListaComidas.PantallaListaComidasScreen
import com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaListaComidas.PantallaListaComidasViewModel


@Composable
fun Navegacion(auth: AuthManager) {
    val navController = rememberNavController()
    auth.resetAuthState()


    NavHost(
        navController = navController,
        startDestination = PantallaInicio
    ) { composable<PantallaInicio> {
        PantallaInicioScreen(
            auth = auth,
            navegarAPantallaListaComidas = {
                navController.navigate(PantallaListaComidas)

            },
            navegaraConectado = {
                navController.navigate(Conectado)
            },
            navegaraContraseñaOlvidada = {
                navController.navigate(ContraseñaOlvidada)
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
                    navController.navigate(PantallaInicio)
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

        composable<Conectado> {
            ConectadoScreen(auth) {
                navController.navigate(PantallaInicio){
                    popUpTo(PantallaInicio){ inclusive = true }
                }
            }
        }

        composable<ContraseñaOlvidada> {
            ContraseñaOlvidadaScreen(auth) {
                navController.navigate(PantallaInicio){
                    popUpTo(PantallaInicio){ inclusive = true }
                }
            }
        }



    }
}


