package com.angelgallegozayas.proyectoapicomida.data.repositories

import com.angelgallegozayas.proyectoapicomida.data.model.comidas
import retrofit2.http.GET

interface RemoteService {
        @GET("search.php?f=a")
        suspend fun getMeal(): comidas // Cambiado a retornar comidas
}
