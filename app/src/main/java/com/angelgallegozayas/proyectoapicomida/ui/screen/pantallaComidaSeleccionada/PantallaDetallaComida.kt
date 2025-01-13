package com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaComidaSeleccionada


import android.graphics.Color.rgb
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.angelgallegozayas.proyectoapicomida.data.model.Meal
import com.angelgallegozayas.proyectoapicomida.data.repositories.repositoryList



@Composable
 fun PantallaDetalleComidaScreen(
    comidaId: String,
    navigateToBack: () -> Unit
) {
    // Estado para almacenar la comida seleccionada
    val comidaState = remember { mutableStateOf<Meal?>(null) }

    // Llamada a la función suspendida para obtener la comida seleccionada
    LaunchedEffect(comidaId) {
        val comida = repositoryList.getComidaPorId(comidaId)
        comidaState.value = comida
    }


    // Comida seleccionada cargada, mostramos los detalles
    comidaState.value?.let { comida ->
        //columna centrada

        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título
            Text(
                text = comida.strMeal,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp).padding(top = 30.dp)
                    .background(Color(rgb(233, 206, 68)),shape = RoundedCornerShape(16.dp))
                    .padding(5.dp)
            )
            // Imagen
            AsyncImage(
                model = comida.strMealThumb,
                contentDescription = "Imagen de ${comida.strMeal}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp)
            )


            Text(
                text = "Ingredientes",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
                    .padding(top = 10.dp)

                    .background(Color(rgb(233, 206, 68)),shape = RoundedCornerShape(16.dp))
                    .padding(5.dp)

            )
            // Lista de ingredientes
            val strIngredients: List<String> = listOf(
                comida.strIngredient1,
                comida.strIngredient2,
                comida.strIngredient3,
                comida.strIngredient4,
                comida.strIngredient5,
                comida.strIngredient6,
                comida.strIngredient7,
                comida.strIngredient8,
                comida.strIngredient9,
                comida.strIngredient10,
                comida.strIngredient11,
                comida.strIngredient12,
                comida.strIngredient13,
                comida.strIngredient14,
                comida.strIngredient15,
                comida.strIngredient16,
                comida.strIngredient17,
                comida.strIngredient18,
                comida.strIngredient19,
                comida.strIngredient20
            )  // Filtra los valores nulos

            ListaIngredientes(strIngredients)

            //Pasos a seguir
            Text(
                text = "Pasos a seguir",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp).padding(top = 10.dp)
                    .background(Color(rgb(233, 206, 68)),shape = RoundedCornerShape(16.dp))
                    .padding(5.dp)
            )
            Text(
                text = comida.strInstructions,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Button(onClick = navigateToBack,
                modifier = Modifier.padding(bottom = 30.dp))
                {
                Text(text = "Volver")
            }






        }
    }

}


@Composable
fun ListaIngredientes(strIngredients: List<String>) {
    val ingredientesMarcados = remember { mutableStateListOf<Boolean>().apply {
        addAll(List(strIngredients.size) { false })
    } } // Lista de estados para cada checkbox

    for ((index, ingrediente) in strIngredients.withIndex()) {
        if (ingrediente.isEmpty()) {
            break // Detiene el ciclo si se encuentra un valor vacío
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Checkbox(
                checked = ingredientesMarcados[index],
                onCheckedChange = { isChecked ->
                    ingredientesMarcados[index] = isChecked
                }
            )

            Text(
                text = ingrediente,
                style = MaterialTheme.typography.bodyMedium.copy(
                    textDecoration = if (ingredientesMarcados[index]) {
                        TextDecoration.LineThrough // Tachado si está marcado
                    } else {
                        TextDecoration.None
                    }
                ),
                modifier = Modifier.padding(start = 8.dp) // Espacio entre checkbox y texto
            )
        }
    }
}












