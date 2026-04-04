package com.miltonvaz.voltio1.features.delivery.data.datasource.remote.api

import com.miltonvaz.voltio1.features.delivery.data.datasource.remote.model.DirectionsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleDirectionsApiService {
    @GET("https://maps.googleapis.com/maps/api/directions/json")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") apiKey: String,
        @Query("mode") mode: String = "driving"
    ): DirectionsResponse
}
