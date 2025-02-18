package com.angelgallegozayas.proyectoapicomida.data.scaffold

import android.graphics.Color.rgb
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.angelgallegozayas.proyectoapicomida.R
import com.angelgallegozayas.proyectoapicomida.data.AuthManager
import com.angelgallegozayas.proyectoapicomida.data.firebase.FirestoreViewModel
import com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaListaComidas.PantallaListaComidasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    nombre: String,
    auth: AuthManager,
    viewModelFirestore: FirestoreViewModel,
    navigateToPantallaPerfil: () -> Unit,
    navigateToInicio: () -> Unit,
    navigateToFavoritos: () -> Unit,
    viewModel: PantallaListaComidasViewModel,
    navegarAPantallaListaComidas: () -> Unit,

    ) {
    val pantallaActual = viewModel.pantallaActual.value
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                stringResource(id = R.string.nombre_app),
                style = MaterialTheme.typography.titleLarge,
                fontSize = 24.sp
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { },
            ) {
                Image(
                    painter = painterResource(id = R.drawable.restaurant_logo_sin_fondo_),
                    contentDescription = "Botón usuario",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
            }
        },
        actions = {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .background(Color(0xffedf3fc), shape = CircleShape)
                    .clickable { expanded = !expanded }
                    .padding(12.dp)
                    .animateContentSize(
                        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.usuario),
                    contentDescription = "Botón usuario",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )

                if (expanded) {
                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                }
                AnimatedVisibility(
                    visible = expanded,
                    enter = fadeIn(animationSpec = tween(100)),
                    exit = fadeOut(animationSpec = tween(250))
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(top = 10.dp),
                            text = nombre,
                            color = Color.Black
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color.Black
        )
    )

    if (expanded) {
        Popup(
            alignment = Alignment.Center,
            onDismissRequest = { expanded = false }
        ) {
            Box(
                modifier = Modifier
                    .width(220.dp) // Tamaño fijo del popup
                    .height(250.dp)
                    .background(Color(rgb(233, 206, 68)), shape = RoundedCornerShape(12.dp))
                    .border(4.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(Color(rgb(233, 206, 68))),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Text(
                            "✖",
                            color = Color.Red,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .clickable { expanded = false }
                                .padding(4.dp) // Para que tenga espacio y sea más fácil de tocar
                        )
                    }
                    Button(onClick = {
                        expanded = false
                        navigateToPantallaPerfil()
                    }) {
                        Text("Perfil")
                    }
                    if(pantallaActual != "Favoritos"){
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { expanded = false
                        navigateToFavoritos()
                    }) {
                        Text("Favoritos")
                    }
                    }else {

                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            expanded = false
                            navegarAPantallaListaComidas()
                        }) {
                            Text("Listado completo")
                        }
                    }



                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            auth.signOut()
                            navigateToInicio()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Cerrar sesión", color = Color.White)
                    }
                }
            }
        }







}
}