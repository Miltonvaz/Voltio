package com.miltonvaz.voltio_1.features.orders.data.datasource.remote.mapper

import com.miltonvaz.voltio_1.features.orders.data.datasource.remote.model.*
import com.miltonvaz.voltio_1.features.orders.domain.entities.Order
import com.miltonvaz.voltio_1.features.orders.domain.entities.OrderItem
import com.miltonvaz.voltio_1.features.orders.domain.entities.OrderStatus

fun OrderDto.toDomain(): Order {
    return Order(
        id = id ?: 0,
        userId = userId ?: 0,
        orderDate = orderDate ?: "",
        status = OrderStatus.fromString(status),
        totalAmount = totalAmount ?: 0.0,
        description = description,
        address = address,
        paymentType = paymentMethod?.type,
        last4 = paymentMethod?.last4,
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
        id = if (id != 0) id else null,
        userId = userId,
        orderDate = if (orderDate.isNotEmpty()) orderDate else null,
        status = status.apiValue,
        totalAmount = totalAmount,
        description = description,
        address = address,
        paymentMethod = PaymentMethodDto(type = paymentType, last4 = last4),
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

fun Order.toUpdateRequest(last4: String): OrderUpdateRequestDto {
    return OrderUpdateRequestDto(
        userId = this.userId,
        status = this.status.apiValue,
        totalAmount = this.totalAmount,
        description = this.description ?: "",
        address = this.address ?: "",
        paymentMethod = PaymentMethodDto(
            type = this.paymentType ?: "tarjeta",
            last4 = last4
        ),
        products = this.products.map { it.toDto() }
    )
}
