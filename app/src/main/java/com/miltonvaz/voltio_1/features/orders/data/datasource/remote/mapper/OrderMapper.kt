package com.miltonvaz.voltio_1.features.orders.data.datasource.remote.mapper

import com.miltonvaz.voltio_1.features.orders.data.datasource.remote.model.OrderDto
import com.miltonvaz.voltio_1.features.orders.data.datasource.remote.model.OrderItemDto
import com.miltonvaz.voltio_1.features.orders.data.datasource.remote.model.PaymentMethodDto
import com.miltonvaz.voltio_1.features.orders.domain.entities.Order
import com.miltonvaz.voltio_1.features.orders.domain.entities.OrderItem

fun OrderDto.toDomain(): Order {
    return Order(
        id = id ?: 0,
        userId = userId ?: 0,
        orderDate = orderDate ?: "",
        status = status ?: "",
        totalAmount = totalAmount ?: 0.0,
        description = description,
        address = address,
        paymentType = paymentMethod?.type,
        products = products?.map { it.toDomain() } ?: emptyList()
    )
}

fun OrderItemDto.toDomain(): OrderItem {
    return OrderItem(
        productId = productId,
        productName = null,
        quantity = quantity,
        unitPrice = unitPrice
    )
}

fun Order.toDto(last4: String): OrderDto {
    return OrderDto(
        id = id,
        userId = userId,
        orderDate = orderDate,
        status = status.lowercase(),
        totalAmount = totalAmount,
        description = description,
        address = address,
        paymentMethod = paymentType?.let { PaymentMethodDto(it, last4) },
        products = products.map { it.toDto() }
    )
}

fun OrderItem.toDto(): OrderItemDto {
    return OrderItemDto(
        productId = productId,
        quantity = quantity,
        unitPrice = unitPrice
    )
}
