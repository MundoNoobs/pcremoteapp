package com.mundonoobs.pcremoteapp.data.transport

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

class BluetoothTransport(private val adapter: BluetoothAdapter) : ConnectionTransport {
    // UUID estándar para Serial Port Profile (SPP)
    private val SPP_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    private var socket: BluetoothSocket? = null
    private var outputStream: OutputStream? = null
    private val _messages = MutableSharedFlow<String>(extraBufferCapacity = 64)
    override val incomingMessages = _messages.asSharedFlow()

    private val _state = MutableSharedFlow<ConnectionState>(replay = 1)
    override val connectionState = _state.asSharedFlow()

    @SuppressLint("MissingPermission")
    override suspend fun connect(targetAddress: String): Boolean = withContext(Dispatchers.IO) {
        try {
            _state.emit(ConnectionState.Connecting)
            val device = adapter.getRemoteDevice(targetAddress) // Dirección MAC
            socket = device.createRfcommSocketToServiceRecord(SPP_UUID)
            socket?.connect()

            outputStream = socket?.outputStream
            _state.emit(ConnectionState.Connected)

            // Hilo de lectura continua delimitado por saltos de línea (\n)
            launch {
                val reader = socket?.inputStream?.bufferedReader()
                while (isActive) {
                    val line = reader?.readLine() ?: break
                    _messages.emit(line)
                }
            }
            true
        } catch (e: Exception) {
            _state.emit(ConnectionState.Error(e.message ?: "Error Bluetooth"))
            false
        }
    }

    override suspend fun sendMessage(jsonMessage: String): Boolean = withContext(Dispatchers.IO) {
        try {
            outputStream?.write((jsonMessage + "\n").toByteArray(Charsets.UTF_8))
            outputStream?.flush()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun disconnect() = withContext(Dispatchers.IO) {
        socket?.close()
        _state.emit(ConnectionState.Disconnected)
    }
}