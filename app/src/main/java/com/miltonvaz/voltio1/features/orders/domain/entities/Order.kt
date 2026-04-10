package com.miltonvaz.voltio1.features.orders.domain.entities

data class Order(
    val id: Int,
    val userId: Int,
    val clientName: String? = null,
    val companyId: Int? = null,
    val companyName: String? = null,
    val orderDate: String,
    val status: OrderStatus,
    val totalAmount: Double,
    val description: String?,
    val address: String?,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val paymentType: String?,
    val last4: String?,
    val products: List<OrderItem>,
    val deliveryPersonId: Int? = null,
    val deliveryPersonName: String? = null
)

data class OrderItem(
    val productId: Int,
    val productName: String?,
    val quantity: Int,
    val unitPrice: Double,
    val imageUrl: String? = null,
    val subtotal: Double? = null
)