package com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaListaComidas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelgallegozayas.proyectoapicomida.data.model.Meal
import com.angelgallegozayas.proyectoapicomida.data.repositories.repositoryList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PantallaListaComidasViewModel : ViewModel() {

    // Flow para la lista de comidas
    private val _comidas = MutableStateFlow<List<Meal>>(emptyList())
    val comidas: StateFlow<List<Meal>> = _comidas

    init {
        obtenerComidas()
    }

    private fun obtenerComidas() {
        viewModelScope.launch {
            val listaComidas = repositoryList.getListaComidas()
            _comidas.value = listaComidas
        }
    }
}
