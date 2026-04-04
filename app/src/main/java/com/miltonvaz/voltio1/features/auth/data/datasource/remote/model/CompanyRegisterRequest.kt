package com.miltonvaz.voltio1.features.auth.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class CompanyRegisterRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("lastname")
    val lastname: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("nombre_comercial")
    val commercialName: String,
    @SerializedName("direccion_empresa")
    val address: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("telefono_contacto")
    val contactPhone: String,
    @SerializedName("correo_contacto")
    val contactEmail: String,
    @SerializedName("role")
    val role: String = "company",
    @SerializedName("account_type")
    val accountType: String = "company"
)
