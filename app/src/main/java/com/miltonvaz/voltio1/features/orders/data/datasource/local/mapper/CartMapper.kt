package com.miltonvaz.voltio1.features.orders.data.datasource.local.mapper

import com.miltonvaz.voltio1.core.database.entities.CartEntity
import com.miltonvaz.voltio1.features.orders.domain.entities.CartItem
import com.miltonvaz.voltio1.features.products.domain.entities.Product

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
            companyId = companyId,
            registerDate = "",
            specifications = emptyList()
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
        sku = product.sku,
        companyId = product.companyId
    )
}
