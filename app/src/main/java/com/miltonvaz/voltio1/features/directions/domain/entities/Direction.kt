package com.miltonvaz.voltio1.features.directions.domain.entities

data class Direction(
    val id: Int,
    val id_usuario: Int,
    val alias: String?,
    val direccion: String,
    val es_predeterminada: Boolean,
    val created_at: String,
    val latitude: Double? = null,
    val longitude: Double? = null
)