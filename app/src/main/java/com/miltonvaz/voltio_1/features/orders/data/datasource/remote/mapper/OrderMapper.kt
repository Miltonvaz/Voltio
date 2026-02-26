package com.miltonvaz.voltio_1.features.orders.data.datasource.remote.mapper

import com.miltonvaz.voltio_1.features.orders.data.datasource.remote.model.OrderDto
import com.miltonvaz.voltio_1.features.orders.data.datasource.remote.model.OrderItemDto
import com.miltonvaz.voltio_1.features.orders.domain.entities.Order
import com.miltonvaz.voltio_1.features.orders.domain.entities.OrderItem

fun OrderDto.toDomain(): Order {
    return Order(
        id = id,
        userId = userId,
        orderDate = orderDate,
        status = status,
        totalAmount = totalAmount.toDoubleOrNull() ?: 0.0,
        description = description,
        address = address,
        paymentType = paymentMethod?.type,
        products = products?.map { it.toDomain() } ?: emptyList()
    )
}

fun OrderItemDto.toDomain(): OrderItem {
    return OrderItem(
        productId = productId,
        productName = productName,
        quantity = quantity,
        unitPrice = unitPrice.toDoubleOrNull() ?: 0.0
    )
}

fun Order.toDto(): OrderDto {
    return OrderDto(
        id = id,
        userId = userId,
        orderDate = orderDate,
        status = status,
        totalAmount = totalAmount.toString(),
        description = description,
        address = address,
        paymentMethod = null, // No necesario enviar de vuelta por ahora
        products = products.map { it.toDto() }
    )
}

fun OrderItem.toDto(): OrderItemDto {
    return OrderItemDto(
        productId = productId,
        productName = productName,
        quantity = quantity,
        unitPrice = unitPrice.toString()
    )
}
