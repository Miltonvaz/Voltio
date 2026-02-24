package com.miltonvaz.voltio_1.features.orders.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class OrderDto(
    @SerializedName("id_orden") val id: Int,
    @SerializedName("id_usuario") val userId: Int,
    @SerializedName("fecha_orden") val orderDate: String,
    @SerializedName("estado_orden") val status: String,
    @SerializedName("monto_total") val totalAmount: Double,
    @SerializedName("descripcion") val description: String?,
    @SerializedName("productos") val products: List<OrderItemDto>
)

data class OrderItemDto(
    @SerializedName("id_producto") val productId: Int,
    @SerializedName("nombre") val productName: String?,
    @SerializedName("cantidad") val quantity: Int,
    @SerializedName("precio_unitario") val unitPrice: Double
)
