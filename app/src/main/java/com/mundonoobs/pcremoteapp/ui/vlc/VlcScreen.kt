package com.mundonoobs.pcremoteapp.ui.vlc

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mundonoobs.pcremoteapp.repository.RemoteControlManager
import kotlinx.coroutines.launch

@Composable
fun VlcScreen(manager: RemoteControlManager) {
    // Observamos el estado de telemetría en tiempo real
    val telemetry by manager.telemetry.collectAsState()
    val scope = rememberCoroutineScope()

    // Extraemos los datos de forma segura
    val title = telemetry.title?.takeIf { it.isNotBlank() } ?: "VLC Detenido o Sin Medios"
    val time = telemetry.time ?: 0
    val duration = telemetry.duration ?: 0
    val isPlaying = telemetry.status == "playing" // Cambia a pause o stop dependiendo del estado

    // Función auxiliar para formatear los segundos a "MM:SS"
    fun formatTime(seconds: Int): String {
        val m = seconds / 60
        val s = seconds % 60
        return String.format("%02d:%02d", m, s)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icono superior de adorno
        Icon(
            imageVector = Icons.Default.MusicVideo,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Título de la pista o video actual
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 2
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Barra de progreso y tiempos
        val progress = if (duration > 0) time.toFloat() / duration.toFloat() else 0f

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(formatTime(time), style = MaterialTheme.typography.bodyMedium)
            Text(formatTime(duration), style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Botones de control (Anterior, Play/Pause, Siguiente)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { scope.launch { manager.sendCommand("vlc", "prev") } },
                modifier = Modifier.size(64.dp)
            ) {
                Icon(Icons.Default.SkipPrevious, contentDescription = "Anterior", modifier = Modifier.fillMaxSize())
            }

            // Botón central dinámico: cambia de Play a Pause según el estado de VLC
            IconButton(
                onClick = { scope.launch { manager.sendCommand("vlc", "play_pause") } },
                modifier = Modifier.size(80.dp)
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.PauseCircleFilled else Icons.Default.PlayCircleFilled,
                    contentDescription = "Play/Pause",
                    modifier = Modifier.fillMaxSize(),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(
                onClick = { scope.launch { manager.sendCommand("vlc", "next") } },
                modifier = Modifier.size(64.dp)
            ) {
                Icon(Icons.Default.SkipNext, contentDescription = "Siguiente", modifier = Modifier.fillMaxSize())
            }
        }
    }
}