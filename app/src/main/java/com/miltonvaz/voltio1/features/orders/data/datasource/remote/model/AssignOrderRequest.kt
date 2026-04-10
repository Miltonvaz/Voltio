package com.miltonvaz.voltio1.features.orders.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class AssignOrderRequest(
    @SerializedName("id_repartidor") val repartidorId: Int
)
