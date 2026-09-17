package com.mundonoobs.pcremoteapp.ui.keyboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mundonoobs.pcremoteapp.repository.RemoteControlManager
import kotlinx.coroutines.launch

@Composable
fun KeyboardScreen(manager: RemoteControlManager) {
    val scope = rememberCoroutineScope()

    fun pressKey(key: String) {
        scope.launch { manager.sendCommand(target = "keyboard", action = "press", key = key) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("Teclado Global", style = MaterialTheme.typography.headlineMedium)

        // Fila 1: Multimedia y Volumen
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilledTonalButton(onClick = { pressKey("volumemute") }, modifier = Modifier.size(80.dp, 60.dp)) {
                Icon(Icons.Default.VolumeOff, contentDescription = "Mute")
            }
            FilledTonalButton(onClick = { pressKey("volumedown") }, modifier = Modifier.size(80.dp, 60.dp)) {
                Icon(Icons.Default.VolumeDown, contentDescription = "Bajar Volumen")
            }
            FilledTonalButton(onClick = { pressKey("volumeup") }, modifier = Modifier.size(80.dp, 60.dp)) {
                Icon(Icons.Default.VolumeUp, contentDescription = "Subir Volumen")
            }
        }

        // Fila 2: Flecha Arriba
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            ElevatedButton(onClick = { pressKey("up") }, modifier = Modifier.size(80.dp, 60.dp)) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Arriba")
            }
        }

        // Fila 3: Flechas Izquierda, Abajo, Derecha
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ElevatedButton(onClick = { pressKey("left") }, modifier = Modifier.size(80.dp, 60.dp)) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Izquierda")
            }
            Spacer(modifier = Modifier.width(16.dp))
            ElevatedButton(onClick = { pressKey("down") }, modifier = Modifier.size(80.dp, 60.dp)) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Abajo")
            }
            Spacer(modifier = Modifier.width(16.dp))
            ElevatedButton(onClick = { pressKey("right") }, modifier = Modifier.size(80.dp, 60.dp)) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Derecha")
            }
        }

        // Fila 4: Teclas Especiales
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { pressKey("esc") }, modifier = Modifier.weight(1f).padding(horizontal = 4.dp)) {
                Text("ESC")
            }
            Button(onClick = { pressKey("space") }, modifier = Modifier.weight(1f).padding(horizontal = 4.dp)) {
                Text("ESPACIO")
            }
            Button(onClick = { pressKey("enter") }, modifier = Modifier.weight(1f).padding(horizontal = 4.dp)) {
                Text("ENTER")
            }
        }

        // Fila 5: Mostrar y Ocultar (NUEVOS BOTONES)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { pressKey("´") }, // Envía el acento agudo
                modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Mostrar")
            }
            Button(
                onClick = { pressKey("'") }, // Envía la comilla simple
                modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Ocultar")
            }
        }
    }
}