package com.miltonvaz.voltio1.features.delivery.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class RepartidorDto(
    @SerializedName("id") val idRepartidor: Int,
    @SerializedName("id_usuario") val userId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("lastname") val lastname: String,
    @SerializedName("vehiculo") val vehicle: String?,
    @SerializedName("placas") val plates: String?
)
