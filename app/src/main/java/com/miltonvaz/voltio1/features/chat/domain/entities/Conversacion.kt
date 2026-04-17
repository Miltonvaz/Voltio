package com.miltonvaz.voltio1.features.chat.domain.entities

data class Conversacion(
    val id_conversacion: Int,
    val id_usuario: Int,
    val id_empresa: Int?,
    val nombre_usuario: String,
    val ultimo_mensaje: String?,
    val ultimo_mensaje_fecha: String?,
    val no_leidos: Int
)