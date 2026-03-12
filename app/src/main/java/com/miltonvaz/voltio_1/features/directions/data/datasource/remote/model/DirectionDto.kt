package com.miltonvaz.voltio_1.features.directions.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class DirectionDto(
    val id: Int,
    val id_usuario: Int,
    val alias: String?,
    val direccion: String,
    val es_predeterminada: Boolean,
    val created_at: String
)