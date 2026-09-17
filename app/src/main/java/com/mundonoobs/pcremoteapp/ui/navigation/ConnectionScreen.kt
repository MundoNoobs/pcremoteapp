package com.mundonoobs.pcremoteapp.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mundonoobs.pcremoteapp.data.transport.ConnectionState
import com.mundonoobs.pcremoteapp.repository.RemoteControlManager
import kotlinx.coroutines.launch

@Composable
fun ConnectionScreen(manager: RemoteControlManager, onConnected: () -> Unit) {
    // Estado local para guardar la IP que escribe el usuario
    var ipAddress by remember { mutableStateOf("192.168.") }

    // Observamos el estado del flujo que viene del Repository
    val connectionState by manager.connectionState.collectAsState()
    val scope = rememberCoroutineScope()

    // Si el estado cambia a "Connected", ejecutamos la acción para cambiar de pantalla
    LaunchedEffect(connectionState) {
        if (connectionState is ConnectionState.Connected) {
            onConnected()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Conectar al Servidor PC", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = ipAddress,
            onValueChange = { ipAddress = it },
            label = { Text("Dirección IP (Wi-Fi)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { scope.launch { manager.connectWifi(ipAddress) } },
            modifier = Modifier.fillMaxWidth(),
            // Deshabilitar el botón si ya está intentando conectar
            enabled = connectionState !is ConnectionState.Connecting
        ) {
            if (connectionState is ConnectionState.Connecting) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Conectar por Wi-Fi")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar errores si los hay
        if (connectionState is ConnectionState.Error) {
            Text(
                text = "Error: ${(connectionState as ConnectionState.Error).message}",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}