package com.miltonvaz.voltio_1.features.directions.domain.repositories

import com.miltonvaz.voltio_1.features.directions.data.datasource.remote.model.DirectionRequest
import com.miltonvaz.voltio_1.features.directions.data.datasource.remote.model.DirectionResponse
import com.miltonvaz.voltio_1.features.directions.domain.entities.Direction

interface IDirectionRepository {
    suspend fun createDirection(request: DirectionRequest): Direction
    suspend fun getAllDirections(): List<Direction>
    suspend fun getDirectionById(id: Int): Direction
    suspend fun getDirectionsByUserId(userId: Int): List<Direction>
    suspend fun updateDirection(id: Int, request: DirectionRequest): Direction
    suspend fun deleteDirection(id: Int)
}