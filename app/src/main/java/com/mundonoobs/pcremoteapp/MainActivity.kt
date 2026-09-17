package com.mundonoobs.pcremoteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.mundonoobs.pcremoteapp.repository.RemoteControlManager
import com.mundonoobs.pcremoteapp.ui.navigation.ConnectionScreen
import okhttp3.OkHttpClient

class MainActivity : ComponentActivity() {

    // Inicializamos las dependencias en la Actividad base
    private val okHttpClient = OkHttpClient()
    private lateinit var remoteManager: RemoteControlManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializamos el gestor (dejamos Bluetooth null temporalmente para probar Wi-Fi)
        remoteManager = RemoteControlManager(okHttpClient, null)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var isConnected by remember { mutableStateOf(false) }

                    // Enrutador básico: Si no está conectado, muestra el login; si sí, muestra el teclado
                    if (!isConnected) {
                        ConnectionScreen(
                            manager = remoteManager,
                            onConnected = { isConnected = true }
                        )
                    } else {
                        // Aquí conectaremos la Vista del Teclado en el siguiente paso
                        Text(text = "¡Conexión Exitosa! Preparando módulos...")
                    }
                }
            }
        }
    }
}