package com.miltonvaz.voltio_1.features.directions.data.datasource.remote.model

data class DirectionRequest(
    val id_usuario: Int,
    val alias: String? = null,
    val direccion: String,
    val es_predeterminada: Boolean = false
)