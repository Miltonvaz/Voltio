package com.miltonvaz.voltio1.features.delivery.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class OpenRouteResponse(
    @SerializedName("features") val features: List<Feature>? = null
)

data class Feature(
    @SerializedName("geometry") val geometry: Geometry? = null,
    @SerializedName("properties") val properties: RouteProperties? = null
)

data class Geometry(
    @SerializedName("coordinates") val coordinates: List<List<Double>>? = null
)

data class RouteProperties(
    @SerializedName("summary") val summary: RouteSummary? = null
)

data class RouteSummary(
    @SerializedName("distance") val distance: Double? = null,  // metros
    @SerializedName("duration") val duration: Double? = null   // segundos
)
