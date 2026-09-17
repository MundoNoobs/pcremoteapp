package com.mundonoobs.pcremoteapp.data.model

data class CommandMessage(
    val target: String? = null,
    val action: String? = null,
    val key: String? = null,
    val set_view: String? = null
)