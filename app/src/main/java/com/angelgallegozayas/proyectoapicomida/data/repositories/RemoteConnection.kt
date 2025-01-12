package com.angelgallegozayas.proyectoapicomida.data.repositories

import com.angelgallegozayas.proyectoapicomida.data.model.Meal
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RemoteConnection {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.themealdb.com/api/json/v1/1/") // URL base correcta
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val remoteService: RemoteService = retrofit.create(RemoteService::class.java)

    // Función para obtener una comida por su ID
    suspend fun obtenerComidaPorId(comidaId: String): Meal? {
        return try {
            val response = remoteService.getMealById(comidaId)
            response // Retorna la comida obtenida
        } catch (e: Exception) {
            null // En caso de error, retorna null
        }
    }
}
