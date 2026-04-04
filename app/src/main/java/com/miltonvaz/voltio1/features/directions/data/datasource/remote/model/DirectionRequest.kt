package com.miltonvaz.voltio1.features.directions.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class DirectionRequest(
    val id_usuario: Int,
    val alias: String? = null,
    val direccion: String,
    @SerializedName("latitud") val latitude: Double? = null,
    @SerializedName("longitud") val longitude: Double? = null,
    val es_predeterminada: Boolean = false
)