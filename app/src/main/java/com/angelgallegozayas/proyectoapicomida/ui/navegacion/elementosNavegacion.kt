package com.angelgallegozayas.proyectoapicomida.ui.navegacion

import kotlinx.serialization.Serializable

@Serializable
object PantallaInicio

@Serializable
object PantallaListaComidas

@Serializable
object PantallaPerfil
@Serializable
object PantallaCrearReceta

@Serializable
object PantallaListaFavoritosScreen

@Serializable
data class PantallaDetalleComida(val id: String)

@Serializable
object Conectado

@Serializable
object ContraseñaOlvidada

@Serializable
data class PantallaModificarReceta(val id: String)
