package com.miltonvaz.voltio1.features.chat.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class ConversacionDto(
    @SerializedName("id_conversacion") val id_conversacion: Int,
    @SerializedName("id_usuario") val id_usuario: Int,
    @SerializedName("id_empresa") val id_empresa: Int?,
    @SerializedName("nombre_usuario") val nombre_usuario: String,
    @SerializedName("ultimo_mensaje") val ultimo_mensaje: String?,
    @SerializedName("ultimo_mensaje_fecha") val ultimo_mensaje_fecha: String?,
    @SerializedName("no_leidos") val no_leidos: Int
)