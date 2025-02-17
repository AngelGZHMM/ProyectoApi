package com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaModificacionReceta

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.angelgallegozayas.proyectoapicomida.data.AuthManager
import com.angelgallegozayas.proyectoapicomida.data.firebase.FirestoreViewModel
import com.angelgallegozayas.proyectoapicomida.data.model.Meal

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color

@Composable
fun PantallaModificarReceta(
    initialMeal: Meal,
    auth: AuthManager,
    firestoreviewModel: FirestoreViewModel,
    navigateBack: () -> Unit
) {
    var mealName by remember { mutableStateOf(initialMeal.strMeal) }
    var mealThumb by remember { mutableStateOf(initialMeal.strMealThumb) }
    var mealInstructions by remember { mutableStateOf(initialMeal.strInstructions) }
    var mealDescription by remember { mutableStateOf(initialMeal.strMealDescription) }

    // Lista observable de ingredientes
    val ingredients = remember {
        mutableStateListOf<String>().apply {
            addAll(
                listOf(
                    initialMeal.strIngredient1,
                    initialMeal.strIngredient2,
                    initialMeal.strIngredient3,
                    initialMeal.strIngredient4,
                    initialMeal.strIngredient5,
                    initialMeal.strIngredient6,
                    initialMeal.strIngredient7,
                    initialMeal.strIngredient8,
                    initialMeal.strIngredient9,
                    initialMeal.strIngredient10,
                    initialMeal.strIngredient11,
                    initialMeal.strIngredient12,
                    initialMeal.strIngredient13,
                    initialMeal.strIngredient14,
                    initialMeal.strIngredient15,
                    initialMeal.strIngredient16,
                    initialMeal.strIngredient17,
                    initialMeal.strIngredient18,
                    initialMeal.strIngredient19,
                    initialMeal.strIngredient20
                ).filter { it.isNotEmpty() }
            )
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .padding(top = 16.dp)
            .padding(bottom = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Editar Receta",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        OutlinedTextField(
            value = mealName,
            onValueChange = { mealName = it },
            label = { Text("Nombre de la receta") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))


        if (mealThumb.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(mealThumb),
                contentDescription = "Imagen de la receta",
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = mealThumb,
            onValueChange = { mealThumb = it },
            label = { Text("URL de la imagen") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))


        OutlinedTextField(
            value = mealDescription,
            onValueChange = { mealDescription = it },
            label = { Text("Descripcion de la receta") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ingredientes",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Mostrar la lista de ingredientes
        ingredients.forEachIndexed { index, ingredient ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = ingredient,
                    onValueChange = { newVal -> ingredients[index] = newVal },
                    label = { Text("Ingrediente ${index + 1}") },
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = { ingredients.removeAt(index) },
                    modifier = Modifier.size(40.dp),
                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Red)
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Botón para agregar un nuevo ingrediente
        Button(
            onClick = { ingredients.add("") },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Agregar Ingrediente")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón para guardar los cambios
        Button(
            onClick = {
                val updatedMeal = initialMeal.copy(
                    strMeal = mealName,
                    strMealThumb = mealThumb,
                    strInstructions = mealInstructions,
                    strMealDescription = mealDescription,
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
                firestoreviewModel.updateReceta(updatedMeal, auth.getCurrentUser()?.uid ?: "")
                navigateBack()
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        ) {
            Text("Guardar Cambios", style = MaterialTheme.typography.titleMedium)
        }
    }
}
