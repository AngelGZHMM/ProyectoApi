package com.angelgallegozayas.proyectoapicomida.data.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.angelgallegozayas.proyectoapicomida.data.AuthManager
import com.angelgallegozayas.proyectoapicomida.data.model.Meal
import com.angelgallegozayas.proyectoapicomida.data.model.RecetaUser
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class FirestoreViewModel(
    private val firestoreManager: FirestoreManager,
    auth: AuthManager
) : ViewModel() {

    private val _firestoreProducts = MutableLiveData<List<Meal>>()
    val firestoreMeals: LiveData<List<Meal>> = _firestoreProducts

    // Convertimos el userId en MutableLiveData para poder actualizarlo desde otras pantallas
    private val _userId = MutableLiveData<String>().apply {
        value = auth.getCurrentUser()?.uid.orEmpty()
    }
    // Exponemos el userId como LiveData para que otros puedan observarlo
    val userId: LiveData<String> get() = _userId

    // Función para actualizar el userId desde otras pantallas
    fun updateUserId(newUserId: String) {
        _userId.value = newUserId
        // Opcional: Si tienes lógica que depende del cambio de userId, puedes llamarla aquí,
        // por ejemplo, volver a cargar los favoritos.
//        actualizarFavoritos()
    }

    private val _firestoreProduct = MutableLiveData<Meal>()


    private val _syncState = MutableLiveData<SyncState>()
    val syncState: LiveData<SyncState> = _syncState

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    sealed class SyncState {
        object Loading : SyncState()
        data class Success(val message: String) : SyncState()
        data class Error(val exception: Throwable) : SyncState()
    }


fun addReceta(recetaItem: Meal,userid : String) {
    firestoreManager.let {
        viewModelScope.launch {
            it.addReceta(recetaItem,userid)
        }
    }
}
//Establecer en la variable Recetas creadas todas las recetas creadas
    fun listarTodaslasRecetas() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val recetas = firestoreManager.getRecetas().first()
                _firestoreProducts.value = recetas

            } catch (e: Exception) {
                _syncState.value = SyncState.Error(e)
            }
            _isLoading.value = false
        }
    }


    // Estado de favoritos para el usuario actual


    val favorites = _userId.asFlow()
        .filter { it.isNotEmpty() } // Aseguramos que se usa un UID válido
        .flatMapLatest { id ->
            // Consulta Firestore con el UID real
            firestoreManager.getAgregadosPorUsuario(id)
                .map { favoritosDBList ->
                    // Mapea la lista de FavoritosDB a una lista de Meal.
                    favoritosDBList.map { favoritoDB ->
                        // Si en tu modelo 'Meal' solo usas idMeal para identificar, lo asignas así:
                        Meal(idMeal = favoritoDB.idreceta)
                    }
                }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    //  Función para agregar favorito y actualizar estado
    fun addFavorito(item: String,usuario: String) {

        _isLoading.value = true
        viewModelScope.launch {
            try {
                Log.e("Usuario",userId.toString())
                firestoreManager.addFavorito(item,usuario)
                _syncState.value = SyncState.Success("Favorito añadido")

                //  Forzar actualización de favoritos
//                actualizarFavoritos()
            } catch (e: Exception) {
                _syncState.value = SyncState.Error(e)
            }
            _isLoading.value = false
        }
    }

    private val _recetaDetalle = MutableLiveData<RecetaUser?>()
    val recetaDetalle: LiveData<RecetaUser?> = _recetaDetalle

    suspend fun cargarRecetaPorIdConCreador(id: String): RecetaUser? {
        try {
            val resultado = firestoreManager.getRecetaConCreador(id)
            _recetaDetalle.postValue(resultado)
            return resultado // Asegúrate de que haya un return válido
        } catch (e: Exception) {
            Log.e("ViewModel", "Error cargando receta", e)
            _recetaDetalle.postValue(null)
            return null
        }

    }


    suspend fun cargarMealPorId(id: String): Meal? {
        return try {
            val producto = firestoreManager.getRecetaById(id)
            _firestoreProduct.value = producto // Actualiza LiveData (opcional)
            producto //  Retorna el producto obtenido
        } catch (e: Exception) {
            Log.e("FirestoreViewModel", "Error al cargar producto por ID", e)
            null
        }
    }



    class FirestoreViewModelFactory(
        private val firestoreManager: FirestoreManager,
        private val authManager: AuthManager  // Almacena la instancia recibida
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            // Utiliza la instancia de authManager pasada a la factoría
            return FirestoreViewModel(firestoreManager, authManager) as T
        }
    }


    // FirestoreViewModel.kt
    fun updateReceta(recetaItem: Meal, userid: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                firestoreManager.updateReceta(recetaItem, userid)
                _syncState.value = SyncState.Success("Receta actualizada correctamente")
            } catch (e: Exception) {
                _syncState.value = SyncState.Error(e)
            }
            _isLoading.value = false
        }
    }

    fun eliminarReceta(recetaId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                firestoreManager.deleteReceta(recetaId)
                _syncState.value = SyncState.Success("Receta eliminada correctamente")
            } catch (e: Exception) {
                _syncState.value = SyncState.Error(e)
            }
            _isLoading.value = false
        }
    }





}

