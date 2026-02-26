package com.miltonvaz.voltio_1.features.orders.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class SocketNotificationDto(
    @SerializedName("tipo") val type: String,
    @SerializedName("evento") val event: String,
    @SerializedName("datos") val data: OrderDto
)
