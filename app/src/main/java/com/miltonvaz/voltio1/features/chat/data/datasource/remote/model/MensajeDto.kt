package com.miltonvaz.voltio1.features.chat.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class MensajeDto(
    @SerializedName("id_mensaje") val id_mensaje: Int,
    @SerializedName("id_conversacion") val id_conversacion: Int,
    @SerializedName("id_remitente") val id_remitente: Int,
    @SerializedName("nombre_remitente") val nombre_remitente: String?,
    @SerializedName("contenido") val contenido: String?,
    @SerializedName("tipo_mensaje") val tipo_mensaje: String,
    @SerializedName("archivo_url") val archivo_url: String?,
    @SerializedName("leido") val leido: Boolean,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("id_mensaje_reply") val id_mensaje_reply: Int?,
    @SerializedName("reply_contenido") val reply_contenido: String?,
    @SerializedName("reply_nombre_remitente") val reply_nombre_remitente: String?,
    @SerializedName("reply_tipo_mensaje") val reply_tipo_mensaje: String?,
    @SerializedName("reply_archivo_url") val reply_archivo_url: String?
)