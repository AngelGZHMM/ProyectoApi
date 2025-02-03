package com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaCrearReceta

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.angelgallegozayas.proyectoapicomida.data.AuthManager
import com.angelgallegozayas.proyectoapicomida.data.firebase.FirestoreViewModel
import com.angelgallegozayas.proyectoapicomida.data.model.Meal
import com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaListaComidas.PantallaListaComidasViewModel




@Composable
fun PantallaCrearReceta(
    auth: AuthManager,
    viewModel: PantallaListaComidasViewModel,
    firestoreviewModel: FirestoreViewModel,
    navegaraListaComidas: () -> Unit
) {
    viewModel.pantallaActual.value = "Creacion"
    var mealName by remember { mutableStateOf("") }
    var mealThumb by remember { mutableStateOf("") }
    var mealInstructions by remember { mutableStateOf("") }

    var descriptions by remember { mutableStateOf(listOf("")) }
    var ingredients by remember { mutableStateOf(listOf("")) }

    Column(
        modifier = Modifier
            .padding(30.dp)
            .padding(bottom = 16.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título de la pantalla
        Text("Crear nueva receta", style = MaterialTheme.typography.headlineLarge)
        // Nombre de la receta
        OutlinedTextField(
            value = mealName,
            onValueChange = { mealName = it },
            label = { Text("Nombre de la receta") },
            modifier = Modifier.fillMaxWidth()
        )
if(mealThumb!="") {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .background(Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(mealThumb),
            contentDescription = "Imagen de la comida",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

    }
}
        // URL de imagen
        OutlinedTextField(
            value = mealThumb,
            onValueChange = { mealThumb = it },
            label = { Text("URL de imagen") },
            modifier = Modifier.fillMaxWidth()
        )
        Text("PASOS A SEGUIR", style = MaterialTheme.typography.headlineLarge)


        // Instrucciones
        OutlinedTextField(
            value = mealInstructions,
            onValueChange = { mealInstructions = it },
            label = { Text("Pasos a seguir: 1-... .") },
            modifier = Modifier.fillMaxWidth()
        )

        // Sección de descripciones dinámicas
        Text("Descripcion", style = MaterialTheme.typography.headlineLarge)
        descriptions.forEachIndexed { index, text ->
            OutlinedTextField(
                value = text,
                onValueChange = { newValue -> descriptions = descriptions.toMutableList().apply { set(index, newValue) } },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Sección de ingredientes dinámicos
        Text("Ingredientes", style = MaterialTheme.typography.headlineLarge)
        ingredients.forEachIndexed { index, text ->
            OutlinedTextField(
                value = text,
                onValueChange = { newValue -> ingredients = ingredients.toMutableList().apply { set(index, newValue) } },
                label = { Text("Ingrediente ${index + 1}") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Button(onClick = { if (ingredients.size < 20) ingredients = ingredients + "" }) {
            Text("Agregar ingrediente")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para guardar la receta
        Button(
            onClick = {
                val mealcreada = Meal(
                    strMeal = mealName,
                    strMealThumb = mealThumb,
                    strInstructions = mealInstructions,
                    strMealDescription = descriptions.getOrNull(0) ?: "",
                    strDescription2 = descriptions.getOrNull(1) ?: "",
                    strDescription3 = descriptions.getOrNull(2) ?: "",
                    strDescription4 = descriptions.getOrNull(3) ?: "",
                    strIngredient1 = ingredients.getOrNull(0) ?: "",
                    strIngredient2 = ingredients.getOrNull(1) ?: "",
                    strIngredient3 = ingredients.getOrNull(2) ?: "",
                    strIngredient4 = ingredients.getOrNull(3) ?: "",
                    strIngredient5 = ingredients.getOrNull(4) ?: "",
                    strIngredient6 = ingredients.getOrNull(5) ?: "",
                    strIngredient7 = ingredients.getOrNull(6) ?: "",
                    strIngredient8 = ingredients.getOrNull(7) ?: "",
                    strIngredient9 = ingredients.getOrNull(8) ?: "",
                    strIngredient10 = ingredients.getOrNull(9) ?: "",
                    strIngredient11 = ingredients.getOrNull(10) ?: "",
                    strIngredient12 = ingredients.getOrNull(11) ?: "",
                    strIngredient13 = ingredients.getOrNull(12) ?: "",
                    strIngredient14 = ingredients.getOrNull(13) ?: "",
                    strIngredient15 = ingredients.getOrNull(14) ?: "",
                    strIngredient16 = ingredients.getOrNull(15) ?: "",
                    strIngredient17 = ingredients.getOrNull(16) ?: "",
                    strIngredient18 = ingredients.getOrNull(17) ?: "",
                    strIngredient19 = ingredients.getOrNull(18) ?: "",
                    strIngredient20 = ingredients.getOrNull(19) ?: ""
                )
                firestoreviewModel.addReceta(mealcreada,auth.getCurrentUser()?.uid.toString())

                navegaraListaComidas()


            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar nueva Receta")
        }
    }
}