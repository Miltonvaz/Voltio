package com.miltonvaz.voltio_1.features.orders.data.datasource.local.mapper

import com.miltonvaz.voltio_1.core.database.entities.CartEntity
import com.miltonvaz.voltio_1.features.orders.domain.entities.CartItem
import com.miltonvaz.voltio_1.features.products.domain.entities.Product

fun CartEntity.toDomain(): CartItem {
    return CartItem(
        product = Product(
            id = productId,
            sku = sku,
            name = name,
            description = "",
            price = price,
            stock = 0,
            imageUrl = imageUrl,
            categoryId = null,
            registerDate = ""
        ),
        quantity = quantity
    )
}

fun CartItem.toEntity(): CartEntity {
    return CartEntity(
        productId = product.id,
        name = product.name,
        price = product.price,
        quantity = quantity,
        imageUrl = product.imageUrl,
        sku = product.sku
    )
}
