package com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaFavoritos

import com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaListaComidas.PantallaListaComidasViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.angelgallegozayas.proyectoapicomida.data.AuthManager
import com.angelgallegozayas.proyectoapicomida.data.firebase.FirestoreViewModel
import com.angelgallegozayas.proyectoapicomida.data.model.Meal
import com.angelgallegozayas.proyectoapicomida.data.scaffold.TopBar

@Composable
fun PantallaListaFavoritosScreen(
    auth: AuthManager,
    viewModel: PantallaListaComidasViewModel,
    navigateToDetalle: (String) -> Unit,
    navigateToInicio: () -> Unit,
    navigateToPerfil: () -> Unit,
    firestoreviewModel: FirestoreViewModel,
    navigateToCrearReceta: () -> Unit,
    navigateToFavoritos: () -> Unit,
    navegarAPantallaListaComidas: () -> Unit,

    ) {
    //val comidasState by viewModel.comidas.collectAsStateWithLifecycle()
    firestoreviewModel.listarTodaslasRecetas()
    viewModel.pantallaActual.value = "Favoritos"

    val comidasCreadas by firestoreviewModel.firestoreMeals.observeAsState(emptyList())
    val comidasState by viewModel.comidas.collectAsStateWithLifecycle()
    val listacomidasTotal = comidasState + comidasCreadas
//    datos comidas favoritas solo contiene el id del usuario y una lista de favoritos , tenemos que recorrer la lista de favoritos en la cual obtendremos
//    el id de la receta y compararlo con la lista de comidas totales para obtener las comidas favoritas
    val datoscomidasFavoritas = firestoreviewModel.favorites.collectAsStateWithLifecycle()
    val comidasFavoritas = mutableListOf<Meal>()
    datoscomidasFavoritas.value.forEach { favoritos ->
        listacomidasTotal.forEach { comida ->
            if (favoritos.idMeal == comida.idMeal) {
                comidasFavoritas.add(comida)
            }
        }
    }



    val errorState by viewModel.error.collectAsStateWithLifecycle()
    val user = auth.getCurrentUser()
    val progressBar by firestoreviewModel.isLoading.observeAsState(false)

    // Guardar la posición de la lista
    val scrollState = rememberLazyGridState()

    Column(modifier = Modifier.fillMaxSize()) {
        when {
            errorState != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = errorState ?: "Error desconocido", color = Color.Red, style = MaterialTheme.typography.bodyMedium)
                }
            }
            comidasState.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            else -> {
                if (user != null) {
                    Scaffold(
                        topBar = {
                            val nombre = user.displayName ?: "Usuario"
                            TopBar(nombre,
                                auth,
                                firestoreviewModel,
                                navigateToPerfil,
                                navigateToInicio,
                                navigateToFavoritos,
                                viewModel,
                                navegarAPantallaListaComidas)
                        }
                    ) { innerPadding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            if (progressBar) {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator()
                                }
                            } else {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(2),
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp)
                                        .clip(RoundedCornerShape(16.dp)),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    state = scrollState // Evita que la lista vuelva al inicio
                                ) {
                                    items(comidasFavoritas) { comida ->
                                        ComidaCard(
                                            comida = comida,
                                            navigateToDetalle = navigateToDetalle,
                                            auth = auth,
                                            firestoreviewModel = firestoreviewModel
                                        )
                                    }
                                    // Tarjeta para agregar receta
                                    item {
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .wrapContentHeight()
                                                .clickable {navigateToCrearReceta() }
                                        ) {
                                            Column(
                                                modifier = Modifier.background(Color.Green),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .aspectRatio(1f)
                                                ) {
                                                    Image(
                                                        painter = rememberAsyncImagePainter("https://png.pngtree.com/png-clipart/20230915/original/pngtree-plus-sign-symbol-simple-design-pharmacy-logo-black-vector-png-image_12186664.png"),
                                                        contentDescription = "Agregar Receta",
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
    }
}

@Composable
private fun ComidaCard(
    auth: AuthManager,
    firestoreviewModel: FirestoreViewModel,
    comida: Meal,
    navigateToDetalle: (String) -> Unit
) {
    val favorites by firestoreviewModel.favorites.collectAsStateWithLifecycle()
    val isFavorited = favorites.any { it.idMeal == comida.idMeal }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { navigateToDetalle(comida.idMeal) }
    ) {
        Column(
            modifier = Modifier.background(Color(0xFFE9CE44)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
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
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = comida.strMeal,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp, top = 8.dp, end = 30.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                IconButton(
                    onClick = {
                        firestoreviewModel.addFavorito(
                            comida.idMeal,
                            usuario = auth.getCurrentUser()?.uid ?: "Anonimo"
                        )
                    },
                    modifier = Modifier.align(Alignment.TopEnd).padding(end = 2.dp)
                ) {
                    if (isFavorited) {
                        Icon(imageVector = Icons.Filled.Favorite, contentDescription = "Favorito", tint = Color.Red)
                    } else {
                        Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = "No es favorito", tint = Color.Gray)
                    }
                }
            }
        }
    }
}
