package com.mundonoobs.pcremoteapp.data.transport

import kotlinx.coroutines.flow.SharedFlow

sealed interface ConnectionState {
    object Disconnected : ConnectionState
    object Connecting : ConnectionState
    object Connected : ConnectionState
    data class Error(val message: String) : ConnectionState
}

interface ConnectionTransport {
    val connectionState: SharedFlow<ConnectionState>
    val incomingMessages: SharedFlow<String>

    suspend fun connect(targetAddress: String): Boolean
    suspend fun disconnect()
    suspend fun sendMessage(jsonMessage: String): Boolean
}