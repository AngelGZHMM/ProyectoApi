package com.angelgallegozayas.proyectoapicomida.data.firebase

import FavoritosDB
import android.content.Context
import android.util.Log
import com.angelgallegozayas.proyectoapicomida.data.AuthManager
import com.angelgallegozayas.proyectoapicomida.data.model.Meal
import com.angelgallegozayas.proyectoapicomida.data.model.RecetaUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.tasks.await

class FirestoreManager(auth: AuthManager, context: Context) {
    private val firestore = FirebaseFirestore.getInstance()
    private val userId = auth.getCurrentUser()?.uid

    companion object {
        // Colección para almacenar las recetas creadas por los usuarios.
        const val RECETAS = "recetas"
        const val FAVORITOS = "favoritos"
    }

    // ----------------------------------- RECETAS -----------------------------
    /**
     * Obtiene todas las recetas, extrayendo el objeto Meal de cada documento.
     */
    suspend fun getRecetas(): Flow<List<Meal>> {
        return firestore.collection(RECETAS).snapshots().map { querySnapshot ->
            querySnapshot.documents.mapNotNull { document ->
                // Convertimos el documento a RecetaUser y devolvemos el Meal
                val recetaUser = document.toObject(RecetaUser::class.java)
                recetaUser?.meal
            }
        }
    }

    /**
     * Obtiene una receta en específico por su ID.
     */
    suspend fun getRecetaConCreador(id: String): RecetaUser {
        // Obtener la receta
        val recetaDoc = firestore.collection("recetas").document(id).get().await()
        val receta = recetaDoc.toObject(RecetaUser::class.java)
            ?: throw Exception("Receta no encontrada")

        // Obtener información del creador
        val usuarioDoc = firestore.collection("usuarios")
            .document(receta.idusuario)
            .get()
            .await()

        return RecetaUser( receta.idusuario, receta.meal)
    }


    suspend fun getRecetaById(id: String): Meal {
        val document = firestore.collection(RECETAS).document(id).get().await()

        if (!document.exists()) {
            throw Exception("Documento con ID $id no encontrado en Firestore")
        }

        val recetaUser = document.toObject(RecetaUser::class.java)
        return recetaUser?.meal ?: throw Exception("El campo 'meal' no existe en el documento")
    }


    /**
     * Agrega una nueva receta creada por el usuario autenticado.
     * Se guarda el idusuario junto con el objeto Meal.
     */
    suspend fun addReceta(recetaItem: Meal, userid: String) {
        // Verificar si la receta ya existe para este usuario
        val querySnapshot = firestore.collection(RECETAS)
            .whereEqualTo("idusuario", userid)
            .whereEqualTo("meal.strMeal", recetaItem.strMeal)
            .get()
            .await()

        if (!querySnapshot.isEmpty) {
            throw Exception("La receta ya existe en tu colección.")
        }

        // Generar ID único y agregar la receta si no existe
        val idGenerado = firestore.collection(RECETAS).document().id
        val recetaConId = recetaItem.copy(idMeal = idGenerado)
        val recetaUser = RecetaUser(userid, recetaConId)

        firestore.collection(RECETAS)
            .document(idGenerado)
            .set(recetaUser)
            .await()
    }



    suspend fun addFavorito(recipeId: String, userId: String) {
        val agregadosRef = firestore.collection(FAVORITOS).document(userId)

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(agregadosRef)
            if (!snapshot.exists()) {
                // Si no existe el documento para el usuario, se crea con la receta en la lista.
                val newData = hashMapOf(
                    "idusuario" to userId,
                    "recetas" to listOf(recipeId)
                )
                transaction.set(agregadosRef, newData)
            } else {
                // Si existe, se obtiene el arreglo actual de recetas.
                val recetasList = snapshot.get("recetas") as? List<String> ?: emptyList()
                if (recetasList.contains(recipeId)) {
                    // Si la receta ya está en la lista, se elimina.
                    transaction.update(agregadosRef, "recetas", FieldValue.arrayRemove(recipeId))
                } else {
                    // Si no está, se agrega.
                    transaction.update(agregadosRef, "recetas", FieldValue.arrayUnion(recipeId))
                }
            }
        }.await()
    }

    /**
     * Elimina la receta de la colección.
     */
    suspend fun deleteReceta(id: String) {
        firestore.collection(RECETAS).document(id).delete().await()
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAgregadosPorUsuario(userId: String): Flow<List<FavoritosDB>> {
        return firestore.collection(FAVORITOS)
            .document(userId)
            .snapshots()
            .mapLatest { snapshot ->
                if (!snapshot.exists()) {
                    emptyList()
                } else {
                    // Se asume que "recetas" es un arreglo de String (IDs de recetas)
                    val recetasList = snapshot.get("recetas") as? List<String> ?: emptyList()
                    recetasList.map { recetaId ->
                        FavoritosDB(
                            idreceta = recetaId,
                            idusuario = userId
                        )
                    }
                }
            }
            .flowOn(Dispatchers.IO)
    }



    // FirestoreManager.kt
    suspend fun updateReceta(recetaItem: Meal, userid: String) {
        // Verificar que la receta pertenece al usuario
        val document = firestore.collection(RECETAS).document(recetaItem.idMeal)
        val snapshot = document.get().await()
        val existingReceta = snapshot.toObject(RecetaUser::class.java)

        if (existingReceta?.idusuario != userid) {
            throw Exception("No tienes permiso para modificar esta receta.")
        }

        // Actualizar la receta
        val updatedRecetaUser = RecetaUser(userid, recetaItem)
        document.set(updatedRecetaUser).await()
    }



}