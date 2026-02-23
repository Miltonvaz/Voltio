package com.miltonvaz.voltio_1.features.products.data.datasource.remote.mapper

import com.miltonvaz.voltio_1.features.products.data.datasource.remote.model.ProductDto
import com.miltonvaz.voltio_1.features.products.domain.entities.Product


fun ProductDto.toDomain(): Product {
    return Product(
        id = this.id_producto,
        sku = this.sku,
        name = this.nombre,
        description = this.descripcion ?: "Sin descripción",
        price = this.precio_venta,
        stock = this.stock_actual,
        imageUrl = this.imagen_url,
        categoryId = this.id_categoria,
        registerDate = this.fecha_registro,
        specifications = this.especificaciones?.map { it.toDomain() } ?: emptyList()
    )
}

