package com.mundonoobs.pcremoteapp.ui.ppt

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mundonoobs.pcremoteapp.repository.RemoteControlManager
import kotlinx.coroutines.launch

@Composable
fun PptScreen(manager: RemoteControlManager) {
    // Escuchamos la telemetría en tiempo real
    val telemetry by manager.telemetry.collectAsState()
    val scope = rememberCoroutineScope()

    // Extraemos el número de diapositiva y las notas
    val currentSlide = telemetry.current_slide ?: 0
    val notes = telemetry.notes?.takeIf { it.isNotBlank() } ?: "Sin notas del moderador para esta diapositiva."

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Fila superior: Botón Anterior, Número actual, Botón Siguiente
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilledIconButton(
                onClick = { scope.launch { manager.sendCommand("ppt", "prev") } },
                modifier = Modifier.size(80.dp)
            ) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Anterior", modifier = Modifier.size(32.dp))
            }

            Text(
                text = "Slide $currentSlide",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            FilledIconButton(
                onClick = { scope.launch { manager.sendCommand("ppt", "next") } },
                modifier = Modifier.size(80.dp)
            ) {
                Icon(Icons.Default.ArrowForwardIos, contentDescription = "Siguiente", modifier = Modifier.size(32.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Notas del Moderador",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Tarjeta desplazable para leer las notas cómodamente
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // Ocupa todo el espacio vertical restante
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Text(
                text = notes,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            )
        }
    }
}