package com.mundonoobs.pcremoteapp.data.transport

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.*

class WebSocketTransport(private val client: OkHttpClient) : ConnectionTransport {
    private var webSocket: WebSocket? = null
    private val _messages = MutableSharedFlow<String>(extraBufferCapacity = 64)
    override val incomingMessages = _messages.asSharedFlow()

    private val _state = MutableSharedFlow<ConnectionState>(replay = 1)
    override val connectionState = _state.asSharedFlow()

    override suspend fun connect(targetAddress: String): Boolean {
        // targetAddress: "ws://192.168.1.50:8765"
        val request = Request.Builder().url(targetAddress).build()
        _state.emit(ConnectionState.Connecting)

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                _state.tryEmit(ConnectionState.Connected)
            }
            override fun onMessage(webSocket: WebSocket, text: String) {
                _messages.tryEmit(text)
            }
            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                _state.tryEmit(ConnectionState.Error(t.message ?: "Fallo de conexión"))
            }
            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                _state.tryEmit(ConnectionState.Disconnected)
            }
        })
        return true
    }

    override suspend fun sendMessage(jsonMessage: String): Boolean {
        return webSocket?.send(jsonMessage) ?: false
    }

    override suspend fun disconnect() {
        webSocket?.close(1000, "Cierre voluntario")
    }
}