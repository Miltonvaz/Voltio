package com.miltonvaz.voltio1.features.directions.data.datasource.remote.model

data class DirectionDto(
    val id: Int,
    val id_usuario: Int,
    val alias: String?,
    val direccion: String,
    val es_predeterminada: Boolean,
    val created_at: String
)