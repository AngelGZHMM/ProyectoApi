package com.angelgallegozayas.proyectoapicomida.data.repositories

import com.angelgallegozayas.proyectoapicomida.data.model.Meal

object repositoryList {

    suspend fun getListaComidas(): List<Meal> {
        return try {
            val response = RemoteConnection.remoteService.getMeal()
            response.meals ?: emptyList() // Devuelve una lista vacía si no hay comidas
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Devuelve una lista vacía en caso de error
        }
    }

    suspend fun mostrarComidas() {
        val comidas = getListaComidas()
        comidas.forEach { comida ->
            println("Nombre: ${comida.strMeal}, Imagen: ${comida.strMealThumb}")
        }
    }
}
