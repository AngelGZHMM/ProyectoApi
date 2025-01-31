package com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaListaComidas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelgallegozayas.proyectoapicomida.data.AuthManager
import com.angelgallegozayas.proyectoapicomida.data.model.Meal
import com.angelgallegozayas.proyectoapicomida.data.repositories.repositoryList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PantallaListaComidasViewModel : ViewModel() {

    val usuario = AuthManager()

    // Flow para la lista de comidas
    private val _comidas = MutableStateFlow<List<Meal>>(emptyList())
    val comidas: StateFlow<List<Meal>> = _comidas

    // Flow para el error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        obtenerComidas()
    }

    private fun obtenerComidas() {
        viewModelScope.launch {
            try {
                val listaComidas = repositoryList.getListaComidas()
                _comidas.value = listaComidas
                if (listaComidas.isEmpty()) {
                    _error.value = "No se encontraron comidas."
                }
            } catch (e: Exception) {
                _error.value = "Error al obtener las comidas: ${e.localizedMessage}"
                _comidas.value = emptyList() // En caso de error, vaciar lista
            }
        }
    }
}
