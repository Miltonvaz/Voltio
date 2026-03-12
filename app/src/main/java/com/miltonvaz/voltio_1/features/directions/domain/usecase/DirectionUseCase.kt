package com.miltonvaz.voltio_1.features.directions.domain.usecase

import com.miltonvaz.voltio_1.features.directions.data.datasource.remote.model.DirectionRequest
import com.miltonvaz.voltio_1.features.directions.domain.entities.Direction
import com.miltonvaz.voltio_1.features.directions.domain.repositories.IDirectionRepository
import javax.inject.Inject

class DirectionUseCase @Inject constructor(
    private val repository: IDirectionRepository
) {
    suspend fun getByUserId(userId: Int): Result<List<Direction>> {
        return try {
            Result.success(repository.getDirectionsByUserId(userId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun create(request: DirectionRequest): Result<Direction> {
        return try {
            Result.success(repository.createDirection(request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun update(id: Int, request: DirectionRequest): Result<Direction> {
        return try {
            Result.success(repository.updateDirection(id, request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun delete(id: Int): Result<Unit> {
        return try {
            repository.deleteDirection(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}