package com.miltonvaz.voltio_1.features.orders.presentation.screens.UiState

data class CheckoutUiState(
    val cardInfo: CardInfo = CardInfo(),
    val addressInfo: AddressInfo = AddressInfo(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val orderPlacedSuccessfully: Boolean? = null
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
    val reference: String? = null
)
