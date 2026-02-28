package com.miltonvaz.voltio_1.features.orders.domain.entities

enum class OrderStatus(val apiValue: String) {
    PENDING("pendiente"),
    CONFIRMED("confirmada"),
    IN_PROGRESS("en_proceso"),
    COMPLETED("completada"),
    CANCELLED("cancelada");

    companion object {

        fun fromString(status: String?): OrderStatus {
            val normalizedStatus = status?.lowercase()
            return when (normalizedStatus) {
                "entregado", "completado", "completada" -> COMPLETED
                "confirmado", "confirmada" -> CONFIRMED
                "proceso", "en_proceso" -> IN_PROGRESS
                "cancelado", "cancelada" -> CANCELLED
                "pendiente" -> PENDING
                else -> entries.find { it.apiValue == normalizedStatus } ?: PENDING
            }
        }
    }
}