package com.angelgallegozayas.proyectoapicomida

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import com.angelgallegozayas.proyectoapicomida.data.AuthManager
import com.angelgallegozayas.proyectoapicomida.data.firebase.FirestoreManager
import com.angelgallegozayas.proyectoapicomida.data.firebase.FirestoreViewModel
import com.angelgallegozayas.proyectoapicomida.ui.navegacion.Navegacion
import com.angelgallegozayas.proyectoapicomida.ui.theme.ProyectoApiComidaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoApiComidaTheme {
                val auth = AuthManager()
                auth.resetAuthState()
                auth.initializeGoogleSignIn(this)
                auth.signOut()

                val context = LocalContext.current
                val firestoreManager = FirestoreManager(auth, context)
                val factory = FirestoreViewModel.FirestoreViewModelFactory(firestoreManager, auth)
                val viewModelFirestore = factory.create(FirestoreViewModel::class.java)

                Navegacion(auth,viewModelFirestore)

            }
        }
    }
}
