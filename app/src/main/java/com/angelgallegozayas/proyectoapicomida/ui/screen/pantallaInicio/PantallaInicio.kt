package com.angelgallegozayas.proyectoapicomida.ui.screen.pantallaInicio

import android.R
import com.angelgallegozayas.proyectoapicomida.data.AuthManager
import com.angelgallegozayas.proyectoapicomida.ui.theme.Purple40
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun PantallaInicioScreen(
    auth: AuthManager,
    navegarAPantallaListaComidas: () -> Unit,
    navegaraConectado: () -> Unit,
    navegaraContraseñaOlvidada: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var passwd by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val authState by auth.authState.collectAsState()
    val googleSignLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                Log.d("GoogleSignIn", "Handling Google Sign-In result")
                auth.handleGoogleSignInResult(task)
            } catch (e: Exception) {
                Log.e("GoogleSignIn", "Error handling Google Sign-In result", e)
            }
        }
    }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthManager.AuthRes.Success -> {
                Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                auth.resetAuthState()
                navegarAPantallaListaComidas()
            }
            is AuthManager.AuthRes.Error -> {
                Toast.makeText(
                    context,
                    (authState as AuthManager.AuthRes.Error).errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
            is AuthManager.AuthRes.Idle -> {}
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 40.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberImagePainter("https://png.pngtree.com/png-clipart/20220923/original/pngtree-chef-and-spatula-logo-png-image_8628649.png"),
                    contentDescription = "Tu recetario logo",
                    modifier = Modifier.size(200.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "T u   r e c e t a r i o",
                    fontSize = 28.sp,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.Monospace
                )

                Spacer(modifier = Modifier.height(50.dp))

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.width(335.dp),
                    leadingIcon = {
                        Icon(Icons.Default.Mail, contentDescription = "Ícono de email")
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = passwd,
                    onValueChange = { passwd = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.width(335.dp),
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = "Ícono de password")
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    modifier = Modifier
                        .width(335.dp)
                        .height(50.dp),
                    onClick = {
                        scope.launch {
                            signIn(auth, email, passwd, context)
                        }
                    },
                ) {
                    if (auth.progressBar.observeAsState().value == true) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(30.dp),
                            strokeWidth = 3.dp
                        )
                    } else {
                        Text("Acceder")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = ("¿Olvidaste tu contraseña?"),
                    modifier = Modifier.clickable { navegaraContraseñaOlvidada() },
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Default,
                        textDecoration = TextDecoration.Underline,
                        color = Purple40
                    )
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "",
                    style = TextStyle(color = Color.Gray)
                )
                Text(
                    text = ("¿No tienes cuenta? ¡Registrate!"),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 20.dp)
                        .clickable { navegaraConectado() },
                    style = TextStyle(
                        color = Purple40,
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Default,
                        textDecoration = TextDecoration.Underline
                    )
                )



                BtnGoogle(
                    onClick = {
                        scope.launch {
                            signAnonimously(auth)
                        }
                    },
                    text = "Continuar como invitado",
                    icon = R.drawable.ic_delete,
                    color = Color(0xFF363636),
                    cargando = auth.progressBarAnonimous.observeAsState().value?:false
                )
                Spacer(modifier = Modifier.height(15.dp))

                BtnGoogle(
                    onClick = {
                        googleSignLauncher.launch(auth.getGoogleSignInIntent())
                    },
                    text = "Continuar con Google",
                    icon = R.drawable.ic_menu_add,
                    color = Color(0xFFF1F1F1),
                    cargando = auth.progressBarGoogle.observeAsState().value ?: false
                )


            }
        }
    }
}

@Composable
fun BtnGoogle(onClick: () -> Unit, text: String, icon: Int, color: Color, cargando: Boolean) {
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .width(335.dp)
            .height(50.dp),
        border = BorderStroke(
            width = 2.dp,
            color = if (icon == R.drawable.alert_light_frame) color else Color.Gray
        ),
        colors = ButtonColors(
            containerColor = color,
            contentColor = if (icon == R.drawable.btn_dialog) Color.White else Color.Black,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.Gray,
        )
    ) {
        if (cargando) {
            if (icon == R.drawable.bottom_bar) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(30.dp),
                    strokeWidth = 3.dp
                )
            } else {
                CircularProgressIndicator(
                    color = Color.Black,
                    modifier = Modifier.size(30.dp),
                    strokeWidth = 3.dp
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .padding(start = 6.dp, end = 8.dp, top = 6.dp, bottom = 6.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    modifier = Modifier.size(24.dp),
                    contentDescription = text,
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = text,
                    color = if (icon == R.drawable.dialog_frame) Color.White else Color.Black
                )
            }
        }
    }
}

suspend fun signIn(auth: AuthManager, email: String, passwd: String, context: Context) {
    if (email.isNotEmpty() && passwd.isNotEmpty()) {
        auth.signInWithEmailAndPassword(email, passwd)
    } else {
        Toast.makeText(context, "Complete los campos", Toast.LENGTH_SHORT).show()
    }
}

suspend fun signAnonimously(auth: AuthManager) {
    // Cerramos la sesion antes de iniciar como anonimos
    // Para evitar posibles intervenciones de la cache del sistema
    auth.signOut()
    auth.signAnonimously()
}