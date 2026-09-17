package com.mundonoobs.pcremoteapp.repository

import android.bluetooth.BluetoothAdapter
import com.google.gson.Gson
import com.mundonoobs.pcremoteapp.data.model.CommandMessage
import com.mundonoobs.pcremoteapp.data.model.TelemetryMessage
import com.mundonoobs.pcremoteapp.data.transport.BluetoothTransport
import com.mundonoobs.pcremoteapp.data.transport.ConnectionState
import com.mundonoobs.pcremoteapp.data.transport.ConnectionTransport
import com.mundonoobs.pcremoteapp.data.transport.WebSocketTransport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

class RemoteControlManager(
    private val okHttpClient: OkHttpClient,
    private val bluetoothAdapter: BluetoothAdapter?
) {
    private var activeTransport: ConnectionTransport? = null
    private val gson = Gson()
    private val scope = CoroutineScope(Dispatchers.IO)

    // El estado de la conexión que la UI observará para mostrar un ícono verde o rojo
    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionState = _connectionState.asStateFlow()

    // La telemetría en vivo (tiempo de VLC, número de PPT) que la UI dibujará
    private val _telemetry = MutableStateFlow(TelemetryMessage())
    val telemetry = _telemetry.asStateFlow()

    suspend fun connectWifi(ip: String, port: Int = 8765): Boolean {
        activeTransport?.disconnect()
        val transport = WebSocketTransport(okHttpClient)
        activeTransport = transport

        observeTransport(transport)
        return transport.connect("ws://$ip:$port")
    }

    suspend fun connectBluetooth(macAddress: String): Boolean {
        if (bluetoothAdapter == null) return false

        activeTransport?.disconnect()
        val transport = BluetoothTransport(bluetoothAdapter)
        activeTransport = transport

        observeTransport(transport)
        return transport.connect(macAddress)
    }

    private fun observeTransport(transport: ConnectionTransport) {
        scope.launch {
            transport.connectionState.collect { state ->
                _connectionState.value = state
            }
        }
        scope.launch {
            transport.incomingMessages.collect { jsonString ->
                try {
                    val data = gson.fromJson(jsonString, TelemetryMessage::class.java)
                    _telemetry.value = data
                } catch (e: Exception) {
                    // Ignorar paquetes malformados
                }
            }
        }
    }

    suspend fun sendCommand(target: String? = null, action: String? = null, key: String? = null) {
        val command = CommandMessage(target = target, action = action, key = key)
        val json = gson.toJson(command)
        activeTransport?.sendMessage(json)
    }

    suspend fun changeView(viewName: String) {
        val command = CommandMessage(set_view = viewName)
        activeTransport?.sendMessage(gson.toJson(command))
    }

    suspend fun disconnect() {
        activeTransport?.disconnect()
        activeTransport = null
    }
}