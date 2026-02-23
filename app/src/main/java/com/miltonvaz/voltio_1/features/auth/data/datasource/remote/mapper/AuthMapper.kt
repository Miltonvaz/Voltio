package com.miltonvaz.voltio_1.features.auth.data.datasource.remote.mapper

import com.miltonvaz.voltio_1.features.auth.data.datasource.remote.model.UserDto
import com.miltonvaz.voltio_1.features.auth.domain.entities.Auth

fun UserDto.toDomain(): Auth {
    return Auth(
        id = this.id,
        name = this.name,
        secondname = this.secondname,
        lastname = this.lastname,
        secondlastname = this.secondlastname,
        email = this.email,
        phone = this.phone,
        image_profile = this.image_profile,
        role = this.role,
        created_at = this.created_at
    )
}
