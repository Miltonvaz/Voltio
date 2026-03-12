package com.miltonvaz.voltio_1.features.directions.data.datasource.remote.api

import com.miltonvaz.voltio_1.features.directions.data.datasource.remote.model.DirectionRequest
import com.miltonvaz.voltio_1.features.directions.data.datasource.remote.model.DirectionResponse
import retrofit2.http.*

interface DirectionApiService {

    @POST("directions")
    suspend fun createDirection(
        @Body request: DirectionRequest
    ): DirectionResponse

    @GET("directions")
    suspend fun getAllDirections(): List<DirectionResponse>

    @GET("directions/{id}")
    suspend fun getDirectionById(
        @Path("id") id: Int
    ): DirectionResponse

    @GET("usuarios/{id_usuario}/directions")
    suspend fun getDirectionsByUserId(
        @Path("id_usuario") userId: Int
    ): List<DirectionResponse>

    @PUT("directions/{id}")
    suspend fun updateDirection(
        @Path("id") id: Int,
        @Body request: DirectionRequest
    ): DirectionResponse

    @DELETE("directions/{id}")
    suspend fun deleteDirection(
        @Path("id") id: Int
    )
}