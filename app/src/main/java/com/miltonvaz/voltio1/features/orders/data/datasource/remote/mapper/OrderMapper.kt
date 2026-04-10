package com.miltonvaz.voltio1.features.orders.data.datasource.remote.mapper

import com.miltonvaz.voltio1.features.orders.data.datasource.remote.model.*
import com.miltonvaz.voltio1.features.orders.domain.entities.Order
import com.miltonvaz.voltio1.features.orders.domain.entities.OrderItem
import com.miltonvaz.voltio1.features.orders.domain.entities.OrderStatus

fun OrderDto.toDomain(): Order {
    return Order(
        id = id ?: 0,
        userId = userId ?: 0,
        clientName = clientName,
        companyId = companyId,
        companyName = companyName,
        orderDate = orderDate ?: "",
        status = OrderStatus.fromString(status),
        totalAmount = totalAmount ?: 0.0,
        description = description,
        address = address,
        latitude = latitude,
        longitude = longitude,
        paymentType = paymentMethod?.type,
        last4 = paymentMethod?.last4,
        products = products?.map { it.toDomain() } ?: emptyList(),
        deliveryPersonId = deliveryPersonId,
        deliveryPersonName = deliveryPersonName
    )
}

fun OrderItemDto.toDomain(): OrderItem {
    return OrderItem(
        productId = productId,
        productName = null,
        quantity = quantity,
        unitPrice = unitPrice,
        imageUrl = imageUrl,
        subtotal = subtotal
    )
}

fun Order.toDto(last4: String): OrderDto {
    return OrderDto(
        id = null, // No enviar ID en creación
        userId = userId,
        companyId = companyId,
        orderDate = null, // Dejar que el servidor asigne la fecha
        status = status.apiValue,
        totalAmount = totalAmount,
        description = description ?: "Pedido desde App",
        address = address ?: "Dirección no especificada",
        latitude = latitude,
        longitude = longitude,
        paymentMethod = PaymentMethodDto(
            type = paymentType ?: "tarjeta",
            last4 = last4
        ),
        products = products.map { it.toDto() },
        deliveryPersonId = deliveryPersonId,
        deliveryPersonName = deliveryPersonName
    )
}

fun OrderItem.toDto(): OrderItemDto {
    return OrderItemDto(
        productId = productId,
        quantity = quantity,
        unitPrice = unitPrice,
        imageUrl = imageUrl,
        subtotal = subtotal
    )
}

fun Order.toUpdateRequest(last4: String): OrderUpdateRequestDto {
    return OrderUpdateRequestDto(
        userId = this.userId,
        companyId = this.companyId,
        status = this.status.apiValue,
        totalAmount = this.totalAmount,
        description = this.description ?: "Pedido desde App",
        address = this.address ?: "",
        paymentMethod = PaymentMethodDto(
            type = this.paymentType ?: "tarjeta",
            last4 = last4
        ),
        products = this.products.map { it.toDto() }
    )
}
