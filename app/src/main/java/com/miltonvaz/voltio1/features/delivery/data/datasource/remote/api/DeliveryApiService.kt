package com.miltonvaz.voltio1.features.delivery.data.datasource.remote.api

import com.miltonvaz.voltio1.features.delivery.data.datasource.remote.model.RepartidorRequest
import com.miltonvaz.voltio1.features.delivery.data.datasource.remote.model.RepartidorDto
import com.miltonvaz.voltio1.features.auth.data.datasource.remote.model.MessageResponse
import retrofit2.http.*

interface DeliveryApiService {
    @POST("repartidores")
    suspend fun createRepartidorInfo(
        @Header("Authorization") token: String,
        @Body request: RepartidorRequest
    ): MessageResponse

    @GET("repartidores/disponibles")
    suspend fun getAvailableDrivers(
        @Header("Authorization") token: String
    ): List<RepartidorDto>
}
