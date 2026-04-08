package com.miltonvaz.voltio1.features.auth.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class CompanyDto(
    @SerializedName("id_empresa")
    val id: Int,
    @SerializedName("id_usuario")
    val userId: Int,
    @SerializedName("nombre_comercial")
    val commercialName: String,
    @SerializedName("direccion")
    val address: String?,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?,
    @SerializedName("telefono_contacto")
    val contactPhone: String?,
    @SerializedName("correo_contacto")
    val contactEmail: String?,
    @SerializedName("logo_url")
    val logoUrl: String?,
    @SerializedName("fecha_registro")
    val registrationDate: String?,
    @SerializedName("total_productos")
    val totalProducts: Int? = null
)
