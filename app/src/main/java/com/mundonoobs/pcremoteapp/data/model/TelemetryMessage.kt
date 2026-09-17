package com.mundonoobs.pcremoteapp.data.model

data class TelemetryMessage(
    val direction: String? = null,
    val view: String? = null,
    val status: String? = null,
    val time: Int? = null,
    val duration: Int? = null,
    val title: String? = null,
    val current_slide: Int? = null,
    val notes: String? = null
)