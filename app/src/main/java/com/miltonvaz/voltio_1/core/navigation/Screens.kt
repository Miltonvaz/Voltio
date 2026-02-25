package com.miltonvaz.voltio_1.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object Register

@Serializable
object Home

@Serializable
object HomeClient

@Serializable
data class ProductDetailArg(val id: Int)

@Serializable
data class ProductFormArg(val id: Int = -1)