package com.miltonvaz.voltio_1.features.orders.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class OrderDto(
    @SerializedName("id_orden") val id: Int? = null,
    @SerializedName("id_usuario") val userId: Int? = null,
    @SerializedName("fecha_orden") val orderDate: String? = null,
    @SerializedName("estado_orden") val status: String? = null,
    @SerializedName("monto_total") val totalAmount: Double? = null,
    @SerializedName("descripcion") val description: String? = null,
    @SerializedName("direccion") val address: String? = null,
    @SerializedName("metodo_pago") val paymentMethod: PaymentMethodDto? = null,
    @SerializedName("productos") val products: List<OrderItemDto>? = null
)

data class PaymentMethodDto(
    @SerializedName("tipo") val type: String,
    @SerializedName("ultimos4") val last4: String
)

data class OrderItemDto(
    @SerializedName("id_producto") val productId: Int,
    @SerializedName("cantidad") val quantity: Int,
    @SerializedName("precio_unitario") val unitPrice: Double
)
