package com.angelgallegozayas.proyectoapicomida.ui.navegacion

import kotlinx.serialization.Serializable

@Serializable
object PantallaInicio

@Serializable
object PantallaListaComidas

@Serializable
data class PantallaDetalleComida(val id: String)