package com.miltonvaz.voltio1.features.orders.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class OrderDto(
    @SerializedName("id_orden") val id: Int? = null,
    @SerializedName("id_usuario") val userId: Int? = null,
    @SerializedName("nombre_cliente") val clientName: String? = null,
    @SerializedName("id_empresa") val companyId: Int? = null,
    @SerializedName("nombre_empresa") val companyName: String? = null,
    @SerializedName("fecha_orden") val orderDate: String? = null,
    @SerializedName("estado_orden") val status: String? = null,
    @SerializedName("monto_total") val totalAmount: Double? = null,
    @SerializedName("descripcion") val description: String? = null,
    @SerializedName("direccion") val address: String? = null,
    @SerializedName("latitud") val latitude: Double? = null,
    @SerializedName("longitud") val longitude: Double? = null,
    @SerializedName("metodo_pago") val paymentMethod: PaymentMethodDto? = null,
    @SerializedName("productos") val products: List<OrderItemDto>? = null,
    @SerializedName("id_repartidor") val deliveryPersonId: Int? = null,
    @SerializedName("nombre_repartidor") val deliveryPersonName: String? = null
)

data class PaymentMethodDto(
    @SerializedName("tipo") val type: String?,
    @SerializedName("ultimos4") val last4: String?
)

data class OrderItemDto(
    @SerializedName("id_producto") val productId: Int,
    @SerializedName("cantidad") val quantity: Int,
    @SerializedName("precio_unitario") val unitPrice: Double,
    @SerializedName("imagen_url") val imageUrl: String? = null,
    @SerializedName("subtotal") val subtotal: Double? = null
)

data class OrderStatusUpdateDto(
    @SerializedName("id_orden") val id: Int,
    @SerializedName("id_usuario") val userId: Int,
    @SerializedName("estado_orden") val status: String
)

data class OrderUpdateRequestDto(
    @SerializedName("id_usuario") val userId: Int,
    @SerializedName("id_empresa") val companyId: Int? = null,
    @SerializedName("estado_orden") val status: String,
    @SerializedName("monto_total") val totalAmount: Double,
    @SerializedName("descripcion") val description: String,
    @SerializedName("direccion") val address: String,
    @SerializedName("metodo_pago") val paymentMethod: PaymentMethodDto,
    @SerializedName("productos") val products: List<OrderItemDto>
)
