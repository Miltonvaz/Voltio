package com.miltonvaz.voltio1.features.delivery.data.datasource.remote.api

import com.miltonvaz.voltio1.features.delivery.data.datasource.remote.model.OpenRouteResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenRouteService {
    @GET("https://api.openrouteservice.org/v2/directions/driving-car")
    suspend fun getDirections(
        @Query("api_key") apiKey: String,
        @Query("start") start: String, // lon,lat
        @Query("end") end: String      // lon,lat
    ): OpenRouteResponse
}
