package com.miltonvaz.voltio1.features.products.data.datasource.remote.model

data class CreateProductRequest(
    val sku: String,
    val nombre: String,
    val descripcion: String?,
    val precio_venta: Double,
    val stock_actual: Int,
    val imagen_url: String?,
    val id_categoria: Int?,
    val id_empresa: Int? = null,
    val especificaciones: List<CreateSpecificationRequest>? = null
)
