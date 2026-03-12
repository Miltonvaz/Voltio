package com.miltonvaz.voltio_1.features.directions.data.datasource.remote.mapper

import com.miltonvaz.voltio_1.features.directions.data.datasource.remote.model.DirectionResponse
import com.miltonvaz.voltio_1.features.directions.domain.entities.Direction

fun DirectionResponse.toDomain(): Direction {
    return Direction(
        id = this.id,
        id_usuario = this.id_usuario,
        alias = this.alias,
        direccion = this.direccion,
        es_predeterminada = this.es_predeterminada,
        created_at = this.created_at
    )
}