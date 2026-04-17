package com.miltonvaz.voltio1.features.orders.presentation.screens.UiState

data class CheckoutUiState(
    val cardInfo: CardInfo = CardInfo(),
    val addressInfo: AddressInfo = AddressInfo(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val orderPlacedSuccessfully: Boolean? = null,
    val paypalOrderId: String? = null,
    val paypalReady: Boolean = false,
    // PayPal capturado exitosamente — navegar a elegir dirección
    val paypalCaptured: Boolean = false
)

data class CardInfo(
    val number: String = "",
    val name: String = "",
    val expiry: String = "",
    val cvv: String = ""
)

data class AddressInfo(
    val street: String = "",
    val city: String = "",
    val state: String = "",
    val zipCode: String = "",
    val reference: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)
