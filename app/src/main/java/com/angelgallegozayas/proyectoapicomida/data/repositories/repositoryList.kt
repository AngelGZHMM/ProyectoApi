package com.angelgallegozayas.proyectoapicomida.data.repositories

import com.angelgallegozayas.proyectoapicomida.data.model.Meal

object repositoryList {

    suspend fun getListaComidas(): List<Meal> {
        return try {
            val response = RemoteConnection.remoteService.getAllMeals()
            response.meals
            // Devuelve una lista vacía si no hay comidas
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Devuelve una lista vacía en caso de error
        }
    }


    // Nueva función que recibe un ID y devuelve la comida seleccionada
    suspend fun getComidaPorId(id: String): Meal? {
        return try {
            val response = getListaComidas()
            return response.find { it.idMeal == id }
        } catch (e: Exception) {
            e.printStackTrace()
            null // Devuelve null en caso de error
        }
    }
}








