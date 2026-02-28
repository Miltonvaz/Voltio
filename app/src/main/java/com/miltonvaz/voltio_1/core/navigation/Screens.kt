package com.miltonvaz.voltio_1.core.navigation

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
