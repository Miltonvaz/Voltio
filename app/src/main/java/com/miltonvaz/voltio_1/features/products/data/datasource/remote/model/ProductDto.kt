package com.miltonvaz.voltio_1.features.products.data.datasource.remote.model



data class ProductDto(
    val id_producto: Int,
    val sku: String,
    val nombre: String,
    val descripcion: String?,
    val precio_venta: Double,
    val stock_actual: Int,
    val imagen_url: String?,
    val id_categoria: Int?,
    val fecha_registro: String,
    val especificaciones: List<SpecificationDto>? = null
)