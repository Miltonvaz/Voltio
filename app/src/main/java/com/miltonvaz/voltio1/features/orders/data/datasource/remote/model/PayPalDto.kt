package com.miltonvaz.voltio1.features.orders.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class CreatePayPalOrderRequest(
    @SerializedName("monto") val monto: Double,
    @SerializedName("currency_code") val currencyCode: String = "MXN",
    @SerializedName("return_url") val returnUrl: String = "com.miltonvaz.voltio1://paypalpay",
    @SerializedName("cancel_url") val cancelUrl: String = "com.miltonvaz.voltio1://paypalpay"
)

data class CreatePayPalOrderResponse(
    @SerializedName("orderID") val id: String,
    @SerializedName("status") val status: String? = null
)

data class CapturePayPalOrderRequest(
    @SerializedName("orderID") val orderID: String
)

data class CapturePayPalOrderResponse(
    @SerializedName("orderID") val id: String? = null,
    @SerializedName("status") val status: String? = null
)
