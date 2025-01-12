package com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaListaComidas

import android.graphics.Color.rgb
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.angelgallegozayas.proyectoapicomida.data.model.Meal

@Composable
fun PantallaListaComidasScreen(
    viewModel: PantallaListaComidasViewModel,
    navigateToDetalle: (String) -> Unit,
    navigateToInicio: () -> Unit
) {
    val comidasState by viewModel.comidas.collectAsStateWithLifecycle()
    val errorState by viewModel.error.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        // Manejo de estados: error, cargando, o mostrando las comidas
        when {
            errorState != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorState ?: "Error desconocido",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            comidasState.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            else -> {Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(bottom = 35.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, bottom = 30.dp,top = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween // Distribuye los botones uniformemente
                    ) {
                        // Caja centrada para "Seleccione su Receta"
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 35.dp)
                        ) {
                            // FAB para regresar a la pantalla de inicio
                            FloatingActionButton(
                                onClick = {
                                    navigateToInicio()
                                },
                                modifier = Modifier
                            ) {
                                Text(
                                    text = "Inicio",
                                    color = Color.Red
                                )
                            }

                            //  para centrar la caja
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                //  para centrar el texto
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = Color(rgb(233, 206, 68)), // Fondo de la caja
                                            shape = RoundedCornerShape(16.dp) // Bordes redondeados
                                        )
                                        .padding(
                                            top = 16.dp,
                                            end = 16.dp,
                                            bottom = 16.dp
                                        ), // Padding interno
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Seleccione su Receta",
                                        color = Color.Black,
                                        modifier = Modifier.padding(start = 12.dp)
                                    )
                                }
                            }
                        }
                    }

                    // LazyVerticalGrid para las comidas
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),

                    ) {
                        items(comidasState) { comida ->
                            ComidaCard(comida = comida, navigateToDetalle = navigateToDetalle)
                        }
                        item{
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .clickable {}
                            ) {
                                Column(
                                    modifier = Modifier
                                        .background(Color.Green),
                                    horizontalAlignment = Alignment.CenterHorizontally

                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(1f)
                                    ) {
                                        Image(
                                            painter = rememberAsyncImagePainter("https://png.pngtree.com/png-clipart/20230915/original/pngtree-plus-sign-symbol-simple-design-pharmacy-logo-black-vector-png-image_12186664.png"),
                                            contentDescription = "Imagen",
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .heightIn(min = 150.dp),
                                            contentScale = ContentScale.Crop
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Agregar Receta",
                                        modifier = Modifier.padding(8.dp),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }

                        }
                    }
                }
            }

            }

        }

    }
}

@Composable
private fun ComidaCard(comida: Meal, navigateToDetalle: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                navigateToDetalle(comida.idMeal)
            }
    ) {
        Column(
            modifier = Modifier
                .background(Color(rgb(233, 206, 68))),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(comida.strMealThumb),
                    contentDescription = "Imagen de ${comida.strMeal}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 150.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = comida.strMeal,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}


