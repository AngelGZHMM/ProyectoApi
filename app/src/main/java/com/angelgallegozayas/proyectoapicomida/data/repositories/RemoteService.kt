package com.angelgallegozayas.proyectoapicomida.data.repositories

import com.angelgallegozayas.proyectoapicomida.data.model.Meal
import com.angelgallegozayas.proyectoapicomida.data.model.comidas
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteService {

        // Obtener todas las comidas (con un query opcional)
        @GET("search.php")
        suspend fun getAllMeals(@Query("s") query: String = ""): comidas

        // Obtener una comida por su ID
        @GET("search.php/{id}")
        suspend fun getMealById(@Path("id") comidaId: String): Meal
}
