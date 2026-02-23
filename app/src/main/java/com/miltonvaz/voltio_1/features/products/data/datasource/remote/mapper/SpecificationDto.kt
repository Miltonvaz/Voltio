package com.miltonvaz.voltio_1.features.products.data.datasource.remote.mapper

import com.miltonvaz.voltio_1.features.products.data.datasource.remote.model.SpecificationDto
import com.miltonvaz.voltio_1.features.products.domain.entities.Specification


fun SpecificationDto.toDomain(): Specification {
    return Specification(
        id = this.id_especificacion,
        key = this.clave,
        value = this.valor
    )
}