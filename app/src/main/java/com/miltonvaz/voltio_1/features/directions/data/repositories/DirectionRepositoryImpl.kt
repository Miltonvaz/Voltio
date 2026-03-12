package com.miltonvaz.voltio_1.features.directions.data.repositories

import com.miltonvaz.voltio_1.features.directions.data.datasource.remote.api.DirectionApiService
import com.miltonvaz.voltio_1.features.directions.data.datasource.remote.mapper.toDomain
import com.miltonvaz.voltio_1.features.directions.data.datasource.remote.model.DirectionRequest
import com.miltonvaz.voltio_1.features.directions.domain.entities.Direction
import com.miltonvaz.voltio_1.features.directions.domain.repositories.IDirectionRepository
import javax.inject.Inject

class DirectionRepositoryImpl @Inject constructor(
    private val api: DirectionApiService
) : IDirectionRepository {

    override suspend fun createDirection(request: DirectionRequest): Direction {
        return api.createDirection(request).toDomain()
    }

    override suspend fun getAllDirections(): List<Direction> {
        return api.getAllDirections().map { it.toDomain() }
    }

    override suspend fun getDirectionById(id: Int): Direction {
        return api.getDirectionById(id).toDomain()
    }

    override suspend fun getDirectionsByUserId(userId: Int): List<Direction> {
        return api.getDirectionsByUserId(userId).map { it.toDomain() }
    }

    override suspend fun updateDirection(id: Int, request: DirectionRequest): Direction {
        return api.updateDirection(id, request).toDomain()
    }

    override suspend fun deleteDirection(id: Int) {
        api.deleteDirection(id)
    }
}