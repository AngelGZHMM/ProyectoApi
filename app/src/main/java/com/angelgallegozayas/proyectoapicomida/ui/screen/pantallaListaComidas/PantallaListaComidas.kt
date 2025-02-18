package com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaListaComidas

import android.util.Log
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.angelgallegozayas.proyectoapicomida.data.AuthManager
import com.angelgallegozayas.proyectoapicomida.data.firebase.FirestoreViewModel
import com.angelgallegozayas.proyectoapicomida.data.model.Meal
import com.angelgallegozayas.proyectoapicomida.data.model.RecetaUser
import com.angelgallegozayas.proyectoapicomida.data.scaffold.TopBar

@Composable
fun PantallaListaComidasScreen(
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
    firestoreviewModel.updateUserId(auth.getCurrentUser()?.uid ?: "Anonimo")
    firestoreviewModel.listarTodaslasRecetas()
    viewModel.pantallaActual.value = "Listado"

    val comidasCreadas by firestoreviewModel.firestoreMeals.observeAsState(emptyList())
    val comidasState by viewModel.comidas.collectAsStateWithLifecycle()
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
                                navegarAPantallaListaComidas
                                )
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
                                    items(comidasState) { comida ->
                                        ComidaCard(
                                            comida = comida,
                                            navigateToDetalle = navigateToDetalle,
                                            auth = auth,
                                            firestoreviewModel = firestoreviewModel
                                        )
                                    }
                                    items(comidasCreadas) { comida ->
                                        ComidaCard(
                                            comida = comida,
                                            navigateToDetalle = navigateToDetalle,
                                            auth = auth,
                                            firestoreviewModel = firestoreviewModel
                                        )
                                    }

//                                    si el usuario es anonimo no puede agregar recetas
                                    if (user.displayName != null) {
// Tarjeta para agregar receta
                                    item {
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .wrapContentHeight()
                                                .clickable { navigateToCrearReceta() }
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

                                }else{
                                        item {
                                            // Estado para controlar la visibilidad del diálogo
                                            var showDialog by remember { mutableStateOf(false) }

                                            // Si showDialog es true, mostramos el AlertDialog
                                            if (showDialog) {
                                                AlertDialog(
                                                    onDismissRequest = { showDialog = false },
                                                    title = { Text(text = "Iniciar Sesión") },
                                                    text = { Text(text = "Debe iniciar sesión para agregar una receta.") },
                                                    confirmButton = {
                                                        TextButton(
                                                            onClick = { showDialog = false }
                                                        ) {
                                                            Text("Ok")
                                                        }
                                                    }
                                                )
                                            }

                                            Card(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .wrapContentHeight()
                                                    .clickable {
                                                        // Al hacer click, se activa el diálogo
                                                        showDialog = true
                                                    }
                                            ) {
                                                Column(
                                                    modifier = Modifier.background(Color.Gray),
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
    val creadorDeReceta = remember { mutableStateOf<RecetaUser?>(null) }

    LaunchedEffect(comida.idMeal) {
        val recetaConCreador = firestoreviewModel.cargarRecetaPorIdConCreador(comida.idMeal)
        creadorDeReceta.value = recetaConCreador
    }

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

                if (creadorDeReceta.value?.idusuario == auth.getCurrentUser()?.uid) {
                    Text(
                        text = "Creado por ti",
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                            .background(Color.White.copy(alpha = 0.7f)),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = comida.strMeal,
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color(0xFFB2952A), shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .widthIn(max = 100.dp), // Evita que el cuadro sea muy ancho
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    maxLines = 1, // Limita a 1 líneas para evitar solapamiento
                    overflow = TextOverflow.Ellipsis // Muestra "..." si es muy largo
                )

                IconButton(
                    onClick = {
                        firestoreviewModel.addFavorito(
                            comida.idMeal,
                            usuario = auth.getCurrentUser()?.uid ?: "Anonimo"
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 2.dp)
                ) {
                    if (isFavorited) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Favorito",
                            tint = Color.Red
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = "No es favorito",
                            tint = Color.Gray
                        )
                    }
                }
            }
        }
    }
}
