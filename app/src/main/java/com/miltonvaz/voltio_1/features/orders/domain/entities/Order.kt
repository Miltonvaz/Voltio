package com.miltonvaz.voltio_1.features.orders.domain.entities

data class Order(
    val id: Int,
    val userId: Int,
    val orderDate: String,
    val status: OrderStatus,
    val totalAmount: Double,
    val description: String?,
    val address: String?,
    val paymentType: String?,
    val last4: String?,
    val products: List<OrderItem>
)

data class OrderItem(
    val productId: Int,
    val productName: String?,
    val quantity: Int,
    val unitPrice: Double
)