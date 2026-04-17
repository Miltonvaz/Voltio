package com.miltonvaz.voltio1.features.chat.domain.entities

data class Mensaje(
    val id_mensaje: Int,
    val id_conversacion: Int,
    val id_remitente: Int,
    val nombre_remitente: String?,
    val contenido: String?,
    val tipo_mensaje: String,      // "texto" | "imagen" | "video" | "audio" | "documento"
    val archivo_url: String?,
    val leido: Boolean,
    val created_at: String,
    val id_mensaje_reply: Int?,
    val reply_contenido: String?,
    val reply_nombre_remitente: String?,
    val reply_tipo_mensaje: String?,
    val reply_archivo_url: String?
)