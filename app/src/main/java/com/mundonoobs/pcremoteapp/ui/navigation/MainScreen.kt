package com.mundonoobs.pcremoteapp.ui.navigation

import com.mundonoobs.pcremoteapp.ui.vlc.VlcScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Slideshow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mundonoobs.pcremoteapp.repository.RemoteControlManager
import com.mundonoobs.pcremoteapp.ui.ppt.PptScreen
import com.mundonoobs.pcremoteapp.ui.keyboard.KeyboardScreen
import kotlinx.coroutines.launch

@Composable
fun MainScreen(manager: RemoteControlManager) {
    // Estado para saber qué pestaña está activa (iniciamos en VLC)
    var selectedTab by remember { mutableStateOf("vlc") }
    val scope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.PlayArrow, contentDescription = "VLC") },
                    label = { Text("VLC") },
                    selected = selectedTab == "vlc",
                    onClick = {
                        selectedTab = "vlc"
                        scope.launch { manager.changeView("vlc") }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Slideshow, contentDescription = "PPT") },
                    label = { Text("PPT") },
                    selected = selectedTab == "ppt",
                    onClick = {
                        selectedTab = "ppt"
                        scope.launch { manager.changeView("ppt") }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Keyboard, contentDescription = "Teclado") },
                    label = { Text("Teclado") },
                    selected = selectedTab == "keyboard",
                    onClick = {
                        selectedTab = "keyboard"
                        scope.launch { manager.changeView("keyboard") }
                    }
                )
            }
        }
    ) { innerPadding ->
        // Contenedor dinámico que cambia según la pestaña
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when (selectedTab) {
                "vlc" -> VlcScreen(manager)
                "ppt" -> PptScreen(manager)
                "keyboard" -> KeyboardScreen(manager)
            }
        }
    }
}
