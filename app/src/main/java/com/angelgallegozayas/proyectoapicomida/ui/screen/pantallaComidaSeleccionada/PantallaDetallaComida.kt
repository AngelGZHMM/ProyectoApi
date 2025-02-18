package com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaComidaSeleccionada

import android.graphics.Color.rgb
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.angelgallegozayas.proyectoapicomida.data.AuthManager
import com.angelgallegozayas.proyectoapicomida.data.firebase.FirestoreViewModel
import com.angelgallegozayas.proyectoapicomida.data.model.Meal
import com.angelgallegozayas.proyectoapicomida.data.model.RecetaUser
import com.angelgallegozayas.proyectoapicomida.data.repositories.repositoryList

@Composable
fun PantallaDetalleComidaScreen(
    comidaId: String,
    navigateToBack: () -> Unit,
    firestoreviewModel: FirestoreViewModel,
    navegaraModificarReceta: (recipeId: String) -> Unit,
    auth: AuthManager
) {
    // Definimos dos colores personalizados:
    // - titleBgColor: el color de fondo original para los títulos (amarillo brillante)
    // - cardBgColor: una versión más clara de ese color para el fondo del Card
    val titleBgColor = Color(rgb(233, 206, 68))
    val cardBgColor = Color(255, 250, 200)

    // Estados para la comida y el creador
    val comidaState = remember { mutableStateOf<Meal?>(null) }
    val creadorDeReceta = remember { mutableStateOf<RecetaUser?>(null) }

    LaunchedEffect(comidaId) {
        // Cargar la comida
        val comida = repositoryList.getComidaPorId(comidaId)
            ?: firestoreviewModel.cargarMealPorId(comidaId)
        comidaState.value = comida

        // Si la comida tiene creador, cargarlo
        comida?.idMeal?.let { id ->
            val recetaConCreador = firestoreviewModel.cargarRecetaPorIdConCreador(id)
            creadorDeReceta.value = recetaConCreador
        }
    }

    comidaState.value?.let { comida ->
        // Envolvemos todo en un Card con fondo personalizado
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = cardBgColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .padding(16.dp)
                .padding(bottom = 30.dp)
                .padding(top = 30.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Título de la receta con el fondo original
                Text(
                    text = comida.strMeal,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = titleBgColor,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Imagen con borde redondeado
                AsyncImage(
                    model = comida.strMealThumb,
                    contentDescription = "Imagen de ${comida.strMeal}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                // Sección de descripción (si existe)
                if (comida.strMealDescription.isNotBlank()) {
                    Text(
                        text = "Descripción",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = titleBgColor,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = comida.strMealDescription,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // Sección de ingredientes
                Text(
                    text = "Ingredientes",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = titleBgColor,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
                ListaIngredientes(strIngredients = listOf(
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
                ))

                // Sección de pasos a seguir
                Text(
                    text = "Pasos a seguir",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = titleBgColor,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = comida.strInstructions,
                    style = MaterialTheme.typography.bodyMedium
                )

                // Si el usuario es el creador, mostramos los botones de modificar y eliminar
                if (creadorDeReceta.value?.idusuario == auth.getCurrentUser()?.uid) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { navegaraModificarReceta(comida.idMeal) }
                        ) {
                            Text(text = "Modificar")
                        }

                        var showDeleteDialog by remember { mutableStateOf(false) }
                        if (showDeleteDialog) {
                            AlertDialog(
                                onDismissRequest = { showDeleteDialog = false },
                                title = { Text(text = "Confirmar eliminación") },
                                text = { Text(text = "¿Está seguro que desea eliminar esta receta?") },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            firestoreviewModel.eliminarReceta(comida.idMeal)
                                            navigateToBack()
                                            showDeleteDialog = false
                                        }
                                    ) {
                                        Text(text = "Confirmar")
                                    }
                                },
                                dismissButton = {
                                    TextButton(
                                        onClick = { showDeleteDialog = false }
                                    ) {
                                        Text(text = "Cancelar")
                                    }
                                }
                            )
                        }
                        Button(
                            onClick = { showDeleteDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text(text = "Eliminar")
                        }
                    }
                }

                // Botón de volver
                Button(
                    onClick = navigateToBack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Volver")
                }
            }
        }
    }
}

@Composable
fun ListaIngredientes(strIngredients: List<String>) {
    val ingredientesMarcados = remember { mutableStateListOf<Boolean>().apply {
        addAll(List(strIngredients.size) { false })
    } }
    for ((index, ingrediente) in strIngredients.withIndex()) {
        if (ingrediente.isEmpty()) {
            break
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
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    }
                ),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}
