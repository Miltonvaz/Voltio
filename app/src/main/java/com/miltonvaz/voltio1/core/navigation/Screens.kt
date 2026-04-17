package com.miltonvaz.voltio1.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object Register

@Serializable
object AdminMenu

@Serializable
object Home

@Serializable
object Orders

@Serializable
object UserOrders

@Serializable
data class Stock(val isAdmin: Boolean = false)

@Serializable
object Inventory

@Serializable
object UserHome

@Serializable
object Cart

@Serializable
object Checkout

@Serializable
object CheckoutAddress

@Serializable
data class CheckoutResult(val isSuccess: Boolean)

@Serializable
data class OrderDetailArg(val orderId: Int, val isAdmin: Boolean = false)

@Serializable
data class ProductDetailArg(val id: Int)

@Serializable
data class ProductDetailClientArg(val id: Int)

@Serializable
data class ProductFormArg(val id: Int = -1)

@Serializable
data class CompanyProfileArg(val companyId: Int)

@Serializable
data class Directions(val paymentType: String = "tarjeta")

@Serializable
object UserProfile

@Serializable
data class UserTracking(val orderId: Int)

// Delivery Screens
@Serializable
object DeliveryDashboard

@Serializable
data class DeliveryTracking(val orderId: Int)

@Serializable
object ChatConversaciones
@Serializable
data class ChatDetalle(val idConversacion: Int, val nombreUsuario: String)