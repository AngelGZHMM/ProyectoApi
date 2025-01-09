package com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaInicio


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PantallaInicioScreen(navegarAPantallaListaComidas: () -> Unit) {
    // Variables para el estado de los campos de texto
    var usuario = remember { androidx.compose.runtime.mutableStateOf("") }
    var contrasena = remember { androidx.compose.runtime.mutableStateOf("") }

    // Aquí definimos el diseño de la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Título
        Text(
            text = "Bienvenido",
            fontSize = 32.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Campo para el nombre de usuario
        Text("Usuario:")
        TextField(
            value = usuario.value,
            onValueChange = { usuario.value = it },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            placeholder = { Text("Introduce tu usuario") }
        )

        // Campo para la contraseña
        Text("Contraseña:")
        TextField(
            value = contrasena.value,
            onValueChange = { contrasena.value = it },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            placeholder = { Text("Introduce tu contraseña") },
            visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para navegar a la siguiente pantalla
        Button(
            onClick = {
                // Aquí puedes agregar validación si lo necesitas
                if (usuario.value.isNotEmpty() && contrasena.value.isNotEmpty()) {
                    navegarAPantallaListaComidas()
                }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text(text = "Iniciar sesión")
        }
    }
}