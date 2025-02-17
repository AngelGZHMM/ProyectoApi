package com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaPerfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.angelgallegozayas.proyectoapicomida.data.AuthManager
import kotlinx.coroutines.launch

@Composable
fun PantallaPerfil(auth: AuthManager, onBack: () -> Unit) {
    val user = auth.getCurrentUser()
    var nombre by remember { mutableStateOf(TextFieldValue(user?.displayName ?: "")) }
    var mensajeError by remember { mutableStateOf<String?>(null) }
    var mensajeExito by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope() // Scope para ejecutar la coroutine

    Scaffold(
        topBar = {
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally



        ) {
           Box(
    modifier = Modifier
        .size(100.dp)
        .clip(CircleShape)
        .background(Color.Gray),
    contentAlignment = Alignment.Center
) {
    val initial = if (nombre.text.isNotEmpty()) nombre.text.first().uppercase().toString() else "I"
    Text(
        text = initial,
        style = MaterialTheme.typography.titleMedium,
        color = Color.White,
        fontSize =  50.sp

    )
}

            OutlinedTextField(
                value = user?.uid ?: "Desconocido",
                onValueChange = {},
                label = { Text("ID de Usuario") },
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = user?.email ?: "Invitado",
                onValueChange = {},
                label = { Text("Correo Electrónico") },
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
//                tendra el campo por defecto el nom del usuario

//                label = { Text("Nombre" ) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        try {
                            auth.updateName(nombre.text) // Llamamos a la función suspend dentro de la coroutine
                            mensajeExito = "Nombre actualizado correctamente"
                            mensajeError = null
                            onBack()
                        } catch (e: Exception) {
                            mensajeError = "Error al actualizar el nombre: ${e.message}"
                            mensajeExito = null
                        }
                    }


                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Cambios")
            }

            // Mostrar mensaje de éxito o error
            mensajeExito?.let {
                Text(text = it, color = Color.Green, modifier = Modifier.padding(top = 8.dp))
            }
            mensajeError?.let {
                Text(text = it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}

