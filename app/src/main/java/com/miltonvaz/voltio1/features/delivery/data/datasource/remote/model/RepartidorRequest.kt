package com.miltonvaz.voltio1.features.delivery.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class RepartidorRequest(
    @SerializedName("id_usuario")
    val idUsuario: Int,
    @SerializedName("vehiculo")
    val vehiculo: String,
    @SerializedName("placas")
    val placas: String?
)
