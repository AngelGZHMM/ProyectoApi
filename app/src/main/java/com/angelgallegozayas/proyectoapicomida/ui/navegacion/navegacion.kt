package com.angelgallegozayas.proyectoapicomida.ui.navegacion

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.angelgallegozayas.proyectoapicomida.data.AuthManager
import com.angelgallegozayas.proyectoapicomida.data.firebase.FirestoreViewModel
import com.angelgallegozayas.proyectoapicomida.ui.navegacion.PantallaCrearReceta
import com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaComidaSeleccionada.PantallaDetalleComidaScreen
import com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaCrearReceta.PantallaCrearReceta
import com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaFavoritos.PantallaListaFavoritosScreen
import com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaInicio.ConectadoScreen
import com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaInicio.ContraseñaOlvidadaScreen
import com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaInicio.PantallaInicioScreen
import com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaListaComidas.PantallaListaComidasScreen
import com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaListaComidas.PantallaListaComidasViewModel
import com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaPerfil.PantallaPerfil


@Composable
fun Navegacion(auth: AuthManager, viewModelFirestore: FirestoreViewModel) {
    val navController = rememberNavController()
    auth.resetAuthState()
    val viewModel = PantallaListaComidasViewModel()



    NavHost(
        navController = navController,
        startDestination = PantallaInicio
    ) {

        composable<PantallaInicio> {
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
            PantallaListaComidasScreen(
                viewModel = viewModel,
                navigateToDetalle = { comidaId ->
                    navController.navigate(PantallaDetalleComida(comidaId))
                },
                navigateToInicio = {
                    navController.navigate(PantallaInicio)
                },
                navigateToPerfil =
                {
                    navController.navigate(PantallaPerfil)
                },
                auth = auth,
                firestoreviewModel = viewModelFirestore,
                navigateToCrearReceta = {
                    navController.navigate(PantallaCrearReceta)
                },
                navigateToFavoritos = {
                    navController.navigate(PantallaListaFavoritosScreen)
                },
                navegarAPantallaListaComidas = {
                    navController.navigate(PantallaListaComidas)

                }
            )
        }

        composable<PantallaPerfil> {
            PantallaPerfil(auth) {
                navController.navigate(PantallaInicio) {
                    popUpTo(PantallaInicio) { inclusive = true }
                }
            }
        }

        composable<PantallaCrearReceta> {
            PantallaCrearReceta(
                auth = auth,
                viewModel = viewModel<PantallaListaComidasViewModel>(),
                firestoreviewModel = viewModelFirestore,
                navegaraListaComidas = {
                    navController.navigate(PantallaListaComidas)
                }
            )
        }

        composable<PantallaDetalleComida> { backStackEntry ->
            val comidaId = backStackEntry.toRoute<PantallaDetalleComida>().id

            PantallaDetalleComidaScreen(
                comidaId = comidaId
                , navigateToBack = {
                    navController.popBackStack()
                },
                firestoreviewModel = viewModelFirestore
            )
        }

        composable<Conectado> {
            ConectadoScreen(auth) {
                navController.navigate(PantallaInicio) {
                    popUpTo(PantallaInicio) { inclusive = true }
                }
            }
        }

        composable<ContraseñaOlvidada> {
            ContraseñaOlvidadaScreen(auth) {
                navController.navigate(PantallaInicio) {
                    popUpTo(PantallaInicio) { inclusive = true }
                }
            }
        }

        composable<PantallaListaFavoritosScreen>{
            PantallaListaFavoritosScreen(
                viewModel = viewModel,
                navigateToDetalle = { comidaId ->
                    navController.navigate(PantallaDetalleComida(comidaId))
                },
                navigateToInicio = {
                    navController.navigate(PantallaInicio)
                },
                navigateToPerfil =
                {
                    navController.navigate(PantallaPerfil)
                },
                auth = auth,
                firestoreviewModel = viewModelFirestore,
                navigateToCrearReceta = {
                    navController.navigate(PantallaCrearReceta)
                },
                navigateToFavoritos = {
                    navController.navigate(PantallaListaFavoritosScreen)
                },
                navegarAPantallaListaComidas = {
                    navController.navigate(PantallaListaComidas)

                }
            )
        }


    }
}




